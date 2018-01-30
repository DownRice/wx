package cn.downrice.wx.service;

import cn.downrice.wx.model.message.Article;
import cn.downrice.wx.model.message.NewsMessage;
import cn.downrice.wx.model.message.TextMessage;
import cn.downrice.wx.util.JedisAdapter;
import cn.downrice.wx.util.MessageUtil;
import cn.downrice.wx.util.RedisKeyUtil;
import cn.downrice.wx.util.TulingUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 微信——图灵机器人对话服务
 * @author 下饭
 */
@Service
public class ChatService {

    @Autowired
    private JedisAdapter jedisAdapter;

    public String chat(Map<String, String> data){
        String info = data.get("Content");
        String userid = data.get("FromUserName");
        String loc = jedisAdapter.get(RedisKeyUtil.getLocationKey(userid));
        Map<String, String> map = new HashMap<>();
        map.put("info", info);
        map.put("userid", userid);
        map.put("loc", loc);
        System.out.println("loc:" + loc);
        JSONObject object = TulingUtil.chat(map);
        System.out.println("code:" + object.get("code"));
        /**
         *  100000	文本类
            200000	链接类
            302000	新闻类
            308000	菜谱类
         */
        int code = (Integer)object.get("code");
        if(code == TulingUtil.TYPE_TEXT){
            //文本类
            JSONObject textData = new JSONObject();
            textData.put("FromUserName", data.get("ToUserName"));
            textData.put("ToUserName", data.get("FromUserName"));
            textData.put("Content",object.get("text"));
            return MessageUtil.objectToXml(MessageUtil.createText(textData));

        }else if(code==TulingUtil.TYPE_URL){
            //链接类
            TextMessage message = new TextMessage();
            message.setFromUserName(data.get("ToUserName"));
            message.setToUserName(data.get("FromUserName"));
            message.setMsgType(MessageUtil.MSGTYPE_TEXT);
            message.setContent((String)object.get("text")+"\n"+"<a href=\""+object.get("url")+"\">打开页面</a>");
            message.setCreateTime(System.currentTimeMillis());
            return MessageUtil.objectToXml(message);

        }else if(code==TulingUtil.TYPE_NEWS){
            //新闻类
            JSONArray array = object.getJSONArray("list");
            JSONArray articleArray = new JSONArray();

            JSONObject newsData = new JSONObject();
            newsData.put("FromUserName", data.get("ToUserName"));
            newsData.put("ToUserName", data.get("FromUserName"));

            for (Iterator i = array.iterator(); i.hasNext();) {
                JSONObject jo = (JSONObject) i.next();
                JSONObject newsObject = new JSONObject();
                newsObject.put("Title",jo.get("article"));
                newsObject.put("Description",jo.get("source"));
                newsObject.put("PicUrl",jo.get("icon"));
                newsObject.put("Url",jo.get("detailurl"));
                articleArray.add(newsObject);
            }
            return MessageUtil.objectToXml(MessageUtil.createNews(newsData, articleArray));

        }else if(code==TulingUtil.TYPE_COOK_BOOK){
            //菜谱类
            JSONArray array = object.getJSONArray("list");
            JSONArray cookArray = new JSONArray();
            JSONObject cookData = new JSONObject();
            cookData.put("FromUserName", data.get("ToUserName"));
            cookData.put("ToUserName", data.get("FromUserName"));

            for (Iterator i = array.iterator(); i.hasNext();) {
                JSONObject jo = (JSONObject) i.next();
                JSONObject newsObject = new JSONObject();
                newsObject.put("Title",jo.get("name"));
                newsObject.put("Description",jo.get("info"));
                newsObject.put("PicUrl",jo.get("icon"));
                newsObject.put("Url",jo.get("detailurl"));
                cookArray.add(newsObject);
            }
            return MessageUtil.objectToXml(MessageUtil.createNews(cookData, cookArray));

        }else{
            return "sucess";
        }

    }
}
