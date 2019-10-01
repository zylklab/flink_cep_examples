package net.zylklab.flink.sandbox.cep_examples.util.avro;

import java.io.IOException;
import java.util.Properties;

import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.reflect.ReflectDatumReader;
import org.apache.avro.specific.SpecificDatumReader;
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

	private transient HWXSchemaRegistry<T> schemaRegistry;
	private transient BinaryDecoder decoder;
	private boolean ignoreException = false;
	
	public HWXMagicBytesAvroDeserializationSchema(Properties schemaRegistryConfig,  Class<T> avroType) {
		_log.debug(String.format("Creando el nuevo BobAvroDeserializationSchema para la clase %s",avroType.getClass().getName() ));
		this.schemaRegistry = new HWXSchemaRegistry<>(schemaRegistryConfig, avroType) ;
		this.avroType = avroType;
	}
	
	public HWXMagicBytesAvroDeserializationSchema(Properties schemaRegistryConfig, Class<T> avroType, boolean ignoreException) {
		_log.debug(String.format("Creando el nuevo BobAvroDeserializationSchema para la clase %s",avroType.getClass().getName() ));
		this.avroType = avroType;
		this.ignoreException = ignoreException;
		this.schemaRegistry = new HWXSchemaRegistry<>(schemaRegistryConfig, avroType);
	}

	@Override
	public T deserialize(byte[] message) throws IOException {
		return this.schemaRegistry.deserialize(message);
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
