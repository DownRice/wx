package cn.downrice.wx.model.message;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.List;

/**
 * 图文消息,类型为news
 * @author 下饭
 */
@XStreamAlias("xml")
public class NewsMessage extends BaseMessage {
    /**
     * 图文消息个数，限制为8条以内
     */
    @XStreamAlias("ArticleCount")
    private int articleCount;
    /**
     * 多条图文消息信息，默认第一个item为大图,注意，如果图文数超过8，则将会无响应
     */
    @XStreamAlias("Articles")
    private List<Article> articles;

    public int getArticleCount() {
        return articleCount;
    }

    public void setArticleCount(int articleCount) {
        this.articleCount = articleCount;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}
