package com.example.mapper;

import com.example.pojo.Policy;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface PolicyMapper {

    //查询政策信息
    List<Policy> list(String name, String document, String organ, String text, List<String> checkList,String flag);

    //查询所有政策类型和总数
    @Select("SELECT DISTINCT type, COUNT(*) AS num FROM policy GROUP BY type")
    List<Map<String, Object>> typeList();

    List<Policy> listOr(String name, String document, String organ, String text, List<String> checkList, String flag);
}
