# wx
基于SringBoot的微信公众号开发框架，并整合图灵API

GitHub:https://github.com/DownRice/wx</br>
微信公众平台：https://mp.weixin.qq.com/</br>
微信公众平台开发者文档：https://mp.weixin.qq.com/wiki</br>

本文以公众平台测试账号为例描述，正式公众号配置大同小异，参考开发者文档  

一、服务器验证  
1.公众平台配置  
注：在本地开发时，可使用内网穿透工具  

2.验证服务开发  

即提交URL后公众服务器会以GET方式向配置URL发送以上信息，后续开发公众号所接收消息均以POST方式发送至配置URL。  

验证过程：  
        1）将token、timestamp、nonce三个参数进行字典序排序  
        2）将三个参数字符串拼接成一个字符串进行sha1加密  
        3）开发者获得加密后的字符串可与signature对比，标识该请求来源于微信  
验证代码：  
/**
 *
 * @param signature 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数
 * @param timestamp 时间戳
 * @param nonce     随机数
 * @param echostr   随机字符串  
 */  
@RequestMapping(method = RequestMethod.GET)  
@ResponseBody  
public String check(@RequestParam(value = "signature")String signature,  
@RequestParam(value = "timestamp")String timestamp, @RequestParam(value = "nonce")String nonce,   
@RequestParam(value = "echostr")String echostr){  
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
SHA1加密：
/**
 * Sha1加密工具类
 * @author 下饭
 */
public class Sha1Util {
    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        // 把密文转换成十六进制的字符串形式
        for (int j = 0; j < len; j++) {
            buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
        }
        return buf.toString();
    }
    public static String encode(String str) {
        if (str == null) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            messageDigest.update(str.getBytes());
            return getFormattedText(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
二、消息接收回复开发
消息类型非常多，具体参考开发文档，但共通点为：
接收消息均以xml格式字符串方式POST过来，
回复消息均以xml格式字符串返回
因此，开发一个工具类，从request中获取xml并转换为map以供后续开发

request->map转换代码：
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
同理，返回消息以xml文档返回
object->xml转换代码：
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
消息类型定义，以文本消息（Text）为例：（此处省略get/set方法）
基类：
/**
 * 消息的基类
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
}
TextMessage：
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
}

三、ACCESS_TOKEN获取
access_token是公众号的全局唯一接口调用凭据，公众号调用各接口时都需使用access_token。开发者需要进行妥善保存。access_token的存储至少要保留512个字符空间。access_token的有效期目前为2个小时，需定时刷新，重复获取将导致上次获取的access_token失效。
具体何时需要用到ACCESS_TOKEN可参考公众开发文档具体业务，应存储该ACCESS_TOKEN并定时更新，而不是在需要使用时获取，会造成并发冲突以及调用次数超上限等问题。
在此提供一种解决方案：定时请求获取ACCESS_TOKEN，主要通过spring的InitializingBean的 afterPropertiesSet方法实现，存储此处采用redis
定时更新ACCESS_TOKEN代码：
/**
 * 定时更新AccessToken
 *
 * @author 下饭
 */
@Service
public class AccessTokenGetter implements InitializingBean {
    private Logger logger = LoggerFactory.getLogger(AccessTokenGetter.class);

    @Autowired
    private JedisAdapter jedisAdapter;

    ThreadFactory eventConsumerFactory = new ThreadFactoryBuilder()
            .setNameFormat("pool-%d").build();
    private ExecutorService cachedThreadPool = new ThreadPoolExecutor(5, 200,
            0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(1024), eventConsumerFactory,
            new ThreadPoolExecutor.AbortPolicy());

    @Override
    public void afterPropertiesSet() throws Exception {
        String key = RedisKeyUtil.getAccessTokenKey();
        cachedThreadPool.execute(new Runnable() {
                                     @Override
                                     public void run() {
                                         while (true) {
                                             AccessToken token = WxUtil.getAccessToken();
                                             jedisAdapter.set(key, token.getAccessToken());
                                             jedisAdapter.expire(key, token.getExpiresIn());
                                             logger.info("AccessToken已更新");
                                             try {
                                                 Thread.sleep(7200000);
                                             } catch (InterruptedException e) {
                                                 e.printStackTrace();
                                             }
                                         }
                                     }
                                 }
        );
    }
}
获取ACCESS_TOKEN代码：
参考文档：https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140183
ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET
/**
 * 获取AccessToken
 * 参考文档：https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140183
 * @return
 */
public static AccessToken getAccessToken(){
    String url = ACCESS_TOKEN_URL.replace("APPID", APPID).replace("APPSECRET", APPSECRET);
    JSONObject object = HttpClientUtil.doGet(url);
    if(object != null && object.get("errcode")==null){
        String access_token = object.getString("access_token");
        int expires_in = object.getInteger("expires_in");
        AccessToken token = new AccessToken(access_token, expires_in);
        return token;
    }
    return null;
}
四、菜单开发
待更新
