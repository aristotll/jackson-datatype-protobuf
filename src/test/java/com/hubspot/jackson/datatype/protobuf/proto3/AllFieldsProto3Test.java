package com.hubspot.jackson.datatype.protobuf.proto3;

import static com.hubspot.jackson.datatype.protobuf.util.ObjectMapperHelper.camelCase;
import static com.hubspot.jackson.datatype.protobuf.util.ObjectMapperHelper.create;
import static com.hubspot.jackson.datatype.protobuf.util.ObjectMapperHelper.underscore;
import static com.hubspot.jackson.datatype.protobuf.util.ObjectMapperHelper.writeAndReadBack;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.hubspot.jackson.datatype.protobuf.ProtobufJacksonConfig;
import com.hubspot.jackson.datatype.protobuf.util.ProtobufCreator;
import com.hubspot.jackson.datatype.protobuf.util.TestProtobuf3.AllFieldsProto3;
import com.hubspot.jackson.datatype.protobuf.util.TestProtobuf3.NestedProto3;
import java.io.IOException;
import java.util.List;
import org.junit.Test;

public class AllFieldsProto3Test {

  @Test
  public void testSingleMessageCamelCase() {
    AllFieldsProto3 message = ProtobufCreator.create(AllFieldsProto3.class);

    AllFieldsProto3 parsed = writeAndReadBack(camelCase(), message);

    assertThat(parsed).isEqualTo(message);
  }

  @Test
  public void testMultipleMessagesCamelCase() {
    List<AllFieldsProto3> messages = ProtobufCreator.create(AllFieldsProto3.class, 10);

    List<AllFieldsProto3> parsed = writeAndReadBack(camelCase(), messages);

    assertThat(parsed).isEqualTo(messages);
  }

  @Test
  public void testSingleBuilderCamelCase() {
    AllFieldsProto3.Builder builder = ProtobufCreator.createBuilder(
      AllFieldsProto3.Builder.class
    );

    AllFieldsProto3.Builder parsed = writeAndReadBack(camelCase(), builder);

    assertThat(parsed.build()).isEqualTo(builder.build());
  }

  @Test
  public void testMultipleBuildersCamelCase() {
    List<AllFieldsProto3.Builder> builders = ProtobufCreator.createBuilder(
      AllFieldsProto3.Builder.class,
      10
    );

    List<AllFieldsProto3.Builder> parsed = writeAndReadBack(camelCase(), builders);

    assertThat(build(parsed)).isEqualTo(build(builders));
  }

  @Test
  public void testSingleMessageUnderscore() {
    AllFieldsProto3 message = ProtobufCreator.create(AllFieldsProto3.class);

    AllFieldsProto3 parsed = writeAndReadBack(underscore(), message);

    assertThat(parsed).isEqualTo(message);
  }

  @Test
  public void testMultipleMessagesUnderscore() {
    List<AllFieldsProto3> messages = ProtobufCreator.create(AllFieldsProto3.class, 10);

    List<AllFieldsProto3> parsed = writeAndReadBack(underscore(), messages);

    assertThat(parsed).isEqualTo(messages);
  }

  @Test
  public void testSingleBuilderUnderscore() {
    AllFieldsProto3.Builder builder = ProtobufCreator.createBuilder(
      AllFieldsProto3.Builder.class
    );

    AllFieldsProto3.Builder parsed = writeAndReadBack(underscore(), builder);

    assertThat(parsed.build()).isEqualTo(builder.build());
  }

  @Test
  public void testMultipleBuildersUnderscore() {
    List<AllFieldsProto3.Builder> builders = ProtobufCreator.createBuilder(
      AllFieldsProto3.Builder.class,
      10
    );

    List<AllFieldsProto3.Builder> parsed = writeAndReadBack(underscore(), builders);

    assertThat(build(parsed)).isEqualTo(build(builders));
  }

  @Test
  public void testEmptyNestedObject() throws IOException {
    String json = "{\"nested\":{}}";

    AllFieldsProto3 parsed = camelCase().readValue(json, AllFieldsProto3.class);

    assertThat(parsed.getNested()).isEqualTo(NestedProto3.getDefaultInstance());
  }

  @Test
  public void itSerializesLongsAsStringsIfEnabled() throws IOException {
    ObjectMapper mapper = create(
      ProtobufJacksonConfig.builder().serializeLongsAsStrings(true).build()
    );

    AllFieldsProto3 original = AllFieldsProto3
      .newBuilder()
      .setInt64(123)
      .setUint64(456)
      .build();

    JsonNode json = mapper.valueToTree(original);

    assertThat(json.path("int64").isTextual());
    assertThat(json.get("int64").textValue()).isEqualTo("123");
    assertThat(json.path("uint64").isTextual());
    assertThat(json.get("uint64").textValue()).isEqualTo("456");

    AllFieldsProto3 parsed = mapper.treeToValue(json, AllFieldsProto3.class);

    assertThat(parsed.getInt64()).isEqualTo(123);
    assertThat(parsed.getUint64()).isEqualTo(456);
  }

  private static List<AllFieldsProto3> build(List<AllFieldsProto3.Builder> builders) {
    return Lists.transform(builders, AllFieldsProto3.Builder::build);
  }
}
