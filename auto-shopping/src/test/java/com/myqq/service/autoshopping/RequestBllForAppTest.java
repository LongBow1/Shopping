package com.myqq.service.autoshopping;

import com.myqq.service.youza.bll.RequestBllForApp;
import com.myqq.service.youza.entity.ShoppingForAppDTO;
import com.myqq.service.youza.entity.ToBuyGoodInfoAppDTO;
import com.myqq.service.youza.util.CustomThreadFactory;
import com.myqq.service.youza.util.TimeUtil;
import org.apache.http.entity.StringEntity;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class RequestBllForAppTest {

    public static ThreadPoolExecutor testExecutorServiceForCommitOrder = new ThreadPoolExecutor (3, 10, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<>(100), new CustomThreadFactory("RequestBllForAppTest")) {
    };
    @Test
    public void commitOrderDetailTest(){
        ToBuyGoodInfoAppDTO.ToBuyGoodAndAddressInfoDTO toBuy = new  ToBuyGoodInfoAppDTO.ToBuyGoodAndAddressInfoDTO();
        ShoppingForAppDTO.GoodDataStockDetailDTO buyGood = new ShoppingForAppDTO.GoodDataStockDetailDTO();
        StringEntity commitPostEntity = null;
        String auth = "";

        /*String result = RequestBllForApp.commitOrderDetail(toBuy, buyGood, commitPostEntity, auth, 1);
        System.out.println(result);
        String result2 = RequestBllForApp.commitOrderDetail(toBuy, buyGood, commitPostEntity, auth, 2);
        System.out.println(result2);*/

        List<Future<String>> commitOrderFutures = new ArrayList<>();
        /*commitOrderFutures.add(testExecutorServiceForCommitOrder.submit(() -> RequestBllForApp.commitOrderDetail(toBuy, buyGood, commitPostEntity, auth, 1)));
        commitOrderFutures.add(testExecutorServiceForCommitOrder.submit(() -> RequestBllForApp.commitOrderDetail(toBuy, buyGood, commitPostEntity, auth, 2)));*/

        commitOrderFutures.forEach(task -> {
            try {
                String commitResult = task.get();
                System.out.println(TimeUtil.getCurrentTimeString() + commitResult);
                if(commitResult != null && (commitResult.contains("下单太频繁") || commitResult.contains("系统繁忙"))){
                    Thread.sleep(500);
                    System.out.println(TimeUtil.getCurrentTimeString() +  Thread.currentThread().getName()+" 暂停500 ");
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            } catch (ExecutionException ex) {
                ex.printStackTrace();
            }
        });
    }
}
