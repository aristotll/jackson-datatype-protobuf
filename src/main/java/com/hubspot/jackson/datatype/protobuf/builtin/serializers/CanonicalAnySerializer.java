package com.hubspot.jackson.datatype.protobuf.builtin.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.common.collect.ImmutableMap;
import com.google.protobuf.Any;
import com.google.protobuf.BoolValue;
import com.google.protobuf.ByteString;
import com.google.protobuf.BytesValue;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.DoubleValue;
import com.google.protobuf.Duration;
import com.google.protobuf.FieldMask;
import com.google.protobuf.FloatValue;
import com.google.protobuf.Int32Value;
import com.google.protobuf.Int64Value;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.ListValue;
import com.google.protobuf.Message;
import com.google.protobuf.StringValue;
import com.google.protobuf.Struct;
import com.google.protobuf.Timestamp;
import com.google.protobuf.UInt32Value;
import com.google.protobuf.UInt64Value;
import com.google.protobuf.Value;
import com.google.protobuf.util.JsonFormat.TypeRegistry;
import com.hubspot.jackson.datatype.protobuf.ProtobufJacksonConfig;
import com.hubspot.jackson.datatype.protobuf.ProtobufSerializer;
import java.io.IOException;
import java.util.Map;

public class CanonicalAnySerializer extends ProtobufSerializer<Any> {

  private interface ByteStringParser {
    Message parse(ByteString bytes) throws InvalidProtocolBufferException;
  }

  private static final Map<String, ByteStringParser> SPECIAL_TYPES = ImmutableMap
    .<String, ByteStringParser>builder()
    .put(Any.getDescriptor().getFullName(), Any::parseFrom)
    .put(BoolValue.getDescriptor().getFullName(), BoolValue::parseFrom)
    .put(Int32Value.getDescriptor().getFullName(), Int32Value::parseFrom)
    .put(UInt32Value.getDescriptor().getFullName(), UInt32Value::parseFrom)
    .put(Int64Value.getDescriptor().getFullName(), Int64Value::parseFrom)
    .put(UInt64Value.getDescriptor().getFullName(), UInt64Value::parseFrom)
    .put(StringValue.getDescriptor().getFullName(), StringValue::parseFrom)
    .put(BytesValue.getDescriptor().getFullName(), BytesValue::parseFrom)
    .put(FloatValue.getDescriptor().getFullName(), FloatValue::parseFrom)
    .put(DoubleValue.getDescriptor().getFullName(), DoubleValue::parseFrom)
    .put(Timestamp.getDescriptor().getFullName(), Timestamp::parseFrom)
    .put(Duration.getDescriptor().getFullName(), Duration::parseFrom)
    .put(FieldMask.getDescriptor().getFullName(), FieldMask::parseFrom)
    .put(Struct.getDescriptor().getFullName(), Struct::parseFrom)
    .put(Value.getDescriptor().getFullName(), Value::parseFrom)
    .put(ListValue.getDescriptor().getFullName(), ListValue::parseFrom)
    .build();

  private final TypeRegistry typeRegistry;

  public CanonicalAnySerializer(ProtobufJacksonConfig config) {
    super(Any.class, config);
    this.typeRegistry = config.typeRegistry();
  }

  @Override
  public void serialize(
    Any any,
    JsonGenerator generator,
    SerializerProvider serializerProvider
  )
    throws IOException {
    if (Any.getDefaultInstance().equals(any)) {
      generator.writeStartObject();
      generator.writeEndObject();
      return;
    }

    String typeUrl = any.getTypeUrl();
    Descriptor type = typeRegistry.find(getTypeName(typeUrl));
    if (type == null) {
      throw new InvalidProtocolBufferException("Cannot find type for url: " + typeUrl);
    }

    ByteString value = any.getValue();
    if (SPECIAL_TYPES.containsKey(type.getFullName())) {
      Message parsedValue = SPECIAL_TYPES.get(type.getFullName()).parse(any.getValue());

      generator.writeStartObject();
      generator.writeStringField("@type", typeUrl);
      generator.writeObjectField("value", parsedValue);
      generator.writeEndObject();
    } else {
      generator.writeStartObject();
      generator.writeStringField("@type", typeUrl);
      // TODO parse and write value
      generator.writeEndObject();
    }
  }

  private static String getTypeName(String typeUrl)
    throws InvalidProtocolBufferException {
    int lastSlash = typeUrl.lastIndexOf('/');
    if (lastSlash < 0 || lastSlash == typeUrl.length()) {
      throw new InvalidProtocolBufferException("Invalid type url found: " + typeUrl);
    }

    return typeUrl.substring(lastSlash + 1);
  }
}
