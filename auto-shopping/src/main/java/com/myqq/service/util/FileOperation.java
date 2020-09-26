package com.myqq.service.util;

import com.myqq.service.youza.util.TimeUtil;
import org.springframework.util.StringUtils;

import java.io.*;

public class FileOperation {

    public static boolean writeFile(String jsonContent, String filePath){
        if(StringUtils.isEmpty(jsonContent)){
            return true;
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            fileOutputStream.write(jsonContent.getBytes("utf-8"));
            fileOutputStream.close();
        } catch (Exception e) {
            System.out.println(TimeUtil.getCurrentTimeString()+" writeFile "+filePath+" ex");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static String readFile(String filePath){
        StringBuilder fileContentBuilder = new StringBuilder();
        String addressInfo;
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream,"utf-8"));
            while ((addressInfo = bufferedReader.readLine()) != null){
                fileContentBuilder.append(addressInfo);
            }
            fileInputStream.close();
        } catch (Exception ex) {
            System.out.println(TimeUtil.getCurrentTimeString()+" readFile "+filePath+" ex");
            ex.printStackTrace();
        }
        return fileContentBuilder.toString();
    }

    public static void main(String[] args) {
        String json = "{'title':'标题','content':'内容'}";
        String filePath = "writeFileTest.txt";
        System.out.println(writeFile(json, filePath));
        System.out.println(readFile(filePath));
    }
}
