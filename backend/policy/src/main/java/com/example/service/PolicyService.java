package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.pojo.PageBean;
import com.example.pojo.Policy;

import java.util.List;
import java.util.Map;

public interface PolicyService extends IService<Policy> {
    // 查询政策列表
    PageBean<Policy> list(Integer pageNum, Integer pageSize, String name,
                          String document,String organ,String text, List<String> checkList,String optionValue);

    //查询所有政策类型和总数
    List<Map<String, Object>> typeList();

    List<Map<String,Object>> listSecond(String second);
}
