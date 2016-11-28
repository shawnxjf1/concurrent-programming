package com.concurrent.thread;

import org.apache.log4j.Logger;
/**
 * 知识：
 * Calling yield simply tells the JVM to put the current thread in a waiting state 
 * even if the JVM wasn't going to.<br>
 * 
 */
/**
 * User: shijingui
 * Date: 2016/8/25
 */
public class ThreadYield {
	static Logger log = Logger.getLogger(ThreadYield.class);
	
    public static void main(String[] args) {
        Thread producer = new Producer();
        Thread consumer = new Consumer();

        producer.setPriority(Thread.MIN_PRIORITY); //Min Priority
        consumer.setPriority(Thread.MAX_PRIORITY); //Max Priority

        producer.start();
        consumer.start();
        for(int i =0 ;i<100000;i++)
        {
        	try {
				Thread.currentThread().sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        	log.info("producer status=" + producer.getState().toString());
        	log.info("consumer status=" + consumer.getState().toString());
        	/**
        	 * 2016-11-27执行结果:<br>执行main 得时候其他两个线程肯定是wait 状态咯<br>
        	 * 2016-11-27 15:06:24,957 INFO  main com.concurrent.thread.ThreadYield producer status=TIMED_WAITING
             * 2016-11-27 15:06:24,957 INFO  main com.concurrent.thread.ThreadYield consumer status=TIMED_WAITING
             * Producer : Produced Item 5
             * Consumer : Consumed Item 5
        	 */

        }
    }

    static class Producer extends Thread {
        @Override
        public void run() {
            for (int i = 1; i <= 5; i++) {
                System.out.println("Producer : Produced Item " + i);
//                Thread.yield();
                try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					log.error("error occur in Producer.",e);
					e.printStackTrace();
				}
            }
        }
    }

    static class Consumer extends Thread {
        public void run() {
            for (int i = 1; i <= 5; i++) {
                System.out.println("Consumer : Consumed Item " + i);
//                Thread.yield();
                try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					log.error("error occur in Consumer.",e);
					e.printStackTrace();
				}
            }
        }
    }
}
