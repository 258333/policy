package com.example.controller;

import com.example.pojo.PageBean;
import com.example.pojo.Params;
import com.example.pojo.Policy;
import com.example.pojo.Result;
import com.example.service.PolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("policy")
@CrossOrigin
public class PolicyController {
    @Autowired
    private PolicyService policyService;

    //分页查询政策列表
    @PostMapping
    public Result<PageBean<Policy>> list(
        @RequestBody Params params
    ) {
        Integer pageNum = params.getPageNum();
        Integer pageSize = params.getPageSize();
        String name = params.getName();
        String document = params.getDocument();
        String organ = params.getOrgan();
        String text = params.getText();
        List<String> checkList = params.getCheckList();

        System.out.println(params);
        System.out.println(pageNum+ " " + pageSize + " " + name + " " + document + " " + organ + " " + text + " " + checkList);
        
        PageBean<Policy> pb = policyService.list(pageNum, pageSize, name, document, organ, text, checkList);
        return Result.success(pb);
    }


    //查询所所有政策类型和总数
    @GetMapping
    public Result<List<Map<String,Object>>> typeList() {
        return Result.success(policyService.typeList());
    }
}
