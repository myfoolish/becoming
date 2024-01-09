import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserInfo {
    // 柜子
    private Cabinet cabinet;
    // 存储的数字
    private int storeNumber;

    public UserInfo(Cabinet cabinet, int storeNumber) {
        this.cabinet = cabinet;
        this.storeNumber = storeNumber;
    }

    public void useCabinet() {
        cabinet.setStoreNumber(storeNumber);
    }

    public static void main(String[] args) {
        final Cabinet cabinet = new Cabinet();
        ExecutorService es = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 3; i++) {
            final int storeNumber = i;
            es.execute(() -> {
                UserInfo users = new UserInfo(cabinet, storeNumber);
                synchronized (cabinet) {
                    users.useCabinet();
                    System.out.println("我是用户" + storeNumber + ",我存储的数字是：" + cabinet.getStoreNumber());
                }
            });
        }
        es.shutdown();
    }
}