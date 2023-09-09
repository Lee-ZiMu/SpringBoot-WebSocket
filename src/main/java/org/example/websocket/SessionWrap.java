package org.example.websocket;

import lombok.Data;

import javax.websocket.Session;
import java.util.Date;

/**
 * @Version v1.0
 * @Description:
 * @Author Lee
 * @Date 2023/7/18 13:50
 * @Copyright 李子木
 */
@Data
public class SessionWrap {

    private String from;

    private String type;

    private Session session;

    private Date lastTime;
}
