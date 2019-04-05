package net.zylklab.flink.sandbox.cep_examples.job;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.IterativeCondition;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.api.functions.AssignerWithPunctuatedWatermarks;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.util.Collector;

import net.zylklab.flink.sandbox.cep_examples.pojo.GeoHashEvent;
import net.zylklab.flink.sandbox.cep_examples.util.GeoHashEventsGenerator;



public class AlertsGeoHashRepSubJob {
	private static final int PAUSE = 1000;
	private static final int NUMBER_OF_EVENTS_STD = 100;
	private static final int NUMBER_OF_EVENTS_MEAN = 180;
	private static final int NUMBER_OF_ZONES = 2;
	private static final int DELTA_LIMIT = 10;
	
	public static void main(String[] args) throws Exception {
		//final StreamExecutionEnvironment env = new BobStreamExecutionEnvironmentFactory().createExecutionEnvironment();
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // setting Parallelism to 1 
        env.setParallelism(1);
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
		AlertsGeoHashRepSubJob s = new AlertsGeoHashRepSubJob();
		s.addJob(env);
		env.execute(AlertsGeoHashRepSubJob.class.getCanonicalName());
	}

	@SuppressWarnings("serial")
	public void addJob(StreamExecutionEnvironment env) throws Exception {
		
		//KeyedStream<GeoHashEvent, Tuple> keyedRecord = new BobFlinkKafkaHelper<GeoHashEvent>().getStream(env, RepSubJobsConstants.KAFKA_GEOIP_ENRICH_EVENT_TOPIC, new BobAvroDeserializationSchema<GeoHashEvent>(GeoHashEvent.class)).keyBy("geohash8");
		//TODO: asegurar el orden por tiempo de evento
		
		DataStream<GeoHashEvent> inputEventStream = env.addSource(new GeoHashEventsGenerator(PAUSE, NUMBER_OF_EVENTS_STD, NUMBER_OF_EVENTS_MEAN, NUMBER_OF_ZONES))
                .assignTimestampsAndWatermarks(new AssignerWithPunctuatedWatermarks<GeoHashEvent>() {
					public long extractTimestamp(GeoHashEvent event, long previousElementTimestamp) {
						return event.getTimestamp();
					}
					public Watermark checkAndGetNextWatermark(GeoHashEvent lastElement, long extractedTimestamp) {
						return new Watermark(extractedTimestamp);
					}

        		});
        
        //Continuously prints the input events
		//inputEventStream.print("RAW:"); 
		
		KeyedStream<GeoHashEvent, String> inputEventStreamKeyed = inputEventStream.keyBy(new KeySelector<GeoHashEvent, String>() {
			@Override
			public String getKey(GeoHashEvent value) throws Exception {
				return value.getGeohash();
			}
		});
		
		
		SingleOutputStreamOperator<GeoHashEvent> inputStream = inputEventStreamKeyed.countWindow(2,1).apply(new WindowFunction<GeoHashEvent, GeoHashEvent, String, GlobalWindow>() {
			@Override
			public void apply(String key, GlobalWindow window, Iterable<GeoHashEvent> input, Collector<GeoHashEvent> out) throws Exception {
				//debería haber dos eventos y calcular el delta de los mismos
				List<GeoHashEvent> l = new ArrayList<>();
				input.forEach(l::add);
				if(l.size() == 2) {
					l.get(1).setDeltaGPRSEvents(l.get(0).getTotalGPRSEvents() - l.get(1).getTotalGPRSEvents());
					out.collect(l.get(1));
				} else { //es el primero de los eventos para la key concreta ... por lo que el delta es 0
					l.get(0).setDeltaGPRSEvents(0);
					out.collect(l.get(0));
				}
				
			}
		});
		
		inputStream.print("ENRICHED:");
		
		Pattern<GeoHashEvent, ?> warningPattern = Pattern.<GeoHashEvent>begin("first")
				.subtype(GeoHashEvent.class)
				.where(new IterativeCondition<GeoHashEvent>() {
					@Override
					public boolean filter(GeoHashEvent value, Context<GeoHashEvent> ctx) throws Exception {
						System.out.println("first");
						System.out.println((value.getTotalGPRSEvents() - Math.abs(value.getDeltaGPRSEvents()))/value.getTotalGPRSEvents());
						System.out.println((value.getTotalGPRSEvents() - Math.abs(value.getDeltaGPRSEvents()))/value.getTotalGPRSEvents() > DELTA_LIMIT);
						return (value.getTotalGPRSEvents() - Math.abs(value.getDeltaGPRSEvents()))/value.getTotalGPRSEvents() > DELTA_LIMIT;
					}
				})
				.next("second")
				.subtype(GeoHashEvent.class)
                .where(new IterativeCondition<GeoHashEvent>() {
					@Override
					public boolean filter(GeoHashEvent value, Context<GeoHashEvent> ctx) throws Exception {
						System.out.println("Second");
						System.out.println((value.getTotalGPRSEvents() - Math.abs(value.getDeltaGPRSEvents()))/value.getTotalGPRSEvents());
						System.out.println((value.getTotalGPRSEvents() - Math.abs(value.getDeltaGPRSEvents()))/value.getTotalGPRSEvents() > DELTA_LIMIT);
						return (value.getTotalGPRSEvents() - Math.abs(value.getDeltaGPRSEvents()))/value.getTotalGPRSEvents() > DELTA_LIMIT;
					}
				})
                .within(Time.seconds(15));
		
		DataStream<Tuple2<GeoHashEvent,GeoHashEvent>> result = CEP.pattern(inputStream, warningPattern)
				.select(
					new PatternSelectFunction<GeoHashEvent, Tuple2<GeoHashEvent,GeoHashEvent>>() {
						@Override
						public Tuple2<GeoHashEvent,GeoHashEvent>  select(Map<String, List<GeoHashEvent>> pattern) throws Exception {
							// TODO Auto-generated method stub
							return new Tuple2<GeoHashEvent, GeoHashEvent>((GeoHashEvent)pattern.get("first"), (GeoHashEvent)pattern.get("second"));
						}
					}
				);
		
		result.print("ALARM:");
	}
}
