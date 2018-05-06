package com.company.lucene;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;

/**
 * Created by lee on 2017/3/1.
 */
@SuppressWarnings(value = "all")
public class LuceneFirst {
    /**
     * 创建索引库
     * @throws IOException
     */
    public void createIndex() throws IOException {
        // 指定索引库存入路径
        String path = "G:\\i_学习\\java\\javascrip\\Day71_lucene&solr_20170301\\lucene&solr\\day01\\资料\\index";
        FSDirectory directory = FSDirectory.open(new File(path));
        // 也可将索引库放入内存，一般不建议这样
        //Directory ramDirectory = new RAMDirectory();
        // 创建一个标准分析器对象
        //Analyzer analyzer = new StandardAnalyzer();
        Analyzer analyzer = new IKAnalyzer();
        // 创建indexWriterConfig对象
        /**
         * 第一个参数： Lucene的版本信息，可以选择对应的lucene版本也可以使用LATEST
         * 第二根参数：分析器对象*/
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LATEST,analyzer);
        // 创建indexWriter对象
        /**
         * 第一个参数是索引库存入路径
         * 第二个参数是indexWriterConfig对象*/
        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
        // 源文件路径
        String sourcePath = "G:\\i_学习\\java\\javascrip\\Day71_lucene&solr_20170301\\lucene&solr\\day01\\资料\\searchsource";
        File dir = new File(sourcePath);
        // 遍历文件夹中的文件
        for (File file : dir.listFiles()) {
            // 文件名
            String fileName = file.getName();
            // 文件内容
            String fileContent = FileUtils.readFileToString(file);
            // 文件路径
            String filePath = file.getPath();
            // 文件大小
            long sizeOfFile = FileUtils.sizeOf(file);
            /**
             * 创建文件名域
             * 第一个参数：域的名称
             * 第二个参数：域的内容
             * 第三个参数：是否存储*/
            // 文件名
            Field fileNameField = new TextField("fileName", fileName, Field.Store.YES);
            // 文件内容
            Field contentField = new TextField("content", fileContent, Field.Store.YES);
            // 文件路径
            Field pathField = new StoredField("path", filePath);
            // 文件大小
            Field sizeField = new LongField("size", sizeOfFile, Field.Store.YES);
            // 创建document对象
            Document document = new Document();
            document.add(fileNameField);
            document.add(contentField);
            document.add(pathField);
            document.add(sizeField);
            // 创建索引，并写入索引库
            indexWriter.addDocument(document);
        }
        // 关闭indexWriter
        indexWriter.close();
    }

    /**
     * 查询索引库
     */
    public void searchIndex() throws IOException {
        // 指定索引库存放路径
        String path = "G:\\i_学习\\java\\javascrip\\Day71_lucene&solr_20170301\\lucene&solr\\day01\\资料\\index";
        Directory directory = FSDirectory.open(new File(path));
        // 创建indexRead对象
        IndexReader indexReader = DirectoryReader.open(directory);
        // 创建indexSearch对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        // 设置查询条件
        Query query = new TermQuery(new Term("fileName", "spring"));
        /**
         * 执行查询
         * 第一个参数：查询对象
         * 第二个参数：查询结果返回的最大值*/
        TopDocs topDocs = indexSearcher.search(query, 10);
        // 查询结果的总条数
        int totalHits = topDocs.totalHits;
        System.out.println("查询结果的总条数：" + totalHits);
        /**
         * 遍历查询结果
         * topDocs.scoreDocs存储了document的对象id*/
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            /**
             * scoreDoc.doc属性就是document对象的id
             * 根据document的id查找document对象*/
            Document doc = indexSearcher.doc(scoreDoc.doc);
            // 打印结果
            System.out.println("fileName:" + doc.get("fileName"));
            System.out.println("content:" + doc.get("content"));
            System.out.println("path:" + doc.get("path"));
            System.out.println("size:" + doc.get("size"));
        }
        indexReader.close();
     }

    /**
     * 查看标准分析器的分词效果
     */
    public void testTokenStream() throws IOException {
        // 创建一个标准分析器对象
        Analyzer standardAnalyzer = new StandardAnalyzer();
        /**获得tokenStream对象
         * 第一个参数：域名，可以随便给一个
         * 第二个参数：要分析的文本内容*/
        TokenStream tokenStream = standardAnalyzer.tokenStream("test", "The Spring Framework provides a comprehensive programming and configuration model.");
        // 添加一个引用，可以获得每个关键词
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        // 添加一个偏移量的引用，记录关键词的开始位置及结束位置
        OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
        // 将指针调整到列表头部
        tokenStream.reset();
        // 遍历关键词列表，通过incrementToken方法判断列表是否结束
        while (tokenStream.incrementToken()) {
            // 关键词的起始位置
            System.out.println("start-->"+offsetAttribute.startOffset());
            // 取关键词
            System.out.println(charTermAttribute);
            // 关键词的结束位置
            System.out.println("end-->"+offsetAttribute.endOffset());
        }
        // 关流
        tokenStream.close();
    }
}
