package cn.downrice.wx.controller;

import cn.downrice.wx.util.Sha1Util;
import cn.downrice.wx.util.WxUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;

/**
 * 微信公众号接入验证
 * @author 下饭
 */
@Controller
public class WxCheckController {

    /**
     *
     * @param signature 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数
     * @param timestamp 时间戳
     * @param nonce     随机数
     * @param echostr   随机字符串
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public String check(@RequestParam(value = "signature")String signature, @RequestParam(value = "timestamp")String timestamp,
                      @RequestParam(value = "nonce")String nonce, @RequestParam(value = "echostr")String echostr){

        /*
        1）将token、timestamp、nonce三个参数进行字典序排序
        2）将三个参数字符串拼接成一个字符串进行sha1加密
        3）开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
         */
        String token = WxUtil.TOKEN;
        String[] arr = new String[]{token, timestamp, nonce};
        Arrays.sort(arr);
        String str = StringUtils.join(arr);
        if(Sha1Util.encode(str).equals(signature)){
            //返回echostr
            return echostr;
        }
        return "";
    }
}
