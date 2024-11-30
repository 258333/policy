package com.example.controller;

import com.example.pojo.Params;
import com.example.pojo.Result;
import com.example.service.ESService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @Author HongWei
 * @Date 2024/11/30 14:35
 */

@RestController
@RequestMapping("es")
@RequiredArgsConstructor
public class ESController {
    private final ESService esService;

    @PostMapping("getFirstList")
    public Result getFirstList(@RequestBody Params params) throws IOException {
        System.out.println(params);
        return Result.success(esService.getFirstList(params));
    }
}
