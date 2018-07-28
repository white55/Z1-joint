package com.joint.Rnterfac;

import com.alibaba.fastjson.JSONObject;
import com.joint.Entity.SaleOrderObj;
import com.joint.Service.QueryService;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * Created by 张超 on 2018/7/26.
 */
@Component
@RestController
@EnableScheduling
public class OrderInter  implements CommandLineRunner {
    @Autowired
    private QueryService queryService;
    String appId="FSAID_13174e6";
    String appSecret="c1dc0fb3db0145e0964b39db0bf8d910";
    String permanentCode="A4B0AE8FE1C05E410F2080FCE228F937";
    String currentOpenUserId="FSUID_86D6587EBBC9997632D2CB92C344E6F8";
    String corpAccessToken="";
    String corpId="";

    //表达式生成网站 http://cron.qqe2.com/
    //启动时首先建立连接  开机启动方法：1.CommandLineRunner  2.AppliacationRunner
    @Override
    public void run(String... var1) throws Exception{
        getConnect();
    }
    //每两小时执行一次
    @Scheduled(cron = "10/59 0 0/2 * * ?   ")
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
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            httpClient.getConnectionManager().shutdown();
        }
    }
    //每分钟执行一次
    @Scheduled(cron = "0/59 * * * * ? ")
    public void reqToThird(){
        //获取纷享未发货的订单
        List<SaleOrderObj> jsonObject2 = queryService.orderQuery(corpAccessToken,corpId,currentOpenUserId);
        System.out.println(jsonObject2.size());
        //请求第三方对接系统接口处理订单数据并返回
        DefaultHttpClient httpClient = new DefaultHttpClient();
        String url = "https://www.baidu.com";
        HttpPost httpPost = new HttpPost(url);
        // 设置请求的header
        httpPost.addHeader("Content-Type", "application/json;charset=utf-8");
        // 设置请求的参数
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("test",appId);
        StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        // 执行请求
        HttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
            String json2 = EntityUtils.toString(response.getEntity(), "utf-8");
            //第三方返回数据 jsonObject
            JSONObject jsonObject = JSONObject.parseObject(json2);
            System.out.println(jsonObject);
            //TODO 处理第三方数据
            //TODO 将处理过的第三方数据插入纷享入库单
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            httpClient.getConnectionManager().shutdown();
        }

    }

}
