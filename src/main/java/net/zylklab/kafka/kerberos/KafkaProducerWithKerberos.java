package net.zylklab.kafka.kerberos;

import java.util.Date;
import java.util.Properties;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.velocity.runtime.parser.node.SetPropertyExecutor;
import org.json.JSONObject;


public class KafkaProducerWithKerberos {

	private static final String KAFKA_BROKER = "enbarr001.bigdata.zylk.net:6667,enbarr002.bigdata.zylk.net:6667";
	private static final String KAFKA_PROTOCOL = "SASL_PLAINTEXT";
	private static final String KAFKA_TOPIC = "KERBEROS_JSON";
	private static final String KAFKA_KERBEROS_SERVICE_NAME = "kafka";
	private static final String JAAS_CLIENT_CONF = "/home/aian/pruebas/kafka-kerberos/jaas-client.conf";
	private static final String KRB5_CONF = "/home/aian/pruebas/kafka-kerberos/krb5.conf";

	public static void main(String[] args) {
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
			msg.setMsg("This is a message with key: " + key);
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
