package com.xw;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/3/21
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ConcurrencyBook {

    @Test
    public void tes01() {
    }

    public class VolatileBarrierExample {
        int a;
        volatile int v1 = 1;
        volatile int v2 = 2;

        void readAndWrite() {
            int i = v1; // 第一个volatile读
//            LoadLoad屏障（禁止上面的volatile读和下面的volatile读重排序）
//            此处省略LoadStore屏障，因为下面的普通写之前有一个volatile读
            int j = v2; // 第二个volatile读
//            此处省略LoadLoad屏障，因为下面没有普通读
//            LoadStore屏障（禁止下面的普通写和上面的volatile读重排序）
            a = i + j;  // 普通写
//            StoreStore屏障（禁止上面的普通写和下面的volatile写重排序）
            v1 = i + 1; // 第一个volatile写
//            StoreStore屏障（禁止上面的volatile写和下面的volatile写重排序）
//            此处省略StoreLoad屏障，因为下面跟着一个volatile写
            v2 = j * 2; // 第二个 volatile写
//            StoreLoad屏障（防止上面的volatile写和后面可能有的的volatile读/写重排序）
        }
    }
}
