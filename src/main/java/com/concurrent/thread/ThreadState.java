package com.concurrent.thread;

import org.apache.log4j.Logger;

import com.concurrent.util.SleepUtil;

/**
 * 线程状态,使用jstack命令打印堆栈信息查看
 * User: shijingui
 * Date: 2016/2/4
 * 1.线程sleep 之后就处于timed_waitting的状态<br>
 */
public class ThreadState {
	
	static Logger log = Logger.getLogger(ThreadState.class);

    public static void main(String... args) {
    	
    	Thread runnableThread = new Thread(new RUNNABLE(),"runnableThread.");
    	runnableThread.start();
    	
        Thread timeWaitT = new Thread(new TimeWaiting(), "TimeWaitingThread");
        timeWaitT.start();
        Thread waitT = new Thread(new Waiting(), "WaitingThread");
        waitT.start();

        //使用两个Blocked线程，一个获取锁成功，另一个被阻塞
        Thread blockT1 = new Thread(new Blocked(), "BlockedThread-1");
        blockT1.start();
        Thread blockT2 =  new Thread(new Blocked(), "BlockedThread-2");
        blockT2.start();
        
        SleepUtil.millisecond(500);
        
        log.info("runnableThread.state=" + runnableThread.getState().toString());
        log.info("timeWaitT.state=" + timeWaitT.getState().toString());
        log.info("WaitT.state=" + waitT.getState().toString());
        log.info("blockT1.state=" + blockT1.getState().toString());
        log.info("blockT2.state=" + blockT2.getState().toString());
        /**
         * 1.2016-12-01 程序执行结果：<br>
         * 2016-12-01 19:15:40,469 INFO  main com.concurrent.thread.ThreadState timeWaitT.state=TIMED_WAITING
2016-12-01 19:15:40,469 INFO  main com.concurrent.thread.ThreadState WaitT.state=WAITING
2016-12-01 19:15:40,469 INFO  main com.concurrent.thread.ThreadState blockT1.state=TIMED_WAITING //阻塞在SleepUtil.second(100);
2016-12-01 19:15:40,469 INFO  main com.concurrent.thread.ThreadState blockT2.state=BLOCKED  //阻塞在  synchronized (Blocked.class) {
         */
        
        /**
         * 注意synchronized (Blocked.class) ，两个block线程共同引用同一对象。
         */
        
        /**
         * 2017-01-04
         */
       /* 2017-01-04 12:33:45,089 INFO  main com.concurrent.thread.ThreadState runnableThread.state=RUNNABLE
        		2017-01-04 12:33:45,090 INFO  main com.concurrent.thread.ThreadState timeWaitT.state=TIMED_WAITING
        		2017-01-04 12:33:45,090 INFO  main com.concurrent.thread.ThreadState WaitT.state=WAITING
        		2017-01-04 12:33:45,090 INFO  main com.concurrent.thread.ThreadState blockT1.state=BLOCKED
        		2017-01-04 12:33:45,090 INFO  main com.concurrent.thread.ThreadState blockT2.state=TIMED_WAITING*/
        
    }

    //线程不断地进行睡眠  ->  就会处于timed_waiting的状态<br>
    static class TimeWaiting implements Runnable {
        @Override
        public void run() {
            while (true) {
                SleepUtil.second(100);
            }
        }
    }
    
    //线程不断地进行while(true) 处于Runnable的状态<br>
    static class RUNNABLE implements Runnable {
        @Override
        public void run() {
            while (true) {
               //...
            }
        }
    }

    //线程在Waiting class实例上等待
    static class Waiting implements Runnable {
        @Override
        public void run() {
            while (true) {
                synchronized (Waiting.class) {
                    try {
                        Waiting.class.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    //线程在Blocked.class实例上加锁后，不会释放该锁
    static class Blocked implements Runnable {
        @Override
        public void run() {
            synchronized (Blocked.class) {
                while (true) {//一直循环下去不释放锁
                    SleepUtil.second(100);
                }
            }
        }
    }
}
