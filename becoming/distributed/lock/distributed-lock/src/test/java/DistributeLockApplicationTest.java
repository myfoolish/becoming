import com.xw.DistributeLockApplication;
import com.xw.entity.TelCheckRequest;
import com.xw.lock.zookeeper.ZookeeperLock;
import com.xw.mapper.CustInfoMapper;
import com.xw.service.OrderServiceBack;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.*;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/6/15
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = DistributeLockApplication.class)
public class DistributeLockApplicationTest {

    @Autowired
    private OrderServiceBack orderService;

    @Test
    public void concurrentCreateOder() throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(5);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(5);

        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            executorService.execute(()->{
                try {
                    cyclicBarrier.await();
                    int orderId = orderService.createOrder();
                    System.out.println("订单id：" + orderId);
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        executorService.shutdown();
    }

    @Test
    public void zookeeperLockTest01() throws Exception {
        ZookeeperLock zookeeperLock = new ZookeeperLock();
        boolean lock = zookeeperLock.getLock("order");
        System.out.println("获得锁🔒的结果" + lock);
        zookeeperLock.close();
    }

    @Test
    public void curatorLockTest01() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.31.197:2181", retryPolicy);
        client.start();

        InterProcessMutex lock = new InterProcessMutex(client, "/order");
        try {
            if (lock.acquire(20, TimeUnit.SECONDS)) {
                try {
                    // do some work inside of the critical section here
                    System.out.println("拿到锁🔒");
                } finally {
                    lock.release();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        client.close();
    }

    /**
     * <a href="https://github.com/redisson/redisson#quick-start">...</a>
     * @throws IOException
     */
    @Test
    public void redissonLockTest() throws IOException {
        // 1. Create config object
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379").setPassword("xwcoding");
        // 2. Create Redisson instance
        // Sync and Async API
        RedissonClient redisson = Redisson.create(config);
        // 3. Get Redis based implementation of java.util.concurrent.ConcurrentMap
//        RMap<String, Object> map = redisson.getMap("myMap");
        // 4. Get Redis based implementation of java.util.concurrent.locks.Lock
        RLock rLock = redisson.getLock("order");
        System.out.println("进入了方法");
        try {
            rLock.lock(30, TimeUnit.SECONDS);
            System.out.println("拿到锁🔒");
            Thread.sleep(20000);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            System.out.println("释放锁🔒");
            rLock.unlock();
        }
    }

    @Resource
    private CustInfoMapper custInfoMapper;

    @Test
    public void three() {
        String idNo = "410923199911246013";
        String phone = "17800000000";
        String name = "xwor";
        TelCheckRequest req = new TelCheckRequest();
        req.setMobile(phone);
        req.setCertNo(idNo);
        req.setCertType("00");
        req.setEncryptType("0");
        req.setName(name);
        req.setProductCode("MBVG0001");
        custInfoMapper.addTelephoneThreeRecord(req);
        System.out.println("....");
    }
}
