package com.hubspot.jackson.datatype.protobuf.builtin;

import com.google.common.collect.ImmutableMap;
import com.google.protobuf.ByteString;
import com.google.protobuf.ExtensionRegistryLite;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.Parser;
import com.hubspot.jackson.datatype.protobuf.ExtensionRegistryWrapper;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AnyTypeRegistry {
  private static final AnyTypeRegistry EMPTY = new AnyTypeRegistry(ImmutableMap.of());

  private final Map<String, Message> registry;

  private AnyTypeRegistry(Map<String, Message> registry) {
    this.registry = ImmutableMap.copyOf(registry);
  }

  public static AnyTypeRegistry empty() {
    return EMPTY;
  }

  public static AnyTypeRegistry.Builder builder() {
    return new AnyTypeRegistry.Builder();
  }

  public Message defaultInstance(String typeUrl) throws InvalidProtocolBufferException {
    Message defaultInstance = registry.get(getTypeName(typeUrl));
    if (defaultInstance == null) {
      throw new InvalidProtocolBufferException("Cannot find type for url: " + typeUrl);
    }

    return defaultInstance;
  }

  public Message parse(
    String typeUrl,
    ByteString value,
    ExtensionRegistryWrapper extensionRegistryWrapper
  )
    throws InvalidProtocolBufferException {
    Parser<? extends Message> parser = defaultInstance(typeUrl).getParserForType();
    Optional<ExtensionRegistryLite> extensionRegistry = extensionRegistryWrapper.getWrappedExtensionRegistry();
    if (extensionRegistry.isPresent()) {
      return parser.parseFrom(value, extensionRegistry.get());
    } else {
      return parser.parseFrom(value);
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

  public static class Builder {
    private final Map<String, Message> registry = new HashMap<>();

    private Builder() {}

    public <T extends Message> Builder addMessageType(T message) {
      String name = message.getDescriptorForType().getFullName();
      if (!registry.containsKey(name)) {
        registry.put(name, message.getDefaultInstanceForType());
      }

      return this;
    }

    public AnyTypeRegistry build() {
      return new AnyTypeRegistry(registry);
    }
  }
}
