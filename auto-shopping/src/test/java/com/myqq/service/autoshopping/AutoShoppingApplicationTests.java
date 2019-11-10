package com.myqq.service.autoshopping;

import com.alibaba.fastjson.JSONObject;
import com.myqq.service.youza.bll.ShopInfoDTO;
import com.myqq.service.youza.entity.IntendOrderDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootTest
class AutoShoppingApplicationTests {
	public static ConcurrentHashMap<String,Boolean> mapStartShoppingSymbol = new ConcurrentHashMap<>(2);
	String session = "YZ575840077289181184YZL1i1uMFh";
	@Test
	void contextLoads() {

		System.out.println(MessageFormat.format("this {0} a {0}", "oohhh"));
		String testOStr = "{\"goodShotName\":\"1918\",\"goodColorList\":[\"白\"],\"goodSizeList\":[\"36\"],\"goodNum\":\"1\",\"receptNameList\":[\"李松俊\",\"赵赵\"]}";
		IntendOrderDTO intendOrderInfo = null;
		mapStartShoppingSymbol.putIfAbsent(session,false);
		System.out.println(mapStartShoppingSymbol.get(session));
		/*try {
			intendOrderInfo = JSONObject.parseObject(testOStr, IntendOrderDTO.class);
		}catch (Exception ex){
			ex.printStackTrace();
		}*/
		mapStartShoppingSymbol.put(session,true);
		System.out.println(mapStartShoppingSymbol.get(session));

		System.out.println(UUID.randomUUID().toString());

		System.out.println("calendar:"+Calendar.getInstance().toString());
		System.out.println("calendar ins:"+Calendar.getInstance().toInstant().toString());
	}


	@Test
	public void otherTest(){

		ShopInfoDTO.ToBuyGoodSkuInfo toBuyGoodSkuInfo = new ShopInfoDTO.ToBuyGoodSkuInfo(1,1,"");
		ShopInfoDTO.ToBuyGoodSkuInfo toBuyGoodSkuInfo2 = new ShopInfoDTO.ToBuyGoodSkuInfo(1,2,"");
		ShopInfoDTO.ToBuyGoodSkuInfo toBuyGoodSkuInfo3 = new ShopInfoDTO.ToBuyGoodSkuInfo(1,3,"");
		List<ShopInfoDTO.ToBuyGoodSkuInfo> toBuyGoodSkuInfos = new ArrayList<>();
		toBuyGoodSkuInfos.add(toBuyGoodSkuInfo);
		toBuyGoodSkuInfos.add(toBuyGoodSkuInfo2);
		toBuyGoodSkuInfos.add(toBuyGoodSkuInfo3);
		toBuyGoodSkuInfo.num = 4;
		toBuyGoodSkuInfos.add(toBuyGoodSkuInfo);

		toBuyGoodSkuInfos.remove(toBuyGoodSkuInfo);
		System.out.println(toBuyGoodSkuInfo.equals(toBuyGoodSkuInfo2));

		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		System.out.println(ZoneId.systemDefault());
		System.out.println(dateTimeFormatter.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(1572763513273L), ZoneId.systemDefault())));
		System.out.printf(UUID.randomUUID().toString());
	}
}
