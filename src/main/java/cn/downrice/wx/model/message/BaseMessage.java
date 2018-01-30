package cn.downrice.wx.model.message;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 消息基本类
 * @author 下饭
 */
public class BaseMessage {
    /**
     * 开发者微信号
     */
    @XStreamAlias("ToUserName")
    private String toUserName;
    /**
     * 发送方帐号（一个OpenID）
     */
    @XStreamAlias("FromUserName")
    private String fromUserName;
    /**
     * 消息创建时间 （整型）
     */
    @XStreamAlias("CreateTime")
    private Long createTime;
    /**
     * 消息类型（text/image/location/link/video/shortvideo）
     */
    @XStreamAlias("MsgType")
    private String msgType;
    /**
     * 消息id，64位整型
     */
    @XStreamAlias("MsgId")
    private Long msgId;

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public Long getMsgId() {
        return msgId;
    }

    public void setMsgId(Long msgId) {
        this.msgId = msgId;
    }
}
