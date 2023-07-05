package ru.arkantos.solr;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.arkantos.solr.builder.BuildSolrQueryException;
import ru.arkantos.solr.builder.JoinOperator;
import ru.arkantos.solr.builder.QueryConstructor;
import ru.arkantos.solr.params.Container;

import java.util.Map;

public class JoinQueryConstructorTest {
    QueryConstructor firstQueryConstructor = QueryConstructor.templateBuilder()
            .queryTemplate("some_field:{#param}")
            .build();
    QueryConstructor secondQueryConstructor = QueryConstructor.templateBuilder()
            .queryTemplate("other_field:{#param}")
            .build();

    @Test
    void testSimpleJoinConstructors() throws BuildSolrQueryException {
        QueryConstructor queryConstructor = QueryConstructor.joinBuilder()
                .add(firstQueryConstructor)
                .add(secondQueryConstructor)
                .joinOperator(JoinOperator.OR)
                .build();

        Container container = new Container(Map.of("param", "val"));
        String query = queryConstructor.buildQuery(container);

        Assertions.assertEquals("(some_field:val) OR (other_field:val)", query);
    }
}
