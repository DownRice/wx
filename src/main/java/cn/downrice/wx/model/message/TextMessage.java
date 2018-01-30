package cn.downrice.wx.model.message;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 文本消息，类型为text
 * @author 下饭
 */
@XStreamAlias("xml")
public class TextMessage extends BaseMessage{
    /**
     * 文本消息内容
     */
    @XStreamAlias("Content")
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
