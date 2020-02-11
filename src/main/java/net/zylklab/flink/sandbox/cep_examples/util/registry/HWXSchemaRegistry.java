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



public class HWXSchemaRegistry {
	//private static final Logger _log = LoggerFactory.getLogger(HWXSchemaRegistry.class);
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
			System.out.println("messsage "+message.length);
		else
			System.out.println("messsage is null");
		Object o = singleon.deserializer.deserialize(new ByteArrayInputStream(message), null);
		return o;
	}
	
	protected static Map<String,Object> properties2Map(Properties config) {
		Enumeration<Object> keys = config.keys();
		Map<String, Object> configMap = new HashMap<String,Object>();
		while (keys.hasMoreElements()) {
			Object key = (Object) keys.nextElement();
			configMap.put(key.toString(), config.get(key));
		}
		return configMap;
	}
	
	private HWXSchemaRegistry(Properties schemaRegistryConfig) {
		System.out.println("Init SchemaRegistry Client");
		this.config = HWXSchemaRegistry.properties2Map(schemaRegistryConfig);
		this.client = new SchemaRegistryClient(this.config);
		try {
			System.out.println("-----------> "+this.client.getAllVersions("geohash").size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Create the deserializer");
		this.deserializer = this.client.getDefaultDeserializer(AvroSchemaProvider.TYPE);
		System.out.println("Initialize the deserializer");
		this.deserializer.init(this.config);
	}
	
	

	
	public static void main(String[] args) throws IOException {
		
		
		//se puede pasar como -Djava.security.auth.login.config
		String jaasClientFile = "/home/gus/git/flink/flink_cep_examples/external-resources/jaas/jaas-client.conf";
		String hwxRegistryUrl = "http://enbarr001.bigdata.zylk.net:7788/api/v1"; 
		if (args != null && args.length == 2) {
			jaasClientFile = args[0];
			hwxRegistryUrl = args[1];
		}
		System.out.println("**************************************jaasClientFile "+jaasClientFile);
		System.out.println("**************************************hwxRegistryUrl "+hwxRegistryUrl);
		
		System.setProperty("java.security.auth.login.config", jaasClientFile);
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
		schemaRegistryProperties.put(SCHEMA_REGISTRY_URL_KEY, hwxRegistryUrl);
		byte[] message = {};
		System.out.println("**************************************111111 ");
		HWXSchemaRegistry a = HWXSchemaRegistry.getInstance(schemaRegistryProperties);
		System.out.println("**************************************222222 ");
		Object b = a.deserialize(message);
		System.out.println("**************************************333333 ");
	}
}
