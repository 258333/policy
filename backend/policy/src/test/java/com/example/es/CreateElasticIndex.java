package com.example.es;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.pojo.PageBean;
import com.example.pojo.Policy;
import com.example.pojo.PolicyDoc;
import com.example.service.PolicyService;
import com.example.utils.CollUtils;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.List;

/**
 * @Author HongWei
 * @Date 2024/11/29 9:03
 */

@SpringBootTest(properties = "spring.profiles.active=local")
@Slf4j
@RequiredArgsConstructor
public class CreateElasticIndex {

    private RestHighLevelClient client;
    @Autowired
    private PolicyService policyService;

    static final String MAPPING_TEMPLATE = """
            {
              "mappings": {
                "properties": {
                  "id": {
                    "type": "keyword"
                  },
                  "name": {
                    "type": "text",
                    "analyzer": "ik_max_word"
                  },
                  "type": {
                    "type": "keyword"
                  },
                  "category": {
                    "type": "keyword"
                  },
                  "range": {
                    "type": "text",
                    "analyzer": "ik_max_word"
                  },
                  "document": {
                    "type": "text",
                    "analyzer": "ik_max_word"
                  },
                  "form": {
                    "type": "text",
                    "analyzer": "ik_max_word"
                  },
                  "organ": {
                    "type": "text",
                    "analyzer": "ik_max_word"
                  },
                  "viadata": {
                    "type": "date"
                  },
                  "pubdata": {
                    "type": "date"
                  },
                  "perdata": {
                    "type": "date"
                  },
                  "text": {
                    "type": "text",
                    "analyzer": "ik_max_word"
                  }
                }
              }
            }""";


    /**
     * 创建policy的索引
     **/
    @Test
    void testCreateIndex() throws IOException {
        // 1.创建Request对象
        CreateIndexRequest request = new CreateIndexRequest("policy");
        // 2.准备请求参数
        request.source(MAPPING_TEMPLATE, XContentType.JSON);
        // 3.发送请求
        client.indices().create(request, RequestOptions.DEFAULT);
    }


    /**
     * 导入政策数据
     **/
    @Test
    void testLoadPolicyDocs() throws IOException {
        // 分页查询商品数据
        int pageNo = 1;
        int size = 10;
        while (true) {
//            Page<Policy> page = policyService.lambdaQuery().page(new Page<Policy>(pageNo, size));
            PageBean<Policy> list = policyService.list(pageNo, size, null, null, null, null, List.of(), "AND");
            // 非空校验
            List<Policy> policies = list.getItems();
            if (CollUtils.isEmpty(policies)) {
                return;
            }
            log.info("加载第{}页数据，共{}条", pageNo, policies.size());
//            System.err.println("加载第" + pageNo + "页数据，共" + policies.size() + "条");
//             1.创建Request
            BulkRequest request = new BulkRequest("policy");
            // 2.准备参数，添加多个新增的Request
            for (Policy policy : policies) {
                // 2.1.转换为文档类型ItemDTO
                PolicyDoc policyDoc = BeanUtil.copyProperties(policy, PolicyDoc.class);
                // 2.2.创建新增文档的Request对象
                request.add(new IndexRequest()
                        .id(policyDoc.getId())
                        .source(JSONUtil.toJsonStr(policyDoc), XContentType.JSON));
            }
            // 3.发送请求
            client.bulk(request, RequestOptions.DEFAULT);

            // 翻页
            pageNo++;
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

}
