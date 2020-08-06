package net.zylklab.kafka.kerberos;

import java.util.Date;
import java.util.Properties;

import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class KafkaProducerWithKerberos {

	private static final Logger _log = LoggerFactory.getLogger(KafkaProducerWithKerberos.class);
			
	private static String KAFKA_BROKER = "enbarr001.bigdata.zylk.net:6667,enbarr002.bigdata.zylk.net:6667";
	private static String KAFKA_PROTOCOL = "SASL_PLAINTEXT";
	private static String KAFKA_TOPIC = "KERBEROS_JSON";
	private static String KAFKA_KERBEROS_SERVICE_NAME = "kafka";
	private static String JAAS_CLIENT_CONF = "/home/gus/git/flink/flink_cep_examples/external-resources/jaas/jaas-client.conf";
	private static String KRB5_CONF = "/home/gus/git/flink/flink_cep_examples/external-resources/jaas/krb5.conf";
	private static String MSG = "";
	
	private static final String KAFKA_BROKER_FLAG = "kafka-broker";
	private static final String KAFKA_PROTOCOL_FLAG = "kafka-protocol";
	private static final String KAFKA_TOPIC_FLAG = "kafka-topic";
	private static final String KAFKA_KERBEROS_SERVICE_NAME_FLAG = "kerberos-service-name";
	private static final String JAAS_CLIENT_CONF_FLAG = "jaas-client-conf";
	private static final String KRB5_CONF_FLAG = "krb5-conf";
	private static final String MSG_FLAG = "message";

	private static void usage() {
		_log.error(String.format("Usage:"));
		_log.error(String.format("$ java -jar JarExample.jar"));
		_log.error(String.format("--%s\t%s", KAFKA_BROKER_FLAG, "Kafka brokers separated by commas"));
		_log.error(String.format("--%s\t%s", KAFKA_PROTOCOL_FLAG, "Kafka protocol (SASL_PLAINTEXT)"));
		_log.error(String.format("--%s\t%s", KAFKA_TOPIC_FLAG, "Kafka topic name"));
		_log.error(String.format("--%s\t%s", KAFKA_KERBEROS_SERVICE_NAME_FLAG, "Kerberos service name"));
		_log.error(String.format("--%s\t%s", JAAS_CLIENT_CONF_FLAG, "jaas client configuration file"));
		_log.error(String.format("--%s\t%s", KRB5_CONF_FLAG, "KRB5 configuration file"));
		_log.error(String.format("--%s\t%s", MSG_FLAG, "Message to send"));
	}
	
	public static void main(String[] args) {
		
		// Get configurations from arguments
		ParameterTool parameters = ParameterTool.fromArgs(args);
		if (parameters.has(KAFKA_BROKER_FLAG)) {
			KAFKA_BROKER = parameters.get(KAFKA_BROKER_FLAG);
		} else {
			_log.error(String.format("Kafka broker is not specified (--%s)", KAFKA_BROKER_FLAG));
			usage();
			throw new NullArgumentException();
		}
		if (parameters.has(KAFKA_PROTOCOL_FLAG)) {
			KAFKA_PROTOCOL = parameters.get(KAFKA_PROTOCOL_FLAG);
		} else {
			_log.error(String.format("Kafka protocol is not specified (--%s)", KAFKA_PROTOCOL_FLAG));
			usage();
			throw new NullArgumentException();
		}
		if (parameters.has(KAFKA_TOPIC_FLAG)) {
			KAFKA_TOPIC = parameters.get(KAFKA_TOPIC_FLAG);
		} else {
			_log.error(String.format("Kafka topic is not specified (--%s)", KAFKA_TOPIC_FLAG));
			usage();
			throw new NullArgumentException();
		}
		if (parameters.has(KAFKA_KERBEROS_SERVICE_NAME_FLAG)) {
			KAFKA_KERBEROS_SERVICE_NAME = parameters.get(KAFKA_KERBEROS_SERVICE_NAME_FLAG);
		} else {
			_log.error(String.format("Kafka kerberos service name is not specified (--%s)", KAFKA_KERBEROS_SERVICE_NAME_FLAG));
			usage();
			throw new NullArgumentException();
		}
		if (parameters.has(JAAS_CLIENT_CONF_FLAG)) {
			JAAS_CLIENT_CONF = parameters.get(JAAS_CLIENT_CONF_FLAG);
		} else {
			_log.error(String.format("JAAS client configuration file is not specified (--%s)", JAAS_CLIENT_CONF_FLAG));
			usage();
			throw new NullArgumentException();
		}
		if (parameters.has(KRB5_CONF_FLAG)) {
			KRB5_CONF = parameters.get(KRB5_CONF_FLAG);
		} else {
			_log.error(String.format("KRB5 configuration file is not specified (--%s)", KRB5_CONF_FLAG));
			usage();
			throw new NullArgumentException();
		}
		if (parameters.has(MSG_FLAG)) {
			MSG = parameters.get(MSG_FLAG);
		} else {
			_log.error(String.format("Kafka message is not specified (--%s)", MSG_FLAG));
			usage();
			throw new NullArgumentException();
		}

		System.setProperty("java.security.auth.login.config", JAAS_CLIENT_CONF);
		System.setProperty("java.security.krb5.conf", KRB5_CONF);
		System.setProperty("sun.security.krb5.debug", "true");

		Properties props = new Properties();
		props.put("sasl.kerberos.service.name", KAFKA_KERBEROS_SERVICE_NAME);
		props.put("security.protocol", KAFKA_PROTOCOL);
		props.put("bootstrap.servers", KAFKA_BROKER);
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		Producer<String, String> producer = new KafkaProducer<String, String>(props);
		
		TestCallback callback = new TestCallback();
		MsgPojo msg = new MsgPojo();
		
		for (long i = 0; i < 100 ; i++) {
			String key = "key-" + 1;
			
			msg.setKey(key);
//			msg.setMsg("This is a message with key: " + key);
			msg.setMsg(MSG);
			msg.setTs(new Date().getTime());
			
			ProducerRecord<String, String> event = new ProducerRecord<String, String>(KAFKA_TOPIC, key, new JSONObject(msg).toString() );
			producer.send(event, callback);
		}

		producer.close();

	}
	
	private static class TestCallback implements Callback {
		@Override
		public void onCompletion(RecordMetadata recordMetadata, Exception e) {
			if (e != null) {
				System.out.println("Error while producing message to topic: " + recordMetadata);
				e.printStackTrace();
			} else {
				String message = String.format("Sent message to topic: %s, partition: %s, offset: %s", recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset());
				System.out.println(message);
			}
		}
	}
	
	@SuppressWarnings("unused")
	private static class MsgPojo {
		private String key;
		private String msg;
		private Long ts;
		
		public MsgPojo() {
		}
		
		public String getKey() {
			return key;
		}
		
		public void setKey(String key) {
			this.key = key;
		}
		
		public String getMsg() {
			return msg;
		}
		
		public void setMsg(String msg) {
			this.msg = msg;
		}
		
		public Long getTs() {
			return ts;
		}
		
		public void setTs(Long ts) {
			this.ts = ts;
		}
	}

}
