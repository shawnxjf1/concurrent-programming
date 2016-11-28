package test;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: shijingui
 * Date: 2016/10/21
 */
public class DateTimePerformanceTest {
    @Test
    public void test() {
        long time = System.nanoTime();
        long timeTest = System.currentTimeMillis();
        System.out.println(System.nanoTime() - time);

        time = System.nanoTime();
        Date date = new Date();
        System.out.println("new Date cost " + (System.nanoTime() - time) + " nanoseconds" );

        time = System.nanoTime();
        Object object = new Object();
        System.out.println("new Object=" + (System.nanoTime() - time)  + " nanoseconds");

        time = System.nanoTime();
        List list = new ArrayList();
        System.out.println("new ArrayList=" + (System.nanoTime() - time) + " nanoseconds");
        
        /**
         * 注意：1毫秒 = 1000000 纳秒
         * 2016-11-26 执行结果：
4000
new Date cost 145000 nanoseconds
new Object=6000 nanoseconds
new ArrayList=24000 nanoseconds
         */

    }
}
