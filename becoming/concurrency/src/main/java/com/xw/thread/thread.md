# 线程

```java
/**
 * 🌟进程和线程？
 *      进程：程序运行资源分配的最小单位，进程内部有多个线程共享这个进程的资源。
 *          运行一个java程序的实质是启动一个java虚拟机进程，也就是说一个运行的java程序就是一个java虚拟机进程。
 *          进程是程序向操作系统申请资源(如内存空间和文件句柄)的基本单位。
 *      线程：CPU调度的最小单位，必须依赖进程而存在。是进程中可独立执行的最小单位，并且不拥有资源。
 *          进程相当于工厂老板，整个工厂的机器都是属于老板的，但是工厂里面的活都是由工人完成的。
 *      任务：线程所要完成的计算就被称为任务，特定的线程总是执行特定的任务
 *
 * CPU核心数和线程数的关系    核心数:线程数=1:1 使用了超线程技术后 ---> 1:2
 * CPU时间片轮转机制   又称RR调度，会导致上下文切换
 *
 * 🌟并行和并发？
 *      并行：同一时刻，可以同时处理事情的能力
 *      并发：与单位时间相关，在单位时间内可以处理事情的能力
 * 高并发编程的意义、好处和注意事项
 *      好处：充分利用cpu的资源、加快用户响应的时间，程序模块化，异步化
 *      问题：
 * 　　   1、线程共享资源，存在冲突；
 * 　　   2、容易导致死锁；
 * 　　   3、启用太多的线程，就有搞垮机器的可能
 */
```

## 线程池
```java
/**
 * corePoolSize: 核心线程数目
 *  如何确定核心线程数？
 *      首先如何查看机器的CPU核数：idea中执行 Runtime.getRuntime().availableProcessors();
 *      ⚠️IO密集型任务（一般来说：文件读写、DB读写、网络请求等）   核心线程数大小设置为2N+1 N：CPU核数
 *      CPU密集行任务（一般来说：计算型代码、Bitmap转换、Gson转换等）   核心线程数大小设置为N+1
 *      
 *      1⃣️：高并发、任务执行时间短，则认为是CPU密集行任务 - > 设置为CPU核数+1 减少线程上下文的切换
 *      2⃣️：并发不高、任务执行时间长，具体分析
 *          IO密集型任务 - > 设置为CPU核数*2+1 
 *          CPU密集行任务 - > 设置为CPU核数+1
 *      3⃣️：并发高且任务执行时间长，解决这种类型任务的关键不在于线程池而在于整体架构的设计，
 *          1.看看这些业务里面某些数据是否能做缓存
 *          2.是否能增加服务器，至于线程池的设置，参考2⃣️
 *      
 * maximumPoolSize: 最大线程数目 = （核心线程数目 + 救急线程的最大数目）
 * keepAliveTime: 生存时间 -> 救急线程的生存时间。生存时间内没有新任务，此线程资源会释放
 * unit: 时间单位 -> 救急线程的生存时间单位，如秒、毫秒等
 * workQueue: 当没有空闲核心线程时，新来任务会加入到此队列排队，队列满会创建救急线程执行任务
 *  线程池中常见的阻塞队列
 *      1、ArrayBlockingQueue<Runnable>：基于数组结构的有界（可以设置大小）阻塞队列，FIFO
 *      2、LinkedBlockingQueue<Runnable>：基于链表（单向链表）结构的有界（可以设置大小）阻塞队列，FIFO
 *      3、DelayedWorkQueue<Runnable>：优先级队列，可以保证每次出队的任务都是当前队列中执行时间最靠前的
 *      4、SynchronousQueue<Runnable>：不存储元素的阻塞队列，每个插入操作都必须等待一个移出操作
 * threadFactory: 线程工厂 -> 可以定制线程对象的创建，例如设置线程名字，是否是守护线程等
 * handler: 拒绝策略 -> 当所有线程都在繁忙，workQueue也放满时，会触发拒绝策略
 *      new ThreadPoolExecutor.AbortPolicy()            直接抛出异常，默认拒绝策略
 *      new ThreadPoolExecutor.CallerRunsPolicy()       用调用者所在的线程来执行任务
 *      new ThreadPoolExecutor.DiscardOldestPolicy()    丢弃阻塞队列中最靠前的任务，并执行当前任务
 *      new ThreadPoolExecutor.DiscardPolicy()          直接丢弃任务
 */

public ThreadPoolExecutor(int corePoolSize,
        int maximumPoolSize,
        long keepAliveTime,
        TimeUnit unit,
        BlockingQueue<Runnable> workQueue,
        ThreadFactory threadFactory,
        RejectedExecutionHandler handler){}
```

在 java.util.concurrent.Executors类中提供了大量创建连接池的静态方法，常见的四种：

1、newFixedThreadPool: 创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待
2、newSingleThreadExecutor：创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序（FIFO）执行
3、newCachedThreadPool：创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收则新建线程
4、newScheduledThreadPool：可以执行延迟任务的线程池，支持定长及周期性任务执行

```java
/**
 * 1、指定工作线程数量的线程池
 *  核心线程数与最大线程数一样，即没有救急线程
 *  阻塞队列是LinkedBlockingQueue，最大容量为Integer.MAX_VALUE
 *  【适用于任务量已知，相对耗时的任务】 
 */
public static ExecutorService newFixedThreadPool(int nThreads) {
    return new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS,  new LinkedBlockingQueue<Runnable>());
}

public static void newFixedThreadPool() {
    Runnable runnable = () -> {
        try {
            System.out.println(Thread.currentThread().getName() + "正在被执行");
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    };

    // 创建一个固定大小的线程池，核心线程数和最大线程数都是3，无救急线程
    ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
    for (int i = 0; i < 5; i++) {
        fixedThreadPool.submit(runnable);
        fixedThreadPool.execute(runnable);
    }
    fixedThreadPool.shutdown();
}
```

```java
/**
 * 2、单线程化的线程池
 * 核心线程数与最大线程数都为1，也没有救急线程
 * 阻塞队列是LinkedBlockingQueue，最大容量为Integer.MAX_VALUE
 * 【适用于按照顺序执行的任务】
 */
public static ExecutorService newSingleThreadExecutor() {
    return new FinalizableDelegatedExecutorService(new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>()));
}

public static void newSingleThreadExecutor() {
    // 创建一个单线程化的线程池，核心线程数和最大线程数都是1，无救急线程
    ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
    for (int i = 0; i < 5; i++) {
        final int index = i;
        singleThreadExecutor.execute(()->{
            try {
                // 结果依次输出，相当于顺序执行各个任务
                System.out.println(Thread.currentThread().getName() + "正在被执行,打印的值是:" + index);
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
    singleThreadExecutor.shutdown();
}
```

```java
/**
 * 3、可缓存线程池
 * 核心线程数为0，最大线程数为Integer.MAX_VALUE，使用临时线程来执行任务
 * 阻塞队列是SynchronousQueue，不存储元素的阻塞队列，每个插入操作都必须等待一个移出操作
 * 当来了新任务之后，首先会判断临时线程是否还存活，若存活就用临时线程来执行，若没有存活的临时线程，则去创建临时线程去执行当前的任务
 * 【适用于任务比较密集，但每个任务执行时间较短的情况，否则可能会创建大量的线程占有内存】
 */
public static ExecutorService newCachedThreadPool() {
    return new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
}

public static void newCacheThreadPool() {
    // 创建一个可缓存线程池，没有核心线程数，最大线程数为Integer.MAX_VALUE，都是用临时线程来执行任务，
    // 来了新任务之后，首先会判断临时线程是否还存活，若有存活就用临时线程来执行，若没有存活，则去创建临时线程去执行当前的任务
    ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
    for (int i = 0; i < 10; i++) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        cachedThreadPool.execute(() -> {
            System.out.println(Thread.currentThread().getName() + "正在被执行");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
    cachedThreadPool.shutdown();
}
```

```java
/**
 * “延迟”和“周期执行” 功能的ThreadPoolExecutor
 * corePoolSize：核心线程数
 * Integer.MAX_VALUE：最大线程数
 * 0, NANOSECONDS：临时线程没有存活时间
 * new DelayedWorkQueue()：存储可延迟执行的任务
 */
public ScheduledThreadPoolExecutor(int corePoolSize) {
    super(corePoolSize, Integer.MAX_VALUE, 0, NANOSECONDS, new DelayedWorkQueue());
}

public ScheduledThreadPoolExecutor(int corePoolSize, ThreadFactory threadFactory) {
    super(corePoolSize, Integer.MAX_VALUE, 0, NANOSECONDS, new DelayedWorkQueue(), threadFactory);
}

public ScheduledThreadPoolExecutor(int corePoolSize, RejectedExecutionHandler handler) {
    super(corePoolSize, Integer.MAX_VALUE, 0, NANOSECONDS, new DelayedWorkQueue(), handler);
}

public ScheduledThreadPoolExecutor(int corePoolSize, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
    super(corePoolSize, Integer.MAX_VALUE, 0, NANOSECONDS, new DelayedWorkQueue(), threadFactory, handler);
}

public static void newScheduledThreadPool() {
    Runnable runnable = () -> {
        try {
            System.out.println(Thread.currentThread().getName() + "，开始：" + new Date());
            Thread.sleep(1000);
            System.out.println(Thread.currentThread().getName() + "，结束：" + new Date());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    };
    // 按照周期执行的线程池，核心线程数为传入的参数3，最大线程数是Integer.MAX_VALUE
    ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(3);
    System.out.println("程序开始：" + new Date());

    scheduledThreadPool.schedule(runnable, 0, TimeUnit.SECONDS);
    scheduledThreadPool.schedule(runnable, 1, TimeUnit.SECONDS);
    scheduledThreadPool.schedule(runnable, 5, TimeUnit.SECONDS);
    scheduledThreadPool.schedule(() -> System.out.println("延迟5秒执行"), 5, TimeUnit.SECONDS);

//        scheduledThreadPool.scheduleWithFixedDelay(() -> {
//            System.out.println("延迟1秒后每3秒执行一次");
//        }, 1, 3, TimeUnit.SECONDS);

    scheduledThreadPool.shutdown();
}
```

### 线程池中常见的阻塞队列

线程池中常见的阻塞队列
1. ArrayBlockingQueue<Runnable>：基于数组结构的有界（可以设置大小）阻塞队列，FIFO
2. LinkedBlockingQueue<Runnable>：基于链表（单向链表）结构的有界（可以设置大小）阻塞队列，FIFO
3. DelayedWorkQueue<Runnable>：优先级队列，可以保证每次出队的任务都是当前队列中执行时间最靠前的
4. SynchronousQueue<Runnable>：不存储元素的阻塞队列，每个插入操作都必须等待一个移出操作

ArrayBlockingQueue和LinkedBlockingQueue的区别

| ArrayBlockingQueue | LinkedBlockingQueue     |
|--------------------|-------------------------|
| 强制有界（必须给定一个初始值）    | 默认无界（默认Integer.MAX_VALUE），支持有界（可以传一个初始值） |
| 底层是数组              | 底层是链表（单向）               |
| 提前初始化Node数组        | 是懒惰的，创建节点的时候会添加数据       |
| Node需要是提前创建好的      | 入队会生成新的Node             |
| 一把锁                | 两把锁（头尾）                 |

### 为什么不建议使用Executors创建线程池

参考阿里开发手册《Java开发手册-嵩山版》

【强制】线程池不允许使用Executors去创建，而是通过ThreadPoolExecutor的方式，这样的处理方式让创建的开发人员更加明确线程池的运行规则，避免资源耗尽的风险。

说明：Executors返回的线程池对象的弊端如下：
1. newFixedThreadPool 和 newSingleThreadExecutor：允许的请求队列长度为Integer.MAX_VALUE，可能会堆积大量的请求，从而导致OOM。
2. newCachedThreadPool：允许的创建线程数量为Integer.MAX_VALUE，可能会创建大量的线程，从而导致OOM

### 线程池的使用场景

#### CountDownLatch

CountDownLatch（闭锁/倒计时锁）用来进行线程同步协作，等待所有线程安成倒计时（一个或多个线程，等待其他多个线程完成某件事情之后才能执行）
* 构造参数用来初始化等待计数值
* await() 用来等待计数归零
* countDown() 用来让计数减一