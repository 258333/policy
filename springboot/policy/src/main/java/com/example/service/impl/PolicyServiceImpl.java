package com.example.service.impl;

import com.example.mapper.PolicyMapper;
import com.example.pojo.PageBean;
import com.example.pojo.Policy;
import com.example.service.PolicyService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service


public class PolicyServiceImpl implements PolicyService {
    @Autowired
    private PolicyMapper policyMapper;

    List<Policy> goods = new ArrayList<Policy>();
    //分页查询政策列表
    @Override
    public PageBean<Policy> list(Integer pageNum, Integer pageSize, String name,
                                 String document,String organ,String text, List<String> checkList,String optionValue) {
        //创建pageBean对象
        PageBean<Policy> pb = new PageBean<>();
        //开启分页查询 PageHelper
        PageHelper.startPage(pageNum, pageSize);
        //调用mapper
        String flag = null;
        for(String check:checkList){
            if(check.equals("null")){
                System.out.println("check为空");
                flag = "null";
                break;
            }
        }

        if(optionValue.equals("AND")){
            goods = policyMapper.list(name,document,organ,text,checkList,flag);
        }else{
            goods = policyMapper.listOr(name,document,organ,text,checkList,flag);
        }

//        System.out.println(name+document+organ+type);
        //Page中提供了方法，可以获取PageHelper
        //获取到当前页和总页数分页查询后   得到的总记录条数和当前页数据
        Page<Policy> page = (Page<Policy>) goods;

        //把数据填充到PageBean对象中
        pb.setTotal(page.getTotal());
        pb.setItems(page.getResult());
        return pb;
    }

    //查询所有政策类型和总数
    @Override
    public List<Map<String, Object>> typeList() {
        return policyMapper.typeList();
    }

    //二次查询
    @Override
    public List<Map<String, Object>> listSecond(String second) {
        return null;
    }
}
