package net.zylklab.flink.sandbox.unordered_events;

import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumerBase;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.zylklab.flink.sandbox.broadcaststate.pojo.Event;
import net.zylklab.flink.sandbox.cep_examples.util.avro.AvroDeserializationSchema;
import net.zylklab.flink.sandbox.cep_examples.util.avro.AvroSerializationSchema;


public class UnorderedEventsJob {

	private static final Logger _log = LoggerFactory.getLogger(UnorderedEventsJob.class);

	private static final Integer PARALLELISM = 2;
	private static final Integer WATERMARK_INTERVAL_MS = 500;
	private static final Integer MAX_OUT_OF_ORDERNESS_MS = 1000;
	private static final Integer MAX_WAIT_FOR_EVENTS_SEC = 60; 

	private static final String BOOTSTRAP_SERVERS = "amaterasu001.bigdata.zylk.net:6667, amaterasu002.bigdata.zylk.net:6667";
	private static final String GROUP_ID = "flink_unordered";
	private static final String SOURCE_TOPIC = "UNORDERED_EVENTS";
	private static final String SINK_TOPIC = "ORDERED_EVENTS";

	public static void main(String[] args) throws Exception {
		_log.debug("Starting application");
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		
		
		env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
		env.getConfig().setAutoWatermarkInterval(WATERMARK_INTERVAL_MS);
		
		env.setParallelism(PARALLELISM);
		
		_log.debug("Environment created.");
		UnorderedEventsJob w = new UnorderedEventsJob();
		w.addJob(env);
		env.execute("EventTimeWindowGroupAndProcessSubJob");

	}

	private void addJob(StreamExecutionEnvironment env) {
		
		// Kafka consumer properties
		Properties props = new Properties();
		props.setProperty("bootstrap.servers", BOOTSTRAP_SERVERS);
		props.setProperty("group.id", GROUP_ID);

		FlinkKafkaConsumerBase<Event> kafkaSource = new FlinkKafkaConsumer<>(SOURCE_TOPIC, new AvroDeserializationSchema<>(Event.class), props)
				.assignTimestampsAndWatermarks(WatermarkStrategy
						.<Event>forBoundedOutOfOrderness(Duration.ofMillis(MAX_OUT_OF_ORDERNESS_MS))
						.withTimestampAssigner((event, ts) -> event.getTs())  // Timestamp extractor
						.withIdleness(Duration.ofSeconds(MAX_WAIT_FOR_EVENTS_SEC))  // Wait for a partition when it stop sending events
						);
		
		FlinkKafkaProducer<Event> kafkaSink = new FlinkKafkaProducer<>(SINK_TOPIC, new AvroSerializationSchema<>(Event.class), props);
		
		DataStreamSource<Event> stream = env.addSource(kafkaSource);
		
		stream
		.keyBy(event -> event.getVarId())
		.process(new BufferedKeyedProcessFunction())
		.addSink(kafkaSink);

	}
}
