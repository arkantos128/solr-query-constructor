package ru.arkantos.solr;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.arkantos.solr.builder.BuildSolrQueryException;
import ru.arkantos.solr.builder.QueryConstructor;
import ru.arkantos.solr.params.Container;

import java.util.Map;

public class CaseQueryConstructorTest {
    QueryConstructor firstQueryConstructor = QueryConstructor.templateBuilder()
            .queryTemplate("true_field:{#param}")
            .build();
    QueryConstructor secondQueryConstructor = QueryConstructor.templateBuilder()
            .queryTemplate("false_field:{#param}")
            .build();

    @Test
    void testSimpleSelectByFieldValue() throws BuildSolrQueryException {
        QueryConstructor queryConstructor = QueryConstructor.caseBuilder()
                .caseParamKey("condition")
                .addCase("true", firstQueryConstructor)
                .addCase("false", secondQueryConstructor)
                .build();
        Container container = new Container(Map.of(
                "condition", "true",
                "param", "value"));
        String query = queryConstructor.buildQuery(container);

        Assertions.assertEquals("true_field:value", query);

        container = new Container(Map.of(
                "condition", "false",
                "param", "value"));
        query = queryConstructor.buildQuery(container);

        Assertions.assertEquals("false_field:value", query);
    }
}
