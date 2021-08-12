package com.hubspot.jackson.datatype.protobuf.builtin.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.protobuf.Any;
import com.google.protobuf.ByteString;
import com.google.protobuf.Message;
import com.hubspot.jackson.datatype.protobuf.ExtensionRegistryWrapper;
import com.hubspot.jackson.datatype.protobuf.ProtobufJacksonConfig;
import com.hubspot.jackson.datatype.protobuf.ProtobufSerializer;
import com.hubspot.jackson.datatype.protobuf.builtin.AnyTypeRegistry;
import com.hubspot.jackson.datatype.protobuf.builtin.BuiltInTypes;
import java.io.IOException;

public class CanonicalAnySerializer extends ProtobufSerializer<Any> {
  private final AnyTypeRegistry anyTypeRegistry;
  private final ExtensionRegistryWrapper extensionRegistryWrapper;

  public CanonicalAnySerializer(ProtobufJacksonConfig config) {
    super(Any.class, config);
    this.anyTypeRegistry = config.anyTypeRegistry();
    this.extensionRegistryWrapper = config.extensionRegistry();
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
    ByteString value = any.getValue();
    Message parsedValue = anyTypeRegistry.parse(typeUrl, value, extensionRegistryWrapper);

    if (BuiltInTypes.isBuiltInType(parsedValue.getDescriptorForType())) {
      generator.writeStartObject();
      generator.writeStringField("@type", typeUrl);
      generator.writeObjectField("value", parsedValue);
      generator.writeEndObject();
    } else {
      generator.writeStartObject();
      generator.writeStringField("@type", typeUrl);
      findSerializer(parsedValue.getClass(), serializerProvider)
        .unwrappingSerializer(null)
        .serialize(parsedValue, generator, serializerProvider);
      generator.writeEndObject();
    }
  }
}
