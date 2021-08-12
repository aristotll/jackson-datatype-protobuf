package com.hubspot.jackson.datatype.protobuf.builtin;

import com.google.common.collect.ImmutableMap;
import com.google.protobuf.Any;
import com.google.protobuf.BoolValue;
import com.google.protobuf.BytesValue;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.DoubleValue;
import com.google.protobuf.Duration;
import com.google.protobuf.FieldMask;
import com.google.protobuf.FloatValue;
import com.google.protobuf.Int32Value;
import com.google.protobuf.Int64Value;
import com.google.protobuf.ListValue;
import com.google.protobuf.Message;
import com.google.protobuf.Parser;
import com.google.protobuf.StringValue;
import com.google.protobuf.Struct;
import com.google.protobuf.Timestamp;
import com.google.protobuf.UInt32Value;
import com.google.protobuf.UInt64Value;
import com.google.protobuf.Value;
import java.util.Map;

public class BuiltInTypes {
  private static final Map<String, Parser<? extends Message>> BUILT_IN_TYPES = ImmutableMap
    .<String, Parser<? extends Message>>builder()
    .put(Any.getDescriptor().getFullName(), Any.parser())
    .put(BoolValue.getDescriptor().getFullName(), BoolValue.parser())
    .put(Int32Value.getDescriptor().getFullName(), Int32Value.parser())
    .put(UInt32Value.getDescriptor().getFullName(), UInt32Value.parser())
    .put(Int64Value.getDescriptor().getFullName(), Int64Value.parser())
    .put(UInt64Value.getDescriptor().getFullName(), UInt64Value.parser())
    .put(StringValue.getDescriptor().getFullName(), StringValue.parser())
    .put(BytesValue.getDescriptor().getFullName(), BytesValue.parser())
    .put(FloatValue.getDescriptor().getFullName(), FloatValue.parser())
    .put(DoubleValue.getDescriptor().getFullName(), DoubleValue.parser())
    .put(Timestamp.getDescriptor().getFullName(), Timestamp.parser())
    .put(Duration.getDescriptor().getFullName(), Duration.parser())
    .put(FieldMask.getDescriptor().getFullName(), FieldMask.parser())
    .put(Struct.getDescriptor().getFullName(), Struct.parser())
    .put(Value.getDescriptor().getFullName(), Value.parser())
    .put(ListValue.getDescriptor().getFullName(), ListValue.parser())
    .build();

  public static boolean isBuiltInType(Descriptor descriptor) {
    return BUILT_IN_TYPES.containsKey(descriptor.getFullName());
  }
}
