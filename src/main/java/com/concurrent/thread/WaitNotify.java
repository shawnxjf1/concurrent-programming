package com.concurrent.thread;

import com.concurrent.util.SleepUtil;

import java.util.Date;

import org.junit.Test;

/**
 * 线程等待通知<br>
 * 测试 obj.wait(),obj.notifyAll(),当调用obj.wait()会让线程释放 obj 锁。
 */
public class WaitNotify {

    static volatile boolean flag = true;

    static Object obj = new Object();

    @Test
    public  void testWait100() {
      
      Thread waitThread = new Thread(new Wait1000(), "WaitThread");
      waitThread.start();
      
      SleepUtil.second(2);
      
      System.out.println("in main thread:wait thread get lock(obj) first,when call obj.wait(),obj release lock.");
      
      System.out.println("after wait ,wait thread status=" + waitThread.getState().toString());
      SleepUtil.second(2);
       

      Thread notifyThread = new Thread(new Notify(), "NotifyThread");
      notifyThread.start();
      SleepUtil.second(2);
      System.out.println("in main thread:after notifyall ,wait thread status=" + waitThread.getState().toString());
      System.out.println("in main thread: after notifyall ,notify thread status=" + notifyThread.getState().toString());

      
     /**
      * 2016年11月30日 执行：1).不管notifyThread.sleep()多长，一定 notify thread Complete...再waitThread is done...(即要等待notify thread 释放锁),再wait线程拿到锁。<br>
      * 2).根据object.class wait(long),代码注释规范 wait 一定要放在循环内部，来判断条件。<br>
      * WaitThread  is waiting..... Wed Nov 30 23:52:49 CST 2016
wait 1000,Perform action appropriate to condition
WaitThread  is waiting..... Wed Nov 30 23:52:50 CST 2016
in main thread:wait thread get lock(obj) first,when call obj.wait(),obj release lock.
after wait ,wait thread status=TIMED_WAITING
wait 1000,Perform action appropriate to condition
WaitThread  is waiting..... Wed Nov 30 23:52:51 CST 2016
wait 1000,Perform action appropriate to condition
WaitThread  is waiting..... Wed Nov 30 23:52:52 CST 2016
NotifyThread is notify done....Wed Nov 30 23:52:53 CST 2016
in main thread:after notifyall ,wait thread status=BLOCKED
in main thread: after notifyall ,notify thread status=TIMED_WAITING
notify thread complete...
wait 1000,Perform action appropriate to condition
WaitThread  is done.... Wed Nov 30 23:53:13 CST 2016
*/

  }
    
    @Test
    public  void testWait() {
        Thread waitThread = new Thread(new Wait(), "WaitThread");
        waitThread.start();

        
        SleepUtil.second(2);
        
        System.out.println("in main thread:wait thread get lock(obj) first,when call obj.wait(),obj release lock.");
        
        System.out.println("after wait ,wait thread status=" + waitThread.getState().toString());
        SleepUtil.second(2);
         

        Thread notifyThread = new Thread(new Notify(), "NotifyThread");
        notifyThread.start();
        SleepUtil.second(2);
        System.out.println("in main thread:after notifyall ,wait thread status=" + waitThread.getState().toString());
        System.out.println("in main thread: after notifyall ,notify thread status=" + notifyThread.getState().toString());

        
       /**
        * 2016年11月30日 执行：不管notifyThread.sleep()多长，一定 notify thread Complete...再waitThread is done...(即要等待notify thread 释放锁)<br>
        *  WaitThread  is waiting..... Wed Nov 30 23:38:52 CST 2016   // 注意这里waitthread只打印一条说明一直等待wait()可以无止境的等待。
        in main thread:wait thread get lock(obj) first,when call obj.wait(),obj release lock.
        after wait ,wait thread status=WAITING
        NotifyThread is notify done....Wed Nov 30 23:38:56 CST 2016
        in main thread:after notifyall ,wait thread status=BLOCKED
        in main thread: after notifyall ,notify thread status=TIMED_WAITING
        notify thread complete...
        WaitThread  is done.... Wed Nov 30 23:39:16 CST 2016*/

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
//                        If
//                        * {@code timeout} is zero, however, then real time is not taken into
//                        * consideration and the thread simply waits until notified.
                        
                       /* Obj.wait()注释：
                        A thread can also wake up without being notified, interrupted, or
                        * timing out, a so-called <i>spurious wakeup</i>.  While this will rarely
                        * occur in practice, applications must guard against it by testing for
                        * the condition that should have caused the thread to be awakened, and
                        * continuing to wait if the condition is not satisfied.  In other words,
                        * waits should always occur in loops, like this one:
                        * <pre>
                        *     synchronized (obj) {
                        *         while (&lt;condition does not hold&gt;)
                        *             obj.wait(timeout);
                        *         ... // Perform action appropriate to condition
                        *     }*/
                    }
                    
                }
                System.out.println(Thread.currentThread().getName() + "  is done.... " + new Date());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
    
    static class Wait1000 implements Runnable {
        @Override
        public void run() {
            try {
                synchronized (obj) {
                    //当条件不满足时，继续wait,同时释放obj的锁
                    while (flag) {
                        System.out.println(Thread.currentThread().getName() + "  is waiting..... " + new Date());
                        obj.wait(1000);
                        System.out.println("wait 1000,Perform action appropriate to condition");

//                        If
//                        * {@code timeout} is zero, however, then real time is not taken into
//                        * consideration and the thread simply waits until notified.
                        
                       /* Obj.wait()注释：
                        A thread can also wake up without being notified, interrupted, or
                        * timing out, a so-called <i>spurious wakeup</i>.  While this will rarely
                        * occur in practice, applications must guard against it by testing for
                        * the condition that should have caused the thread to be awakened, and
                        * continuing to wait if the condition is not satisfied.  In other words,
                        * waits should always occur in loops, like this one:
                        * <pre>
                        *     synchronized (obj) {
                        *         while (&lt;condition does not hold&gt;)
                        *             obj.wait(timeout);
                        *         ... // Perform action appropriate to condition
                        *     }*/
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
                SleepUtil.second(20);  //注意把这行注释放开的话 wait和notify线程都执行不完。
                System.out.println("notify thread complete...");
            }
        }
    }
}
