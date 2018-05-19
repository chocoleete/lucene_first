package com.company.lucene;

import org.junit.Before;
import org.junit.Test;

public class IndexManagerTest {

    private IndexManager indexManager;

    @Before
    public void setUp() throws Exception {
        // 创建对象
        indexManager = new IndexManager();
    }

    @Test
    public void testAddDocument() throws Exception {
        // 调用方法
        indexManager.addDocument();
    }

    @Test
    public void testDeleteAllIndex() throws Exception {
        // 调用方法
        indexManager.deleteAllIndex();
    }

    @Test
    public void testDeleteIndexByQuery() throws Exception {
        // 调用方法
        indexManager.deleteIndexByQuery();
    }

    @Test
    public void testUpdateIndex() throws Exception {
        // 调用方法
        indexManager.updateIndex();
    }
}