package cn.downrice.wx.model.message;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 视频消息/小视频消息，类型为video/shortvideo
 * @author 下饭
 */
@XStreamAlias("xml")
public class VideoMessage extends BaseMessage {
    /**
     * 视频消息媒体id，可以调用多媒体文件下载接口拉取数据
     */
    @XStreamAlias("MediaId")
    private String mediaId;
    /**
     * 视频消息缩略图的媒体id，可以调用多媒体文件下载接口拉取数据
     */
    @XStreamAlias("ThumbMediaId")
    private String thumbMediaId;

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getThumbMediaId() {
        return thumbMediaId;
    }

    public void setThumbMediaId(String thumbMediaId) {
        this.thumbMediaId = thumbMediaId;
    }
}
