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
    static Map<String, Integer> correctLimitOtherFacet;

    @BeforeAll
    static void beforeAll() {
        correctLimitOtherFacet = Map.of(
                "first field", 61,
                "second field", 49,
                "third field", 35,
                "other", 31 + 30 + 21 + 16
        );
    }

    @Test
    void testLimitAddOtherFacet() throws IOException, SolrFacetException {
        String path = this.getClass().getResource("/facet-test-data.json").getPath();
        String solrResponse = Files.readString(Paths.get(path));

        Map<String, Integer> facetDataMap = solrFacet.getFacetData(solrResponse);
        facetDataMap = solrFacet.limitAndAddOtherField(facetDataMap);

        Assertions.assertEquals(correctLimitOtherFacet, facetDataMap);
    }
}
