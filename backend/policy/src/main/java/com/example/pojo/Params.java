package com.example.pojo;

import lombok.Data;

import java.util.List;

@Data
public class Params {
    private Integer pageNum;
    private Integer pageSize;
    private String name;
    private String document;
    private String organ;
    private String text;
    private List<String> checkList;
    private String optionValue;
}
