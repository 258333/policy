package com.example.es;

import cn.hutool.json.JSONUtil;
import com.example.pojo.PolicyDoc;
import com.example.utils.CollUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author HongWei
 * @Date 2024/11/30 10:21
 */
@SpringBootTest(properties = "spring.profiles.active=local")
@Slf4j
public class ElasticSearch {

    private RestHighLevelClient client;
    private static final String INDEX_NAME = "policy";  // 您的索引名

    private void handleResponse(SearchResponse response) {
        SearchHits searchHits = response.getHits();
        // 1.获取总条数
        long total = searchHits.getTotalHits().value;
        System.out.println("共搜索到" + total + "条数据");
        // 2.遍历结果数组
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit hit : hits) {
            // 3.得到_source，也就是原始json文档
            String source = hit.getSourceAsString();
            // 4.反序列化并打印
            PolicyDoc item = JSONUtil.toBean(source, PolicyDoc.class);
            System.out.println(item);
        }
    }

    @Test
    void testBool() throws IOException {
        int pageNo = 1, pageSize = 5;
        String[] types = new String[]{};
        // 1.创建Request
        SearchRequest request = new SearchRequest(INDEX_NAME);
        // 2.组织请求参数
        // 2.1.准备bool查询
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        // 2.2.关键字搜索
        // 针对每个字段使用 match 查询

        boolQuery.must(QueryBuilders.matchQuery("name", "中共中央国务院"));


        boolQuery.must(QueryBuilders.matchQuery("document", "中发"));


        boolQuery.must(QueryBuilders.matchQuery("organ", "国务院"));


        boolQuery.must(QueryBuilders.matchQuery("text", "碳"));

        // AND 关系下，type 字段的精确匹配
        if (types != null && types.length > 0) {
            // 使用 filter 来实现 type 字段的 OR 关系查询
            boolQuery.filter(QueryBuilders.termsQuery("type", types)); // 精确匹配 type 字段的任意值
        }
        //查询
        request.source().query(boolQuery);
        // 2.1.分页
        request.source().from((pageNo - 1) * pageSize).size(pageSize);
        // 2.2.高亮条件
        request.source().highlighter(
                SearchSourceBuilder.highlight()
                        .field("name")
                        .preTags("<em>")
                        .postTags("</em>")
        );

        // 3.发送请求
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        // 4.解析响应
        handleHighLightResponse(response);
    }


    private void handleHighLightResponse(SearchResponse response) {
        SearchHits searchHits = response.getHits();
        // 1.获取总条数
        long total = searchHits.getTotalHits().value;
        System.out.println("共搜索到" + total + "条数据");
        // 2.遍历结果数组
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit hit : hits) {
            // 3.得到_source，也就是原始json文档
            String source = hit.getSourceAsString();
            // 4.反序列化
            PolicyDoc policyDoc = JSONUtil.toBean(source, PolicyDoc.class);
            // 5.获取高亮结果
            Map<String, HighlightField> hfs = hit.getHighlightFields();
            if (CollUtils.isNotEmpty(hfs)) {
                // 5.1.有高亮结果，获取text的高亮结果
                HighlightField hf = hfs.get("name");
                if (hf != null) {
                    // 5.2.获取第一个高亮结果片段，就是商品名称的高亮值
                    String hfName = hf.getFragments()[0].string();
                    policyDoc.setName(hfName);
                }
            }
            System.out.println(policyDoc);
        }
    }

    @BeforeEach
    void setUp() {
        this.client = new RestHighLevelClient(RestClient.builder(
                HttpHost.create("http://192.168.88.128:9200")
        ));
    }

    @AfterEach
    void tearDown() throws IOException {
        if (client != null) {
            client.close();
        }
    }

    @Test
    void testConnection() {
        System.out.println("client = " + client);
    }

}
