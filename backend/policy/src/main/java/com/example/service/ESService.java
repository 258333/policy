package com.example.service;

import com.example.pojo.PageBean;
import com.example.pojo.Params;
import com.example.pojo.Result;

import java.io.IOException;

/**
 * @Author HongWei
 * @Date 2024/11/30 14:36
 */

public interface ESService {
    PageBean getFirstList(Params params) throws IOException;
}
