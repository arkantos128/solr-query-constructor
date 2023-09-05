package ru.arkantos.solr;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.arkantos.solr.limit.DefaultSolrFacet;
import ru.arkantos.solr.limit.SolrFacet;
import ru.arkantos.solr.limit.SolrFacetException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

class SolrFacetTest {
    SolrFacet solrFacet = new DefaultSolrFacet(new ObjectMapper());
    static Map<String, Long> correctLimitOtherFacet;

    @BeforeAll
    static void beforeAll() {
        correctLimitOtherFacet = Map.of(
                "first field", 61L,
                "second field", 49L,
                "third field", 35L,
                "other", 31L + 30L + 21L + 16L
        );
    }

    @Test
    void testLimitAddOtherFacet() throws IOException, SolrFacetException {
        String path = this.getClass().getResource("/facet-test-data.json").getPath();
        String solrResponse = Files.readString(Paths.get(path));

        var facetData = solrFacet.getFacetData(solrResponse);
        var limitFacetData = facetData.limitAndAddOtherField(3, "other");

        Assertions.assertEquals(correctLimitOtherFacet, limitFacetData.getFacetMap());
    }
}
