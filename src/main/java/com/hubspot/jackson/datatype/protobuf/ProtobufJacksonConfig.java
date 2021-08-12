package com.hubspot.jackson.datatype.protobuf;

import com.google.protobuf.ExtensionRegistry;
import com.hubspot.jackson.datatype.protobuf.builtin.AnyTypeRegistry;

public class ProtobufJacksonConfig {
  private static final ProtobufJacksonConfig DEFAULT = ProtobufJacksonConfig
    .builder()
    .build();

  private final ExtensionRegistryWrapper extensionRegistry;
  private final AnyTypeRegistry anyTypeRegistry;
  private final boolean acceptLiteralFieldnames;
  private final boolean serializeLongsAsStrings;
  private final boolean useCanonicalAnySerialization;

  private ProtobufJacksonConfig(
    ExtensionRegistryWrapper extensionRegistry,
    AnyTypeRegistry anyTypeRegistry,
    boolean acceptLiteralFieldnames,
    boolean serializeLongsAsStrings,
    boolean useCanonicalAnySerialization
  ) {
    this.extensionRegistry = extensionRegistry;
    this.anyTypeRegistry = anyTypeRegistry;
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

  public AnyTypeRegistry anyTypeRegistry() {
    return anyTypeRegistry;
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
    private AnyTypeRegistry anyTypeRegistry = AnyTypeRegistry.empty();
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

    public Builder useCanonicalAnySerialization(AnyTypeRegistry anyTypeRegistry) {
      this.anyTypeRegistry = anyTypeRegistry;
      this.useCanonicalAnySerialization = true;
      return this;
    }

    /**
     * Tries to make JSON serialization conform to the protobuf spec:
     * <a href="https://developers.google.com/protocol-buffers/docs/proto3#json">https://developers.google.com/protocol-buffers/docs/proto3#json</a>
     *
     * The behavior of this method may change in the future as new discrepancies are discovered.
     */
    public Builder useCanonicalSerialization(AnyTypeRegistry anyTypeRegistry) {
      useCanonicalAnySerialization(anyTypeRegistry);
      return serializeLongsAsStrings(true);
    }

    public ProtobufJacksonConfig build() {
      return new ProtobufJacksonConfig(
        extensionRegistry,
        anyTypeRegistry,
        acceptLiteralFieldnames,
        serializeLongsAsStrings,
        useCanonicalAnySerialization
      );
    }
  }
}
