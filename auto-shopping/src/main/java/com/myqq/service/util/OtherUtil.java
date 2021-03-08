package com.myqq.service.util;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.myqq.service.youza.bll.ConstInfo.*;

/**
 * @Description TODO
 * @Author lisongjun
 * @Date 2021/2/2 20:11
 */
public class OtherUtil {

    static String contentTypeJson = "application/json; charset=UTF-8";
    static String contentTypeUrl = "application/x-www-form-urlencoded; charset=UTF-8";
    static String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36 MicroMessenger/6.5.2.501 NetType/WIFI WindowsWechat QBCore/3.43.1021.400 QQBrowser/9.0.2524.400";

    /*static String cookieFormat = "KDTSESSIONID={0}; nobody_sign={0}; yz_log_ftime=1558612331736; yz_log_uuid=dbbd2da3-6a7e-42b2-71c8-a64988aba939; trace_sdk_context_dc_ps=294989; trace_sdk_context_dc_ps_utime=1558613955; trace_sdk_context_is_share=1; _kdt_id_=41763685; trace_sdk_context_banner_id=f.74895446~goods.2~17~qqpXNhTj; yz_log_seqb=1558612332141; yz_log_seqn=22; Hm_lvt_679ede9eb28bacfc763976b10973577b=1558613970; Hm_lpvt_679ede9eb28bacfc763976b10973577b=1558613970";*/


    static String cookieFormat = "selectedCluster={%22id%22:%222260%22%2C%22name%22:%22jiesi-strategy%22%2C%22nodeUrl%22:%2211.3.52.171:40000%22}; jd.erp.lang=zh_CN; jdd69fo72b8lfeoe=SNJ7GNXC5IJTVIOUHI6RFMBOTBN5FX7HZNHFGTKPVHEZL5724KWQBY4KRD54A7AYN54LBFZCCGHARJS63LHQB57IVY; __jdv=101385626|direct|-|none|-|1611631076715; pinId=tBcmVx6sFNpDOIMrlE_hBw; pin=pro_pop_zl; unick=pro_pop_zl; ceshi3.com=000; _tp=pJCviU9AH11HXwwJq14RSg%3D%3D; _pst=pro_pop_zl; mba_muid=1611631076713257864636; shshshfp=e3c0ca1db3637f63cdcc4d287ed441ba; shshshfpa=80f1854c-1c42-a19d-471a-5bc369d4c5f8-1611715301; shshshfpb=l9SwHupayB2JQnhUZ2AwycQ%3D%3D; visitkey=44590290478658255; __jdu=159342452418030085014; __jd_ref_cls=MCouponBag_UnclaimedPageExpo; ipLoc-djd=22-1930-50947-0; areaId=22; sso.jd.com=BJ.8a2e486f7302496cace184a2ec33f091; erp1.jd.com=1D14FDE442D8F5230E1A7737D3C2EA10337BF9BF4FF1229EFF48F3CCE5380286028740DBEAB5E581A79EC6B342ED50E76634C8CAF38D80F6B972221ABD29F655E7CE79EC4AB941CF2084B59F0DA6CD2E; language=zh_CN; RT=\"z=1&dm=jd.com&si=xqz0ie5l9d&ss=kkm7ua52&sl=1&tt=36o&ld=13ur9u\"; TrackID=13e1QknHX0ciW_lFGSJ_W-MUIlovt4HH6jRoGleyMV7R8F46mRvmRqRFPb19PbaaFigpFBcelxcyh7LVndwmQ9S2HcVYdHgNjQqEQn7_u6jA; 3AB9D23F7A4B3C9B=7B2EUZ63ZGKVSPE6RMFIMKBBHWU6KJQCXMJCKOKXCHSAS5E74XCG3AUJ4OL2R7BXDWKZNUXRH6A5AALAUREBABM7VU; __datamill_vpin_erp=eyJhbGciOiJIUzUxMiJ9.eyJyYXQiOjE2MTIyMzI4MTAsImV4cCI6MTYxMjMxOTIxMCwiaWF0IjoxNjExNjQ3NTc3LCJpc3MiOiJkYXRhbWlsbC5qZC5jb20iLCJhdWQiOiJqZF92cGluKjAxMjU2NiJ9.zObAXbWoLCWsA2V4oE4tYjjdwJqBtTaRYBAhJErSplx1ZGWYCeCtATJ-CJ6MXXI0PjEiId6lyMkPrSTod3N7EQ; b-sec=LGGDCPBOJ2AK667XENKAADTWOQ7NQUZ7JIMZO5T2VQQ2ILIJH3KMASEHCWOCZY6AR7GY3A37P62ZS; _base_=YKH2KDFHMOZBLCUV7NSRBWQUJPBI7JIMU5R3EFJ5UDHJ5LCU7R2NILKK5UJ6GLA2RGYT464UKXAI5KK7PNC5B5UHJ2HVQ4ENFP57OCYBO7YLVBHURRS5DP2RWQCHXTSHSCF555UOYV4EACK5TEGWP3JK5BZELA6E4S4L2GLWBLTAIW5N6ZGEONMNNA5DQRDPVL52KNRE2QP7OBTCWMQOXV7WFEG4MEKLVTSGRNAWOPMSBYS4MYYYIX47SKLSD3IK7KJ3OCM75PRQDA4STCY2DN6WJT3UAVELU4HLWJT6LDNEERERTGCVWXP52VVDW7JUI37ESHEYK2YDJVQAQIWW3S2YBHTHY3RXSIOAAXQCEFJZTR53VJBA; __jda=92116191.159342452418030085014.1593424524.1612262098.1612264466.263; __jdc=92116191; __jdb=92116191.14.159342452418030085014|263.1612264466";



    static String dbPlanInfo = "108521\t14\t100000012\t2\t3\tcate_order_3m\t1\t1\n" +
            "108491\t14\t100000013\t2\t1\tcate_order_3m\t2\t2\n" +
            "108463\t14\t100000013\t2\t2\tcate_order_3m\t2\t2\n" +
            "108431\t14\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "108406\t2\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "108405\t14\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "108403\t14\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "108402\t2\t100000012\t2\t1\tcate_order_history\t1\t1\n" +
            "108380\t14\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "108378\t2\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "108376\t2\t100000013\t2\t1\tcate_order_history\t2\t2\n" +
            "108375\t2\t100000012\t2\t1\tcate_order_history\t1\t1\n" +
            "108353\t2\t100000013\t2\t1\tcate_order_history\t2\t2\n" +
            "108346\t2\t100000011\t2\t3\tcate_order_history\t0\t1\n" +
            "108344\t6\t100000013\t2\t1\tcate_order_history\t2\t2\n" +
            "108343\t6\t100000012\t2\t1\tcate_order_history\t1\t1\n" +
            "108342\t6\t100000012\t2\t1\tcate_order_history\t1\t1\n" +
            "108318\t2\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "108317\t2\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "108312\t2\t100000012\t2\t1\tcate_order_history\t1\t1\n" +
            "108289\t14\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "108287\t2\t100000012\t2\t1\tcate_order_history\t1\t1\n" +
            "108259\t14\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "108258\t2\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "108257\t2\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "108256\t2\t100000013\t2\t1\tcate_order_history\t2\t2\n" +
            "108255\t2\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "108254\t2\t100000013\t2\t1\tcate_order_history\t2\t2\n" +
            "108252\t2\t100000011\t2\t3\tcate_order_history\t0\t1\n" +
            "108229\t2\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "108227\t14\t100000013\t2\t1\tcate_order_3m\t2\t2\n" +
            "108223\t2\t100000011\t2\t3\tcate_order_history\t0\t1\n" +
            "108198\t2\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "108196\t2\t100000013\t2\t1\tcate_order_history\t2\t2\n" +
            "108193\t2\t100000011\t2\t3\tcate_order_history\t0\t1\n" +
            "108171\t2\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "108169\t2\t100000013\t2\t1\tcate_order_history\t2\t2\n" +
            "108168\t2\t100000013\t2\t3\tcate_order_history\t2\t2\n" +
            "108167\t2\t100000013\t2\t1\tcate_order_history\t2\t2\n" +
            "108166\t2\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "108165\t2\t100000012\t2\t1\tcate_order_history\t1\t1\n" +
            "108141\t14\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "108135\t2\t100000012\t2\t1\tcate_order_history\t1\t1\n" +
            "108132\t2\t100000013\t2\t1\tcate_order_history\t2\t2\n" +
            "108079\t6\t100000011\t2\t2\tcate_order_3y\t0\t1\n" +
            "108076\t6\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "108075\t6\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "108074\t6\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "108072\t6\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "108053\t6\t100000011\t2\t3\tcate_order_history\t0\t1\n" +
            "108052\t6\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "108051\t6\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "108048\t2\t100000012\t2\t1\tcate_order_1y\t1\t1\n" +
            "108047\t2\t100000012\t2\t1\tcate_order_history\t1\t1\n" +
            "108046\t6\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "108045\t6\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "108044\t6\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "108029\t6\t100000012\t2\t1\tcate_order_history\t1\t1\n" +
            "108027\t6\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "108026\t6\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "108025\t6\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "108022\t6\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "108021\t6\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "108019\t6\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "108018\t6\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "107996\t6\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "107995\t6\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "107994\t6\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "107992\t6\t100000011\t2\t1\tcate_order_3y\t0\t1\n" +
            "107991\t6\t100000011\t2\t1\tcate_order_1y\t0\t1\n" +
            "107990\t6\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "107989\t6\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "107987\t6\t100000012\t2\t2\tcate_order_history\t1\t1\n" +
            "107967\t6\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "107966\t6\t100000011\t2\t3\tcate_order_history\t0\t1\n" +
            "107965\t6\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "107962\t6\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "107960\t6\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "107948\t6\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107943\t6\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "107941\t6\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "107939\t6\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "107938\t6\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "107937\t6\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "107936\t6\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "107935\t6\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "107932\t6\t100000013\t2\t3\tcate_order_history\t2\t2\n" +
            "107922\t11\t100000012\t2\t1\tcate_order_history\t1\t1\n" +
            "107916\t6\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "107914\t6\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "107911\t6\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "107910\t6\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "107909\t6\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "107908\t6\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "107907\t6\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "107906\t6\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "107903\t6\t100000013\t2\t2\tcate_order_history\t2\t2\n" +
            "107902\t6\t100000011\t2\t1\tcate_order_1y\t0\t1\n" +
            "107901\t6\t100000012\t2\t2\tcate_order_1y\t1\t1\n" +
            "107891\t6\t100000011\t2\t2\tcate_order_history\t0\t1\n" +
            "107879\t6\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "107878\t6\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "107876\t6\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "107873\t6\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "107872\t6\t100000011\t2\t2\tcate_order_1y\t0\t1\n" +
            "107870\t6\t100000011\t2\t2\tcate_order_1y\t0\t1\n" +
            "107862\t6\t100000011\t2\t3\tcate_order_history\t0\t1\n" +
            "107856\t6\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "107854\t6\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "107853\t6\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "107852\t6\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "107850\t6\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "107849\t6\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "107848\t6\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "107847\t6\t100000012\t2\t1\tcate_order_history\t1\t1\n" +
            "107834\t11\t100000011\t2\t2\tcate_order_history\t0\t1\n" +
            "107833\t11\t100000011\t2\t2\tcate_order_history\t0\t1\n" +
            "107832\t11\t100000011\t2\t2\tcate_order_history\t0\t1\n" +
            "107831\t10\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107826\t6\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "107825\t6\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "107824\t6\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "107821\t6\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "107809\t6\t100000011\t2\t2\tcate_order_history\t0\t1\n" +
            "107808\t6\t100000011\t2\t2\tcate_order_history\t0\t1\n" +
            "107806\t6\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "107804\t6\t100000011\t2\t2\tcate_order_history\t0\t1\n" +
            "107803\t11\t100000011\t2\t2\tcate_order_history\t0\t1\n" +
            "107802\t11\t100000011\t2\t2\tcate_order_history\t0\t1\n" +
            "107801\t11\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107796\t11\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107795\t6\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107794\t10\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107790\t6\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107788\t6\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107787\t6\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107786\t6\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107783\t11\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "107777\t11\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "107772\t11\t100000011\t2\t1\tcate_order_1y\t0\t1\n" +
            "107765\t10\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107763\t6\t100000012\t2\t3\tcate_order_3m\t1\t1\n" +
            "107762\t6\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107761\t6\t100000013\t2\t3\tcate_order_3m\t2\t2\n" +
            "107759\t6\t100000012\t2\t3\tcate_order_3m\t1\t1\n" +
            "107758\t6\t100000012\t2\t3\tcate_order_3m\t1\t1\n" +
            "107757\t10\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107756\t10\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107755\t6\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107754\t6\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107746\t11\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107736\t6\t100000011\t2\t2\tcate_order_history\t0\t1\n" +
            "107727\t6\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107726\t6\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107721\t10\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107719\t6\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107717\t11\t100000011\t2\t2\tcate_order_history\t0\t1\n" +
            "107710\t11\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107708\t6\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107707\t6\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107704\t6\t100000012\t2\t3\tcate_order_3m\t1\t1\n" +
            "107703\t6\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107702\t10\t100000013\t2\t3\tcate_order_3m\t2\t2\n" +
            "107700\t6\t100000013\t2\t3\tcate_order_6m\t2\t2\n" +
            "107699\t6\t100000012\t2\t1\tcate_order_3m\t1\t1\n" +
            "107698\t6\t100000012\t2\t3\tcate_order_3m\t1\t1\n" +
            "107684\t11\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107683\t11\t100000011\t2\t1\tcate_order_1y\t0\t1\n" +
            "107673\t11\t100000011\t2\t2\tcate_order_history\t0\t1\n" +
            "107671\t6\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107670\t6\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107668\t6\t100000012\t2\t1\tcate_order_history\t1\t1\n" +
            "107664\t6\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107663\t2\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107647\t6\t100000013\t2\t1\tcate_order_history\t2\t2\n" +
            "107646\t6\t100000012\t2\t1\tcate_order_history\t1\t1\n" +
            "107645\t6\t100000011\t2\t2\tcate_order_history\t0\t1\n" +
            "107642\t11\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107639\t6\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107638\t11\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107637\t11\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107635\t6\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107634\t2\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107632\t6\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107631\t6\t100000012\t2\t1\tcate_order_history\t1\t1\n" +
            "107630\t8\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107627\t11\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107618\t11\t100000011\t2\t2\tcate_order_history\t0\t1\n" +
            "107615\t6\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107611\t6\t100000011\t2\t2\tcate_order_3y\t0\t1\n" +
            "107610\t6\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107609\t6\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107608\t6\t100000012\t2\t1\tcate_order_history\t1\t1\n" +
            "107607\t6\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107606\t6\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107604\t11\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107602\t11\t100000011\t2\t2\tcate_order_history\t0\t1\n" +
            "107589\t2\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107588\t6\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107586\t11\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107585\t6\t100000012\t2\t3\tcate_order_3m\t1\t1\n" +
            "107584\t6\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107580\t6\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107578\t6\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107577\t10\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107576\t6\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107575\t11\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107573\t11\t100000012\t2\t1\tcate_order_history\t1\t1\n" +
            "107570\t6\t100000011\t2\t3\tcate_order_history\t0\t1\n" +
            "107562\t11\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "107560\t10\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107558\t6\t100000011\t2\t2\tcate_order_3y\t0\t1\n" +
            "107557\t6\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107556\t6\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107553\t6\t100000012\t2\t1\tcate_order_history\t1\t1\n" +
            "107552\t10\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107551\t6\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107550\t6\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107549\t11\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107548\t6\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107547\t11\t100000011\t2\t2\tcate_order_history\t0\t1\n" +
            "107546\t11\t100000011\t2\t2\tcate_order_history\t0\t1\n" +
            "107529\t6\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "107528\t6\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "107521\t6\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107520\t6\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107517\t6\t100000012\t2\t3\tcate_order_3m\t1\t1\n" +
            "107516\t6\t100000011\t2\t2\tcate_order_3y\t0\t1\n" +
            "107515\t6\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107513\t6\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107512\t6\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "107507\t11\t100000012\t2\t1\tcate_order_3m\t1\t1\n" +
            "107171\t6\t100000011\t2\t2\tcate_order_1y\t0\t1\n" +
            "107143\t11\t100000012\t2\t1\tcate_order_history\t1\t1\n" +
            "107119\t11\t100000012\t2\t1\tcate_order_history\t1\t1\n" +
            "107112\t11\t100000012\t2\t1\tcate_order_history\t1\t1\n" +
            "107085\t11\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "107081\t11\t100000013\t2\t1\tcate_order_history\t2\t2\n" +
            "107054\t11\t100000013\t2\t1\tcate_order_history\t2\t2\n" +
            "106994\t6\t100000011\t2\t2\tcate_order_1y\t0\t1\n" +
            "106993\t6\t100000011\t2\t2\tcate_order_1y\t0\t1\n" +
            "106969\t6\t100000011\t2\t2\tcate_order_1y\t0\t1\n" +
            "106968\t6\t100000011\t2\t2\tcate_order_1y\t0\t1\n" +
            "106911\t11\t100000012\t2\t1\tcate_order_history\t1\t1\n" +
            "106883\t11\t100000011\t2\t1\tcate_order_1y\t0\t1\n" +
            "106872\t11\t100000013\t2\t1\tcate_order_history\t2\t2\n" +
            "106854\t11\t100000013\t2\t1\tcate_order_history\t2\t2\n" +
            "106823\t11\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "106822\t11\t100000012\t2\t1\tcate_order_history\t1\t1\n" +
            "106821\t11\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "106820\t11\t100000011\t2\t1\tcate_order_1y\t0\t1\n" +
            "106818\t11\t100000013\t2\t2\tcate_order_history\t2\t2\n" +
            "106812\t11\t100000012\t2\t1\tcate_order_history\t1\t1\n" +
            "106796\t11\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "106795\t11\t100000013\t2\t1\tcate_order_history\t2\t2\n" +
            "106787\t11\t100000011\t2\t1\tcate_order_1y\t0\t1\n" +
            "106737\t11\t100000011\t2\t1\tcate_order_1y\t0\t1\n" +
            "106727\t11\t100000011\t2\t1\tcate_order_1y\t0\t1\n" +
            "106722\t11\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "106679\t11\t100000012\t2\t1\tcate_order_history\t1\t1\n" +
            "106678\t11\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "106675\t11\t100000011\t2\t1\tcate_order_1y\t0\t1\n" +
            "106666\t11\t100000012\t2\t1\tcate_order_history\t1\t1\n" +
            "106633\t11\t100000012\t2\t2\tcate_order_history\t1\t1\n" +
            "106618\t11\t100000012\t2\t1\tcate_order_history\t1\t1\n" +
            "106613\t11\t100000011\t2\t3\tcate_order_1y\t0\t1\n" +
            "106609\t11\t100000011\t2\t2\tcate_order_history\t0\t1\n" +
            "106602\t11\t100000011\t2\t1\tcate_order_1y\t0\t1\n" +
            "106578\t11\t100000011\t2\t2\tcate_order_history\t0\t1\n" +
            "106576\t11\t100000011\t2\t2\tcate_order_history\t0\t1\n" +
            "106574\t11\t100000011\t2\t3\tcate_order_history\t0\t1\n" +
            "106547\t11\t100000012\t2\t1\tcate_order_history\t1\t1\n" +
            "106542\t11\t100000012\t2\t1\tcate_order_history\t1\t1\n" +
            "106519\t11\t100000012\t2\t3\tcate_order_history\t1\t1\n" +
            "106518\t11\t100000011\t2\t3\tcate_order_history\t0\t1\n" +
            "106516\t11\t100000012\t2\t1\tcate_order_history\t1\t1\n" +
            "106491\t11\t100000011\t2\t3\tcate_order_history\t0\t1\n" +
            "106488\t11\t100000011\t2\t1\tcate_order_1y\t0\t1\n" +
            "106487\t11\t100000012\t2\t1\tcate_order_history\t1\t1\n" +
            "106460\t11\t100000011\t2\t1\tcate_order_1y\t0\t1\n" +
            "106426\t11\t100000012\t2\t3\tcate_order_history\t1\t1\n" +
            "106376\t11\t100000012\t2\t1\tcate_order_history\t1\t1\n" +
            "106370\t11\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "106368\t11\t100000011\t2\t1\tcate_order_history\t0\t1\n" +
            "106367\t11\t100000013\t2\t3\tcate_order_history\t2\t2\n" +
            "106366\t11\t100000012\t2\t3\tcate_order_history\t1\t1\n" +
            "106335\t11\t100000011\t2\t1\tcate_order_1y\t0\t1\n";


    public static String doGet(String goodsUrl) {
        CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet(goodsUrl);

        httpGet.addHeader("contentType", contentTypeJson);
        httpGet.addHeader("User-Agent",userAgent);
        httpGet.addHeader("Cookie", cookieFormat);
        String res = null;

        try {
            HttpResponse httpResponse = closeableHttpClient.execute(httpGet);
            res = EntityUtils.toString(httpResponse.getEntity());

            //System.out.println(res);
            //unescape("%u4E2D%u6587")  goods-data
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    static class PlanDTO{
        Integer code;
        PlanContentDTO content;

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public PlanContentDTO getContent() {
            return content;
        }

        public void setContent(PlanContentDTO content) {
            this.content = content;
        }
    }
    static class PlanContentDTO{
        PlanContentSourceDTO _source;

        public PlanContentSourceDTO get_source() {
            return _source;
        }

        public void set_source(PlanContentSourceDTO _source) {
            this._source = _source;
        }
    }
    static class PlanContentSourceDTO{
        String planId;
        PlanContentSourceMarketCrowdDTO marketCrowd;

        public String getPlanId() {
            return planId;
        }

        public void setPlanId(String planId) {
            this.planId = planId;
        }

        public PlanContentSourceMarketCrowdDTO getMarketCrowd() {
            return marketCrowd;
        }

        public void setMarketCrowd(PlanContentSourceMarketCrowdDTO marketCrowd) {
            this.marketCrowd = marketCrowd;
        }
    }
    static class PlanContentSourceMarketCrowdDTO{
        String excludePin;
        Integer identityFilterType;
        Integer identityCategoryType;
        Integer identityOrderNum;
        Integer identityOrderNumCompareType;
        String identityTimeType;

        public String getExcludePin() {
            return excludePin;
        }

        public void setExcludePin(String excludePin) {
            this.excludePin = excludePin;
        }

        public Integer getIdentityFilterType() {
            return identityFilterType;
        }

        public void setIdentityFilterType(Integer identityFilterType) {
            this.identityFilterType = identityFilterType;
        }

        public Integer getIdentityCategoryType() {
            return identityCategoryType;
        }

        public void setIdentityCategoryType(Integer identityCategoryType) {
            this.identityCategoryType = identityCategoryType;
        }

        public Integer getIdentityOrderNum() {
            return identityOrderNum;
        }

        public void setIdentityOrderNum(Integer identityOrderNum) {
            this.identityOrderNum = identityOrderNum;
        }

        public Integer getIdentityOrderNumCompareType() {
            return identityOrderNumCompareType;
        }

        public void setIdentityOrderNumCompareType(Integer identityOrderNumCompareType) {
            this.identityOrderNumCompareType = identityOrderNumCompareType;
        }

        public String getIdentityTimeType() {
            return identityTimeType;
        }

        public void setIdentityTimeType(String identityTimeType) {
            this.identityTimeType = identityTimeType;
        }
    }

    public static void main(String[] args) {
        String url = "http://esm.jd.com/es/operate?nodeUrl=http%3A%2F%2F11.3.52.171%3A40000%2Fsmart_strategy_plan%2Fplan_info%2F{0}&methodType=GET&opType=clusterOperate&content=";
        String fileName = "planCompare.txt";
        /*String planIds = "108135";
        List<String> planIdList = new ArrayList(Arrays.asList(planIds.split("\\,")));
        for (int i = 0; i < planIdList.size(); i++) {
            PlanDTO planInfo = JSON.parseObject(doGet(MessageFormat.format(url, planIdList.get(i))), PlanDTO.class);
            PlanContentSourceMarketCrowdDTO marketCrowdInfo = planInfo.getContent().get_source().getMarketCrowd();
            System.out.println(planInfo.getContent().get_source().getPlanId() + " : " + JSON.toJSONString(marketCrowdInfo));
        }*/

        List<String> dbPlanIdList = new ArrayList(Arrays.asList(dbPlanInfo.split("\\n")));
        System.out.println(dbPlanIdList.size());
        for (int i = 0; i < dbPlanIdList.size(); i++) {
            List<String> dbPlanInfo = new ArrayList(Arrays.asList(dbPlanIdList.get(i).split("\\t")));
            String planId = dbPlanInfo.get(0).trim();
            PlanDTO planInfo = JSON.parseObject(doGet(MessageFormat.format(url, planId)), PlanDTO.class);
            PlanContentSourceMarketCrowdDTO marketCrowdInfo = planInfo.getContent().get_source().getMarketCrowd();
            String info;
            if(!planInfo.getContent().get_source().getPlanId().equals(planId)){
                info = "error in planId :" +planId;
                System.out.println(info);
                FileOperation.writeFileByAppend(info,fileName);
            }
            boolean identityMatch = marketCrowdInfo.getIdentityFilterType().toString().equals(dbPlanInfo.get(3).trim())
                    && marketCrowdInfo.getIdentityCategoryType().toString().equals(dbPlanInfo.get(4).trim())
                    && marketCrowdInfo.getIdentityOrderNum().toString().equals(dbPlanInfo.get(6).trim())
                    && marketCrowdInfo.getIdentityOrderNumCompareType().toString().equals(dbPlanInfo.get(7).trim())
                    && marketCrowdInfo.getIdentityTimeType().equals(dbPlanInfo.get(5).trim());
            if(!identityMatch){
                info = "identityMatch wrong in planId :" +planId + ", dbInfo :" + dbPlanInfo + ", esInfo: " + JSON.toJSONString(marketCrowdInfo);
                System.out.println(info);
            }else {
                info = "identityMatch success in planId :" +planId + ", dbInfo :" + dbPlanInfo + ", esInfo: " + JSON.toJSONString(marketCrowdInfo);
                System.out.println(info);
            }
            FileOperation.writeFileByAppend(info,fileName);
        }
    }
}
