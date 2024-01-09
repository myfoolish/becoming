package com.xw.test;

import org.junit.Test;

import java.util.*;

/**
 * @author liuxiaowei
 * @description
 * @date 2020/7/17 13:16
 */
public class CollectionTest {
    /**
     * public boolean add(E e) ： 把给定的对象添加到当前集合中 。
     * public void clear() :清空集合中所有的元素。
     * public boolean remove(E e) : 把给定的对象在当前集合中删除。
     * public boolean contains(Object obj) : 判断当前集合中是否包含给定的对象。
     * public boolean isEmpty() : 判断当前集合是否为空。
     * public int size() : 返回集合中元素的个数。
     * public Object[] toArray() : 把集合中的元素，存储到数组中。
     */
    @Test
    public void collection() {
        // 使用多态形式创建集合
        Collection<String> collection = new ArrayList<>();

        // 添加功能 boolean add(String s)
        collection.add("小李广");
        collection.add("扫地僧");
        collection.add("石破天");

        // boolean remove(E e) 删除在集合中的o元素
        boolean b1 = collection.remove("石破天");  //true
        // boolean contains(E e) 判断o是否在集合中存在
        boolean b2 = collection.contains("石破天");    //false
        // boolean isEmpty() 判断是否为空
        boolean b3 = collection.isEmpty();  //false
        // size() 集合中有几个元素
        int size = collection.size();  //2
        // Object[] toArray()转换成一个Object数组
        Object[] objects = collection.toArray();
        //遍历数组
        for (int i = 0; i < objects.length; i++) {
            Object object = objects[i];
            System.out.println(object);
        }
        System.out.println(collection);
        // void clear() 清空集合
        collection.clear();
        System.out.println(collection);
    }

    /**
     * 想要遍历Collection集合，那么就要获取该集合迭代器完成迭代操作，
     *      public Iterator iterator() : 获取集合对应的迭代器，用来遍历集合中的元素的。
     *      迭代：即Collection集合元素的通用获取方式。在取元素之前先要判断集合中有没有元素，如果有，就把这个元素取出来，继续在判断，如果还有就再取出出来。一直把集合中的所有元素全部取出。这种取出方式专业术语称为迭代。
     * Iterator接口的常用方法如下：
     *      public E next() :返回迭代的下一个元素。
     *      public boolean hasNext() :如果仍有元素可以迭代，则返回 true。
     */
    @Test
    public void iterator() {
        // 使用多态方式 创建对象
        Collection<String> collection = new ArrayList<>();
        // 添加元素到集合
        collection.add("串串星人");
        collection.add("吐槽星人");
        collection.add("汪星人");

        // 使用迭代器 遍历 每个集合对象都有自己的迭代器
        Iterator<String> iterator = collection.iterator();
        // 泛型指的是 迭代出 元素的数据类型
        while (iterator.hasNext()) {    // 判断是否有迭代元素
            String next = iterator.next();  // 获取迭代出的元素
            System.out.println(next);
        }

        /**
         * 增强for循环(也称for each循环)是JDK1.5以后出来的一个高级for循环，专门用来遍历数组和集合的。它的内部原理其实是个Iterator迭代器，所以在遍历的过程中，不能对集合中的元素进行增删操作。
         * 格式：
         *      for(元素的数据类型 变量 : Collection集合or数组){
         *          //写操作代码
         *      }
         * 它用于遍历Collection和数组。通常只进行遍历元素，不要在遍历的过程中对集合元素进行增删操作
         */
        for (String s : collection) {   // 接收变量s代表 代表被遍历到的集合元素
            System.out.println(s);
        }
    }

    @Test
    public void generic() {
        Collection<String> collection = new ArrayList();
        collection.add("abc");
        collection.add("itcast");
//        collection.add(5);//由于集合没有做任何限定，任何类型都可以给其中存放
        collection.add(5 + "");//当集合明确类型后，存放类型不一致就会编译报错

        // 集合已经明确具体存放的元素类型，那么在使用迭代器的时候，迭代器也同样会知道具体遍历元素类型
        Iterator<String> it = collection.iterator();
        while(it.hasNext()){
            //需要打印每个字符串的长度,就要把迭代出来的对象转成String类型
//            String str = (String) it.next();
            // 当使用Iterator<String>控制元素类型后，就不需要强转了。获取到的元素直接就是String类型
            String str = it.next();
            System.out.println(str.length());
        }
    }

    /**
     * 自定义泛型类
     * 没有MVP类型，在这里代表 未知的一种数据类型 未来传递什么就是什么类型
     * @param <MVP>
     */
    public class MyGenericClass<MVP> {
        private MVP mvp;

        public MVP getMvp() {
            return mvp;
        }

        public void setMvp(MVP mvp) {
            this.mvp = mvp;
        }
    }

    @Test
    public void GenericClass() {
        // 创建一个泛型为String的类
        MyGenericClass<String> stringMyGenericClass = new MyGenericClass<>();
        stringMyGenericClass.setMvp("大胡子登登");
        String mvp1 = stringMyGenericClass.getMvp();
        System.out.println(mvp1);

        // 创建一个泛型为Integer的类
        MyGenericClass<Integer> integerMyGenericClass = new MyGenericClass<>();
        integerMyGenericClass.setMvp(123);
        Integer mvp2 = integerMyGenericClass.getMvp();
        System.out.println(mvp2);
    }

    /**
     * 含有泛型的方法
     */
    public class MyGenericMethod {
        public <MVP> void show(MVP mvp) {
            System.out.println(mvp.getClass());
        }
        public <MVP> MVP show2(MVP mvp) {
            return mvp;
        }
    }

    @Test
    public void GenericMethod() {   //调用方法时，确定泛型的类型
        MyGenericMethod myGenericMethod = new MyGenericMethod();
        //演示看方法提示
        myGenericMethod.show("passerby");
        myGenericMethod.show(123);
        myGenericMethod.show(123.456);
    }

    /**
     * 含有泛型的接口
     */
    public interface MyGenericInterface<E> {
        public abstract void add(E e);

        public E getE();
    }

    public class MyGenericInterfaceImpl1 implements MyGenericInterface<String> { // 定义类时确定泛型的类型

        @Override
        public void add(String s) {
            //省略。。。
        }

        @Override
        public String getE() {
            return null;
        }
    }

    public class MyGenericInterfaceImpl2 <E> implements MyGenericInterface<E> { // 始终不确定泛型的类型，直到创建对象时，确定泛型的类型

        @Override
        public void add(E e) {
            //省略。。。
        }

        @Override
        public E getE() {
            return null;
        }
    }

    @Test
    public void GenericInterface() {
        MyGenericInterfaceImpl2<String> stringMyGenericInterfaceImpl2 = new MyGenericInterfaceImpl2<>();
        stringMyGenericInterfaceImpl2.add("passerby");
    }

    /**
     * 按照斗地主的规则，完成洗牌发牌的动作。 具体规则：
     * 使用54张牌打乱顺序,三个玩家参与游戏，三人交替摸牌，每人17张牌，最后三张留作底牌。
     *
     * 准备牌：
     *      牌可以设计为一个ArrayList,每个字符串为一张牌。 每张牌由花色数字两部分组成，我们可以使用花色
     *      集合与数字集合嵌套迭代完成每张牌的组装。 牌由Collections类的shuffle方法进行随机排序。
     * 发牌
     *      将每个人以及底牌设计为ArrayList,将最后3张牌直接存放于底牌，剩余牌通过对3取模依次发牌。
     * 看牌
     *      直接打印每个集合。
     */
    @Test
    public void poker() {
        //1.1 创建牌盒 将来存储牌面的
        ArrayList<String> pokerBox = new ArrayList<String>();
        //1.2 创建花色集合
        ArrayList<String> colors = new ArrayList<String>();
        //1.3 创建数字集合
        ArrayList<String> numbers = new ArrayList<String>();
        //1.4 分别给花色 以及 数字集合添加元素
        colors.add("♥");
        colors.add("♦");
        colors.add("♠");
        colors.add("♣");

        for (int i = 2; i <= 10; i++) {
            numbers.add(i + "");
        }
        numbers.add("J");
        numbers.add("Q");
        numbers.add("K");
        numbers.add("A");

        //1.5 创造牌 拼接牌操作
        // 拿出每一个花色 然后跟每一个数字 进行结合 存储到牌盒中
        for (String color : colors) {
            for (String number : numbers) {
                String card = color + number;
                pokerBox.add(card);
            }
        }
        //1.6大王小王
        pokerBox.add("小☺");
        pokerBox.add("大☠");

//        System.out.println(pokerBox);
        //洗牌    shuffer方法
        // static void shuffle(List<?> list)
        // 使用默认随机源对指定列表进行置换
        // 2：洗牌
        Collections.shuffle(pokerBox);
//        System.out.println(pokerBox);
        // 3：发牌
        // 3.1 创建 三个 玩家集合 创建一个底牌集合
        ArrayList<String> player1 = new ArrayList<String>();
        ArrayList<String> player2 = new ArrayList<String>();
        ArrayList<String> player3 = new ArrayList<String>();
        ArrayList<String> dipai = new ArrayList<String>();

        //遍历 牌盒 必须知道索引
        for (int i = 0; i < pokerBox.size(); i++) {
            //获取 牌面
            String card = pokerBox.get(i);
            //留出三张底牌 存到 底牌集合中
            if (i >= 51) {  //存到底牌集合中
                dipai.add(card);
            } else {
                if(i%3==0){ //玩家1 %3 ==0
                    player1.add(card);
                }else if(i%3==1){   //玩家2
                    player2.add(card);
                }else{  //玩家3
                    player3.add(card);
                }
            }
        }
        System.out.println("令狐冲："+player1);
        System.out.println("田伯光："+player2);
        System.out.println("绿竹翁："+player3);
        System.out.println("底牌："+dipai);
    }

    @Test
    public void list() {
        List<Integer> list = new ArrayList();
        LinkedList linkedList = new LinkedList();
        boolean b = Collections.addAll(list, 1, 2, 3, 4, 5, 6);
        Collections.sort(list, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2 - o1;
            }
        });
        System.out.println(list);

        linkedList.addFirst(0);
        linkedList.addLast(8);
        System.out.println(linkedList);
    }

    @Test
    public void set() {
        Set<String> hashSet = new HashSet<>();
        hashSet.add("MyFoolish");
        hashSet.add("坏脾气先生L");
        hashSet.add("Passerby");
        hashSet.add("坏脾气先生L");
        hashSet.add("XwCoding");
        System.out.println(hashSet);

        Set<String> linkedHashSet = new LinkedHashSet<>();
        linkedHashSet.add("MyFoolish");
        linkedHashSet.add("坏脾气先生L");
        linkedHashSet.add("Passerby");
        linkedHashSet.add("坏脾气先生L");
        linkedHashSet.add("XwCoding");
        System.out.println(linkedHashSet);
    }

    /**
     * Collection 单列集合的顶层接口
     *  List    有序    有索引 可重复
     * 		ArrayList
     * 			底层数据是数组 查询快 增删慢 线程不安全 效率高
     * 		LinkedList
     * 			底层数据是链表 查询慢 增删快 线程不安全 效率高
     * 		Vector
     * 			底层数据是数组 查询快 增删慢 线程安全 效率低
     * 	Set 元素唯一
     * 		HashSet
     * 			底层是哈希表 查询和增删都比较快 无序 唯一
     * 			     通过hashCode方法和equals方法
     * 				先判断hashCode
     * 				    不同  添加到集合   相同  继续比较equals
     * 					不同  添加到集合   相同  不添加
     *      LinkedHashSet
     * 			底层是链表 + 哈希表 链表保证元素有序 哈希表保证元素唯一
     */
    @Test
    public void collections() {
        int[] arr = {1, 2, 3, 7, 8, 9};
        int sum1 = getSum1(arr);
        int sum2 = getSum2(1, 2, 3, 7, 8, 9);
        System.out.println(sum2);

        List<String> list = new ArrayList<>();
        Collections.addAll(list, "坏脾气先生L", "Passerby", "MyFoolish", "XwCoding");
        System.out.println(list);
        // 将集合中元素按照默认规则排序。
        Collections.sort(list);
        System.out.println(list);

        // public static <T> void sort(List<T> list，Comparator<? super T> ) :将集合中元素按照指定规则排序
        /**
         * Comparable接口
         * 	    实现此接口 可以进行自然排序 123 abc  Integer String都实现类此接口
         * 		如果自定义类型实现此接口 也可以重新定义规则
         * Comparator比较器
         * 		相当于有个裁判 来进行判断
         * 		自己定义规则来进行排序
         * 		参数1-参数2 升序
         * 		参数2-参数1 降序
         */
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
//                return o2.compareTo(o1);
                return o2.charAt(0) - o1.charAt(0); // 字符串首字母 升序
            }
        });
        System.out.println(list);
    }

    // 常规写法
    // 修饰符 返回值类型 方法名(参数类型... 形参名){ }
    private int getSum1(int[] arr) {
        int sum = 0;
        for (int i : arr) {
            sum += i;
        }
        return sum;
    }
    // 可变参数写法
    // 修饰符 返回值类型 方法名(参数类型[] 形参名){ }
    private int getSum2(int... arr) {
        int sum = 0;
        for (int i : arr) {
            sum += i;
        }
        return sum;
    }
}
