package com.concurrent.objectshare;

import org.junit.Test;

/**
 * 锁总线变成了锁缓存<br>
 * 1. 多处理器环境中，LOCK# 信号确保在声言该信号期间，处理器可以独占使用任何共享内存。（因为它会锁住总线，导致其他CPU不能访问总线，不能访问总线就意味着不能访问系统内存），
 * 但是在最近的处理器里，LOCK＃信号一般不锁总线，而是锁缓存，毕竟锁总线开销比较大。<br>
 * 2. 锁定这块内存区域的缓存并回写到内存，并使用缓存一致性机制来确保修改的原子性，此操作被称为“缓存锁定”，
 * 缓存一致性机制会阻止同时修改被两个以上处理器缓存的内存区域数据。
 * 3.IA-32 和Intel 64处理器能嗅探其他处理器访问系统内存和它们的内部缓存。它们使用嗅探技术保证它的内部缓存，系统内存和其他处理器的缓存的数据在总线上保持一致。
 */

/**
 * FIXME 运行结果还没有出来,多线程非常难模拟。<br>
 * @author shawn
 *
 */
public class VisibilityWithVolatile {

    private  volatile boolean ready = false;
    private  int number;

    @Test
    public  void testVilatile() {
        new ReaderThread().start();
        new ReaderThread().start();
        new ReaderThread().start();
        new ReaderThread().start();
        new ReaderThread().start();
        new ReaderThread().start();
        ready = true;
        System.out.println("in main()，ready=" + ready);
        number = 31;
        
        /**
         * 2016年11月27日 执行外概况：
         */
    }

    /**
     * @author shawn
     *
     */
    private  class ReaderThread extends Thread {
        @Override
        public void run() {
        	//volatile 只是适用于一个读多个写的场景<br>
            while (!ready) {
            	System.out.println(Thread.currentThread().getName() + " executing...");
            }

            System.out.println("read number : " + number + ",ready=" + ready);
        }
    }
}
