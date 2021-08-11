package com.hubspot.jackson.datatype.protobuf.builtin.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.google.protobuf.NullValue;
import java.io.IOException;

public class NullValueSerializer extends StdSerializer<NullValue> {

  public NullValueSerializer() {
    super(NullValue.class);
  }

  @Override
  public void serialize(NullValue value, JsonGenerator gen, SerializerProvider provider)
    throws IOException {
    gen.writeNull();
  }
}
