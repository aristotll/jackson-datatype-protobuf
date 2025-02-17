package com.hubspot.jackson.datatype.protobuf;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubspot.jackson.datatype.protobuf.util.TestProtobuf.AllFields;
import com.hubspot.jackson.datatype.protobuf.util.TestProtobuf.Enum;
import org.junit.Test;

import static com.hubspot.jackson.datatype.protobuf.util.ObjectMapperHelper.camelCase;
import static org.assertj.core.api.Assertions.assertThat;

public class ReadUnknownEnumValuesAsNullTest {

    @Test
    public void testStringEnabled() throws JsonProcessingException {
        ObjectMapper mapper = objectMapper(true);

        AllFields parsed = mapper.treeToValue(buildNode("fakeValue"), AllFields.class);
        assertThat(parsed.hasEnum()).isFalse();
    }

    @Test(expected = JsonMappingException.class)
    public void testStringDisabled() throws JsonProcessingException {
        ObjectMapper mapper = objectMapper(false);

        mapper.treeToValue(buildNode("fakeValue"), AllFields.class);
    }

    @Test
    public void testUnknownEnumValue2() throws JsonProcessingException {
        ObjectMapper mapper = objectMapper(false);

        AllFields parsed = mapper.treeToValue(buildNode("UNKNOWN_ENUM_VALUE_Enum_272"), AllFields.class);
        Enum anEnum = parsed.getEnum();
    }

    @Test
    public void testIntEnabled() throws JsonProcessingException {
        ObjectMapper mapper = objectMapper(true);

        AllFields parsed = mapper.treeToValue(buildNode(999999), AllFields.class);
        assertThat(parsed.hasEnum()).isFalse();
    }

    @Test(expected = JsonMappingException.class)
    public void testIntDisabled() throws JsonProcessingException {
        ObjectMapper mapper = objectMapper(false);

        mapper.treeToValue(buildNode(999999), AllFields.class);
    }

    private static JsonNode buildNode(String value) {
        return camelCase().createObjectNode().put("enum", value);
    }

    private static JsonNode buildNode(int value) {
        return camelCase().createObjectNode().put("enum", value);
    }

    private static ObjectMapper objectMapper(boolean enabled) {
        if (enabled) {
            return camelCase().enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL);
        } else {
            return camelCase().disable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL);
        }
    }
}
