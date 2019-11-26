package com.myqq.service.youza.constinfo;

/**
 * @author li_sj
 * @desc 白秋洁app
 */
public class ConstInfoForApp {
    public static String qqAuth = "bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiIxNTAwMDU2MzQ1OSIsInNjb3BlIjpbInNlcnZpY2UiXSwiaWQiOiJlN2UyYjhjZC01YmJiLWQ4OTAtMWQ2Yy1mNTM5MWZkM2U3MzUiLCJleHAiOjE1NzUwODU2MDQsInR5cGUiOiJtZW1iZXIiLCJhdXRob3JpdGllcyI6WyIwMDAwMDAwMC0wMDAwLTAwMDAtMDAwMC0wMDAwMDAwMDAwMDAiXSwianRpIjoiNGM4YTUxYzEtMjk5MC00NmMyLThjYmQtYmJmMzdiOWYwMmEwIiwiY2xpZW50X2lkIjoiaW5uZXIifQ.L6rnyIDf4xxCZ62L1oNvR4rCRyHx_OpybY2iA_4D4527AEjnhg9-XkN1B1Nbg6mg1qeQl0Omg3_hrkn_aQMUZJaunDAbetr41qrq8H7Fzh-8XC8Ui3PRLCo4KGOI4wSegnmqgyWGwJfskz1yTmU2LWNxnFF_-ateeb2NEL6RG7Rdx3wGfwcHBuQRf1k7it7zKLWTonaX5N8JPIOg-LTYOKtjG3H4b6B2aw80_crMU6RSg1lIDiqRu0HA2dLhYJaF6UIaR8rAKiwQI7EFwQvoN6DBY6rFik-s5aqMoRQu0hu1Ir9bRLzff3UKIhsZ6i4jHxekj-nNj2igAfhSHzPr_A";
    public static String qqMemberId = "e7e2b8cd-5bbb-d890-1d6c-f5391fd3e735";
    public static String cookieFormat = "Authorization={0}";

    public static String zzAuth = "bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiIxMzY2NjE0NzkzNyIsInNjb3BlIjpbInNlcnZpY2UiXSwiaWQiOiI2OGRjMTI5Ni00MmM2LWZkODktM2I1ZS0wNTk3ZGYyNjI5NzEiLCJleHAiOjE1NzcwODE5NDYsInR5cGUiOiJtZW1iZXIiLCJhdXRob3JpdGllcyI6WyIwMDAwMDAwMC0wMDAwLTAwMDAtMDAwMC0wMDAwMDAwMDAwMDAiXSwianRpIjoiZDJiZDkxNzQtYjFhNi00OWEzLWI2MzEtNTgzZDc5Yjg2YWNhIiwiY2xpZW50X2lkIjoiaW5uZXIifQ.deScgfhLs5Nw_SJWI0O52wkgegnsMby7Uq48fQy_6viRvbkZZq9F_vYTVfzTTM4Vsi4Ru6Rki0Znsxe7HrC5-hO6ChE5iuhFNbnYDncaOmop-neAjM9iuK5dWLUIdqQ02uYnSMf6OaokUOdCS8Y7S0MLfRp0cDO0NM1X6aX1quW2uhZJ2c9jrgqh4qNdbVOS_ZZ2nNf5UpkxMhGKk0ydwJ_f5sFmzl5K0bz7JMkEiz8a7BPGJwGjC_l6lpaNprQXEKbpGirVdJpE5QG9JvrA33oWmc4P6hwMq1s6pHjCAF60Wb9vF6oHcs-MoXq3kgkopmI0BGibChvA6ZViNbiqXA";
    public static String zzMemberId = "68dc1296-42c6-fd89-3b5e-0597df262971";

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
    public static final String getGoodListUrlForApp = "http://www.baiqiujie.cn/backend/goods?homeType=4&goodCategory=&otherType=&pageIndex=1&pageSize=60";

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
    public static String getToPayGoodDetailUrlForApp = "http://www.baiqiujie.cn/backend/order?pageIndex=1&pageSize=40&status=pending";
    /**
     * 待发货
     * get
     */
    public static String getToSendGoodUrlForApp = "http://www.baiqiujie.cn/backend/order?pageIndex=1&pageSize=40&status=payed";

    /**
     * 待收货
     * get
     */
    public static String getSendGoodUrlForApp = "http://www.baiqiujie.cn/backend/order?pageIndex=1&pageSize=40&status=posted_out";
    /**
     * 已完成订单
     * get
     */
    public static String getCompletedGoodUrlForApp = "http://www.baiqiujie.cn/backend/order?pageIndex=1&pageSize=40&status=received";

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
