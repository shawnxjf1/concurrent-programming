package com.concurrent.thread.pool;

import com.concurrent.util.SleepUtil;

import java.util.concurrent.CountDownLatch;

/**
 * 2016年11月27日 没有看懂结果<br>
 * User: shijingui
 * Date: 2016/2/18
 */
public class ConnectionPoolTest {

    private static ConnectionPool pool = new ConnectionPool(10);

    private static CountDownLatch startSign = new CountDownLatch(1);


    public static void main(String[] args) {

        for (int i = 0; i < 5; i++) {
            Thread consumerThread = new Thread(new Consumer(startSign), "ConsumerThread-" + i);
            consumerThread.start();
        }

        for (int i = 0; i < 2; i++) {
            Thread producerThread = new Thread(new Producer(startSign), "ProducerThread-" + i);
            producerThread.start();
        }
        startSign.countDown();
    }

    static class Producer implements Runnable {
        CountDownLatch startSign;

        public Producer(CountDownLatch startSign) {
            this.startSign = startSign;
        }

        @Override
        public void run() {
            try {
                startSign.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (true) {
                SleepUtil.second(1);
                try {
                    pool.realeaseConnection();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " put a connection.");
            }
        }
    }

    static class Consumer implements Runnable {
        CountDownLatch startSign;

        public Consumer(CountDownLatch startSign) {
            this.startSign = startSign;
        }

        @Override
        public void run() {
            try {
                startSign.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (true) {
                SleepUtil.second(1);
                try {
                    Connection connection = pool.fetchConnection(0);
                    if (connection == null) {
                        System.out.println(Thread.currentThread().getName() + ": can not get a connection.");
                    } else {
                        System.out.println(Thread.currentThread().getName() + ": get a connection");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
