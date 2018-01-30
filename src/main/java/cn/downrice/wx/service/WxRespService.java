package cn.downrice.wx.service;

import cn.downrice.wx.model.message.GeoMessage;
import cn.downrice.wx.util.GeoUtil;
import cn.downrice.wx.util.JedisAdapter;
import cn.downrice.wx.util.RedisKeyUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 公众号回复服务
 * @author 下饭
 */
@Service
public class WxRespService {
    @Autowired
    private JedisAdapter jedisAdapter;

    /**
     * 将用户地理位置加入redis
     * @param userid
     * @param geo
     */
    public void addLocation(String userid, GeoMessage geo) {
        if (geo != null) {
            String key = RedisKeyUtil.getLocationKey(userid);
            JSONObject object = GeoUtil.getAddressByLocation(geo.getLatitude(), geo.getLongitude());
            if ((int) object.get(GeoUtil.STATUS) == GeoUtil.STATUS_SUCCESS) {
                JSONObject result = (JSONObject) object.get("result");
                String address = (String) result.get("address");
                jedisAdapter.set(key, address);
                //设置12小时后过期
                jedisAdapter.expire(key, 43200);
            }
        }
    }

    /**
     * 将用户地理位置加入redis
     * @param userid
     * @param latitude
     * @param longitude
     */
    public void addLocation(String userid, String latitude, String longitude) {
        String key = RedisKeyUtil.getLocationKey(userid);
        JSONObject object = GeoUtil.getAddressByLocation(latitude, longitude);
        if ((int) object.get(GeoUtil.STATUS) == GeoUtil.STATUS_SUCCESS) {
            JSONObject result = (JSONObject) object.get("result");
            String address = (String) result.get("address");
            jedisAdapter.set(key, address);
            //设置12小时后过期
            jedisAdapter.expire(key, 43200);
        }
    }

}
