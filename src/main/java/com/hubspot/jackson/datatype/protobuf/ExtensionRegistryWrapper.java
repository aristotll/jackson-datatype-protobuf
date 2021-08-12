package com.hubspot.jackson.datatype.protobuf;

import com.google.common.collect.ImmutableSet;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.ExtensionRegistry.ExtensionInfo;
import com.google.protobuf.ExtensionRegistryLite;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ExtensionRegistryWrapper {
  private final Optional<ExtensionRegistryLite> wrappedExtensionRegistry;
  private final Function<Descriptor, Set<ExtensionInfo>> extensionFunction;

  private ExtensionRegistryWrapper() {
    this.wrappedExtensionRegistry = Optional.empty();
    this.extensionFunction = ignored -> ImmutableSet.of();
  }

  private ExtensionRegistryWrapper(final ExtensionRegistry extensionRegistry) {
    this.wrappedExtensionRegistry = Optional.of(extensionRegistry);
    this.extensionFunction =
      new Function<Descriptor, Set<ExtensionInfo>>() {
        private final Map<Descriptor, Set<ExtensionInfo>> extensionCache = new ConcurrentHashMap<>();

        @Override
        public Set<ExtensionInfo> apply(Descriptor descriptor) {
          Set<ExtensionInfo> cached = extensionCache.get(descriptor);
          if (cached != null) {
            return cached;
          }

          Set<ExtensionInfo> extensions = extensionRegistry.getAllImmutableExtensionsByExtendedType(
            descriptor.getFullName()
          );
          extensionCache.put(descriptor, extensions);
          return extensions;
        }
      };
  }

  public static ExtensionRegistryWrapper wrap(ExtensionRegistry extensionRegistry) {
    return new ExtensionRegistryWrapper(extensionRegistry);
  }

  public static ExtensionRegistryWrapper empty() {
    return new ExtensionRegistryWrapper();
  }

  /**
   * @deprecated use {@link #getExtensionsByDescriptor(Descriptor)}
   */
  @Deprecated
  public List<ExtensionInfo> findExtensionsByDescriptor(Descriptor descriptor) {
    return new ArrayList<>(getExtensionsByDescriptor(descriptor));
  }

  public Set<ExtensionInfo> getExtensionsByDescriptor(Descriptor descriptor) {
    return extensionFunction.apply(descriptor);
  }

  public Optional<ExtensionRegistryLite> getWrappedExtensionRegistry() {
    return wrappedExtensionRegistry;
  }

  private interface Function<T, V> {
    V apply(T t);
  }
}
