package com.company.lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;

/**
 * Created by lee on 2017/3/3.
 */
@SuppressWarnings(value = "all")
public class SearchIndex {
    /**
     * 得到indexSearcher对象
     * @return
     * @throws IOException
     */
    private IndexSearcher getIndexSearcher() throws IOException {
        //指定索引库存放路径
        String indexPath = "G:\\i_学习\\java\\javascrip\\Day71_lucene&solr_20170301\\lucene&solr\\day01\\资料\\index";
        FSDirectory directory = FSDirectory.open(new File(indexPath));
        //创建一个indexReader对象
        IndexReader indexReader = DirectoryReader.open(directory);
        //创建indexSearcher对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        return indexSearcher;
    }

    /**
     * 查询索引库
     */
    private void printResult(IndexSearcher indexSearcher, Query query) throws IOException {
        /*执行查询
        * 第一个参数：查询对象
        * 第二个参数：查询结果返回的最大值*/
        TopDocs topDocs = indexSearcher.search(query, 10);
        //查询结果的总条数
        int totalHits = topDocs.totalHits;
        System.out.println("查询结果的总条数：" + totalHits);
        /*遍历查询结果
        * topDocs.scoreDocs存储了document的对象id*/
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            /*scoreDoc.doc属性就是document对象的id
            * 根据document的id查找document对象*/
            Document doc = indexSearcher.doc(scoreDoc.doc);
            //打印结果
            System.out.println("fileName:"+doc.get("fileName"));
            System.out.println("size:"+doc.get("size"));
            //System.out.println("content:"+doc.get("content"));
            System.out.println("path:"+doc.get("path"));
        }
        //关闭indexReader
        indexSearcher.getIndexReader().close();
    }

    /**
     * 测试MatchAllDocsQuery()
     */
    public void matchAllDocsQueryDemo() throws IOException {
        //获取indexSearch
        IndexSearcher indexSearcher = this.getIndexSearcher();
        //创建一个Query对象
        Query query = new MatchAllDocsQuery();
        System.out.println(query);
        //执行查询
        printResult(indexSearcher,query);
    }

    /**
     * TermQuery
     */
    public void termQueryDemo() throws IOException {
        //获取indexSearch
        IndexSearcher indexSearcher = this.getIndexSearcher();
        //创建查询条件
        Query query = new TermQuery(new Term("content", "lucene"));
        System.out.println(query);
        //执行查询
        printResult(indexSearcher,query);
    }

    /**
     * NumericRangeQuery
     */
    public void numericRangeQueryDemo() throws IOException {
        //获取indexSearch
        IndexSearcher indexSearcher = this.getIndexSearcher();
        /*创建查询
        * 参数
        * 1、域名
        * 2、最小值
        * 3、最大值
        * 4、是不包含最小值
        * 5、是否包含最大值*/
        Query query = NumericRangeQuery.newLongRange("size", 1L, 1024L, true, true);
        System.out.println(query);
        //执行查询
        printResult(indexSearcher,query);
    }

    /**
     * BooleanQuery
     */
    public void booleanQueryDemo() throws IOException {
        //获取indexSearch对象
        IndexSearcher indexSearcher = this.getIndexSearcher();
        //创建一个boolean查询对象
        BooleanQuery query = new BooleanQuery();
        //创建第一个查询条件
        Query query1 = new TermQuery(new Term("fileName", "apache"));
        //创建第二个查询条件
        Query query2 = new TermQuery(new Term("content", "apache"));
        //组合查询条件
        query.add(query1, BooleanClause.Occur.MUST);
        query.add(query2, BooleanClause.Occur.SHOULD);
        System.out.println(query);
        //执行查询
        printResult(indexSearcher, query);
    }

    /**
     * 使用queryParser
     * 此方法可以直接根据查询语法来查询。Query对象执行的查询语法可通过System.out.println(query);查询。
     * 需要使用到分析器。建议创建索引时使用的分析器和查询索引时使用的分析器要一致
     */
    public void queryParserDemo() throws IOException, ParseException {
        //获取indexSearch
        IndexSearcher indexSearcher = this.getIndexSearcher();
        /*创建queryParser对象
        * 第一个参数：默认搜索的域
        * 第二个参数：分析器对象*/
        QueryParser queryParser = new QueryParser("content", new IKAnalyzer());
        Query query = queryParser.parse("Lucene是java开发的");
        System.out.println(query);
        //执行查询
        printResult(indexSearcher,query);
    }

    /**
     * mulitFieldQueryParser,
     * 可以指定多个默认搜索域
     */
    public void multiFieldQueryParser() throws IOException, ParseException {
        //获取indexSearch对象
        IndexSearcher indexSearcher = this.getIndexSearcher();
        //指定默认搜索域
        String[] fields = {"fileName", "content"};
        /*创建multiFieldQueryParser对象
        * 第一个参数：默认搜索域，参数类型为字符串数组
        * 第二个参数：分析器对象*/
        MultiFieldQueryParser queryParser = new MultiFieldQueryParser(fields, new IKAnalyzer());
        Query query = queryParser.parse("java and apache");
        System.out.println(query);
        //执行查询
        printResult(indexSearcher,query);
    }
}
