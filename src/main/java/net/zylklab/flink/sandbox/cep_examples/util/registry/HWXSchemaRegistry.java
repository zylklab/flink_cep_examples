package net.zylklab.flink.sandbox.cep_examples.util.registry;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hortonworks.registries.schemaregistry.avro.AvroSchemaProvider;
import com.hortonworks.registries.schemaregistry.client.SchemaRegistryClient;
import com.hortonworks.registries.schemaregistry.serdes.avro.AvroSnapshotDeserializer;

import net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo.GeoHashEventAvro;



public class HWXSchemaRegistry<T> {
	private static final Logger _log = LoggerFactory.getLogger(HWXSchemaRegistry.class);
	
	private SchemaRegistryClient client;
	private AvroSnapshotDeserializer deserializer;
	private Class<T> avroType;
	
	
	public HWXSchemaRegistry(Properties schemaRegistryConfig, Class<T> avroType) {
		_log.debug("Init SchemaRegistry Client");
		this.client = new SchemaRegistryClient(HWXSchemaRegistry.properties2Map(schemaRegistryConfig));
		_log.debug("Init deserializer");
		deserializer = this.client.getDefaultDeserializer(AvroSchemaProvider.TYPE);
		this.avroType = avroType;
	}
	
	public T deserialize(byte[] message) throws IOException {
		Object o = deserializer.deserialize(new ByteArrayInputStream(message));
		return avroType.cast(o);
	}
	
	private static Map<String,Object> properties2Map(Properties config) {
		Enumeration<Object> keys = config.keys();
		Map<String, Object> configMap = new HashMap<String,Object>();
		while (keys.hasMoreElements()) {
			Object key = (Object) keys.nextElement();
			configMap.put(key.toString(), config.get(key));
		}
		return configMap;
	}

	
	public static void main(String[] args) {
		
		final String SCHEMA_REGISTRY_CACHE_SIZE_KEY = SchemaRegistryClient.Configuration.CLASSLOADER_CACHE_SIZE.name();
		final String SCHEMA_REGISTRY_CACHE_EXPIRY_INTERVAL_SECS_KEY = SchemaRegistryClient.Configuration.CLASSLOADER_CACHE_EXPIRY_INTERVAL_SECS.name();
		final String SCHEMA_REGISTRY_SCHEMA_VERSION_CACHE_SIZE_KEY = SchemaRegistryClient.Configuration.SCHEMA_VERSION_CACHE_SIZE.name();
		final String SCHEMA_REGISTRY_SCHEMA_VERSION_CACHE_EXPIRY_INTERVAL_SECS_KEY = SchemaRegistryClient.Configuration.SCHEMA_VERSION_CACHE_EXPIRY_INTERVAL_SECS.name();
		final String SCHEMA_REGISTRY_URL_KEY = SchemaRegistryClient.Configuration.SCHEMA_REGISTRY_URL.name();
		
		Properties schemaRegistryProperties = new Properties();
		schemaRegistryProperties.put(SCHEMA_REGISTRY_CACHE_SIZE_KEY, 10L);
		schemaRegistryProperties.put(SCHEMA_REGISTRY_CACHE_EXPIRY_INTERVAL_SECS_KEY, 5000L);
		schemaRegistryProperties.put(SCHEMA_REGISTRY_SCHEMA_VERSION_CACHE_SIZE_KEY, 1000L);
		schemaRegistryProperties.put(SCHEMA_REGISTRY_SCHEMA_VERSION_CACHE_EXPIRY_INTERVAL_SECS_KEY, 60 * 60 * 1000L);
		schemaRegistryProperties.put(SCHEMA_REGISTRY_URL_KEY, "http://enbarr001.bigdata.zylk.net:7788/api/v1");
		
		HWXSchemaRegistry<GeoHashEventAvro> a = new HWXSchemaRegistry<GeoHashEventAvro>(schemaRegistryProperties, GeoHashEventAvro.class);
	}
}
