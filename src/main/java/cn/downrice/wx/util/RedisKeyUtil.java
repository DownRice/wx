package cn.downrice.wx.util;

/**
 * 生成唯一Redis Key
 * @author 下饭
 */
public class RedisKeyUtil {
    private static String SPLIT = "-";
    private static String BIZ_LOCATION = "LOCATION";

    public static String getLocationKey(String userid){
        return BIZ_LOCATION + SPLIT + String.valueOf(userid);
    }
}
