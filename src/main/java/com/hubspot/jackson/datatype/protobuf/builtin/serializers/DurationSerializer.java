package com.hubspot.jackson.datatype.protobuf.builtin.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.protobuf.Duration;
import com.google.protobuf.util.Durations;
import com.hubspot.jackson.datatype.protobuf.ProtobufSerializer;
import java.io.IOException;

public class DurationSerializer extends ProtobufSerializer<Duration> {

  public DurationSerializer() {
    super(Duration.class);
  }

  @Override
  public void serialize(
    Duration duration,
    JsonGenerator generator,
    SerializerProvider serializerProvider
  )
    throws IOException {
    generator.writeString(Durations.toString(duration));
  }
}
