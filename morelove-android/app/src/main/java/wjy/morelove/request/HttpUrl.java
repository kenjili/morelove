package wjy.morelove.request;

public final class HttpUrl {

    //http请求or https请求
    private static final String HTTP_VERSION = "http://";
    //api接口域名
//    private static final String API_DOMAIN = "192.168.43.50:8080";
    private static final String API_DOMAIN = "weiai.wujiuye.com";

    //获取验证码
    public static final String SMS_CODE_URL = HTTP_VERSION + API_DOMAIN + "/sms/send";
    //用户注册
    public static final String USER_REGIST_URL = HTTP_VERSION + API_DOMAIN + "/user/regist";
    //用户登录
    public static final String USER_LOGIN_URL = HTTP_VERSION + API_DOMAIN + "/user/login";
    //用户退出登录
    public static final String USER_LOGOUT_URL = HTTP_VERSION + API_DOMAIN + "/user/logout";


    //申请绑定另一半
    public static final String LOVER_BINDLOVER_URL = HTTP_VERSION + API_DOMAIN + "/lover/bindLover";
    //查询绑定状态
    public static final String LOVER_BINDSTATE_URL = HTTP_VERSION + API_DOMAIN + "/lover/bindState";
    //确认绑定,同意绑定
    public static final String LOVER_ENTERBIND_URL = HTTP_VERSION + API_DOMAIN + "/lover/enterBind";
    //获取情侣关系记录
    public static final String LOVER_RECORD_URL = HTTP_VERSION + API_DOMAIN + "/lover/record";


    //搜索景点接口
    public static final String SCENICAREA_SERACH_URL = HTTP_VERSION + API_DOMAIN + "/found/scenicareas";
    //电影票房接口
    public static final String MOVIE_BOXOFFICE_URL = HTTP_VERSION + API_DOMAIN + "/found/movie";


    //发表侣行记
    public static final String PUBLIC_ITINERARY_URL = HTTP_VERSION + API_DOMAIN + "/itinerary/public";
    //发表时光
    public static final String PUBLIC_LOVETIME_URL = HTTP_VERSION + API_DOMAIN + "/lovetime/public";
    //时光列表
    public static final String LOVETIME_PRIVATE_LIST = HTTP_VERSION + API_DOMAIN + "/lovetime/list/{pageSize}/{page}";
    //时光详情
    public static final String LOVETIME_DETAILS_URL = HTTP_VERSION + API_DOMAIN + "/lovetime/details/{id}";
    //晒一晒列表
    public static final String LOVETIME_PUBLIC_LIST = HTTP_VERSION + API_DOMAIN + "/lovetime/world/list/{pageSize}/{page}";
    //侣行日记列表
    public static final String ITINERARY_LIST_URL = HTTP_VERSION + API_DOMAIN + "/itinerary/list/{pageSize}/{page}";
    //侣行日记详情
    public static final String ITINERARY_DETAILS_URL = HTTP_VERSION + API_DOMAIN + "/itinerary/details/{id}";


    //添加纪念日接口
    public static final String MEMORIALDAY_ADD_URL = HTTP_VERSION + API_DOMAIN + "/memorialDay/add";
    //纪念日列表
    public static final String MEMORIALDAY_LIST_URL = HTTP_VERSION + API_DOMAIN + "/memorialDay/list";
    //删除纪念日
    public static final String MEMORIALDAY_DELETE_URL = HTTP_VERSION + API_DOMAIN + "/memorialDay/delete";
}
