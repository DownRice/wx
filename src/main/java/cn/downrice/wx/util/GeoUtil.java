package cn.downrice.wx.util;

import com.alibaba.fastjson.JSONObject;

/**
 * 通过请求腾讯地图API获取相关信息
 * 相关文档：http://lbs.qq.com/webservice_v1/guide-gcoder.html
 * @author 下饭
 */
public class GeoUtil {

    public static final String KEY = "5PQBZ-7GI6R-TWRWD-W7PYZ-M6PMT-U4BCH";
    public static final String STATUS = "status";
    public static final int STATUS_SUCCESS = 0;
    /**
     * 逆地址解析(坐标位置描述),请求方式为Get
     */
    public static final String GCODER = "http://apis.map.qq.com/ws/geocoder/v1/?location=";

    public static JSONObject getAddressByLocation(String lat, String lng){
        String url = GCODER + lat + "," + lng + "&key=" + KEY;
        JSONObject object = HttpClientUtil.doGet(url);
        return object;
    }
}
