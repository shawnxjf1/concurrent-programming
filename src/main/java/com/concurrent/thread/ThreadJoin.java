package com.concurrent.thread;

import org.junit.Test;

/**
 * 
 * Waits for this thread to die.
 *  2016年11月27日:threadA.join()就是说等待该线程结束，其他线程都别来抢。<br>
 * User: shijingui
 * Date: 2016/8/25
 */
public class ThreadJoin {


	@Test
    public void testWithJoin() {
        Thread threadA = new ThreadA();
        Thread threadB = new ThreadB();

        try {
            threadA.start();
            threadA.join();
            threadB.start();
            threadB.join();
            System.out.println("---------end---------");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /**
         * 2016年11月27日 不管执行多少遍，都是A先执行。 注意A，B都join才行。
         * FIXME 重要的改进，可以通过定时任务或者批量掉
         */
    }
	
	@Test
    public void testWithJoinHalf() {
        Thread threadA = new ThreadA();
        Thread threadB = new ThreadB();

        try {
        	System.out.println("---------begin---------");
            threadA.start();
            threadA.join();
            threadB.start();
//            threadB.join();
            System.out.println("---------end---------");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /**
         * 2016年11月27日 不管执行多少遍，都是A先执行。 注意A，B都join才行。
         * FIXME 重要的改进，可以通过定时任务或者批量掉
         */
    }
	
	@Test
	public void testWithBathJoin()
	{
		for (int i=0; i<2000;i++)
		{
			testWithJoin();
		}
	}
	
	@Test
	public void testWithBathJoinHalf()
	{
		for (int i=0; i<20;i++)
		{
			System.out.println("--------run index---" + i);
			testWithJoinHalf();
		}
		
		/**
		 * 2016年11月27日 运行结果<br>
		 */
//		--------run index---0
//		---------begin---------
//		ThreadA execute done...
//		---------end---------
//		ThreadB execute done...
//		--------run index---1
//		---------begin---------
//		ThreadA execute done...
//		---------end---------
//		ThreadB execute done...
	}
    
    @Test
    public void testWithoutJoin()
    {
    	Thread threadA = new ThreadA();
        Thread threadB = new ThreadB();

        threadA.start();
        threadB.start();
        //2016年11月27日 执行结果:B先于A
//        ThreadB execute done...
//        ThreadA execute done...
    }
    
    @Test
	public void testWithoutBathJoin()
	{
		for (int i=0; i<20;i++)
		{
			System.out.println("--------run index---" + i);
			testWithoutJoin();
		}
//		--------run index---0
//		ThreadA execute done...
//		ThreadB execute done...
//		--------run index---1
//		ThreadA execute done...
//		ThreadB execute done...
//		--------run index---2
//		ThreadA execute done...
//		ThreadB execute done...
//		--------run index---3
//		ThreadA execute done...
//		ThreadB execute done...
//		--------run index---4
//		ThreadA execute done...
//		ThreadB execute done...
//		--------run index---5
//		ThreadA execute done...
//		ThreadB execute done...
//		--------run index---6
//		--------run index---7
//		ThreadB execute done...   //FIXME 这里是B先开始，所以顺序是乱的<br>
//		ThreadA execute done...
//		ThreadA execute done...
//		--------run index---8
//		ThreadB execute done...
	}

    static class ThreadA extends Thread {
        public void run() {
            System.out.println("ThreadA execute done...");
        }
    }

    static class ThreadB extends Thread {
        public void run() {
            System.out.println("ThreadB execute done...");
        }
    }
}
