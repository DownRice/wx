package cn.downrice.wx.model.message;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 公众号请求的地理位置信息，类型为Event，Event类型为LOCATION（注意大写）
 * @author 下饭
 */
@XStreamAlias("xml")
public class GeoMessage extends BaseMessage {
    /**
     * 地理位置纬度
     */
    @XStreamAlias("Latitude")
    private String latitude;
    /**
     * 地理位置经度
     */
    @XStreamAlias("Longitude")
    private String longitude;
    /**
     * 地理位置精度
     */
    @XStreamAlias("Precision")
    private String precision;

    @XStreamAlias("Event")
    private String event;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getPrecision() {
        return precision;
    }

    public void setPrecision(String precision) {
        this.precision = precision;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
