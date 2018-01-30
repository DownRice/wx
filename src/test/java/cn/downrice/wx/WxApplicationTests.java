package cn.downrice.wx;

import cn.downrice.wx.model.message.Article;
import cn.downrice.wx.model.message.NewsMessage;
import cn.downrice.wx.util.GeoUtil;
import cn.downrice.wx.util.JedisAdapter;
import cn.downrice.wx.util.MessageUtil;
import cn.downrice.wx.util.TulingUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WxApplicationTests {
	Logger logger = LoggerFactory.getLogger(WxApplicationTests.class);
	@Autowired
	private JedisAdapter jedisAdapter;
	@Test
	public void contextLoads() {
		//JSONObject object = GeoUtil.getAddressByLocation("39.984154","116.307490");
		//JSONObject result = (JSONObject) object.get("result");
		//String str = (String)result.get("address");
		//System.out.println(str);
		//jedisAdapter.set("test", "test1");
		//jedisAdapter.set("test", "test2");
		//System.out.println(jedisAdapter.get("test"));
		Map<String, String> data = new HashMap<>();
		data.put("info", "我要看新闻");
		data.put("userid", "testid");

		JSONObject object = TulingUtil.chat(data);

		NewsMessage message = new NewsMessage();

		JSONArray array = object.getJSONArray("list");
		int articleCount = 0;
		List<Article> articles = new ArrayList<>();
		for (Iterator i = array.iterator(); i.hasNext();) {
			Article article = new Article();
			JSONObject jo = (JSONObject) i.next();

			article.setTitle((String)jo.get("article"));
			article.setDescription((String)jo.get("source"));
			article.setPicUrl((String)jo.get("icon"));
			article.setUrl((String)jo.get("detailurl"));

			articles.add(article);

			++articleCount;
			if(articleCount >= 8){
				break;
			}
		}
		message.setFromUserName("from");
		message.setToUserName("to");
		message.setCreateTime(System.currentTimeMillis());
		message.setMsgType("news");
		message.setArticles(articles);
		message.setArticleCount(articleCount);

		String xml = MessageUtil.objectToXml(message);
		System.out.println(xml);
	}

}
