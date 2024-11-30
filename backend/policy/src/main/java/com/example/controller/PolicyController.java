package com.example.controller;

import com.example.mapper.PolicyMapper;
import com.example.pojo.*;
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
    @Autowired
    private PolicyMapper policyMapper;

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
        String optionValue = params.getOptionValue();

        System.out.println(params);
        System.out.println(pageNum + " " + pageSize + " " + name + " " + document + " " + organ + " " + text + " " + checkList + " " + optionValue);

        if (name.isEmpty()) name = null;
        if (document.isEmpty()) document = null;
        if (organ.isEmpty()) organ = null;
        if(text.isEmpty()) text = null;

        PageBean<Policy> pb = policyService.list(pageNum, pageSize, name, document, organ, text, checkList, optionValue);
        return Result.success(pb);
    }

    @PostMapping("android")
    public Result<List<Policy>> policyList(
            @RequestParam(value = "checkList", required = false) List<String> checkList,
            @RequestParam(value = "input", required = false) String input
    ) {

        String flag = null;
        if (checkList != null) {
            for (String check : checkList) {
                if (check.equals("无类型")) {
                    System.out.println("check为空");
                    flag = "null";
                    break;
                }
            }
        }
        List<Policy> goods = policyMapper.list(input, null, null, null, checkList, flag);
        return Result.success(goods);
    }

    //查询所所有政策类型和总数
    @GetMapping
    public Result<List<Map<String, Object>>> typeList() {
        return Result.success(policyService.typeList());
    }

    //二次检索
    @GetMapping("{second}")
    public Result policyListSeconde(@PathVariable String second){
        return Result.success(policyService.listSecond(second));
    }
}
