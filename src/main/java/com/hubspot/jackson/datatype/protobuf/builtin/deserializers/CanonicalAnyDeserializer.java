package com.hubspot.jackson.datatype.protobuf.builtin.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.protobuf.Any;
import com.google.protobuf.Message;
import com.hubspot.jackson.datatype.protobuf.ProtobufJacksonConfig;
import com.hubspot.jackson.datatype.protobuf.builtin.BuiltInTypes;
import java.io.IOException;

public class CanonicalAnyDeserializer extends StdDeserializer<Any> {
  private final ProtobufJacksonConfig config;
  private final JsonDeserializer<Any> delegate;

  public CanonicalAnyDeserializer(ProtobufJacksonConfig config) {
    super(Any.class);
    this.config = config;
    this.delegate = new MessageDeserializer<>(Any.class, config).buildAtEnd();
  }

  @Override
  public Any deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    ObjectNode node = p.readValueAsTree();

    if (node.hasNonNull("@type")) {
      String typeUrl = node.get("@type").textValue();
      Message defaultInstance = config.anyTypeRegistry().defaultInstance(typeUrl);

      final JsonNode nodeToParse;
      if (BuiltInTypes.isBuiltInType(defaultInstance.getDescriptorForType())) {
        nodeToParse = node.get("value");
      } else {
        node.remove("@type");
        nodeToParse = node;
      }

      Message message = p.getCodec().treeToValue(nodeToParse, defaultInstance.getClass());
      return Any
        .newBuilder()
        .setTypeUrl(typeUrl)
        .setValue(message.toByteString())
        .build();
    } else {
      // fall back to old deserialization strategy
      JsonParser newParser = p.getCodec().treeAsTokens(node);
      return delegate.deserialize(newParser, ctxt);
    }
  }
}
