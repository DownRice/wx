package cn.downrice.wx.model.message;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 语音消息，类型为voice
 * @author 下饭
 */
@XStreamAlias("xml")
public class VoiceMessage extends BaseMessage {
    /**
     * 语音消息媒体id，可以调用多媒体文件下载接口拉取数据
     */
    @XStreamAlias("MediaId")
    private String mediaId;
    /**
     * 语音格式，如amr，speex等
     */
    @XStreamAlias("Format")
    private String format;

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
