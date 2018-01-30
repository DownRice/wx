package cn.downrice.wx.util;

import cn.downrice.wx.model.message.Article;
import cn.downrice.wx.model.message.NewsMessage;
import cn.downrice.wx.model.message.TextMessage;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import org.apache.http.HttpRequest;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 *消息工具类
 * @author 下饭
 */
public class MessageUtil {
    public static final String MSGTYPE_TEXT = "text";
    public static final String MSGTYPE_LINK = "link";
    public static final String MSGTYPE_VIDEO = "video";
    public static final String MSGTYPE_SHORT_VIDEO = "shortvideo";
    public static final String MSGTYPE_LOCATION = "location";
    public static final String MSGTYPE_VOICE = "voice";
    public static final String MSGTYPE_IMAGE = "image";
    public static final String MSGTYPE_EVENT = "event";
    public static final String MSGTYPE_NEWS = "news";
    public static final String EVENT_LOCATION = "LOCATION";
    /**
     * 从request中获取xml并转换为Map
     * @param request
     * @return
     * @throws IOException
     * @throws DocumentException
     */
    public static Map<String, String> xmlToMap(HttpServletRequest request) throws IOException, DocumentException {
        Map<String, String> map = new HashMap<>();
        SAXReader reader = new SAXReader();
        InputStream ins = request.getInputStream();
        Document doc = reader.read(ins);
        Element root = doc.getRootElement();
        List<Element> list = root.elements();
        for(Element e : list){
            map.put(e.getName(), e.getText());
        }
        ins.close();
        return map;
    }

    /**
     * object转为xml
     * @param object
     * @return
     */
    public static String objectToXml(Object object){
        XStream xStream = new XStream();
        //使用注解转换
        xStream.processAnnotations(object.getClass());
        return xStream.toXML(object);
    }

    /**
     * 构造文本消息
     * @param data
     * @return
     */
    public static TextMessage createText(JSONObject data){
        TextMessage message = new TextMessage();
        message.setFromUserName((String)data.get("FromUserName"));
        message.setToUserName((String)data.get("ToUserName"));
        message.setMsgType(MessageUtil.MSGTYPE_TEXT);
        message.setContent((String)data.get("Content"));
        message.setCreateTime(System.currentTimeMillis());
        return message;
    }

    /**
     * 构造图文消息
     * @param data 包含基本信息的JSONObject
     * @param articleArray [{Title:xx,Description:xx,PicUrl:xx,Url:xx},{...},...]
     * @return
     */
    public static NewsMessage createNews(JSONObject data, JSONArray articleArray){
        NewsMessage message = new NewsMessage();
        int articleCount = 0;
        List<Article> articles = new ArrayList<>();
        for (Iterator i = articleArray.iterator(); i.hasNext();) {
            Article article = new Article();
            JSONObject jo = (JSONObject) i.next();
            article.setTitle((String)jo.get("Title"));
            article.setDescription((String)jo.get("Description"));
            article.setPicUrl((String)jo.get("PicUrl"));
            article.setUrl((String)jo.get("Url"));
            articles.add(article);
            ++articleCount;
            if(articleCount >= 6){
                break;
            }
        }
        message.setFromUserName((String)data.get("FromUserName"));
        message.setToUserName((String)data.get("ToUserName"));
        message.setCreateTime(System.currentTimeMillis());
        message.setMsgType(MessageUtil.MSGTYPE_NEWS);
        message.setArticles(articles);
        message.setArticleCount(articleCount);

        return message;
    }

}
