package com.myqq.service.autoshopping;

import org.assertj.core.util.Lists;
import org.assertj.core.util.Maps;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StreamTest {

    static class AClass{
        public AClass(int code, String desc, String desc2){

        }

    }
    @Test
    public void mergeMapValuesTest(){
        Map<Integer, List<AClass>> map = Maps.newHashMap(1,new ArrayList<>());
        List<AClass> aClassList1 = Lists.newArrayList();
        AClass aClass = new AClass(1, "zhuoli1", "haha1");
        aClassList1.add(aClass);
        aClassList1.add(new AClass(2, "zhuoli2", "haha2"));
        aClassList1.add(new AClass(3, "zhuoli3", "haha3"));

        List<AClass> aClassList2 = Lists.newArrayList();
        aClassList2.add(aClass);
        aClassList2.add(new AClass(5, "zhuoli5", "haha5"));
        aClassList2.add(new AClass(6, "zhuoli6", "haha6"));

        /*交集*/
        /*[AClass(id=1, name=zhuoli1, description=haha1)]*/
        List<AClass> intersectResult = aClassList1.stream().filter(aClassList2::contains).collect(Collectors.toList());
        System.out.println(intersectResult);

        /*并集*/
        List<AClass> unionResult = Stream.of(aClassList1, aClassList2).flatMap(Collection::stream).distinct().collect(Collectors.toList());
        assertEquals(unionResult.size(), 5);
        System.out.println(unionResult);

        /*差集*/
        /*[AClass(id=2, name=zhuoli2, description=haha2), AClass(id=3, name=zhuoli3, description=haha3)]*/
        List<AClass> differenceResult = aClassList1.stream().filter(x -> !aClassList2.contains(x)).collect(Collectors.toList());
        System.out.println(differenceResult);

        map.put(1, new ArrayList(aClassList1));
        map.put(2, new ArrayList(aClassList2));

        /*合并多个list*/
        //List<AClass> aClassListResult = map.values().stream().flatMap(listContainer -> listContainer.getLst().stream()).collect(Collectors.toList());
        /*注意跟并集的区别*/
        //assertEquals(aClassListResult.size(), 6);
        //System.out.println(aClassListResult);
    }


    public class Data1 {
        private int id;
        private String name;
        private int amount;
        public Data1(int id,String name,int amount){
            this.id = id;
            this.name = name;
            this.amount = amount;
        }
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }
    }

    public class Data2 {
        private int id;
        private String name;
        private String type;

        public Data2(int id,String name,String type){
            this.id = id;
            this.name = name;
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public class OutputData {
        private int id;
        private String name;
        private String type;
        private int amount;

        public OutputData(int id,String name,String type,int amount){
            this.id = id;
            this.name = name;
            this.type = type;
            this.amount = amount;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }
    }


    @Test
    public void intersectByKeyTest() {
        List<Data2> listOfData2 = new ArrayList<Data2>();

        listOfData2.add(new Data2(10501, "JOE", "Type1"));
        listOfData2.add(new Data2(10603, "SAL", "Type5"));
        listOfData2.add(new Data2(40514, "PETER", "Type4"));
        listOfData2.add(new Data2(59562, "JIM", "Type2"));
        listOfData2.add(new Data2(29415, "BOB", "Type1"));
        listOfData2.add(new Data2(61812, "JOE", "Type9"));
        listOfData2.add(new Data2(98432, "JOE", "Type7"));
        listOfData2.add(new Data2(62556, "JEFF", "Type1"));
        listOfData2.add(new Data2(10599, "TOM", "Type4"));


        List<Data1> listOfData1 = new ArrayList<Data1>();

        listOfData1.add(new Data1(10501, "JOE", 3000000));
        listOfData1.add(new Data1(10603, "SAL", 6225000));
        listOfData1.add(new Data1(40514, "PETER", 2005000));
        listOfData1.add(new Data1(59562, "JIM", 3000000));
        listOfData1.add(new Data1(29415, "BOB", 3000000));

        List<OutputData> result = listOfData1.stream()
                .flatMap(x -> listOfData2.stream()
                        .filter(y -> x.getId() == y.getId())
                        .map(y -> new OutputData(y.getId(), x.getName(), y.getType(), x.getAmount())))
                .collect(Collectors.toList());
        System.out.println(result);

        /*difference by key*/
        List<Data1> data1IntersectResult = listOfData1.stream().filter(data1 -> listOfData2.stream().map(Data2::getId).collect(Collectors.toList()).contains(data1.getId())).collect(Collectors.toList());
        System.out.println(data1IntersectResult);
    }

    @Test
    public void flatMapToIntTest() {
        List<List<String>> listOfLists = Arrays.asList(
                Arrays.asList("1", "2"),
                Arrays.asList("5", "6"),
                Arrays.asList("3", "4")
        );

        IntStream intStream =
                listOfLists.stream()
                        .flatMapToInt(childList ->
                                childList.stream()
                                        .mapToInt(Integer::new));

        int sum = intStream.peek(System.out::println).sum();
        System.out.println("sum: " + sum);
    }
}
