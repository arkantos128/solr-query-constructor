package ru.arkantos.solr.builder;

import lombok.NoArgsConstructor;

public abstract class AbstractQueryConstructor implements QueryConstructor {

    protected boolean wrapBrackets;
    protected JoinOperator joinOperator;

    @Override
    public boolean isWrapBrackets() {
        return wrapBrackets;
    }

    public AbstractQueryConstructor(Builder<?> builder) {
        this.wrapBrackets = builder.wrapBrackets;
        this.joinOperator = builder.joinOperator;
    }

    @NoArgsConstructor
    static abstract class Builder<T extends Builder<T>> {
        protected boolean wrapBrackets = true;
        protected JoinOperator joinOperator = JoinOperator.AND;

        abstract T self();

        public abstract QueryConstructor build();

        public T wrapBrackets(boolean wrapBrackets) {
            this.wrapBrackets = wrapBrackets;
            return self();
        }

        public T joinOperator(JoinOperator operator) {
            this.joinOperator = operator;
            return self();
        }
    }

}
