package ru.arkantos.solr;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.arkantos.solr.params.Container;
import ru.arkantos.solr.params.converter.Converter;
import ru.arkantos.solr.params.converter.pipeline.ConverterPipeline;

import java.util.List;
import java.util.Map;

public class ConverterTest {

    static ConverterPipeline converterPipeline;
    static Map<String, Object> expectedData;

    @BeforeAll
    static void beforeAll() {
        Converter<String, Integer> intParser = Converter
                .defaultConverter("outerField.inner_field", "inner_value", Integer::parseInt);

        Converter<List<String>, List<String>> simpleExtractor = Converter
                .defaultConverter("baseField", "base_value");

        Converter<List<String>, List<String>> listExtractor = Converter
                .defaultConverter("baseField.0", "list_value");

        converterPipeline = ConverterPipeline.defaultPipeline(intParser, simpleExtractor, listExtractor);
        expectedData = Map.of(
                "inner_value", 156,
                "base_value", List.of(1, 2, 3),
                "list_value", 1);
    }

    @Test
    void testMapConverter() {
        Map<String, Object> testData = Map.of(
                "outerField", Map.of("inner_field", "156"),
                "baseField", List.of(1, 2, 3)
        );
        Container container = converterPipeline.convert(testData);
        Assertions.assertEquals(expectedData, container.params());
    }

    @Test
    void testObjectConverter() {
        TestData testData = new TestData(Map.of("inner_field", "156"), List.of(1, 2, 3));
        Container container = converterPipeline.convert(testData);
        Assertions.assertEquals(expectedData, container.params());
    }

    @Getter
    @AllArgsConstructor
    public static class TestData {
        private Map<String, String> outerField;
        private List<Integer> baseField;
    }
}
