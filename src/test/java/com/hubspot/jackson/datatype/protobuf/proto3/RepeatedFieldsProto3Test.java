package com.hubspot.jackson.datatype.protobuf.proto3;

import static com.hubspot.jackson.datatype.protobuf.util.ObjectMapperHelper.camelCase;
import static com.hubspot.jackson.datatype.protobuf.util.ObjectMapperHelper.create;
import static com.hubspot.jackson.datatype.protobuf.util.ObjectMapperHelper.underscore;
import static com.hubspot.jackson.datatype.protobuf.util.ObjectMapperHelper.writeAndReadBack;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.hubspot.jackson.datatype.protobuf.ProtobufJacksonConfig;
import com.hubspot.jackson.datatype.protobuf.util.ProtobufCreator;
import com.hubspot.jackson.datatype.protobuf.util.TestProtobuf3.RepeatedFieldsProto3;
import java.io.IOException;
import java.util.List;
import org.junit.Test;

public class RepeatedFieldsProto3Test {

  @Test
  public void testSingleMessageCamelCase() {
    RepeatedFieldsProto3 message = ProtobufCreator.create(RepeatedFieldsProto3.class);

    RepeatedFieldsProto3 parsed = writeAndReadBack(camelCase(), message);

    assertThat(parsed).isEqualTo(message);
  }

  @Test
  public void testMultipleMessagesCamelCase() {
    List<RepeatedFieldsProto3> messages = ProtobufCreator.create(
      RepeatedFieldsProto3.class,
      10
    );

    List<RepeatedFieldsProto3> parsed = writeAndReadBack(camelCase(), messages);

    assertThat(parsed).isEqualTo(messages);
  }

  @Test
  public void testSingleBuilderCamelCase() {
    RepeatedFieldsProto3.Builder builder = ProtobufCreator.createBuilder(
      RepeatedFieldsProto3.Builder.class
    );

    RepeatedFieldsProto3.Builder parsed = writeAndReadBack(camelCase(), builder);

    assertThat(parsed.build()).isEqualTo(builder.build());
  }

  @Test
  public void testMultipleBuildersCamelCase() {
    List<RepeatedFieldsProto3.Builder> builders = ProtobufCreator.createBuilder(
      RepeatedFieldsProto3.Builder.class,
      10
    );

    List<RepeatedFieldsProto3.Builder> parsed = writeAndReadBack(camelCase(), builders);

    assertThat(build(parsed)).isEqualTo(build(builders));
  }

  @Test
  public void testSingleMessageUnderscore() {
    RepeatedFieldsProto3 message = ProtobufCreator.create(RepeatedFieldsProto3.class);

    RepeatedFieldsProto3 parsed = writeAndReadBack(underscore(), message);

    assertThat(parsed).isEqualTo(message);
  }

  @Test
  public void testMultipleMessagesUnderscore() {
    List<RepeatedFieldsProto3> messages = ProtobufCreator.create(
      RepeatedFieldsProto3.class,
      10
    );

    List<RepeatedFieldsProto3> parsed = writeAndReadBack(underscore(), messages);

    assertThat(parsed).isEqualTo(messages);
  }

  @Test
  public void testSingleBuilderUnderscore() {
    RepeatedFieldsProto3.Builder builder = ProtobufCreator.createBuilder(
      RepeatedFieldsProto3.Builder.class
    );

    RepeatedFieldsProto3.Builder parsed = writeAndReadBack(underscore(), builder);

    assertThat(parsed.build()).isEqualTo(builder.build());
  }

  @Test
  public void testMultipleBuildersUnderscore() {
    List<RepeatedFieldsProto3.Builder> builders = ProtobufCreator.createBuilder(
      RepeatedFieldsProto3.Builder.class,
      10
    );

    List<RepeatedFieldsProto3.Builder> parsed = writeAndReadBack(underscore(), builders);

    assertThat(build(parsed)).isEqualTo(build(builders));
  }

  @Test
  public void itSerializesLongsAsStringsIfEnabled() throws IOException {
    ObjectMapper mapper = create(
      ProtobufJacksonConfig.builder().serializeLongsAsStrings(true).build()
    );

    RepeatedFieldsProto3 original = RepeatedFieldsProto3
      .newBuilder()
      .addInt64(123)
      .addUint64(456)
      .build();

    JsonNode json = mapper.valueToTree(original);

    assertThat(json.path("int64").isArray());
    assertThat(json.get("int64").size()).isEqualTo(1);
    assertThat(json.get("int64").get(0).isTextual()).isTrue();
    assertThat(json.get("int64").get(0).textValue()).isEqualTo("123");
    assertThat(json.path("uint64").isArray());
    assertThat(json.get("uint64").size()).isEqualTo(1);
    assertThat(json.get("uint64").get(0).isTextual()).isTrue();
    assertThat(json.get("uint64").get(0).textValue()).isEqualTo("456");

    RepeatedFieldsProto3 parsed = mapper.treeToValue(json, RepeatedFieldsProto3.class);

    assertThat(parsed.getInt64List()).isEqualTo(ImmutableList.of(123L));
    assertThat(parsed.getUint64List()).isEqualTo(ImmutableList.of(456L));
  }

  private static List<RepeatedFieldsProto3> build(
    List<RepeatedFieldsProto3.Builder> builders
  ) {
    return Lists.transform(builders, RepeatedFieldsProto3.Builder::build);
  }
}
