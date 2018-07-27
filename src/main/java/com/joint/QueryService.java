package com.joint;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.joint.entity.SaleOrderObj;
import com.joint.util.PostRequst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 张超 on 2018/7/26.
 */
@Service
public class QueryService {
    @Autowired
    private PostRequst postRequst;
    JSONObject jsonObject=new JSONObject();
    JSONObject retObject=new JSONObject();
    public List<SaleOrderObj> orderQuery(String corpAccessToken,String corpId,String currentOpenUserId){
        //先清空jsonObject
        jsonObject.clear();
        retObject.clear();
        List<SaleOrderObj> saleOrderObjs=new ArrayList<>();
        for(int i=0;i<1000000;i+=100){
            retObject = getOrderQuery(corpAccessToken,corpId,currentOpenUserId,"SalesOrderObj",i);
            JSONArray array= (JSONArray) retObject.get("datas");
            saleOrderObjs.addAll(JSONObject.parseArray(array.toJSONString(), SaleOrderObj.class));
            if(i>(Integer)(retObject.get("totalNumber"))){
                break;
            }
        }
        //取出未发货的订单
        List<SaleOrderObj> list_remove=new ArrayList<>();
        for(SaleOrderObj saleOrderObj:saleOrderObjs){
            if(!saleOrderObj.getLogistics_status().equals("1")){
                list_remove.add(saleOrderObj);
            }
        }
        saleOrderObjs.removeAll(list_remove);
      return  saleOrderObjs;
    }
    public JSONObject getOrderQuery(String corpAccessToken,String corpId,String currentOpenUserId,String apiName,int i ){
        jsonObject.clear();
        String json="{\"corpAccessToken\": \""+corpAccessToken+"\",\n" +
                "    \"corpId\":\""+corpId+"\",\n" +
                "    \"currentOpenUserId\": \""+currentOpenUserId+"\",\n" +
                "    \"apiName\": \"" + apiName + "\", \n" +
                "    \"searchQuery\": {\n" +
                "      \"offset\": "+i+", \n" +
                "        \"limit\": 100, \n" +
                "\n" +
                "        \"orders\": [\n" +
                "            {\n" +
                "                \"ascending\": true, \n" +
                "                \"field\": \"name\"\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "}";
        jsonObject= JSON.parseObject(json);
        retObject=postRequst.postRequest("https://open.fxiaoke.com/cgi/crm/data/query",jsonObject);
        return retObject;
    }
}
