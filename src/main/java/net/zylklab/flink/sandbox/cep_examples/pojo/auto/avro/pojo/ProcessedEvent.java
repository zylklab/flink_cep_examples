/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo;

import org.apache.avro.generic.GenericArray;
import org.apache.avro.specific.SpecificData;
import org.apache.avro.util.Utf8;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@org.apache.avro.specific.AvroGenerated
public class ProcessedEvent extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = -2782145269302488654L;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"ProcessedEvent\",\"namespace\":\"net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo\",\"fields\":[{\"name\":\"id\",\"type\":[\"null\",\"int\"],\"default\":null},{\"name\":\"numberOfRecords\",\"type\":[\"null\",\"long\"],\"default\":null},{\"name\":\"meanTs\",\"type\":[\"null\",\"long\"],\"default\":null},{\"name\":\"startTs\",\"type\":[\"null\",\"long\"],\"default\":null},{\"name\":\"endTs\",\"type\":[\"null\",\"long\"],\"default\":null},{\"name\":\"meanValue\",\"type\":[\"null\",\"double\"],\"default\":null},{\"name\":\"minValue\",\"type\":[\"null\",\"double\"],\"default\":null},{\"name\":\"maxValue\",\"type\":[\"null\",\"double\"],\"default\":null},{\"name\":\"err\",\"type\":[\"null\",\"double\"],\"default\":null},{\"name\":\"records\",\"type\":{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"RawEvent\",\"fields\":[{\"name\":\"id\",\"type\":[\"null\",\"int\"],\"default\":null},{\"name\":\"value\",\"type\":[\"null\",\"double\"],\"default\":null},{\"name\":\"timestamp\",\"type\":[\"null\",\"long\"],\"default\":null}]}}}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<ProcessedEvent> ENCODER =
      new BinaryMessageEncoder<ProcessedEvent>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<ProcessedEvent> DECODER =
      new BinaryMessageDecoder<ProcessedEvent>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageEncoder instance used by this class.
   * @return the message encoder used by this class
   */
  public static BinaryMessageEncoder<ProcessedEvent> getEncoder() {
    return ENCODER;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   * @return the message decoder used by this class
   */
  public static BinaryMessageDecoder<ProcessedEvent> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   * @return a BinaryMessageDecoder instance for this class backed by the given SchemaStore
   */
  public static BinaryMessageDecoder<ProcessedEvent> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<ProcessedEvent>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Serializes this ProcessedEvent to a ByteBuffer.
   * @return a buffer holding the serialized data for this instance
   * @throws java.io.IOException if this instance could not be serialized
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /**
   * Deserializes a ProcessedEvent from a ByteBuffer.
   * @param b a byte buffer holding serialized data for an instance of this class
   * @return a ProcessedEvent instance decoded from the given buffer
   * @throws java.io.IOException if the given bytes could not be deserialized into an instance of this class
   */
  public static ProcessedEvent fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  @Deprecated public java.lang.Integer id;
  @Deprecated public java.lang.Long numberOfRecords;
  @Deprecated public java.lang.Long meanTs;
  @Deprecated public java.lang.Long startTs;
  @Deprecated public java.lang.Long endTs;
  @Deprecated public java.lang.Double meanValue;
  @Deprecated public java.lang.Double minValue;
  @Deprecated public java.lang.Double maxValue;
  @Deprecated public java.lang.Double err;
  @Deprecated public java.util.List<net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo.RawEvent> records;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public ProcessedEvent() {}

  /**
   * All-args constructor.
   * @param id The new value for id
   * @param numberOfRecords The new value for numberOfRecords
   * @param meanTs The new value for meanTs
   * @param startTs The new value for startTs
   * @param endTs The new value for endTs
   * @param meanValue The new value for meanValue
   * @param minValue The new value for minValue
   * @param maxValue The new value for maxValue
   * @param err The new value for err
   * @param records The new value for records
   */
  public ProcessedEvent(java.lang.Integer id, java.lang.Long numberOfRecords, java.lang.Long meanTs, java.lang.Long startTs, java.lang.Long endTs, java.lang.Double meanValue, java.lang.Double minValue, java.lang.Double maxValue, java.lang.Double err, java.util.List<net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo.RawEvent> records) {
    this.id = id;
    this.numberOfRecords = numberOfRecords;
    this.meanTs = meanTs;
    this.startTs = startTs;
    this.endTs = endTs;
    this.meanValue = meanValue;
    this.minValue = minValue;
    this.maxValue = maxValue;
    this.err = err;
    this.records = records;
  }

  public org.apache.avro.specific.SpecificData getSpecificData() { return MODEL$; }
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return id;
    case 1: return numberOfRecords;
    case 2: return meanTs;
    case 3: return startTs;
    case 4: return endTs;
    case 5: return meanValue;
    case 6: return minValue;
    case 7: return maxValue;
    case 8: return err;
    case 9: return records;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: id = (java.lang.Integer)value$; break;
    case 1: numberOfRecords = (java.lang.Long)value$; break;
    case 2: meanTs = (java.lang.Long)value$; break;
    case 3: startTs = (java.lang.Long)value$; break;
    case 4: endTs = (java.lang.Long)value$; break;
    case 5: meanValue = (java.lang.Double)value$; break;
    case 6: minValue = (java.lang.Double)value$; break;
    case 7: maxValue = (java.lang.Double)value$; break;
    case 8: err = (java.lang.Double)value$; break;
    case 9: records = (java.util.List<net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo.RawEvent>)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'id' field.
   * @return The value of the 'id' field.
   */
  public java.lang.Integer getId() {
    return id;
  }


  /**
   * Sets the value of the 'id' field.
   * @param value the value to set.
   */
  public void setId(java.lang.Integer value) {
    this.id = value;
  }

  /**
   * Gets the value of the 'numberOfRecords' field.
   * @return The value of the 'numberOfRecords' field.
   */
  public java.lang.Long getNumberOfRecords() {
    return numberOfRecords;
  }


  /**
   * Sets the value of the 'numberOfRecords' field.
   * @param value the value to set.
   */
  public void setNumberOfRecords(java.lang.Long value) {
    this.numberOfRecords = value;
  }

  /**
   * Gets the value of the 'meanTs' field.
   * @return The value of the 'meanTs' field.
   */
  public java.lang.Long getMeanTs() {
    return meanTs;
  }


  /**
   * Sets the value of the 'meanTs' field.
   * @param value the value to set.
   */
  public void setMeanTs(java.lang.Long value) {
    this.meanTs = value;
  }

  /**
   * Gets the value of the 'startTs' field.
   * @return The value of the 'startTs' field.
   */
  public java.lang.Long getStartTs() {
    return startTs;
  }


  /**
   * Sets the value of the 'startTs' field.
   * @param value the value to set.
   */
  public void setStartTs(java.lang.Long value) {
    this.startTs = value;
  }

  /**
   * Gets the value of the 'endTs' field.
   * @return The value of the 'endTs' field.
   */
  public java.lang.Long getEndTs() {
    return endTs;
  }


  /**
   * Sets the value of the 'endTs' field.
   * @param value the value to set.
   */
  public void setEndTs(java.lang.Long value) {
    this.endTs = value;
  }

  /**
   * Gets the value of the 'meanValue' field.
   * @return The value of the 'meanValue' field.
   */
  public java.lang.Double getMeanValue() {
    return meanValue;
  }


  /**
   * Sets the value of the 'meanValue' field.
   * @param value the value to set.
   */
  public void setMeanValue(java.lang.Double value) {
    this.meanValue = value;
  }

  /**
   * Gets the value of the 'minValue' field.
   * @return The value of the 'minValue' field.
   */
  public java.lang.Double getMinValue() {
    return minValue;
  }


  /**
   * Sets the value of the 'minValue' field.
   * @param value the value to set.
   */
  public void setMinValue(java.lang.Double value) {
    this.minValue = value;
  }

  /**
   * Gets the value of the 'maxValue' field.
   * @return The value of the 'maxValue' field.
   */
  public java.lang.Double getMaxValue() {
    return maxValue;
  }


  /**
   * Sets the value of the 'maxValue' field.
   * @param value the value to set.
   */
  public void setMaxValue(java.lang.Double value) {
    this.maxValue = value;
  }

  /**
   * Gets the value of the 'err' field.
   * @return The value of the 'err' field.
   */
  public java.lang.Double getErr() {
    return err;
  }


  /**
   * Sets the value of the 'err' field.
   * @param value the value to set.
   */
  public void setErr(java.lang.Double value) {
    this.err = value;
  }

  /**
   * Gets the value of the 'records' field.
   * @return The value of the 'records' field.
   */
  public java.util.List<net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo.RawEvent> getRecords() {
    return records;
  }


  /**
   * Sets the value of the 'records' field.
   * @param value the value to set.
   */
  public void setRecords(java.util.List<net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo.RawEvent> value) {
    this.records = value;
  }

  /**
   * Creates a new ProcessedEvent RecordBuilder.
   * @return A new ProcessedEvent RecordBuilder
   */
  public static net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo.ProcessedEvent.Builder newBuilder() {
    return new net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo.ProcessedEvent.Builder();
  }

  /**
   * Creates a new ProcessedEvent RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new ProcessedEvent RecordBuilder
   */
  public static net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo.ProcessedEvent.Builder newBuilder(net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo.ProcessedEvent.Builder other) {
    if (other == null) {
      return new net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo.ProcessedEvent.Builder();
    } else {
      return new net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo.ProcessedEvent.Builder(other);
    }
  }

  /**
   * Creates a new ProcessedEvent RecordBuilder by copying an existing ProcessedEvent instance.
   * @param other The existing instance to copy.
   * @return A new ProcessedEvent RecordBuilder
   */
  public static net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo.ProcessedEvent.Builder newBuilder(net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo.ProcessedEvent other) {
    if (other == null) {
      return new net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo.ProcessedEvent.Builder();
    } else {
      return new net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo.ProcessedEvent.Builder(other);
    }
  }

  /**
   * RecordBuilder for ProcessedEvent instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<ProcessedEvent>
    implements org.apache.avro.data.RecordBuilder<ProcessedEvent> {

    private java.lang.Integer id;
    private java.lang.Long numberOfRecords;
    private java.lang.Long meanTs;
    private java.lang.Long startTs;
    private java.lang.Long endTs;
    private java.lang.Double meanValue;
    private java.lang.Double minValue;
    private java.lang.Double maxValue;
    private java.lang.Double err;
    private java.util.List<net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo.RawEvent> records;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo.ProcessedEvent.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.id)) {
        this.id = data().deepCopy(fields()[0].schema(), other.id);
        fieldSetFlags()[0] = other.fieldSetFlags()[0];
      }
      if (isValidValue(fields()[1], other.numberOfRecords)) {
        this.numberOfRecords = data().deepCopy(fields()[1].schema(), other.numberOfRecords);
        fieldSetFlags()[1] = other.fieldSetFlags()[1];
      }
      if (isValidValue(fields()[2], other.meanTs)) {
        this.meanTs = data().deepCopy(fields()[2].schema(), other.meanTs);
        fieldSetFlags()[2] = other.fieldSetFlags()[2];
      }
      if (isValidValue(fields()[3], other.startTs)) {
        this.startTs = data().deepCopy(fields()[3].schema(), other.startTs);
        fieldSetFlags()[3] = other.fieldSetFlags()[3];
      }
      if (isValidValue(fields()[4], other.endTs)) {
        this.endTs = data().deepCopy(fields()[4].schema(), other.endTs);
        fieldSetFlags()[4] = other.fieldSetFlags()[4];
      }
      if (isValidValue(fields()[5], other.meanValue)) {
        this.meanValue = data().deepCopy(fields()[5].schema(), other.meanValue);
        fieldSetFlags()[5] = other.fieldSetFlags()[5];
      }
      if (isValidValue(fields()[6], other.minValue)) {
        this.minValue = data().deepCopy(fields()[6].schema(), other.minValue);
        fieldSetFlags()[6] = other.fieldSetFlags()[6];
      }
      if (isValidValue(fields()[7], other.maxValue)) {
        this.maxValue = data().deepCopy(fields()[7].schema(), other.maxValue);
        fieldSetFlags()[7] = other.fieldSetFlags()[7];
      }
      if (isValidValue(fields()[8], other.err)) {
        this.err = data().deepCopy(fields()[8].schema(), other.err);
        fieldSetFlags()[8] = other.fieldSetFlags()[8];
      }
      if (isValidValue(fields()[9], other.records)) {
        this.records = data().deepCopy(fields()[9].schema(), other.records);
        fieldSetFlags()[9] = other.fieldSetFlags()[9];
      }
    }

    /**
     * Creates a Builder by copying an existing ProcessedEvent instance
     * @param other The existing instance to copy.
     */
    private Builder(net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo.ProcessedEvent other) {
      super(SCHEMA$);
      if (isValidValue(fields()[0], other.id)) {
        this.id = data().deepCopy(fields()[0].schema(), other.id);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.numberOfRecords)) {
        this.numberOfRecords = data().deepCopy(fields()[1].schema(), other.numberOfRecords);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.meanTs)) {
        this.meanTs = data().deepCopy(fields()[2].schema(), other.meanTs);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.startTs)) {
        this.startTs = data().deepCopy(fields()[3].schema(), other.startTs);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.endTs)) {
        this.endTs = data().deepCopy(fields()[4].schema(), other.endTs);
        fieldSetFlags()[4] = true;
      }
      if (isValidValue(fields()[5], other.meanValue)) {
        this.meanValue = data().deepCopy(fields()[5].schema(), other.meanValue);
        fieldSetFlags()[5] = true;
      }
      if (isValidValue(fields()[6], other.minValue)) {
        this.minValue = data().deepCopy(fields()[6].schema(), other.minValue);
        fieldSetFlags()[6] = true;
      }
      if (isValidValue(fields()[7], other.maxValue)) {
        this.maxValue = data().deepCopy(fields()[7].schema(), other.maxValue);
        fieldSetFlags()[7] = true;
      }
      if (isValidValue(fields()[8], other.err)) {
        this.err = data().deepCopy(fields()[8].schema(), other.err);
        fieldSetFlags()[8] = true;
      }
      if (isValidValue(fields()[9], other.records)) {
        this.records = data().deepCopy(fields()[9].schema(), other.records);
        fieldSetFlags()[9] = true;
      }
    }

    /**
      * Gets the value of the 'id' field.
      * @return The value.
      */
    public java.lang.Integer getId() {
      return id;
    }


    /**
      * Sets the value of the 'id' field.
      * @param value The value of 'id'.
      * @return This builder.
      */
    public net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo.ProcessedEvent.Builder setId(java.lang.Integer value) {
      validate(fields()[0], value);
      this.id = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'id' field has been set.
      * @return True if the 'id' field has been set, false otherwise.
      */
    public boolean hasId() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'id' field.
      * @return This builder.
      */
    public net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo.ProcessedEvent.Builder clearId() {
      id = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'numberOfRecords' field.
      * @return The value.
      */
    public java.lang.Long getNumberOfRecords() {
      return numberOfRecords;
    }


    /**
      * Sets the value of the 'numberOfRecords' field.
      * @param value The value of 'numberOfRecords'.
      * @return This builder.
      */
    public net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo.ProcessedEvent.Builder setNumberOfRecords(java.lang.Long value) {
      validate(fields()[1], value);
      this.numberOfRecords = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'numberOfRecords' field has been set.
      * @return True if the 'numberOfRecords' field has been set, false otherwise.
      */
    public boolean hasNumberOfRecords() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'numberOfRecords' field.
      * @return This builder.
      */
    public net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo.ProcessedEvent.Builder clearNumberOfRecords() {
      numberOfRecords = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'meanTs' field.
      * @return The value.
      */
    public java.lang.Long getMeanTs() {
      return meanTs;
    }


    /**
      * Sets the value of the 'meanTs' field.
      * @param value The value of 'meanTs'.
      * @return This builder.
      */
    public net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo.ProcessedEvent.Builder setMeanTs(java.lang.Long value) {
      validate(fields()[2], value);
      this.meanTs = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'meanTs' field has been set.
      * @return True if the 'meanTs' field has been set, false otherwise.
      */
    public boolean hasMeanTs() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'meanTs' field.
      * @return This builder.
      */
    public net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo.ProcessedEvent.Builder clearMeanTs() {
      meanTs = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    /**
      * Gets the value of the 'startTs' field.
      * @return The value.
      */
    public java.lang.Long getStartTs() {
      return startTs;
    }


    /**
      * Sets the value of the 'startTs' field.
      * @param value The value of 'startTs'.
      * @return This builder.
      */
    public net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo.ProcessedEvent.Builder setStartTs(java.lang.Long value) {
      validate(fields()[3], value);
      this.startTs = value;
      fieldSetFlags()[3] = true;
      return this;
    }

    /**
      * Checks whether the 'startTs' field has been set.
      * @return True if the 'startTs' field has been set, false otherwise.
      */
    public boolean hasStartTs() {
      return fieldSetFlags()[3];
    }


    /**
      * Clears the value of the 'startTs' field.
      * @return This builder.
      */
    public net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo.ProcessedEvent.Builder clearStartTs() {
      startTs = null;
      fieldSetFlags()[3] = false;
      return this;
    }

    /**
      * Gets the value of the 'endTs' field.
      * @return The value.
      */
    public java.lang.Long getEndTs() {
      return endTs;
    }


    /**
      * Sets the value of the 'endTs' field.
      * @param value The value of 'endTs'.
      * @return This builder.
      */
    public net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo.ProcessedEvent.Builder setEndTs(java.lang.Long value) {
      validate(fields()[4], value);
      this.endTs = value;
      fieldSetFlags()[4] = true;
      return this;
    }

    /**
      * Checks whether the 'endTs' field has been set.
      * @return True if the 'endTs' field has been set, false otherwise.
      */
    public boolean hasEndTs() {
      return fieldSetFlags()[4];
    }


    /**
      * Clears the value of the 'endTs' field.
      * @return This builder.
      */
    public net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo.ProcessedEvent.Builder clearEndTs() {
      endTs = null;
      fieldSetFlags()[4] = false;
      return this;
    }

    /**
      * Gets the value of the 'meanValue' field.
      * @return The value.
      */
    public java.lang.Double getMeanValue() {
      return meanValue;
    }


    /**
      * Sets the value of the 'meanValue' field.
      * @param value The value of 'meanValue'.
      * @return This builder.
      */
    public net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo.ProcessedEvent.Builder setMeanValue(java.lang.Double value) {
      validate(fields()[5], value);
      this.meanValue = value;
      fieldSetFlags()[5] = true;
      return this;
    }

    /**
      * Checks whether the 'meanValue' field has been set.
      * @return True if the 'meanValue' field has been set, false otherwise.
      */
    public boolean hasMeanValue() {
      return fieldSetFlags()[5];
    }


    /**
      * Clears the value of the 'meanValue' field.
      * @return This builder.
      */
    public net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo.ProcessedEvent.Builder clearMeanValue() {
      meanValue = null;
      fieldSetFlags()[5] = false;
      return this;
    }

    /**
      * Gets the value of the 'minValue' field.
      * @return The value.
      */
    public java.lang.Double getMinValue() {
      return minValue;
    }


    /**
      * Sets the value of the 'minValue' field.
      * @param value The value of 'minValue'.
      * @return This builder.
      */
    public net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo.ProcessedEvent.Builder setMinValue(java.lang.Double value) {
      validate(fields()[6], value);
      this.minValue = value;
      fieldSetFlags()[6] = true;
      return this;
    }

    /**
      * Checks whether the 'minValue' field has been set.
      * @return True if the 'minValue' field has been set, false otherwise.
      */
    public boolean hasMinValue() {
      return fieldSetFlags()[6];
    }


    /**
      * Clears the value of the 'minValue' field.
      * @return This builder.
      */
    public net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo.ProcessedEvent.Builder clearMinValue() {
      minValue = null;
      fieldSetFlags()[6] = false;
      return this;
    }

    /**
      * Gets the value of the 'maxValue' field.
      * @return The value.
      */
    public java.lang.Double getMaxValue() {
      return maxValue;
    }


    /**
      * Sets the value of the 'maxValue' field.
      * @param value The value of 'maxValue'.
      * @return This builder.
      */
    public net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo.ProcessedEvent.Builder setMaxValue(java.lang.Double value) {
      validate(fields()[7], value);
      this.maxValue = value;
      fieldSetFlags()[7] = true;
      return this;
    }

    /**
      * Checks whether the 'maxValue' field has been set.
      * @return True if the 'maxValue' field has been set, false otherwise.
      */
    public boolean hasMaxValue() {
      return fieldSetFlags()[7];
    }


    /**
      * Clears the value of the 'maxValue' field.
      * @return This builder.
      */
    public net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo.ProcessedEvent.Builder clearMaxValue() {
      maxValue = null;
      fieldSetFlags()[7] = false;
      return this;
    }

    /**
      * Gets the value of the 'err' field.
      * @return The value.
      */
    public java.lang.Double getErr() {
      return err;
    }


    /**
      * Sets the value of the 'err' field.
      * @param value The value of 'err'.
      * @return This builder.
      */
    public net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo.ProcessedEvent.Builder setErr(java.lang.Double value) {
      validate(fields()[8], value);
      this.err = value;
      fieldSetFlags()[8] = true;
      return this;
    }

    /**
      * Checks whether the 'err' field has been set.
      * @return True if the 'err' field has been set, false otherwise.
      */
    public boolean hasErr() {
      return fieldSetFlags()[8];
    }


    /**
      * Clears the value of the 'err' field.
      * @return This builder.
      */
    public net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo.ProcessedEvent.Builder clearErr() {
      err = null;
      fieldSetFlags()[8] = false;
      return this;
    }

    /**
      * Gets the value of the 'records' field.
      * @return The value.
      */
    public java.util.List<net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo.RawEvent> getRecords() {
      return records;
    }


    /**
      * Sets the value of the 'records' field.
      * @param value The value of 'records'.
      * @return This builder.
      */
    public net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo.ProcessedEvent.Builder setRecords(java.util.List<net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo.RawEvent> value) {
      validate(fields()[9], value);
      this.records = value;
      fieldSetFlags()[9] = true;
      return this;
    }

    /**
      * Checks whether the 'records' field has been set.
      * @return True if the 'records' field has been set, false otherwise.
      */
    public boolean hasRecords() {
      return fieldSetFlags()[9];
    }


    /**
      * Clears the value of the 'records' field.
      * @return This builder.
      */
    public net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo.ProcessedEvent.Builder clearRecords() {
      records = null;
      fieldSetFlags()[9] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ProcessedEvent build() {
      try {
        ProcessedEvent record = new ProcessedEvent();
        record.id = fieldSetFlags()[0] ? this.id : (java.lang.Integer) defaultValue(fields()[0]);
        record.numberOfRecords = fieldSetFlags()[1] ? this.numberOfRecords : (java.lang.Long) defaultValue(fields()[1]);
        record.meanTs = fieldSetFlags()[2] ? this.meanTs : (java.lang.Long) defaultValue(fields()[2]);
        record.startTs = fieldSetFlags()[3] ? this.startTs : (java.lang.Long) defaultValue(fields()[3]);
        record.endTs = fieldSetFlags()[4] ? this.endTs : (java.lang.Long) defaultValue(fields()[4]);
        record.meanValue = fieldSetFlags()[5] ? this.meanValue : (java.lang.Double) defaultValue(fields()[5]);
        record.minValue = fieldSetFlags()[6] ? this.minValue : (java.lang.Double) defaultValue(fields()[6]);
        record.maxValue = fieldSetFlags()[7] ? this.maxValue : (java.lang.Double) defaultValue(fields()[7]);
        record.err = fieldSetFlags()[8] ? this.err : (java.lang.Double) defaultValue(fields()[8]);
        record.records = fieldSetFlags()[9] ? this.records : (java.util.List<net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo.RawEvent>) defaultValue(fields()[9]);
        return record;
      } catch (org.apache.avro.AvroMissingFieldException e) {
        throw e;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<ProcessedEvent>
    WRITER$ = (org.apache.avro.io.DatumWriter<ProcessedEvent>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<ProcessedEvent>
    READER$ = (org.apache.avro.io.DatumReader<ProcessedEvent>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

  @Override protected boolean hasCustomCoders() { return true; }

  @Override public void customEncode(org.apache.avro.io.Encoder out)
    throws java.io.IOException
  {
    if (this.id == null) {
      out.writeIndex(0);
      out.writeNull();
    } else {
      out.writeIndex(1);
      out.writeInt(this.id);
    }

    if (this.numberOfRecords == null) {
      out.writeIndex(0);
      out.writeNull();
    } else {
      out.writeIndex(1);
      out.writeLong(this.numberOfRecords);
    }

    if (this.meanTs == null) {
      out.writeIndex(0);
      out.writeNull();
    } else {
      out.writeIndex(1);
      out.writeLong(this.meanTs);
    }

    if (this.startTs == null) {
      out.writeIndex(0);
      out.writeNull();
    } else {
      out.writeIndex(1);
      out.writeLong(this.startTs);
    }

    if (this.endTs == null) {
      out.writeIndex(0);
      out.writeNull();
    } else {
      out.writeIndex(1);
      out.writeLong(this.endTs);
    }

    if (this.meanValue == null) {
      out.writeIndex(0);
      out.writeNull();
    } else {
      out.writeIndex(1);
      out.writeDouble(this.meanValue);
    }

    if (this.minValue == null) {
      out.writeIndex(0);
      out.writeNull();
    } else {
      out.writeIndex(1);
      out.writeDouble(this.minValue);
    }

    if (this.maxValue == null) {
      out.writeIndex(0);
      out.writeNull();
    } else {
      out.writeIndex(1);
      out.writeDouble(this.maxValue);
    }

    if (this.err == null) {
      out.writeIndex(0);
      out.writeNull();
    } else {
      out.writeIndex(1);
      out.writeDouble(this.err);
    }

    long size0 = this.records.size();
    out.writeArrayStart();
    out.setItemCount(size0);
    long actualSize0 = 0;
    for (net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo.RawEvent e0: this.records) {
      actualSize0++;
      out.startItem();
      e0.customEncode(out);
    }
    out.writeArrayEnd();
    if (actualSize0 != size0)
      throw new java.util.ConcurrentModificationException("Array-size written was " + size0 + ", but element count was " + actualSize0 + ".");

  }

  @Override public void customDecode(org.apache.avro.io.ResolvingDecoder in)
    throws java.io.IOException
  {
    org.apache.avro.Schema.Field[] fieldOrder = in.readFieldOrderIfDiff();
    if (fieldOrder == null) {
      if (in.readIndex() != 1) {
        in.readNull();
        this.id = null;
      } else {
        this.id = in.readInt();
      }

      if (in.readIndex() != 1) {
        in.readNull();
        this.numberOfRecords = null;
      } else {
        this.numberOfRecords = in.readLong();
      }

      if (in.readIndex() != 1) {
        in.readNull();
        this.meanTs = null;
      } else {
        this.meanTs = in.readLong();
      }

      if (in.readIndex() != 1) {
        in.readNull();
        this.startTs = null;
      } else {
        this.startTs = in.readLong();
      }

      if (in.readIndex() != 1) {
        in.readNull();
        this.endTs = null;
      } else {
        this.endTs = in.readLong();
      }

      if (in.readIndex() != 1) {
        in.readNull();
        this.meanValue = null;
      } else {
        this.meanValue = in.readDouble();
      }

      if (in.readIndex() != 1) {
        in.readNull();
        this.minValue = null;
      } else {
        this.minValue = in.readDouble();
      }

      if (in.readIndex() != 1) {
        in.readNull();
        this.maxValue = null;
      } else {
        this.maxValue = in.readDouble();
      }

      if (in.readIndex() != 1) {
        in.readNull();
        this.err = null;
      } else {
        this.err = in.readDouble();
      }

      long size0 = in.readArrayStart();
      java.util.List<net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo.RawEvent> a0 = this.records;
      if (a0 == null) {
        a0 = new SpecificData.Array<net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo.RawEvent>((int)size0, SCHEMA$.getField("records").schema());
        this.records = a0;
      } else a0.clear();
      SpecificData.Array<net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo.RawEvent> ga0 = (a0 instanceof SpecificData.Array ? (SpecificData.Array<net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo.RawEvent>)a0 : null);
      for ( ; 0 < size0; size0 = in.arrayNext()) {
        for ( ; size0 != 0; size0--) {
          net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo.RawEvent e0 = (ga0 != null ? ga0.peek() : null);
          if (e0 == null) {
            e0 = new net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo.RawEvent();
          }
          e0.customDecode(in);
          a0.add(e0);
        }
      }

    } else {
      for (int i = 0; i < 10; i++) {
        switch (fieldOrder[i].pos()) {
        case 0:
          if (in.readIndex() != 1) {
            in.readNull();
            this.id = null;
          } else {
            this.id = in.readInt();
          }
          break;

        case 1:
          if (in.readIndex() != 1) {
            in.readNull();
            this.numberOfRecords = null;
          } else {
            this.numberOfRecords = in.readLong();
          }
          break;

        case 2:
          if (in.readIndex() != 1) {
            in.readNull();
            this.meanTs = null;
          } else {
            this.meanTs = in.readLong();
          }
          break;

        case 3:
          if (in.readIndex() != 1) {
            in.readNull();
            this.startTs = null;
          } else {
            this.startTs = in.readLong();
          }
          break;

        case 4:
          if (in.readIndex() != 1) {
            in.readNull();
            this.endTs = null;
          } else {
            this.endTs = in.readLong();
          }
          break;

        case 5:
          if (in.readIndex() != 1) {
            in.readNull();
            this.meanValue = null;
          } else {
            this.meanValue = in.readDouble();
          }
          break;

        case 6:
          if (in.readIndex() != 1) {
            in.readNull();
            this.minValue = null;
          } else {
            this.minValue = in.readDouble();
          }
          break;

        case 7:
          if (in.readIndex() != 1) {
            in.readNull();
            this.maxValue = null;
          } else {
            this.maxValue = in.readDouble();
          }
          break;

        case 8:
          if (in.readIndex() != 1) {
            in.readNull();
            this.err = null;
          } else {
            this.err = in.readDouble();
          }
          break;

        case 9:
          long size0 = in.readArrayStart();
          java.util.List<net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo.RawEvent> a0 = this.records;
          if (a0 == null) {
            a0 = new SpecificData.Array<net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo.RawEvent>((int)size0, SCHEMA$.getField("records").schema());
            this.records = a0;
          } else a0.clear();
          SpecificData.Array<net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo.RawEvent> ga0 = (a0 instanceof SpecificData.Array ? (SpecificData.Array<net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo.RawEvent>)a0 : null);
          for ( ; 0 < size0; size0 = in.arrayNext()) {
            for ( ; size0 != 0; size0--) {
              net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo.RawEvent e0 = (ga0 != null ? ga0.peek() : null);
              if (e0 == null) {
                e0 = new net.zylklab.flink.sandbox.cep_examples.pojo.auto.avro.pojo.RawEvent();
              }
              e0.customDecode(in);
              a0.add(e0);
            }
          }
          break;

        default:
          throw new java.io.IOException("Corrupt ResolvingDecoder.");
        }
      }
    }
  }
}









