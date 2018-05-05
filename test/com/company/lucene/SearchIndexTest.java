package com.company.lucene;

import org.junit.Before;
import org.junit.Test;

public class SearchIndexTest {

    private SearchIndex searchIndex;
    @Before
    public void setUp() throws Exception {
        searchIndex = new SearchIndex();
    }

    @Test
    public void testMatchAllDocsQueryDemo() throws Exception {
        //匹配所有索引
        searchIndex.matchAllDocsQueryDemo();
    }

    @Test
    public void testTermQueryDemo() throws Exception {
        //按条件查询
        searchIndex.termQueryDemo();
    }

    @Test
    public void testNumericRangeQueryDemo() throws Exception {
        //根据数值范围查询
        searchIndex.numericRangeQueryDemo();
    }

    @Test
    public void testBooleanQueryDemo() throws Exception {
        //组合查询
        searchIndex.booleanQueryDemo();
    }

    @Test
    public void testQueryParserDemo() throws Exception {
        //使用queryParser查询
        searchIndex.queryParserDemo();
    }

    @Test
    public void testMultiFieldQueryParser() throws Exception {
        //查询多个域
        searchIndex.multiFieldQueryParser();
    }
}