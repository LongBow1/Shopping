package com.myqq.service.autoshopping;

import com.alibaba.fastjson.JSONObject;
import com.myqq.service.youza.bll.AutoShoppingEntryForApp;
import com.myqq.service.youza.bll.ShopInfoDTO;
import com.myqq.service.youza.entity.IntendOrderDTO;
import com.myqq.service.youza.entity.ShoppingForAppDTO;
import com.myqq.service.youza.entity.ToBuyGoodInfoAppDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.myqq.service.youza.constinfo.ConstInfoForApp.*;

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

		System.out.println(AutoShoppingEntryForApp.dateTimeFormatter.format(LocalDateTime.now())+"test");

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
	@Test
	public void localAddressTest(){
		/*String data = "";
		try {
			data = new String(Files.readAllBytes(Paths.get("D:\\DEV\\Shopping-release\\auto-shopping\\src\\test\\java\\com\\myqq\\service\\autoshopping\\address.txt")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(data);*/
		String path = "addressinfo"+qqMemberId+".txt";
		File file = new File(path);
		if(file.exists()){
			System.out.println(path + " exist");
		}else {
			System.out.println(path + " not exist");
		}
		StringBuilder existAddress = new StringBuilder();
		try {
			FileInputStream fileInputStream = new FileInputStream(path);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
			String line = null;
			while ((line = bufferedReader.readLine()) != null){
				System.out.println(line);
				existAddress.append(line);
			}
			fileInputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {

		}
		List<ShoppingForAppDTO.AddressDataRowDetailDTO> toAddAddress = new ArrayList<>();
		List<ShoppingForAppDTO.AddressDataRowDetailDTO> localAddressList = new ArrayList<>();
		if(existAddress.toString().length() > 0){
			localAddressList = JSONObject.parseArray(existAddress.toString(),ShoppingForAppDTO.AddressDataRowDetailDTO.class);
		}
		localAddressList.sort(Comparator.comparing(ShoppingForAppDTO.AddressDataRowDetailDTO::getReceiveName));
		localAddressList.stream().sorted(Comparator.comparing(ShoppingForAppDTO.AddressDataRowDetailDTO::getReceiveName));
		//String address = RequestBllForApp.doGet(MessageFormat.format(getAddressInfoUrlForApp, zzjjMemberId), zzjjAuth);
		try {
			String remoteAddress = AutoShoppingEntryForApp.getAddressInfo(zzjjAuth, zzjjMemberId, false);
			if(remoteAddress != null && remoteAddress.trim() != "null" && remoteAddress.trim() != ""){
				List<ShoppingForAppDTO.AddressDataRowDetailDTO> remoteAddressList = JSONObject.parseArray(remoteAddress,ShoppingForAppDTO.AddressDataRowDetailDTO.class );
				if(remoteAddressList != null && !remoteAddressList.isEmpty()){
					List<ShoppingForAppDTO.AddressDataRowDetailDTO> finalLocalAddressList = localAddressList;
					remoteAddressList.forEach(item -> {
						ShoppingForAppDTO.AddressDataRowDetailDTO tmpInfo = new ShoppingForAppDTO.AddressDataRowDetailDTO();
						tmpInfo.setAddressId(item.getAddressId());
						tmpInfo.setReceiveName(item.getReceiveName());
						tmpInfo.setReceivePhone(item.getReceivePhone());
						tmpInfo.setProvince(item.getProvince());
						tmpInfo.setCity(item.getCity());
						tmpInfo.setArea(item.getArea());
						tmpInfo.setAddress(item.getAddress());
						if(!finalLocalAddressList.contains(tmpInfo)){
							toAddAddress.add(tmpInfo);
							finalLocalAddressList.add(tmpInfo);
						}
					});
				}
			}
			if(!toAddAddress.isEmpty()){
				System.out.println("toAddAddress count: "+ toAddAddress.size());
				FileOutputStream fileOutputStream = new FileOutputStream(file);
				fileOutputStream.write(JSONObject.toJSONString(localAddressList).getBytes());
				fileOutputStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {

		}
	}

}
