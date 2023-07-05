package ru.arkantos.solr.builder;


import ru.arkantos.solr.params.Container;

public interface QueryConstructor {

    String buildQuery(Container params) throws BuildSolrQueryException;

    boolean isWrapBrackets();

    static TemplateQueryConstructor.Builder templateBuilder() {
        return new TemplateQueryConstructor.Builder();
    }

    static JoinQueryConstructor.Builder joinBuilder() {
        return new JoinQueryConstructor.Builder();
    }

    static CaseQueryConstructor.Builder caseBuilder() {
        return new CaseQueryConstructor.Builder();
    }
}
