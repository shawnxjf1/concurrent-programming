package com.concurrent.reentrant;

import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;
import org.junit.Test;

/**
 * The method will return
 * immediately if the current thread already owns the lock. This can
 * be checked using methods {@link #isHeldByCurrentThread}, and {@link
 * #getHoldCount}.
 *
 * <p>The constructor for this class accepts an optional
 * <em>fairness</em> parameter.  When set {@code true}, under
 * contention, locks favor granting access to the longest-waiting
 * thread.  Otherwise this lock does not guarantee any particular
 * access order.  Programs using fair locks accessed by many threads
 * may display lower overall throughput, 但是防止了饥饿的发生。<br>
 */
/**
 * 测试2016年11月28日 注意get中调用了set 说明是可重用的
 * <p/>
 * 1.Lock有更灵活的锁定方式，公平锁与不公平锁，而synchronized永远是公平的。
 */
public class ReentrantLockDemo implements Runnable {
	
	Logger log = Logger.getLogger(ReentrantLockDemo.class);

	private ReentrantLock lock = new ReentrantLock();

	public void get() {
		lock.lock();
		System.out.println("1.holdCount" + lock.getHoldCount());
		System.out.println("get->" + Thread.currentThread().getId());
		set();
		System.out.println("3.holdCount" + lock.getHoldCount());
		lock.unlock();
	}

	public void tryLock() {
		long timeBeforeLock = System.currentTimeMillis();
		if (lock.tryLock()) { // 如果已经被lock，则立即返回false不会等待，达到忽略操作的效果,实际上没有获取锁。
			try {
				log.info("get the lock");
			} finally {
				//lock.unlock();
			}

		}
		long timeAfterLock = System.currentTimeMillis();
		log.info("cost time in try lock=" + (timeAfterLock - timeBeforeLock));
	}
	

	public void blockLock() {
		long timeBeforeLock = System.currentTimeMillis();
		try {
			lock.lock(); // 如果被其它资源锁定，会在此等待锁释放，达到暂停的效果
			log.info("lock has been by other thread ...");
			// 操作
		} finally {
			log.info("release lock...");
		   	lock.unlock();
		}
		long timeAfterLock = System.currentTimeMillis();
		log.info("cost time in lock,waiting releas lock=" + (timeAfterLock - timeBeforeLock));
	}
	

	public void set() {
		lock.lock();
		System.out.println("2.holdCount" + lock.getHoldCount());
		System.out.println("set->" + Thread.currentThread().getId());
		lock.unlock();
	}

	@Override
	public void run() {
		get();
	}

	@Test
	public void testTryLock() {
		//FIXME  2016年11月29日 测试结果没有输出日志
		lock.lock();
		try {
			Thread.currentThread().sleep(50);
			new Thread(new Runnable() {

				@Override
				public void run() {
					tryLock();
				}
			}).start();
			Thread.currentThread().sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		lock.unlock();
		/**
		 * try lock是立马释放
		 * 2016年11月30日 2016-11-30 23:03:38,758 INFO  Thread-0 com.concurrent.reentrant.ReentrantLockDemo cost time in try lock=0
		 */
	}

	@Test
	public void testLockBlock() {
		lock.lock();
		try {
			Thread.currentThread().sleep(50);
			new Thread(new Runnable() {
				@Override
				public void run() {
					blockLock();
				}
			}).start();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		log.info("test() realse lock...");
		lock.unlock();
		/**
		 * 必持有锁的线程先释放，其他才能接着进行下去.<br>
		 * 2016-11-30 23:12:03,954 INFO  main com.concurrent.reentrant.ReentrantLockDemo test() realse lock...
2016-11-30 23:12:03,955 INFO  Thread-0 com.concurrent.reentrant.ReentrantLockDemo lock has been by other thread ...
2016-11-30 23:12:03,955 INFO  Thread-0 com.concurrent.reentrant.ReentrantLockDemo release lock...
2016-11-30 23:12:03,955 INFO  Thread-0 com.concurrent.reentrant.ReentrantLockDemo cost time in lock,waiting releas lock=1
		 */
	}

	public static void main(String[] args) {
		ReentrantLockDemo demo02 = new ReentrantLockDemo();
		new Thread(demo02).start();
		new Thread(demo02).start();
	}

}
