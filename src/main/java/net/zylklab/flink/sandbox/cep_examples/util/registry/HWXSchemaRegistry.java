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
import com.hortonworks.registries.schemaregistry.errors.SchemaNotFoundException;
import com.hortonworks.registries.schemaregistry.serdes.avro.AvroSnapshotDeserializer;



public class HWXSchemaRegistry {
	private static final Logger _log = LoggerFactory.getLogger(HWXSchemaRegistry.class);
	private SchemaRegistryClient client;
	private Map<String,Object> config;
	private AvroSnapshotDeserializer deserializer;
	private static HWXSchemaRegistry singleon = null;
	
	public static HWXSchemaRegistry getInstance(Properties schemaRegistryConfig) {
		if(singleon == null)
			singleon = new HWXSchemaRegistry(schemaRegistryConfig);
		return singleon;
	}
	
	public Object deserialize(byte[] message) throws IOException {
		if(message != null)
			_log.debug("messsage "+message.length);
		else
			_log.debug("messsage is null");
		Object o = singleon.deserializer.deserialize(new ByteArrayInputStream(message), null);
		return o;
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
	
	private HWXSchemaRegistry(Properties schemaRegistryConfig) {
		_log.debug("Init SchemaRegistry Client");
		this.config = HWXSchemaRegistry.properties2Map(schemaRegistryConfig);
		this.client = new SchemaRegistryClient(this.config);
		try {
			_log.info("-----------> "+this.client.getAllVersions("geohash").size());
		} catch (SchemaNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		_log.debug("Create the deserializer");
		this.deserializer = this.client.getDefaultDeserializer(AvroSchemaProvider.TYPE);
		_log.debug("Initialize the deserializer");
		this.deserializer.init(this.config);
	}
	
	

	
	public static void main(String[] args) throws IOException {
		//se puede pasar como -Djava.security.auth.login.config
		System.setProperty("java.security.auth.login.config", "/home/gus/git/flink/flink_cep_examples/external-resources/jaas/jaas-client.conf");
		
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
		byte[] message = {};
		HWXSchemaRegistry.getInstance(schemaRegistryProperties).deserialize(message);
		
	}
}
