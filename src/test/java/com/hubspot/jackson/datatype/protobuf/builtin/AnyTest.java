package com.hubspot.jackson.datatype.protobuf.builtin;

import static com.hubspot.jackson.datatype.protobuf.util.ObjectMapperHelper.camelCase;
import static com.hubspot.jackson.datatype.protobuf.util.ObjectMapperHelper.create;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.Any;
import com.google.protobuf.Int64Value;
import com.google.protobuf.Value;
import com.hubspot.jackson.datatype.protobuf.ProtobufJacksonConfig;
import com.hubspot.jackson.datatype.protobuf.util.BuiltInProtobufs.HasAny;
import java.io.IOException;
import org.junit.Test;

public class AnyTest {
  private static final String TYPE_URL = "type.googleapis.com/google.protobuf.Value";
  private static final Value VALUE = Value.newBuilder().setStringValue("test").build();
  private static final Any ANY = Any
    .newBuilder()
    .setTypeUrl(TYPE_URL)
    .setValue(VALUE.toByteString())
    .build();

  @Test
  public void itWritesDurationWhenSetWithDefaultInclusion() throws IOException {
    HasAny message = HasAny.newBuilder().setAny(ANY).build();
    String json = camelCase().writeValueAsString(message);
    assertThat(camelCase().readTree(json)).isEqualTo(anyNode());
  }

  @Test
  public void itWritesZeroWhenSetToDefaultInstanceWithDefaultInclusion()
    throws IOException {
    HasAny message = HasAny.newBuilder().setAny(Any.getDefaultInstance()).build();
    String json = camelCase().writeValueAsString(message);
    assertThat(camelCase().readTree(json)).isEqualTo(defaultNode());
  }

  @Test
  public void itOmitsDurationWhenNotSetWithDefaultInclusion() throws IOException {
    HasAny message = HasAny.newBuilder().build();
    String json = camelCase().writeValueAsString(message);
    assertThat(json).isEqualTo("{}");
  }

  @Test
  public void itWritesDurationWhenSetWithNonDefaultInclusion() throws IOException {
    HasAny message = HasAny.newBuilder().setAny(ANY).build();
    String json = camelCase().writeValueAsString(message);
    assertThat(camelCase().readTree(json)).isEqualTo(anyNode());
  }

  @Test
  public void itWritesZeroWhenSetToDefaultInstanceWithNonDefaultInclusion()
    throws IOException {
    HasAny message = HasAny.newBuilder().setAny(Any.getDefaultInstance()).build();
    String json = camelCase().writeValueAsString(message);
    assertThat(camelCase().readTree(json)).isEqualTo(defaultNode());
  }

  @Test
  public void itOmitsDurationWhenNotSetWithNonDefaultInclusion() throws IOException {
    HasAny message = HasAny.newBuilder().build();
    String json = camelCase(Include.NON_DEFAULT).writeValueAsString(message);
    assertThat(json).isEqualTo("{}");
  }

  @Test
  public void itWritesDurationSetWithAlwaysInclusion() throws IOException {
    HasAny message = HasAny.newBuilder().setAny(ANY).build();
    String json = camelCase().writeValueAsString(message);
    assertThat(camelCase().readTree(json)).isEqualTo(anyNode());
  }

  @Test
  public void itWritesZeroWhenSetToDefaultInstanceWithAlwaysInclusion()
    throws IOException {
    HasAny message = HasAny.newBuilder().setAny(Any.getDefaultInstance()).build();
    String json = camelCase().writeValueAsString(message);
    assertThat(camelCase().readTree(json)).isEqualTo(defaultNode());
  }

  @Test
  public void itWritesNullWhenNotSetWithAlwaysInclusion() throws IOException {
    HasAny message = HasAny.newBuilder().build();
    String json = camelCase(Include.ALWAYS).writeValueAsString(message);
    assertThat(json).isEqualTo("{\"any\":null}");
  }

  @Test
  public void itWritesDurationWhenSetWithNonNullInclusion() throws IOException {
    HasAny message = HasAny.newBuilder().setAny(ANY).build();
    String json = camelCase().writeValueAsString(message);
    assertThat(camelCase().readTree(json)).isEqualTo(anyNode());
  }

  @Test
  public void itWritesZeroWhenSetToDefaultInstanceWithNonNullInclusion()
    throws IOException {
    HasAny message = HasAny.newBuilder().setAny(Any.getDefaultInstance()).build();
    String json = camelCase().writeValueAsString(message);
    assertThat(camelCase().readTree(json)).isEqualTo(defaultNode());
  }

  @Test
  public void itOmitsDurationWhenNotSetWithNonNullInclusion() throws IOException {
    HasAny message = HasAny.newBuilder().build();
    String json = camelCase(Include.NON_NULL).writeValueAsString(message);
    assertThat(json).isEqualTo("{}");
  }

  @Test
  public void itSetsDurationWhenPresentInJson() throws IOException {
    String json = camelCase().writeValueAsString(anyNode());
    HasAny message = camelCase().readValue(json, HasAny.class);
    assertThat(message.hasAny()).isTrue();
    assertThat(message.getAny()).isEqualTo(ANY);
  }

  @Test
  public void itSetsDurationWhenZeroInJson() throws IOException {
    String json = camelCase().writeValueAsString(defaultNode());
    HasAny message = camelCase().readValue(json, HasAny.class);
    assertThat(message.hasAny()).isTrue();
    assertThat(message.getAny()).isEqualTo(Any.getDefaultInstance());
  }

  @Test
  public void itDoesntSetDurationWhenNullInJson() throws IOException {
    String json = "{\"any\":null}";
    HasAny message = camelCase().readValue(json, HasAny.class);
    assertThat(message.hasAny()).isFalse();
  }

  @Test
  public void itDoesntSetDurationWhenMissingFromJson() throws IOException {
    String json = "{}";
    HasAny message = camelCase().readValue(json, HasAny.class);
    assertThat(message.hasAny()).isFalse();
  }

  @Test
  public void itSerializesLongsAsStringsInsideAny() throws IOException {
    ObjectMapper mapper = create(
      ProtobufJacksonConfig
        .builder()
        .serializeLongsAsStrings(true)
        .useCanonicalAnySerialization(
          AnyTypeRegistry
            .builder()
            .addMessageType(Int64Value.getDefaultInstance())
            .build()
        )
        .build()
    );

    HasAny original = HasAny
      .newBuilder()
      .setAny(
        Any
          .newBuilder()
          .setTypeUrl("type.googleapis.com/google.protobuf.Int64Value")
          .setValue(Int64Value.of(123).toByteString())
          .build()
      )
      .build();

    JsonNode json = mapper.valueToTree(original);

    assertThat(json.path("any").path("@type").textValue())
      .isEqualTo("type.googleapis.com/google.protobuf.Int64Value");
    assertThat(json.path("any").path("value").isTextual()).isTrue();
    assertThat(json.path("any").path("value").textValue()).isEqualTo("123");

    HasAny parsed = mapper.treeToValue(json, HasAny.class);

    assertThat(parsed.getAny().getTypeUrl())
      .isEqualTo("type.googleapis.com/google.protobuf.Int64Value");
    assertThat(parsed.getAny().getValue()).isEqualTo(Int64Value.of(123).toByteString());
  }

  @Test
  public void itSerializesNestedAny() throws IOException {
    ObjectMapper mapper = create(
      ProtobufJacksonConfig
        .builder()
        .useCanonicalAnySerialization(
          AnyTypeRegistry
            .builder()
            .addMessageType(Any.getDefaultInstance())
            .addMessageType(Int64Value.getDefaultInstance())
            .build()
        )
        .build()
    );

    Any nested = Any
      .newBuilder()
      .setTypeUrl("type.googleapis.com/google.protobuf.Int64Value")
      .setValue(Int64Value.of(123).toByteString())
      .build();

    HasAny original = HasAny
      .newBuilder()
      .setAny(
        Any
          .newBuilder()
          .setTypeUrl("type.googleapis.com/google.protobuf.Any")
          .setValue(nested.toByteString())
          .build()
      )
      .build();

    JsonNode json = mapper.valueToTree(original);
    JsonNode expected = mapper
      .createObjectNode()
      .set(
        "any",
        mapper
          .createObjectNode()
          .put("@type", "type.googleapis.com/google.protobuf.Any")
          .set(
            "value",
            mapper
              .createObjectNode()
              .put("@type", "type.googleapis.com/google.protobuf.Int64Value")
              .put("value", 123L)
          )
      );

    assertThat(json).isEqualTo(expected);

    HasAny parsed = mapper.treeToValue(json, HasAny.class);

    assertThat(parsed.getAny().getTypeUrl())
      .isEqualTo("type.googleapis.com/google.protobuf.Any");
    assertThat(Any.parseFrom(parsed.getAny().getValue())).isEqualTo(nested);
  }

  private static JsonNode anyNode() {
    String base64 = camelCase()
      .getSerializationConfig()
      .getBase64Variant()
      .encode(VALUE.toByteArray());
    JsonNode valueNode = camelCase()
      .createObjectNode()
      .put("typeUrl", TYPE_URL)
      .put("value", base64);
    return camelCase().createObjectNode().set("any", valueNode);
  }

  private static JsonNode defaultNode() {
    JsonNode valueNode = camelCase()
      .createObjectNode()
      .put("typeUrl", "")
      .put("value", "");
    return camelCase().createObjectNode().set("any", valueNode);
  }
}
