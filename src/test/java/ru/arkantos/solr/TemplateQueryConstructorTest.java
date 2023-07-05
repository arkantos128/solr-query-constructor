package ru.arkantos.solr;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.arkantos.solr.builder.BuildSolrQueryException;
import ru.arkantos.solr.builder.JoinOperator;
import ru.arkantos.solr.builder.QueryConstructor;
import ru.arkantos.solr.params.Container;

import java.util.List;
import java.util.Map;

public class TemplateQueryConstructorTest {

    @Test
    void testSimpleStringValue() throws BuildSolrQueryException {
        QueryConstructor queryConstructor = QueryConstructor.templateBuilder()
                .queryTemplate("field:{#param|default_value}")
                .build();

        Container container = new Container(Map.of("param", "value"));
        String query = queryConstructor.buildQuery(container);
        Assertions.assertEquals("field:value", query);

        container = new Container(Map.of("other_param", "empty"));
        query = queryConstructor.buildQuery(container);
        Assertions.assertEquals("field:default_value", query);
    }

    @Test
    void testMultivaluedParam() throws BuildSolrQueryException {
        QueryConstructor queryConstructor = QueryConstructor.templateBuilder()
                .queryTemplate("field:{#param}")
                .joinOperator(JoinOperator.OR)
                .build();

        Container container = new Container(Map.of("param", List.of("value1", "value2")));
        String query = queryConstructor.buildQuery(container);
        Assertions.assertEquals("(field:value1) OR (field:value2)", query);
    }

    @Test
    void testPrefixAndSuffix() throws BuildSolrQueryException {
        QueryConstructor queryConstructor = QueryConstructor.templateBuilder()
                .queryTemplate("field:{#param}")
                .prefix("<")
                .suffix(">")
                .build();

        Container container = new Container(Map.of("param", List.of("value1", "value2")));
        String query = queryConstructor.buildQuery(container);
        Assertions.assertEquals("<(field:value1) AND (field:value2)>", query);
    }
}
