package cn.downrice.wx.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import java.nio.charset.StandardCharsets;

/**
 * 用于发起GET/POST请求
 * @author 下饭
 */
public class HttpClientUtil {
    /**
     * 发起Get请求
     * @param url
     * @return
     */
    public static JSONObject doGet(String url){
        HttpClientBuilder builder = HttpClientBuilder.create();
        HttpGet get = new HttpGet(url);
        JSONObject object = null;

        try {
            HttpResponse response = builder.build().execute(get);
            if(response != null){
                HttpEntity entity = response.getEntity();
                if(entity !=  null){
                    String result = EntityUtils.toString(entity, StandardCharsets.UTF_8);
                    object = JSONObject.parseObject(result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    /**
     * 发起Post请求
     * @param url
     * @param param
     * @return
     */
    public static JSONObject doPost(String url, JSONObject param){
        HttpClientBuilder builder = HttpClientBuilder.create();
        HttpPost post = new HttpPost(url);
        JSONObject object = null;
        try{
            StringEntity stringEntity = new StringEntity(param.toString(), StandardCharsets.UTF_8);
            stringEntity.setContentType("application/json");
            post.setEntity(stringEntity);
            HttpResponse response = builder.build().execute(post);
            if(response != null){
                HttpEntity entity = response.getEntity();
                if(entity !=  null){
                    String result = EntityUtils.toString(entity, StandardCharsets.UTF_8);
                    object = JSONObject.parseObject(result);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return object;
    }
}
