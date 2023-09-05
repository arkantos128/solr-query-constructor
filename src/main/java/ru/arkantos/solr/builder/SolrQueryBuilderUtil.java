package ru.arkantos.solr.builder;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Collection;

@UtilityClass
public class SolrQueryBuilderUtil {

    private final char[] MASK_LUCENE_ARR = {'"', '+', '-', '!', '(', ')', '{', '}', '[', ']', '^', '\\', '~', '*', '?', ':'};

    public String escapeQueryChars(String value) {
        StringBuilder bf = new StringBuilder(value.length() + 100);

        for (int i = 0; i < value.length(); i++) {
            char ch = value.charAt(i);
            for (char c : MASK_LUCENE_ARR) {
                if (c == ch) {
                    bf.append('\\');
                    break;
                }
            }
            bf.append(ch);
        }

        return bf.toString();
    }

    public Collection<String> escapeQueryChars(Collection<String> values) {
        Collection<String> resLst = new ArrayList<>();
        for (String value : values) {
            resLst.add(escapeQueryChars(value));
        }
        return resLst;
    }

}
