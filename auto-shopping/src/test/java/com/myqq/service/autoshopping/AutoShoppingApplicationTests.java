package com.myqq.service.autoshopping;

import com.alibaba.fastjson.JSONObject;
import com.myqq.service.youza.entity.IntendOrderDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.MessageFormat;
import java.util.Calendar;
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

		System.out.printf(UUID.randomUUID().toString());
	}
}
