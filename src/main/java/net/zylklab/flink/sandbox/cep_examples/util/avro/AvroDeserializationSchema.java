package net.zylklab.flink.sandbox.cep_examples.util.avro;

import java.io.IOException;

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

public class AvroDeserializationSchema<T> implements DeserializationSchema<T> {
	private static final long serialVersionUID = -5017386200519225614L;
	private static final Logger _log = LoggerFactory.getLogger(AvroDeserializationSchema.class);
	private final Class<T> avroType;

	private transient DatumReader<T> reader;
	private transient BinaryDecoder decoder;
	private boolean ignoreException = false;
	
	public AvroDeserializationSchema(Class<T> avroType) {
		_log.debug(String.format("Creando el nuevo BobAvroDeserializationSchema para la clase %s",avroType.getClass().getName() ));
		this.avroType = avroType;
	}
	
	public AvroDeserializationSchema(Class<T> avroType, boolean ignoreException) {
		_log.debug(String.format("Creando el nuevo BobAvroDeserializationSchema para la clase %s",avroType.getClass().getName() ));
		this.avroType = avroType;
		this.ignoreException = ignoreException;
	}

	@Override
	public T deserialize(byte[] message) throws IOException {
		ensureInitialized();
		T record;
		try {
			_log.debug(String.format("deserialize"));
			decoder = DecoderFactory.get().binaryDecoder(message, decoder);
			record = reader.read(null, decoder);
		}
		catch (Exception e) {
			_log.error(e.getMessage(),e);
			if(!ignoreException) throw new IOException(e.getMessage(),e);
			record = null;
		}
		return record;
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
	
	private void ensureInitialized() {
		if (reader == null) {
			_log.debug(String.format("ensureInitialized reader %s",reader));
			if (org.apache.avro.specific.SpecificRecordBase.class.isAssignableFrom(avroType)) {
				reader = new SpecificDatumReader<T>(avroType);
				_log.debug(String.format("ensureInitialized SpecificDatumReader reader %s",reader));
			} else {
				reader = new ReflectDatumReader<T>(avroType);
				_log.debug(String.format("ensureInitialized ReflectDatumReader reader %s",reader));
			}
		}
	}
}
