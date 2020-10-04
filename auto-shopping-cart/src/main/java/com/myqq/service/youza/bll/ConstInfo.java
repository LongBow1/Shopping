package com.myqq.service.youza.bll;

public class ConstInfo {
    static String contentTypeJson = "application/json; charset=UTF-8";
    static String contentTypeUrl = "application/x-www-form-urlencoded; charset=UTF-8";
    static String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36 MicroMessenger/6.5.2.501 NetType/WIFI WindowsWechat QBCore/3.43.1021.400 QQBrowser/9.0.2524.400";

    /*static String cookieFormat = "KDTSESSIONID={0}; nobody_sign={0}; yz_log_ftime=1558612331736; yz_log_uuid=dbbd2da3-6a7e-42b2-71c8-a64988aba939; trace_sdk_context_dc_ps=294989; trace_sdk_context_dc_ps_utime=1558613955; trace_sdk_context_is_share=1; _kdt_id_=41763685; trace_sdk_context_banner_id=f.74895446~goods.2~17~qqpXNhTj; yz_log_seqb=1558612332141; yz_log_seqn=22; Hm_lvt_679ede9eb28bacfc763976b10973577b=1558613970; Hm_lpvt_679ede9eb28bacfc763976b10973577b=1558613970";*/


    static String cookieFormat = "KDTSESSIONID={0}; nobody_sign={0}; yz_log_ftime=1560347492581; yz_log_uuid=7b46e507-2ef7-0d1d-4e75-ccdaa99b8814; trace_sdk_context_dc_ps=294989; trace_sdk_context_dc_ps_utime=1560347582; trace_sdk_context_is_share=1; yz_ep_view_track=mus9eHIxSXMLeE9QNwv1qQ%3D%3D; yz_ep_page_type_track=iDJ3GNJDHbhHtOl6W3j3ZA%3D%3D; yz_ep_page_track=SdSQdzSTocrQl7yPhWH%2FyQ%3D%3D; loc_dfp=8edbb63f9ff6038d9314792695fcc8e8; dfp=403be77bb25c916776d1ed1116b75666; rdfp=403be77bb25c916776d1ed1116b75666; _kdt_id_={1}; Hm_lvt_679ede9eb28bacfc763976b10973577b=1560347594; Hm_lpvt_679ede9eb28bacfc763976b10973577b=1560347597; yz_log_seqb=1560347493117; yz_log_seqn=22";

    /*
    static String cookie = "KDTSESSIONID=YZ581208139995590656YZaplnXugK; nobody_sign=YZ581208139995590656YZaplnXugK; yz_log_ftime=1558612331736; yz_log_uuid=dbbd2da3-6a7e-42b2-71c8-a64988aba939; trace_sdk_context_dc_ps=294989; trace_sdk_context_dc_ps_utime=1558613955; trace_sdk_context_is_share=1; _kdt_id_=41763685; trace_sdk_context_banner_id=f.74895446~goods.2~17~qqpXNhTj; yz_log_seqb=1558612332141; yz_log_seqn=22; Hm_lvt_679ede9eb28bacfc763976b10973577b=1558613970; Hm_lpvt_679ede9eb28bacfc763976b10973577b=1558613970";

    static String sessionId = "YZ581208139995590656YZaplnXugK";*/

    /*static final String getgoodListUrl = "https://shop41955853.youzan.com/wscshop/showcase/goodsList.json?tagId=103968846&page=1&pageSize=20&goodsIds=&goodsFrom=1&isAdv=0&offlineId=0&goodsNumber=80";
    static String goodsUrl2 = "https://shop41955853.youzan.com/wscshop/showcase/goods/recommend/goods.json?bizName=goods_detail&goodsId%5B%5D=467490122&alias=1y2wd7y69gsth&storeId=&itemSize=10";*/

    static final String getgoodListUrl = "https://shop{0}.youzan.com/wscshop/showcase/goodsList.json?tagId={1}&page=1&pageSize=30&goodsIds=&goodsFrom=1&isAdv=0&offlineId=0&goodsNumber=80";


    static final String buyUrl = "https://cashier.youzan.com/pay/wsctrade/order/buy/v2/bill.json";

    /*static String getGoodDataUrl = "https://shop41955853.youzan.com/v2/goods/{0}";*/

    static String getGoodDataUrl = "https://shop{0}.youzan.com/v2/goods/{1}";

    static String getTopayListUrl = "https://h5.youzan.com/wsctrade/order/list.json?page_id=wsc&page=1&page_type=&page_size=80&type=topay&orderMark=";


    static String cancelOrderUrl = "https://h5.youzan.com/wsctrade/order/cancelOrder.json";

    static String getAddressInfoUrl = "https://h5.youzan.com/wsctrade/order/address/list?switchable=false";

    //待发货
    static String getToSendGoodUrl = "https://h5.youzan.com/wsctrade/order/list.json?page_id=1&page=1&page_type=&page_size=60&type=tosend&orderMark=";
    static String getTopayGoodDetailUrl = "https://h5.youzan.com/wsctrade/order/detail?order_no={0}&kdt_id={1}";
    //已发货
    static String getSendGoodUrl = "https://h5.youzan.com/wsctrade/order/list.json?page_id=1&page=1&page_type=&page_size=50&type=send&orderMark=";
    //待评价
    static String getToCommentUrl = "https://h5.youzan.com/wsctrade/order/list.json?page_id=1&page=1&page_type=&page_size=20&type=toevaluate&orderMark=";

    //region
    static String unicodeData = "";
    //endregion
}
