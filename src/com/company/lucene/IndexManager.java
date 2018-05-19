package com.company.lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;

/**
 * Created by lee on 2017/3/1.
 */
@SuppressWarnings(value = "all")
public class IndexManager {
    /**
     * 获取indexWriter
     */
    public IndexWriter getIndexWriter() throws IOException {
        // 索引库存放路径
        String path = "G:\\i_学习\\java\\javascrip\\Day71_lucene&solr_20170301\\lucene&solr\\day01\\资料\\index";
        FSDirectory directory = FSDirectory.open(new File(path));
        /**
         * 创建一个indexWriterConfig对象
         * 第一个参数： Lucene的版本信息，可以选择对应的lucene版本也可以使用LATEST
         * 第二根参数：分析器对象*/
        IndexWriterConfig conf = new IndexWriterConfig(Version.LATEST, new IKAnalyzer());
        // 创建一个indexWriter对象
        IndexWriter indexWriter = new IndexWriter(directory, conf);
        return indexWriter;
    }

    /**
     * 添加索引
     */
    public void addDocument() throws IOException {
        IndexWriter indexWriter = this.getIndexWriter();
        // 创建一个document对象
        Document document = new Document();
        /**
         * 向document对象中添加域
         * 不同的document可以有不同的域，同一个document可以有相同的域*/
        document.add(new TextField("fileName","新添加的文档.txt", Field.Store.YES));
        document.add(new TextField("content","新添加的文档的内容", Field.Store.YES));
        document.add(new TextField("content","新添加的文档的内容，第二个content", Field.Store.YES));
        document.add(new TextField("content1", "新添加的文档的内容要能看到", Field.Store.YES));
        // 添加文档到索引库
        indexWriter.addDocument(document);
        // 关闭indexWriter
        indexWriter.close();
     }

    /**
     * 删除全部索引 慎用
     */
    public void deleteAllIndex() throws IOException {
        // 获取indexWriter
        IndexWriter indexWriter = this.getIndexWriter();
        // 删除全部
        indexWriter.deleteAll();
        // 关闭indexWriter
        indexWriter.close();
    }

    /**
     * 根据查询条件删除索引
     */
    public void deleteIndexByQuery() throws IOException {
        // 创建indexWriter对象
        IndexWriter indexWriter = this.getIndexWriter();
        // 创建一个查询条件
        Query query = new TermQuery(new Term("fileName", "apache"));
        // 根据查询条件删除
        indexWriter.deleteDocuments(query);
        // 关闭indexWriter
        indexWriter.close();
    }

    /**
     * 修改索引库
     */
    public void updateIndex() throws IOException {
        // 创建indexWriter对象
        IndexWriter indexWriter = this.getIndexWriter();
        // 创建一个document对象
        Document document = new Document();
        /**
         * 向document对象中添加域
         * 不同的document可以有不同的域，同一个document可以有相同的域*/
        document.add(new TextField("fileName","要更新的文档", Field.Store.YES));
        document.add(new TextField("content","2013年11月18日 - Lucene 简介 Lucene 是一个基于 Java 的全文信息检索工具包,它不是一个完整的搜索应用程序,而是为你的应用程序提供索引和搜索功能。", Field.Store.YES));
        // 更新内容,将"java"更新为document中的内容
        indexWriter.updateDocument(new Term("content","java"),document);
        // 关闭indexWriter
        indexWriter.close();
     }
}
