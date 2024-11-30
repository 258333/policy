package com.example.config;

import jakarta.annotation.PreDestroy;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration  // 标记该类为配置类
public class ElasticsearchConfig {

    // 定义 RestHighLevelClient 的 Bean
    @Bean
    public RestHighLevelClient client() {
        return new RestHighLevelClient(RestClient.builder(
                HttpHost.create("http://192.168.88.128:9200")  // 配置你的 Elasticsearch 服务地址
        ));
    }

    // 在应用关闭时关闭 RestHighLevelClient
    @PreDestroy
    public void closeClient() throws Exception {
        if (client() != null) {
            client().close();  // 关闭客户端
        }
    }
}
