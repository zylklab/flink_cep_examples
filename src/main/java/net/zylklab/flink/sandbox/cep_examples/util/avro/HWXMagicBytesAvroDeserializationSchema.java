package net.zylklab.flink.sandbox.cep_examples.util.avro;

import java.io.IOException;
import java.util.Properties;

import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.TypeExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.zylklab.flink.sandbox.cep_examples.util.registry.HWXSchemaRegistry;

public class HWXMagicBytesAvroDeserializationSchema<T> implements DeserializationSchema<T> {
	private static final long serialVersionUID = -5017386200519225614L;
	private static final Logger _log = LoggerFactory.getLogger(HWXMagicBytesAvroDeserializationSchema.class);
	private final Class<T> avroType;
	private final Properties schemaRegistryConfig;
	
	public HWXMagicBytesAvroDeserializationSchema(Properties schemaRegistryConfig,  Class<T> avroType) {
		this.avroType = avroType;
		this.schemaRegistryConfig = schemaRegistryConfig;
	}
	@Override
	public T deserialize(byte[] message) throws IOException {
		if(message != null)
			_log.info("message size "+message.length);
		else
			_log.info("message is null ");
		return this.avroType.cast(HWXSchemaRegistry.getInstance(this.schemaRegistryConfig).deserialize(message));
	}

	@Override
	public boolean isEndOfStream(T nextElement) {
		return false;
	}

	@Override
	public TypeInformation<T> getProducedType() {
		_log.debug(String.format("getProducedType"));
		return TypeExtractor.getForClass(avroType);
	}
	
}
