package wujiuye.morelove.common.utils.showapi;

/**
 * ShowAPI，互联网API入口
 * https://www.showapi.com
 * 全国景点查询接口
 * 用户名：wujiuye，邮箱419611821@qq.com
 * @author wjy
 */
public class MyShowApiRequest extends ShowApiRequest{

    private final static String appId = "76670";
    private final static String appSecret = "e1e2ce605d7549259dc6e3694b3b3c04";

    //景点查询接口
    public final static String JINDIAN_API = "http://route.showapi.com/268-1";
    //获取省份列表
    public final static String PRO_LIST_API = "http://route.showapi.com/268-2";
    //根据省份id获取省份下面的城市
    public final static String CITY_LIST_API = "http://route.showapi.com/268-3";
    //根据城市id获取城市下面的地名（区、县、镇）
    public final static String AREA_LIST_API = "http://route.showapi.com/268-4";

    public MyShowApiRequest(String url) {
        super(url, appId, appSecret);
    }
}
