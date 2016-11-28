package com.concurrent.base;

import java.util.Date;

/**
 * 线程封闭的对象只能由一个线程拥有，对象被封闭在该线程中，并且只能由这个线程修改<br>
 * ThreadLocal 提供了get与set等访问接口或方法，这些方法为每个使用该变量线程都存有一份独立的副本，因此get总是返回由当前执行线程再调用set时设置的最新值<br>
 * ThreadLocal 类似于全局变量，他解决问题的初衷：一个临时对象，例如缓冲区不想每次都重新分配该对象。（滥用：用于作为“隐藏”函数参数的手段）
 * User: shijingui
 * Date: 2016/2/1
 */
public class ThreadLocalExample {

    static  int count = 0;
//    static AtomicInteger count = new AtomicInteger();

    static class MyRunnable implements Runnable {
        ThreadLocal<Integer> threadLocal = new ThreadLocal<Integer>();

        @Override
        public void run() {
            threadLocal.set((int) (Math.random() * 10));
//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            System.out.println(Thread.currentThread().getName() + " value is " + threadLocal.get() + " , count is " + ++count + " ,date is " + new Date());

        }
    }


    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new MyRunnable(), "Thread-01");
        Thread t2 = new Thread(new MyRunnable(), "Thread-02");
        

        t1.start();
//        t1.join();
        t2.start();
//        t2.join();
        
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
        
        
        /**
         * 2016年11月27日 执行结果<br>
         * Thread-01 value is 1 , count is 1 ,date is Sun Nov 27 19:10:34 CST 2016
         * Thread-02 value is 4 , count is 2 ,date is Sun Nov 27 19:10:37 CST 2016
         */
        
        /**
         *     static  int count = 0;去掉 volatile
         * Thread-01 value is 4 , count is 1 ,date is Sun Nov 27 19:12:19 CST 2016
Thread-02 value is 0 , count is 2 ,date is Sun Nov 27 19:12:22 CST 2016

         */
    }
}
