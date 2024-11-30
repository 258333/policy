package com.example.pojo;

import lombok.Data;

/**
 * @Author HongWei
 * @Date 2024/11/29 10:05
 */
@Data
public class PolicyDoc {
    private String id;
    private String name;
    private String type;
    private String category;
    private String range;
    private String document;
    private String form;
    private String organ;
    private java.sql.Date viadata;
    private java.sql.Date pubdata;
    private java.sql.Date perdata;
    private String text;
}
