package cn.downrice.wx.controller;

import cn.downrice.wx.service.ChatService;
import cn.downrice.wx.service.WxRespService;
import cn.downrice.wx.util.MessageUtil;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * 接收微信消息并响应
 * @author 下饭
 */
@Controller
public class WxRespController {

    Logger logger = LoggerFactory.getLogger(WxRespController.class);

    @Autowired
    private WxRespService wxRespService;

    @Autowired
    private ChatService chatService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public String resp(HttpServletRequest request) throws IOException, DocumentException {
        Map<String, String> map = MessageUtil.xmlToMap(request);
        String msgType = map.get("MsgType");
        String event = map.get("Event");

        //系统请求获取的地理位置信息
        if(msgType.equals(MessageUtil.MSGTYPE_EVENT) && event!=null && event.equals(MessageUtil.EVENT_LOCATION)){
            wxRespService.addLocation(map.get("FromUserName"), map.get("Latitude"), map.get("Longitude"));
            logger.info("获取地理位置:"+map.get("Latitude")+","+ map.get("Longitude"));
            return "success";
        }

        //调用图灵机器人进行回复
        return chatService.chat(map);
    }
}
