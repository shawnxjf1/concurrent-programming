package com.concurrent.thread;

import com.concurrent.util.SleepUtil;

import java.util.Date;

/**
 * 线程等待通知<br>
 * 测试 obj.wait(),obj.notifyAll(),当调用obj.wait()会让线程释放 obj 锁。
 */
public class WaitNotify {

    static volatile boolean flag = true;

    static Object obj = new Object();

    public static void main(String... args) {
        Thread waitThread = new Thread(new Wait(), "WaitThread");
        waitThread.start();
        
        SleepUtil.second(2);
        
        System.out.println("wait thread get lock(obj) first,when call obj.wait(),obj release lock.");
        
        System.out.println("after wait ,wait thread status=" + waitThread.getState().toString());
        SleepUtil.second(2);
         

        Thread notifyThread = new Thread(new Notify(), "NotifyThread");
        notifyThread.start();
        SleepUtil.second(10);
        System.out.println("after notifyall ,wait thread status=" + waitThread.getState().toString());

        
        /**
         * 程序执行结果1：<br>
         * WaitThread  is waiting..... Tue Nov 29 19:52:16 CST 2016
         * NotifyThread is notify done....Tue Nov 29 19:52:18 CST 2016
         * WaitThread  is done.... Tue Nov 29 19:52:23 CST 2016
         */
        
        /**
         * 程序执行结果2：
         * WaitThread  is waiting..... Tue Nov 29 20:00:17 CST 2016
wait thread get lock(obj) first,when call obj.wait(),obj release lock.
after wait ,wait thread status=WAITING 【】
NotifyThread is notify done....Tue Nov 29 20:00:21 CST 2016 
(notifythread.start(), thread.sleep(5))
after notifyall ,wait thread status=BLOCKED 【】
WaitThread  is done.... Tue Nov 29 20:00:26 CST 2016
         */
        
        /**
         * WaitThread  is waiting..... Tue Nov 29 20:01:30 CST 2016
wait thread get lock(obj) first,when call obj.wait(),obj release lock.
after wait ,wait thread status=WAITING
NotifyThread is notify done....Tue Nov 29 20:01:34 CST 2016
WaitThread  is done.... Tue Nov 29 20:01:39 CST 2016
(notifythread.start(), thread.sleep(10))
after notifyall ,wait thread status=TERMINATED
         */
    }


    static class Wait implements Runnable {
        @Override
        public void run() {
            try {
                synchronized (obj) {
                    //当条件不满足时，继续wait,同时释放obj的锁
                    while (flag) {
                        System.out.println(Thread.currentThread().getName() + "  is waiting..... " + new Date());
                        obj.wait();
                    }
                }
                System.out.println(Thread.currentThread().getName() + "  is done.... " + new Date());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    static class Notify implements Runnable {
        @Override
        public void run() {
            synchronized (obj) {
                //获取obj的锁，然后进行通知，通知时不会释放obj的锁,类似于过早通知
                //只有当前线程释放了obj锁后，Wait才能从wait方法返回
                obj.notifyAll();
                flag = false;
                System.out.println(Thread.currentThread().getName() + " is notify done...." + new Date());
                SleepUtil.second(5);
            }
        }
    }
}
