package net.zylklab.flink.sandbox.kafka_example.job;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;

import org.apache.flink.api.java.functions.KeySelector;

import org.apache.flink.util.Collector;

import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hortonworks.registries.schemaregistry.client.SchemaRegistryClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.Date;
import java.util.Iterator;

import net.zylklab.flink.sandbox.cep_examples.pojo.GeoHashEvent;
import net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo.GeoHashEventAvro;
import net.zylklab.flink.sandbox.cep_examples.util.avro.AvroDeserializationSchema;
import net.zylklab.flink.sandbox.cep_examples.util.avro.HWXMagicBytesAvroDeserializationSchema;
import net.zylklab.flink.sandbox.cep_examples.util.registry.HWXSchemaRegistry;

public class EventTimeWindowGeoHashSubFromKafkaJob {
	private static final Logger _log = LoggerFactory.getLogger(EventTimeWindowGeoHashSubFromKafkaJob.class);
	private static final Time WINDOW_TIME_SIZE = Time.of(5, TimeUnit.SECONDS);
	private static final Time ALLOWED_LATENESS_TIME = Time.of(10, TimeUnit.MINUTES);
	private static final long MAX_OUT_OF_ORDERNESS_MS = 2000l;
	
	private static final String KAFKA_CONSUMER_GROUP = "consumer-flink";
	private static final String KAFKA_BROKER = "enbarr001.bigdata.zylk.net:6667,enbarr002.bigdata.zylk.net:6667";
	private static final String KAFKA_PROTOCOL = "SASL_PLAINTEXT";
	private static final String KAFKA_TOPIC = "GEOHASH_EVENTS_AVRO";
	private static final String KAFKA_KERBEROS_SERVICE_NAME = "kafka";
	private static final String KAFKA_OFFSET = "earliest";
	
	
	
	private static final String SCHEMA_REGISTRY_CACHE_SIZE_KEY = SchemaRegistryClient.Configuration.CLASSLOADER_CACHE_SIZE.name();
	private static final String SCHEMA_REGISTRY_CACHE_EXPIRY_INTERVAL_SECS_KEY = SchemaRegistryClient.Configuration.CLASSLOADER_CACHE_EXPIRY_INTERVAL_SECS.name();
	private static final String SCHEMA_REGISTRY_SCHEMA_VERSION_CACHE_SIZE_KEY = SchemaRegistryClient.Configuration.SCHEMA_VERSION_CACHE_SIZE.name();
	private static final String SCHEMA_REGISTRY_SCHEMA_VERSION_CACHE_EXPIRY_INTERVAL_SECS_KEY = SchemaRegistryClient.Configuration.SCHEMA_VERSION_CACHE_EXPIRY_INTERVAL_SECS.name();
	private static final String SCHEMA_REGISTRY_URL_KEY = SchemaRegistryClient.Configuration.SCHEMA_REGISTRY_URL.name();
	
	public static void main(String[] args) throws Exception {
		System.setProperty("java.security.auth.login.config", "/home/gus/git/flink/flink_cep_examples/external-resources/jaas/jaas-client.conf");
		_log.debug("Starting application");
		Properties kafkaProperties = new Properties();
		kafkaProperties.put("group.id", KAFKA_CONSUMER_GROUP);
		kafkaProperties.put("bootstrap.servers", KAFKA_BROKER);
		kafkaProperties.put("security.protocol", KAFKA_PROTOCOL);
		kafkaProperties.put("sasl.kerberos.service.name", KAFKA_KERBEROS_SERVICE_NAME);
		kafkaProperties.put("auto.offset.reset", KAFKA_OFFSET);
		

		Properties schemaRegistryProperties = new Properties();
		schemaRegistryProperties.put(SCHEMA_REGISTRY_CACHE_SIZE_KEY, 10L);
		schemaRegistryProperties.put(SCHEMA_REGISTRY_CACHE_EXPIRY_INTERVAL_SECS_KEY, 5000L);
		schemaRegistryProperties.put(SCHEMA_REGISTRY_SCHEMA_VERSION_CACHE_SIZE_KEY, 1000L);
		schemaRegistryProperties.put(SCHEMA_REGISTRY_SCHEMA_VERSION_CACHE_EXPIRY_INTERVAL_SECS_KEY, 60 * 60 * 1000L);
		schemaRegistryProperties.put(SCHEMA_REGISTRY_URL_KEY, "http://enbarr001.bigdata.zylk.net:7788/api/v1");
		
		
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
		_log.debug("Environment created.");
		EventTimeWindowGeoHashSubFromKafkaJob w = new EventTimeWindowGeoHashSubFromKafkaJob();
		
		w.addJob(env, kafkaProperties, schemaRegistryProperties);
		env.execute("EventTimeWindowGeoHasSubJob");
	}
	public void addJob(StreamExecutionEnvironment env, Properties kafkaProperties, Properties schemaRegistryProperties) throws Exception {
		DataStream<GeoHashEventAvro> dataStream = env.addSource(new FlinkKafkaConsumer<>(KAFKA_TOPIC, new HWXMagicBytesAvroDeserializationSchema<GeoHashEventAvro>(schemaRegistryProperties, GeoHashEventAvro.class), kafkaProperties))
		//DataStream<GeoHashEventAvro> dataStream = env.addSource(new FlinkKafkaConsumer<GeoHashEventAvro>(KAFKA_TOPIC, new AvroDeserializationSchema<GeoHashEventAvro>(GeoHashEventAvro.class), kafkaProperties))
		.filter(new FilterFunction<GeoHashEventAvro>() {
			private static final long serialVersionUID = -191514705055797924L;
			@Override
			public boolean filter(GeoHashEventAvro value) throws Exception {
				if(value == null) {
					_log.info("Kafka read event :::::: is null");
					return false;
				} else {
					_log.info("Kafka read event :::::: "+value.toString());
					return true;
				}
			}
		}).assignTimestampsAndWatermarks(new AssignerWithPeriodicWatermarks<GeoHashEventAvro>() {
			private static final long serialVersionUID = 1L;
			private final long maxOutOfOrderness = MAX_OUT_OF_ORDERNESS_MS;
			private long currentMaxTimestamp = MAX_OUT_OF_ORDERNESS_MS;
			
			@Override
			public long extractTimestamp(GeoHashEventAvro element, long previousElementTimestamp) {
				long timestamp = element.getTimestamp();
				if(timestamp - currentMaxTimestamp > ALLOWED_LATENESS_TIME.toMilliseconds()) { 
					_log.warn("The event is so far in the future ... posible unordered queue");
				}
				currentMaxTimestamp = Math.max(timestamp, currentMaxTimestamp);
				return timestamp;
			}
			@Override
			public Watermark getCurrentWatermark() {
				Watermark w = new Watermark(currentMaxTimestamp - maxOutOfOrderness);
				return w;
			}
		});
		
		
		
		/*
		dataStream
		.keyBy(new KeySelector<GeoHashEventAvro, String>() {
			private static final long serialVersionUID = 4026713945628697567L;
			@Override
			public String getKey(GeoHashEventAvro cdr) throws Exception {
				return cdr.getGeohash();
			}
		})
		.timeWindow(WINDOW_TIME_SIZE)
		.allowedLateness(ALLOWED_LATENESS_TIME)
		.apply(new WindowFunction<GeoHashEventAvro, List<GeoHashEventAvro>, String, TimeWindow>() {
			private static final long serialVersionUID = 6850448280789756471L;
			@Override
			public void apply(String key, TimeWindow window, Iterable<GeoHashEventAvro> input, Collector<List<GeoHashEventAvro>> out) throws Exception {
				Iterator<GeoHashEventAvro> iter = input.iterator();
				List<GeoHashEventAvro> records = new ArrayList<GeoHashEventAvro>();
				GeoHashEvent current = null;
				while(iter.hasNext()) {
					current = iter.next();
					records.add(current);
					_log.debug("window cdrs collected. "+current.toString());
				}
				
				_log.info("window cdrs collected. "+records.size()+ " window "+new Date(window.getStart())+":"+new Date(window.getEnd()));
				out.collect(records);
			}

		})
		.addSink(new SinkFunction<List<GeoHashEventAvro>>() {
			private static final long serialVersionUID = 6456951224630272805L;
			@Override
			public void invoke(List<GeoHashEventAvro> batch) throws Exception {
				//do something
			}
		});
		*/
	}
}
