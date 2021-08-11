package com.hubspot.jackson.datatype.protobuf;

import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.util.JsonFormat.TypeRegistry;

public class ProtobufJacksonConfig {
  private static final ProtobufJacksonConfig DEFAULT = ProtobufJacksonConfig
    .builder()
    .build();

  private final ExtensionRegistryWrapper extensionRegistry;
  private final TypeRegistry typeRegistry;
  private final boolean acceptLiteralFieldnames;
  private final boolean serializeLongsAsStrings;
  private final boolean useCanonicalAnySerialization;

  private ProtobufJacksonConfig(
    ExtensionRegistryWrapper extensionRegistry,
    TypeRegistry typeRegistry,
    boolean acceptLiteralFieldnames,
    boolean serializeLongsAsStrings,
    boolean useCanonicalAnySerialization
  ) {
    this.extensionRegistry = extensionRegistry;
    this.typeRegistry = typeRegistry;
    this.acceptLiteralFieldnames = acceptLiteralFieldnames;
    this.serializeLongsAsStrings = serializeLongsAsStrings;
    this.useCanonicalAnySerialization = useCanonicalAnySerialization;
  }

  public static ProtobufJacksonConfig getDefault() {
    return DEFAULT;
  }

  public static Builder builder() {
    return new Builder();
  }

  public ExtensionRegistryWrapper extensionRegistry() {
    return extensionRegistry;
  }

  public TypeRegistry typeRegistry() {
    return typeRegistry;
  }

  public boolean acceptLiteralFieldnames() {
    return acceptLiteralFieldnames;
  }

  public boolean serializeLongsAsStrings() {
    return serializeLongsAsStrings;
  }

  public boolean useCanonicalAnySerialization() {
    return useCanonicalAnySerialization;
  }

  public static class Builder {
    private ExtensionRegistryWrapper extensionRegistry = ExtensionRegistryWrapper.empty();
    private TypeRegistry typeRegistry = TypeRegistry.getEmptyTypeRegistry();
    private boolean acceptLiteralFieldnames = false;
    private boolean serializeLongsAsStrings = false;
    private boolean useCanonicalAnySerialization = false;

    private Builder() {}

    public Builder extensionRegistry(ExtensionRegistry extensionRegistry) {
      return extensionRegistry(ExtensionRegistryWrapper.wrap(extensionRegistry));
    }

    public Builder extensionRegistry(ExtensionRegistryWrapper extensionRegistry) {
      this.extensionRegistry = extensionRegistry;
      return this;
    }

    public Builder acceptLiteralFieldnames(boolean acceptLiteralFieldnames) {
      this.acceptLiteralFieldnames = acceptLiteralFieldnames;
      return this;
    }

    public Builder serializeLongsAsStrings(boolean serializeLongsAsStrings) {
      this.serializeLongsAsStrings = serializeLongsAsStrings;
      return this;
    }

    public Builder typeRegistry(TypeRegistry typeRegistry) {
      this.typeRegistry = typeRegistry;
      return this;
    }

    public Builder useCanonicalAnySerialization(boolean useCanonicalAnySerialization) {
      this.useCanonicalAnySerialization = useCanonicalAnySerialization;
      return this;
    }

    /**
     * Tries to make JSON serialization conform to the protobuf spec:
     * <a href="https://developers.google.com/protocol-buffers/docs/proto3#json">https://developers.google.com/protocol-buffers/docs/proto3#json</a>
     *
     * The behavior of this method may change in the future as new discrepancies are discovered.
     */
    public Builder useCanonicalSerialization() {
      useCanonicalAnySerialization(true);
      return serializeLongsAsStrings(true);
    }

    public ProtobufJacksonConfig build() {
      return new ProtobufJacksonConfig(
        extensionRegistry,
        typeRegistry,
        acceptLiteralFieldnames,
        serializeLongsAsStrings,
        useCanonicalAnySerialization
      );
    }
  }
}
