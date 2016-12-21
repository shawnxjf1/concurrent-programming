package com.concurrent.lock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.log4j.Logger;
import org.junit.Test;

/**
 * 参考：https://examples.javacodegeeks.com/core-java/util/concurrent/locks-concurrent/reentrantlock/java-reentrantreadwritelock-example/<br>
 * @author lakala-shawn
 *
 */
public class TestReteenWriteLock {
	
	private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
	 private static String message = "a";
	 
	 static Logger log = Logger.getLogger(TestReteenWriteLock.class);
	  
	 public static void main(String[] args) throws InterruptedException{
	     Thread t1 = new Thread(new Read());
	     Thread t2 = new Thread(new WriteA());
	     Thread t3 = new Thread(new WriteB());
	     t1.start();
	     t2.start();
	     t3.start();
	     t1.join();
	     t2.join();
	     t3.join();
	 }
	 
	 @Test
	 public void testMultiRead() {
	     Thread t1 = new Thread(new Read(),"read1");
	     Thread t2 = new Thread(new Read(),"read2");
	     Thread t3 = new Thread(new Read(),"read3");
	    t1.start();
	    t2.start();
	    t3.start();
	    try {
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   
	    /**
	     * 2016-12-21  没加 Thread.sleep(15000)之前执行结果为：
	     * Threadread3getLock,and time=1482320680680
Threadread1getLock,and time=1482320680679
Threadread2getLock,and time=1482320680680
           结论：读锁可以共享
	     */
	    
	    /**
	     * 2016-12-21  没加 Thread.sleep(15000)，主线程不会等待其他线程执行完。
	     * Thread：read1 getted the read Lock,and time=1482321281832
Thread：read2 getted the read Lock,and time=1482321281832
Thread：read3 getted the read Lock,and time=1482321281834
ReadThread 12 ---> Message is a
ReadThread 13 ---> Message is a
ReadThread 11 ---> Message is a
	     */
	 }
	 
	 @Test
	 public void testMultiReadWithJoin() {
	     Thread t1 = new Thread(new Read(),"read1");
	     Thread t2 = new Thread(new Read(),"read2");
	     Thread t3 = new Thread(new Read(),"read3");
	    t1.start();
	    t2.start();
	    t3.start();
	    try {
			t1.join();
			t2.join();
			t3.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   /**
	    * join是创建线程的线程等待子线程执行完。 所以t1,t2,t3之间不需要顺序<br>
	    * Thread：read2 getted the read Lock,and time=1482321416438
Thread：read3 getted the read Lock,and time=1482321416438
Thread：read1 getted the read Lock,and time=1482321416438
ReadThread 13 ---> Message is a
ReadThread 11 ---> Message is a
ReadThread 12 ---> Message is a
	    */
	   
	 }
	 
	 @Test
	 public void testReadWrite()
	 {
	     Thread t2 = new Thread(new WriteA(),"write1");
	     try {
			Thread.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		 Thread t1 = new Thread(new Read(),"read1");
	     t1.start();
	     t2.start();
	 }
	  
	 static class Read implements Runnable {
	     public void run() {
	             if(lock.isWriteLocked()) {
	                 System.out.println("I'll take the lock from Write");
	             }
	             lock.readLock().lock();
		    	 System.out.println("Thread：" + Thread.currentThread().getName() + " getted the read Lock,and time=" + System.currentTimeMillis());
	             try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					log.error(e);
					e.printStackTrace();
				}
	             System.out.println("ReadThread " + Thread.currentThread().getId() + " ---> Message is " + message );
	             lock.readLock().unlock();
	         }
	     }
	  
	 static class WriteA implements Runnable {
	     public void run() {
	             try {
	                 lock.writeLock().lock();
	                 try {
						Thread.sleep(5);
					} catch (InterruptedException e) {
						log.error(e);
					}
			    	 System.out.println("Thread：" + Thread.currentThread().getName() + " getted the write Lock,and time=" + System.currentTimeMillis());
	                 message = message.concat("a");
	             } finally {
	                 lock.writeLock().unlock();
	             }
	         }
	     }
	  
	 static class WriteB implements Runnable {
	     public void run() {
	             try {
	                 lock.writeLock().lock();
			    	 System.out.println("Thread：" + Thread.currentThread().getName() + " getted the write Lock,and time=" + System.currentTimeMillis());
	                 message = message.concat("b");
	             } finally {
	                 lock.writeLock().unlock();
	             }
	         }
	     }

}
