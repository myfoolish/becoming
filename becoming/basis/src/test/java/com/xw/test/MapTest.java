package com.xw.test;

import org.junit.Test;

import java.util.*;

/**
 * @author liuxiaowei
 * @description
 * @date 2020/7/23 11:32
 */
public class MapTest {
    @Test
    public void map() {
        Map<String, String> map = new HashMap<>();
        map.put("Passerby", "刘");
        map.put("MyFoolish", "晓");
        map.put("XwCoding", "威");

        //public Set<K> keySet() : 获取Map集合中所有的键，存储到Set集合中。
        Set<String> keys = map.keySet();
        for (String key : keys) {
            String value = map.get(key);
            System.out.println(key + "===" + value);
        }
        //public Set<Map.Entry<K,V>> entrySet() : 获取到Map集合中所有的键值对对象的集合(Set集合)。
        Set<Map.Entry<String, String>> entrySet = map.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            String key = entry.getKey();
            String value = entry.getValue();
            System.out.println(key + "---" + value);
        }

        // 链表和哈希表组合的一个数据存储结构。
        Map<String, String> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("Passerby", "刘");
        linkedHashMap.put("MyFoolish", "晓");
        linkedHashMap.put("XwCoding", "威");
        Set<Map.Entry<String, String>> entrySet1 = linkedHashMap.entrySet();
        for (Map.Entry<String, String> entry : entrySet1) {
            System.out.println(entry.getKey() + "-=-" + entry.getValue());
        }
    }

    @Test
    public void Poker() {
//       1 组装54张扑克牌

        // 1.1 创建Map集合存储
        Map<Integer, String> pokerMap = new HashMap<>();
        // 1.2 创建花色集合与数字集合
        List<String> colors = new ArrayList<>();
        List<String> numbers = new ArrayList<>();
        // 1.3 存储花色与数字
        Collections.addAll(colors, "♦", "♣", "♥", "♠");
        Collections.addAll(numbers, "2", "A", "K", "Q", "J", "10", "9", "8", "7", "6", "5", "4", "3");
        // 设置存储编号
        int count = 1;
        pokerMap.put(count++, "大王");
        pokerMap.put(count++, "小王");
        // 1.4 创建牌 存储到map集合中
        for (String number : numbers) {
            for (String color : colors) {
                String card = number + color;
                pokerMap.put(count++, card);
            }
        }
//       2 将54张牌顺序打乱

        // 取出编号 集合
        Set<Integer> numberSet = pokerMap.keySet();
        // 因为要将编号打乱顺序 所以 应该先进行转换到 list集合中
        List<Integer> numberList = new ArrayList<>();
        numberList.addAll(numberSet);

        Collections.shuffle(numberList);

//        3 完成三个玩家交替摸牌，每人17张牌，最后三张留作底牌
        // 3.1 发牌的编号
        // 创建三个玩家编号集合 和一个 底牌编号集合
        ArrayList<Integer> noP1 = new ArrayList<Integer>();
        ArrayList<Integer> noP2 = new ArrayList<Integer>();
        ArrayList<Integer> noP3 = new ArrayList<Integer>();
        ArrayList<Integer> dipaiNo = new ArrayList<Integer>();

        // 3.2发牌的编号
        for (int i = 0; i < numberList.size(); i++) {
            // 获取该编号
            Integer no = numberList.get(i);
            // 发牌
            // 留出底牌
            if (i >= 51) {
                dipaiNo.add(no);
            } else {
                if (i % 3 == 0) {
                    noP1.add(no);
                } else if (i % 3 == 1) {
                    noP2.add(no);
                } else {
                    noP3.add(no);
                }
            }
        }

//        4 查看三人各自手中的牌（按照牌的大小排序）、底牌

        // 4.1 对手中编号进行排序
        Collections.sort(noP1);
        Collections.sort(noP2);
        Collections.sort(noP3);
        Collections.sort(dipaiNo);
        // 4.2 进行牌面的转换
        // 创建三个玩家牌面集合 以及底牌牌面集合
        ArrayList<String> player1 = new ArrayList<String>();
        ArrayList<String> player2 = new ArrayList<String>();
        ArrayList<String> player3 = new ArrayList<String>();
        ArrayList<String> dipai = new ArrayList<String>();

        // 4.3转换
        for (Integer i : noP1) {
            // 4.4 根据编号找到 牌面 pokerMap
            String card = pokerMap.get(i);
            // 添加到对应的 牌面集合中
            player1.add(card);
        }
        for (Integer i : noP2) {
            String card = pokerMap.get(i);
            player2.add(card);
        }
        for (Integer i : noP3) {
            String card = pokerMap.get(i);
            player3.add(card);
        }
        for (Integer i : dipaiNo) {
            String card = pokerMap.get(i);
            dipai.add(card);
        }
        //4.5 查看
        System.out.println("柳岩："+player1);
        System.out.println("唐嫣："+player2);
        System.out.println("刘德华："+player3);
        System.out.println("底牌："+dipai);
    }
}
