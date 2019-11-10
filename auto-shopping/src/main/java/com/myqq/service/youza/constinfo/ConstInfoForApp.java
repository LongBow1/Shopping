package com.myqq.service.youza.constinfo;

/**
 * @author li_sj
 * @desc 白秋洁app
 */
public class ConstInfoForApp {
    public static String qqAuth = "bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiIxNTAwMDU2MzQ1OSIsInNjb3BlIjpbInNlcnZpY2UiXSwiaWQiOiJlN2UyYjhjZC01YmJiLWQ4OTAtMWQ2Yy1mNTM5MWZkM2U3MzUiLCJleHAiOjE1NzUwODU2MDQsInR5cGUiOiJtZW1iZXIiLCJhdXRob3JpdGllcyI6WyIwMDAwMDAwMC0wMDAwLTAwMDAtMDAwMC0wMDAwMDAwMDAwMDAiXSwianRpIjoiNGM4YTUxYzEtMjk5MC00NmMyLThjYmQtYmJmMzdiOWYwMmEwIiwiY2xpZW50X2lkIjoiaW5uZXIifQ.L6rnyIDf4xxCZ62L1oNvR4rCRyHx_OpybY2iA_4D4527AEjnhg9-XkN1B1Nbg6mg1qeQl0Omg3_hrkn_aQMUZJaunDAbetr41qrq8H7Fzh-8XC8Ui3PRLCo4KGOI4wSegnmqgyWGwJfskz1yTmU2LWNxnFF_-ateeb2NEL6RG7Rdx3wGfwcHBuQRf1k7it7zKLWTonaX5N8JPIOg-LTYOKtjG3H4b6B2aw80_crMU6RSg1lIDiqRu0HA2dLhYJaF6UIaR8rAKiwQI7EFwQvoN6DBY6rFik-s5aqMoRQu0hu1Ir9bRLzff3UKIhsZ6i4jHxekj-nNj2igAfhSHzPr_A";
    public static String qqMemberId = "e7e2b8cd-5bbb-d890-1d6c-f5391fd3e735";
    public static String cookieFormat = "Authorization={0}";

    /**
     * 收件人信息(参数为会员编号：e7e2b8cd-5bbb-d890-1d6c-f5391fd3e735 )
     * get
     */
    public static String getAddressInfoUrlForApp = "http://www.baiqiujie.cn/backend/member/{0}/address";
    /**
     *
     * 获取商品列表信息--前20个
     * get
     */
    public static final String getGoodListUrlForApp = "http://www.baiqiujie.cn/backend/goods?homeType=4&goodCategory=&otherType=&pageIndex=1&pageSize=20";

    /**
     * 查询商品概要信息(getGoodListUrlForApp返回goodsId)
     * get
     */
    public static String getGoodDataUrlForApp = "http://www.baiqiujie.cn/backend/goods/{0}";
    /**
     * 查询商品库存信息(返回mainGoodsId--对应请求goodsId，返回goodsId--下单用)
     * get
     */
    public static String getGoodDataDetailUrlForApp = "http://www.baiqiujie.cn/backend/goods/{0}/specs";

    /**
     * 提交订单(地址+商品信息--getGoodDataDetailUrlForApp中返回goodsId)
     * post
     */
    public static final String commitOrderUrlForApp = "http://www.baiqiujie.cn/backend/order";
    /**
     * 待支付
     * get
     */
    public static String getToPayGoodDetailUrlForApp = "http://www.baiqiujie.cn/backend/order?pageIndex=1&pageSize=20&status=pending";
    /**
     * 待发货
     * get
     */
    public static String getToSendGoodUrlForApp = "http://www.baiqiujie.cn/backend/order?pageIndex=1&pageSize=20&status=payed";

    /**
     * 待收货
     * get
     */
    public static String getSendGoodUrlForApp = "http://www.baiqiujie.cn/backend/order?pageIndex=1&pageSize=20&status=posted_out";
    /**
     * 已完成订单
     * get
     */
    public static String getCompletedGoodUrlForApp = "http://www.baiqiujie.cn/backend/order?pageIndex=1&pageSize=20&status=received";

    /**
     * 取消订单(参数为orderId--下单commitOrderUrlForApp返回)
     * patch
     */
    public static String cancelOrderUrlForApp = "http://www.baiqiujie.cn/backend/order/{0}/cancel?reason=redo";

    /**
     * 添加地址(会员编号， 请求参数url编码:receiveName,receivePhone,province,city,area,address)
     * post
     */
    public static String addAddressForApp = "http://www.baiqiujie.cn/backend/member/{0}/address?receiveName={1}&receivePhone={2}&province={3}&city={4}&area={5}&address={6}&def=true";
}