package net.zylklab.flink.sandbox.cep_examples.util.registry;

import java.util.Map;
import java.util.Properties;

import com.hortonworks.registries.schemaregistry.avro.AvroSchemaProvider;
import com.hortonworks.registries.schemaregistry.client.SchemaRegistryClient;
import com.hortonworks.registries.schemaregistry.serdes.avro.AvroSnapshotDeserializer;

public class Test {
	public static void main(String[] args) {
		// se puede pasar como -Djava.security.auth.login.config
		String jaasClientFile = "/home/gus/git/flink/flink_cep_examples/external-resources/jaas/jaas-client.conf";
		String hwxRegistryUrl = "http://enbarr001.bigdata.zylk.net:7788/api/v1";
		String schemaName = "geohash";
		if (args != null && args.length == 3) {
			jaasClientFile = args[0];
			hwxRegistryUrl = args[1];
			schemaName = args[2];
		}
		System.out.println("**************************************jaasClientFile " + jaasClientFile);
		System.out.println("**************************************hwxRegistryUrl " + hwxRegistryUrl);

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
		
		System.out.println("Init SchemaRegistry Client");
		
		Map<String,Object> config;
		AvroSnapshotDeserializer deserializer;
		config = HWXSchemaRegistry.properties2Map(schemaRegistryProperties);
		try(SchemaRegistryClient client = new SchemaRegistryClient(config)) { 
			try {
				System.out.println("-----Versiones encontradas para el esquema indicado------> "+client.getAllVersions(schemaName).size());
				deserializer = client.getDefaultDeserializer(AvroSchemaProvider.TYPE);
				System.out.println("Initialize the deserializer");
				deserializer.init(config);
			} catch (Exception e) {
				System.out.println("No se ha podido recuperar el schema indicado");
			}
			
		}
		
	}
}
