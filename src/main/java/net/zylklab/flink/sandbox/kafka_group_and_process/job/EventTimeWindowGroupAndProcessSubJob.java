package net.zylklab.flink.sandbox.kafka_group_and_process.job;

import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.util.Collector;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo.ProcessedEvent;
import net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo.RawEvent;
import net.zylklab.flink.sandbox.cep_examples.util.avro.AvroDeserializationSchema;
import net.zylklab.flink.sandbox.cep_examples.util.avro.AvroSerializationSchema;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class EventTimeWindowGroupAndProcessSubJob {

	private static final Logger _log = LoggerFactory.getLogger(EventTimeWindowGroupAndProcessSubJob.class);

	private static final Time WINDOW_TIME_SIZE 			= Time.of(20, TimeUnit.SECONDS);
	private static final Time ALLOWED_LATENESS_TIME 	= Time.of(10, TimeUnit.MINUTES);
	private static final long MAX_OUT_OF_ORDERNESS_MS 	= 2000l;
	private static final long WATERMARK_INTERVAL_MS 	= 1000l;

	private static final String BOOTSTRAP_SERVERS = "amaterasu001.bigtdata.zylk.net:6667, amaterasu001.bigdata.zylk.net:6667";
//	private static final String BOOTSTRAP_SERVERS = "kafka:9092"; //docker-compose service name
	private static final String GROUP_ID 		  = "flink-consumer";
	
	private static final String SOURCE_TOPIC = "RAW-EVENT";	
	private static final String SINK_TOPIC   = "PROCESSED-EVENT";

	private static final String FLINK_NODES = "flink-nodes";
	private static final Integer DEFAULT_FLINK_NODES = 2;
	private static Integer flinkNodes;
	
	public static void main(String[] args) throws Exception {
		_log.debug("Starting application");
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		
		ParameterTool parameters = ParameterTool.fromArgs(args);
		if (parameters.has(FLINK_NODES)) {
			flinkNodes = parameters.getInt(FLINK_NODES);
		} else {
			flinkNodes = DEFAULT_FLINK_NODES;
		}
		
		env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
		env.getConfig().setAutoWatermarkInterval(WATERMARK_INTERVAL_MS);
		
		env.setParallelism(flinkNodes);
		env.setMaxParallelism(flinkNodes);
		
		_log.debug("Environment created.");
		EventTimeWindowGroupAndProcessSubJob w = new EventTimeWindowGroupAndProcessSubJob();
		w.addJob(env);
		env.execute("EventTimeWindowGroupAndProcessSubJob");
	}
	
	public void addJob(StreamExecutionEnvironment env) throws Exception {
		
		Properties props = new Properties();
		props.setProperty("bootstrap.servers", BOOTSTRAP_SERVERS);
		props.setProperty("group.id",          GROUP_ID);

		DataStream<RawEvent> dataStream = env
				.addSource(new FlinkKafkaConsumer<>(SOURCE_TOPIC, new AvroDeserializationSchema<>(RawEvent.class), props))
				.setParallelism(1)
				.assignTimestampsAndWatermarks(new AssignerWithPeriodicWatermarks<RawEvent>() {
					private static final long serialVersionUID = 1L;
					private long currentMaxTimestamp = MAX_OUT_OF_ORDERNESS_MS;
					
					@Override
					public long extractTimestamp(RawEvent element, long previousElementTimestamp) {
						long timestamp = element.getTimestamp();
						if(timestamp - currentMaxTimestamp > ALLOWED_LATENESS_TIME.toMilliseconds()) { 
							_log.warn("The event is so far in the future ... posible unordered queue");
						}
						currentMaxTimestamp = Math.max(timestamp, currentMaxTimestamp);
						return timestamp;
					}
					@Override
					public Watermark getCurrentWatermark() {
						Watermark w = new Watermark(currentMaxTimestamp - MAX_OUT_OF_ORDERNESS_MS);
						System.out.println(w.toString());
						return w;
					}
				});		
		
		dataStream
			.keyBy(new KeySelector<RawEvent, Tuple2<String, Integer>>() {
				private static final long serialVersionUID = 4026713945628697567L;
				@Override
				public Tuple2<String, Integer> getKey(RawEvent event) throws Exception {
					return new Tuple2<String, Integer>(event.getFactory(), event.getVarId());
				}
			})
			.timeWindow(WINDOW_TIME_SIZE)
			.allowedLateness(ALLOWED_LATENESS_TIME)
			.process(new ProcessWindowFunction<RawEvent, ProcessedEvent, Tuple2<String, Integer>, TimeWindow>() {
				private static final long serialVersionUID = 6850448280789756471L;
				@Override
				public void process(Tuple2<String, Integer> key,
						ProcessWindowFunction<RawEvent, ProcessedEvent, Tuple2<String, Integer>, TimeWindow>.Context context,
						Iterable<RawEvent> in,
						Collector<ProcessedEvent> out) throws Exception {

					Long startTs = Long.MAX_VALUE;
					Long endTs = Long.MIN_VALUE;
					
					Double minValue = Double.MAX_VALUE;
					Double maxValue = Double.MIN_VALUE;
					Long numberOfRecords = 0L;
					
					Long sumTs = 0L;      	// need to compute mean timestamp
					Double sumValues = 0D;	// need to compute mean value
					
					List<Double> values = new ArrayList<Double>();	// need to compute cuadratic error
					List<RawEvent> records = new ArrayList<RawEvent>();
					
					for (RawEvent event : in) {
						startTs = Math.min(startTs, event.getTimestamp());
						endTs = Math.max(endTs, event.getTimestamp());
						
						maxValue = Math.max(maxValue, event.getValue());
						minValue = Math.min(minValue, event.getValue());
						
						sumTs += event.getTimestamp();
						sumValues += event.getValue();
						
						numberOfRecords ++;
						values.add(event.getValue());
						records.add(event);
					}
					
					Long meanTs = Long.valueOf(sumTs/numberOfRecords);
					
					Double meanValue = sumValues/numberOfRecords;
					
					Double sumERCM = 0D;
					for (Double v : values) {
						sumERCM += Math.pow(v-meanValue, 2);
					}		
					Double err = Math.sqrt(sumERCM/numberOfRecords);
								
					out.collect(new ProcessedEvent(key.f0, key.f1 , numberOfRecords, meanTs, startTs, endTs, meanValue, minValue, maxValue, err, records));
//					System.out.println("Thread ID: " + Thread.currentThread().getId() + "\nNumber of events: " + numberOfRecords);
				}
			}).setParallelism(flinkNodes)
			.addSink(new FlinkKafkaProducer<>(BOOTSTRAP_SERVERS, SINK_TOPIC, new AvroSerializationSchema<ProcessedEvent>(ProcessedEvent.class)))
			.setParallelism(flinkNodes)
			;

	}
}
