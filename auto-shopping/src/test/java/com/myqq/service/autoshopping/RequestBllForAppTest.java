package com.myqq.service.autoshopping;

import com.myqq.service.youza.bll.RequestBllForApp;
import com.myqq.service.youza.entity.ShoppingForAppDTO;
import com.myqq.service.youza.entity.ToBuyGoodInfoAppDTO;
import org.apache.http.entity.StringEntity;
import org.junit.jupiter.api.Test;

public class RequestBllForAppTest {

    @Test
    public void commitOrderDetailTest(){
        ToBuyGoodInfoAppDTO.ToBuyGoodAndAddressInfoDTO toBuy = new  ToBuyGoodInfoAppDTO.ToBuyGoodAndAddressInfoDTO();
        ShoppingForAppDTO.GoodDataStockDetailDTO buyGood = new ShoppingForAppDTO.GoodDataStockDetailDTO();
        StringEntity commitPostEntity = null;
        String auth = "";

        String result = RequestBllForApp.commitOrderDetail(toBuy, buyGood, commitPostEntity, auth, 1);
        System.out.println(result);
        String result2 = RequestBllForApp.commitOrderDetail(toBuy, buyGood, commitPostEntity, auth, 2);
        System.out.println(result2);
    }
}
