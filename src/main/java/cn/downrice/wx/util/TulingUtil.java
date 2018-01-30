package cn.downrice.wx.util;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * 图灵机器人工具类
 * @author 下饭
 */
public class TulingUtil {
    /**
     * 图灵机器人API接口地址
     */
    public static final String URL = "http://www.tuling123.com/openapi/api";
    /**
     * 图灵机器人APIkey
     */
    public static final String KEY = "be6b87ba4945481991c37572a6a7805c";
    /**
     *  100000	文本类
        200000	链接类
        302000	新闻类
        308000	菜谱类
     */
    public static final int TYPE_TEXT = 100000;
    public static final int TYPE_URL = 200000;
    public static final int TYPE_NEWS = 302000;
    public static final int TYPE_COOK_BOOK = 308000;

    public static final JSONObject chat(Map<String, String> data){
        String info = data.get("info");
        String loc = data.get("loc");
        String userid = data.get("userid");

        JSONObject param = new JSONObject();
        param.put("key", KEY);
        param.put("info", info);
        param.put("userid", userid);
        param.put("loc", loc);

        return  HttpClientUtil.doPost(URL, param);
    }








    /*
    String info = map.get("Content");
    String userid = map.get("FromUserName");

    JSONObject param = new JSONObject();
            param.put("key", key);
            param.put("info", info);
            param.put("userid", userid);

    JSONObject result = HttpClientUtil.doPost(url, param);
*/
}
