package com.xw.other;

import com.sun.jmx.remote.internal.ArrayQueue;

import java.util.*;

/**
 * @author liuxiaowei
 * @description
 * @date 2021/5/18
 */
public class CollectionDemo {

    public static void main(String[] args) {
        // Iterable 顶层接口        public interface Iterable<T>{}
        //    ⬇️
        // Collection 顶层接口      public interface Collection<E> extends Iterable<E> {}
        //    ⬇️
        // List 接口 有序集合  public interface List<E> extends Collection<E> {}     ArrayList LinkedList Vector
        // Queue 接口 先进先出队列  public interface Queue<E> extends Collection<E> {}
        // Set 接口 唯一集合 public interface Set<E> extends Collection<E> {} HashSet LinkedHashSet

        Iterable iterable = new Iterable() {
            @Override
            public Iterator iterator() {
                return null;
            }
        };

        Collection collection1 = new ArrayList();
        Collection collection2 = new ArrayDeque();
        Collection collection3 = new HashSet();

//        System.out.println(TList());
//        System.out.println(TSet());
//        System.out.println(TQueue());
//        System.out.println(TMap());
        TComparator();

    }

    public static List TList() {
        /**
         * List
         *    Arraylist:  是 List 的主要实现类，底层使用 Object[] 数组存储，线程不安全，适用于频繁的查找工作。
         *    Vector:     是 List 的古老实现类，底层使用 Object[] 数组存储，线程安全。
         *    LinkedList: 双向链表(JDK1.6 之前为循环链表，JDK1.7 取消了循环)
         *
         *    Arraylist 和 Vector 的区别？
         *    Arraylist 与 LinkedList 区别?
         *      是否保证线程安全： ArrayList 和 LinkedList 都是不同步的，也就是不保证线程安全；
         *      底层数据结构: Arraylist 底层使用的是 Object[] 数组；LinkedList 底层使用的是双向链表数据结构
         *                  （JDK1.6 之前为循环链表，JDK1.7 取消了循环。注意双向链表和双向循环链表的区别，下面有介绍到！）
         *      插入和删除是否受元素位置的影响:
         *          ArrayList 采用数组存储，所以插入和删除元素的时间复杂度受元素位置的影响。
         *              比如：执行add(E e)方法的时候，ArrayList 会默认在将指定的元素追加到此列表的末尾，这种情况时间复杂度就是 O(1)。
         *              但是如果要在指定位置 i 插入和删除元素的话（add(int index, E element)）时间复杂度就为 O(n-i)，
         *              因为在进行上述操作的时候集合中第 i 和第 i 个元素之后的(n-i)个元素都要执行向后位/向前移一位的操作。
         *          LinkedList 采用链表存储，所以，
         *              如果是在头尾插入或者删除元素不受元素位置的影响（add(E e)、addFirst(E e)、addLast(E e)、removeFirst()、removeLast()），时间复杂度为 O(1)，
         *              如果是要在指定位置 i 插入和删除元素的话（add(int index, E element)，remove(Object o)），时间复杂度为 O(n) ，因为需要先移动到指定位置再插入。
         *      是否支持快速随机访问: LinkedList 不支持高效的随机元素访问，而 ArrayList 支持。
         *                          快速随机访问就是通过元素的序号快速获取元素对象(对应于get(int index)方法)。
         *      内存空间占用: ArrayList 的空间浪费主要体现在在 list 列表的结尾会预留一定的容量空间，
         *                  而 LinkedList 的空间花费则体现在它的每一个元素都需要消耗比 ArrayList 更多的空间（因为要存放直接后继和直接前驱以及数据）。
         *    我们在项目中一般是不会使用到 LinkedList 的，需要用到 LinkedList 的场景几乎都可以使用 ArrayList 来代替，并且，性能通常会更好！
         *    就连 LinkedList 的作者约书亚 · 布洛克（Josh Bloch）自己都说从来不会使用 LinkedList
         *    另外，不要下意识地认为 LinkedList 作为链表就最适合元素增删的场景。
         *    我在上面也说了，LinkedList 仅仅在头尾插入或者删除元素的时候时间复杂度近似 O(1)，其他情况增删元素的时间复杂度都是 O(n) 。
         *
         * public interface RandomAccess {}
         * 查看源码我们发现实际上 RandomAccess 接口中什么都没有定义。
         * 所以，在我看来 RandomAccess 接口不过是一个标识罢了。标识什么？ 标识实现这个接口的类具有随机访问功能。
         *
         * 在 binarySearch（) 方法中，它要判断传入的 list 是否 RandomAccess 的实例，
         * 如果是，调用indexedBinarySearch()方法，如果不是，那么调用iteratorBinarySearch()方法
         *     public static <T> int binarySearch(List<? extends Comparable<? super T>> list, T key) {
         *         if (list instanceof RandomAccess || list.size()<BINARYSEARCH_THRESHOLD)
         *             return Collections.indexedBinarySearch(list, key);
         *         else
         *             return Collections.iteratorBinarySearch(list, key);
         *     }
         * ArrayList 实现了 RandomAccess 接口， 而 LinkedList 没有实现。为什么呢？
         * 我觉得还是和底层数据结构有关！ArrayList 底层是数组，而 LinkedList 底层是链表。
         * 数组天然支持随机访问，时间复杂度为 O(1)，所以称为快速随机访问。链表需要遍历到特定位置才能访问特定位置的元素，时间复杂度为 O(n)，所以不支持快速随机访问。
         * ArrayList 实现了 RandomAccess 接口，就表明了他具有快速随机访问功能。
         * RandomAccess 接口只是标识，并不是说 ArrayList 实现 RandomAccess 接口才具有快速随机访问功能的！
         */
        List list = new ArrayList();
        list.add("刘");
        list.add("晓");
        list.add("威");
        list.add("刘");
        return list;
    }

    public static Queue TQueue() {
        /**
         * Queue
         *    PriorityQueue:    Object[] 数组来实现二叉堆
         *    ArrayQueue:       Object[] 数组 + 双指针
         */
        Queue queue = new ArrayDeque();
        queue.add("刘");
        queue.add("晓");
        queue.add("威");
        queue.add("刘");
        return queue;
    }

    public static Set TSet() {
        /**
         * Set
         *    HashSet(无序，唯一):   基于 HashMap 实现的，底层采用 HashMap 来保存元素
         *    LinkedHashSet:       LinkedHashSet 是 HashSet 的子类，并且其内部是通过 LinkedHashMap 来实现的。
         *                         有点类似于我们之前说的 LinkedHashMap 其内部是基于 HashMap 实现一样，不过还是有一点点区别的
         *    TreeSet(有序，唯一):   红黑树(自平衡的排序二叉树)
         */
        Set set = new HashSet();
        set.add("刘");
        set.add("晓");
        set.add("威");
        set.add("刘");
        return set;
    }

    public static Map TMap() {
        /**
         * Map
         *    HashMap:          JDK1.8 之前 HashMap 由数组+链表组成的，数组是 HashMap 的主体，链表则是主要为了解决哈希冲突而存在的（“拉链法”解决冲突）。
         *                      JDK1.8 以后在解决哈希冲突时有了较大的变化，当链表长度大于阈值（默认为 8）
         *                      （将链表转换成红黑树前会判断，如果当前数组的长度小于 64，那么会选择先进行数组扩容，而不是转换为红黑树）时，将链表转化为红黑树，以减少搜索时间
         *    Hashtable:        数组+链表组成的，数组是 Hashtable 的主体，链表则是主要为了解决哈希冲突而存在的
         *    TreeMap:          红黑树（自平衡的排序二叉树）
         *    LinkedHashMap:    LinkedHashMap 继承自 HashMap，所以它的底层仍然是基于拉链式散列结构即由数组和链表或红黑树组成。
         *                      另外，LinkedHashMap 在上面结构的基础上，增加了一条双向链表，使得上面的结构可以保持键值对的插入顺序。
         *                      同时通过对链表进行相应的操作，实现了访问顺序相关逻辑。
         *                      详细可以查看：《LinkedHashMap 源码详细分析（JDK1.8）》https://www.imooc.com/article/22931
         */
        Map map = new HashMap();
        map.put(1, "刘");
        map.put(2, "晓");
        map.put(3, "威");
        map.put(4, "刘");
        Map map1 = new HashMap();
        Map map2 = new Hashtable();
        Map map3 = new LinkedHashMap();
        return map;
    }

    public static List<Integer> TComparator() {
        List<Integer> list = new ArrayList<>();
        list.add(-1);
        list.add(3);
        list.add(-5);
        System.out.println("原始数组：" + "\r\n" + list);
        // void reverse(List list)：反转
        Collections.reverse(list);
        System.out.println("Collections.reverse(list)：" + "\r\n" + list);
        // void sort(List list),按自然排序的升序排序
        Collections.sort(list);
        System.out.println("Collections.sort(arrayList)：" + "\r\n" + list);
        // 定制排序的用法
        list.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2.compareTo(o1);
            }
        });
        list.sort((o1, o2) -> o2.compareTo(o1));
        list.sort(Comparator.reverseOrder());
        System.out.println("定制排序后：" + "\r\n" + list);
        return list;
    }
}
