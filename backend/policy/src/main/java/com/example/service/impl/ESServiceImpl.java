package com.example.service.impl;

import cn.hutool.json.JSONUtil;
import com.example.pojo.PageBean;
import com.example.pojo.Params;
import com.example.pojo.PolicyDoc;
import com.example.pojo.Result;
import com.example.service.ESService;
import com.example.utils.CollUtils;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author HongWei
 * @Date 2024/11/30 14:36
 */
@Service
@RequiredArgsConstructor
public class ESServiceImpl implements ESService {

    private final RestHighLevelClient client;

    private static final String INDEX_NAME = "policy";  // 您的索引名

    @Override
    public PageBean getFirstList(Params params) throws IOException {
        int pageNo = 1, pageSize = 5;
        String[] types = new String[]{};
        // 1.创建Request
        SearchRequest request = new SearchRequest(INDEX_NAME);
        // 2.组织请求参数
        // 2.1.准备bool查询
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        // 2.2.关键字搜索
        if (params.getOptionValue().equals("AND")){
            // 针对每个字段使用 match 查询
            if (params.getName() != null && !params.getName().isEmpty()) {
                boolQuery.must(QueryBuilders.matchQuery("name", params.getName()));
            }
            if (params.getDocument() != null && !params.getDocument().isEmpty()) {
                boolQuery.must(QueryBuilders.matchQuery("document", params.getDocument()));
            }
            if (params.getOrgan() != null && !params.getOrgan().isEmpty()) {
                boolQuery.must(QueryBuilders.matchQuery("organ", params.getOrgan()));
            }
            if (params.getText() != null && !params.getText().isEmpty()) {
                boolQuery.must(QueryBuilders.matchQuery("text", params.getText()));
            }
            // AND 关系下，type 字段的精确匹配
            if (params.getCheckList() != null && !params.getCheckList().isEmpty()) {
                // 使用 filter 来实现 type 字段的 OR 关系查询
                boolQuery.filter(QueryBuilders.termsQuery("type", params.getCheckList())); // 精确匹配 type 字段的任意值
            }
        }else {
            // 使用 should 来实现 OR 关系
            if (params.getName() != null && !params.getName().isEmpty()) {
                boolQuery.should(QueryBuilders.matchQuery("name", params.getName()));
            }
            if (params.getDocument() != null && !params.getDocument().isEmpty()) {
                boolQuery.should(QueryBuilders.matchQuery("document", params.getDocument()));
            }
            if (params.getOrgan() != null && !params.getOrgan().isEmpty()) {
                boolQuery.should(QueryBuilders.matchQuery("organ", params.getOrgan()));
            }
            if (params.getText() != null && !params.getText().isEmpty()) {
                boolQuery.should(QueryBuilders.matchQuery("text", params.getText()));
            }

            // OR 关系下，type 字段的精确匹配
            if (params.getCheckList() != null && !params.getCheckList().isEmpty()) {
                // 使用 should 来实现 type 字段的 OR 关系查询
                boolQuery.should(QueryBuilders.termsQuery("type", params.getCheckList())); // 精确匹配 type 字段的任意值
            }

        }
        //查询
        request.source().query(boolQuery);
        // 2.1.分页
        request.source().from((params.getPageNum() - 1) * params.getPageSize()).size(params.getPageSize());
        // 2.2.高亮条件
        request.source().highlighter(
                SearchSourceBuilder.highlight()
                        .field("name")
                        .preTags("<em style='color: red;'>")
                        .postTags("</em>")
        );
        // 3.发送请求
        SearchResponse response = null;
        try {
            response = client.search(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 4.解析响应
        return handleHighLightResponse(response);
    }

    private PageBean handleHighLightResponse(SearchResponse response) {
        SearchHits searchHits = response.getHits();
        // 1.获取总条数
        long total = searchHits.getTotalHits().value;
        System.out.println("共搜索到" + total + "条数据");
        // 2.遍历结果数组
        SearchHit[] hits = searchHits.getHits();
        List<PolicyDoc> policyDocs = new ArrayList<>();
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
            policyDocs.add(policyDoc);
        }
        return new PageBean(total, policyDocs);
    }

}
