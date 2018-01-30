package cn.downrice.wx.model.message;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 图片消息，类型为image
 * @author 下饭
 */
@XStreamAlias("xml")
public class PicMessage extends BaseMessage {
    /**
     * 图片链接（由系统生成）
     */
    @XStreamAlias("PicUrl")
    private String picUrl;
    /**
     * 图片消息媒体id，可以调用多媒体文件下载接口拉取数据。
     */
    @XStreamAlias("MediaId")
    private String mediaId;

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }
}
