package com.joint.interfac;

import com.alibaba.fastjson.JSONObject;
import com.joint.QueryService;
import com.joint.entity.SaleOrderObj;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

/**
 * Created by 张超 on 2018/7/26.
 */
@RestController
@EnableScheduling
public class OrderInter {
    @Autowired
    private QueryService queryService;
    String appId="FSAID_13174e6";
    String appSecret="c1dc0fb3db0145e0964b39db0bf8d910";
    String permanentCode="A4B0AE8FE1C05E410F2080FCE228F937";
    String currentOpenUserId="FSUID_86D6587EBBC9997632D2CB92C344E6F8";
    String corpAccessToken="";
    String corpId="";
    RestTemplate restTemplate = new RestTemplate();
    //表达式生成网站 http://cron.qqe2.com/
    //每两小时执行一次
    @Scheduled(cron = "0/2 * * * * ? ")
    public void getConnect(){
        DefaultHttpClient httpClient = new DefaultHttpClient();
        String url = "https://open.fxiaoke.com/cgi/corpAccessToken/get/V2";
        HttpPost httpPost = new HttpPost(url);
        // 设置请求的header
        httpPost.addHeader("Content-Type", "application/json;charset=utf-8");
        // 设置请求的参数
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("appId",appId);
        jsonParam.put("appSecret",appSecret);
        jsonParam.put("permanentCode",permanentCode);
        StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        // 执行请求
        HttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
            String json2 = EntityUtils.toString(response.getEntity(), "utf-8");
            JSONObject jsonObject = JSONObject.parseObject(json2);
            corpAccessToken= (String) jsonObject.get("corpAccessToken");
            corpId= (String) jsonObject.get("corpId");
            System.out.println(corpAccessToken+"=="+corpId);
            List<SaleOrderObj> jsonObject2 = queryService.orderQuery(corpAccessToken,corpId,currentOpenUserId);
            System.out.println(jsonObject2.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//    public static void main(String[] args) {
//        OrderInter orderInter=new OrderInter();
//        orderInter.getConnect();
//    }
}
