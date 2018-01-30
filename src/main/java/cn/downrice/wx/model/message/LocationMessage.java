package cn.downrice.wx.model.message;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 地理位置消息，类型为location
 * @author 下饭
 */
@XStreamAlias("xml")
public class LocationMessage extends BaseMessage {
    /**
     * 地理位置维度
     */
    @XStreamAlias("Location_X")
    private String locationX;
    /**
     * 地理位置经度
     */
    @XStreamAlias("Location_Y")
    private String locationY;
    /**
     * 地图缩放大小
     */
    @XStreamAlias("Scale")
    private String scale;
    /**
     * 地理位置信息
     */
    @XStreamAlias("Label")
    private String label;

    public String getLocationX() {
        return locationX;
    }

    public void setLocationX(String locationX) {
        this.locationX = locationX;
    }

    public String getLocationY() {
        return locationY;
    }

    public void setLocationY(String locationY) {
        this.locationY = locationY;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
