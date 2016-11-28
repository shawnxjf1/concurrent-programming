package com.concurrent.objectshare;

import java.util.Date;

import org.apache.log4j.Logger;
import org.junit.Test;

/**
 * 2016年11月27日 测试结果还没有说明力，volatile和非volatile结果都是ThreadLocalExample count !=0都是可见的<br>
 */
public class VisibilityVolatileTest {
	
	Logger log = Logger.getLogger(VisibilityVolatileTest.class);

    static   int count = 0;
//    static AtomicInteger count = new AtomicInteger();

     class MyRunnable implements Runnable {
        ThreadLocal<Integer> threadLocal = new ThreadLocal<Integer>();

        @Override
        public void run() {
            threadLocal.set((int) (Math.random() * 10));
//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            System.out.println(Thread.currentThread().getName() + " value is " + threadLocal.get() + "date is " + new Date());
            if (count == 0)
            {
            	log.info("count =0");
            }
            else
            {
            	log.info("count !=0");
            }
        }
    }


    @Test
    public  void testWithoutJoinWithoutVolatile(){
        Thread t1 = new Thread(new MyRunnable(), "Thread-01");
        Thread t2 = new Thread(new MyRunnable(), "Thread-02");
        
        t1.start();
//        t1.join();
        t2.start();
//        t2.join();
        count  = 5;
        Thread t3 = new Thread(new MyRunnable());
        Thread t4 = new Thread(new MyRunnable());
        Thread t5 = new Thread(new MyRunnable());
        Thread t6 = new Thread(new MyRunnable());
        Thread t7 = new Thread(new MyRunnable());
        
        t3.start();
        t4.start();
        t5.start();
        t6.start();
        t7.start();
         
        //2016年11月27日 输出结果： count 有两个1<br>
//        Thread-0 value is 5 , count is 2 ,date is Sun Nov 27 19:20:06 CST 2016
//        Thread-1 value is 2 , count is 3 ,date is Sun Nov 27 19:20:06 CST 2016
//        Thread-02 value is 8 , count is 1 ,date is Sun Nov 27 19:20:06 CST 2016
//        Thread-2 value is 7 , count is 4 ,date is Sun Nov 27 19:20:06 CST 2016
//        Thread-3 value is 4 , count is 5 ,date is Sun Nov 27 19:20:06 CST 2016
//        Thread-01 value is 0 , count is 1 ,date is Sun Nov 27 19:20:06 CST 2016
//        Thread-4 value is 0 , count is 6 ,date is Sun Nov 27 19:20:06 CST 2016
    }
    
    @Test
    public  void testWithoutJoinWithVolatile(){
        Thread t1 = new Thread(new MyRunnable(), "Thread-01");
        Thread t2 = new Thread(new MyRunnable(), "Thread-02");
        
        t1.start();
//        t1.join();
        t2.start();
//        t2.join();
        count  = 5;

        Thread t3 = new Thread(new MyRunnable());
        Thread t4 = new Thread(new MyRunnable());
        Thread t5 = new Thread(new MyRunnable());
        Thread t6 = new Thread(new MyRunnable());
        Thread t7 = new Thread(new MyRunnable());
        
        t3.start();
        t4.start();
        t5.start();
        t6.start();
        t7.start();
         
        //2016年11月27日 输出结果： count 有两个1<br>
//        Thread-0 value is 5 , count is 2 ,date is Sun Nov 27 19:20:06 CST 2016
//        Thread-1 value is 2 , count is 3 ,date is Sun Nov 27 19:20:06 CST 2016
//        Thread-02 value is 8 , count is 1 ,date is Sun Nov 27 19:20:06 CST 2016
//        Thread-2 value is 7 , count is 4 ,date is Sun Nov 27 19:20:06 CST 2016
//        Thread-3 value is 4 , count is 5 ,date is Sun Nov 27 19:20:06 CST 2016
//        Thread-01 value is 0 , count is 1 ,date is Sun Nov 27 19:20:06 CST 2016
//        Thread-4 value is 0 , count is 6 ,date is Sun Nov 27 19:20:06 CST 2016
    }
}
