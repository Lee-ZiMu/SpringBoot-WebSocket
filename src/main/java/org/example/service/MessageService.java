package org.example.service;

import com.alibaba.fastjson2.JSONObject;

/**
 * @Version v1.0
 * @Description:
 * @Author Lee
 * @Date 2023/7/18 14:58
 * @Copyright 李子木
 */
public interface MessageService {

    /**
     * @author Lee
     * @date 2023/7/18 15:03
     * @description 处理消息
     */
    JSONObject insertMessage(String message);


}
