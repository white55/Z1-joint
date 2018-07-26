package com.joint.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by 张超 on 2018/7/26.
 */
@Component
public class PostRequst {
    public JSONObject postRequest(String url,JSONObject args){
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        // 设置请求的header
        httpPost.addHeader("Content-Type", "application/json;charset=utf-8");
        // 设置请求的参数
        StringEntity entity = new StringEntity(args.toString(), "utf-8");
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        // 执行请求
        HttpResponse response = null;
        JSONObject jsonObject = null;
        try {
            response = httpClient.execute(httpPost);
            String json2 = EntityUtils.toString(response.getEntity(), "utf-8");
            jsonObject = JSONObject.parseObject(json2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
