package org.example.service.Impl;

import com.alibaba.fastjson2.JSONObject;
import org.example.service.MessageService;
import org.springframework.stereotype.Service;

/**
 * @Version v1.0
 * @Description:
 * @Author Lee
 * @Date 2023/9/9 17:12
 * @Copyright 李子木
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Override
    public JSONObject insertMessage(String message) {
        JSONObject jsonObject = new JSONObject();
        // 处理消息
//        jsonObject.put("message", message);


        return jsonObject;
    }

}
