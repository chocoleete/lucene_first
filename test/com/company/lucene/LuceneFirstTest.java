package com.company.lucene;

import org.junit.Before;
import org.junit.Test;

public class LuceneFirstTest {

    private LuceneFirst luceneFirst;

    @Before
    public void setUp() throws Exception {
        //创建对象
        luceneFirst = new LuceneFirst();
    }

    @Test
    public void testCreateIndex() throws Exception {
        //调用方法
        luceneFirst.createIndex();
    }

    @Test
    public void testSearchIndex() throws Exception {
        //调用方法
        luceneFirst.searchIndex();
    }

    @Test
    public void testTestTokenStream() throws Exception {
        //调用方法
        luceneFirst.testTokenStream();
    }
}