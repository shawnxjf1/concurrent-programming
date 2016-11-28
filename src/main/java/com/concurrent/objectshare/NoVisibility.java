package com.concurrent.objectshare;

/**
 * 存在可变的共享变量，非线程安全类。
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/9/8
 * Time: 21:14
 */
public class NoVisibility {

    private static boolean ready = false;
    private static int number;

    public static void main(String[] args) {
        new ReaderThread().start();
        new ReaderThread().start();
        new ReaderThread().start();
        new ReaderThread().start();
        new ReaderThread().start();
        new ReaderThread().start();
        ready = true;
        System.out.println("in main(),number="+ number + ",ready=" + ready);
        number = 31;
        
        /**
         * 2016年11月27日 执行外概况：
         */
        //结果是 main中已经设置为true，但是thread-2，thread-1,thread-0读取的还是false进入了while循环。当然thread-4，thread3 凑巧刚好读取的read=true<br>
//        Thread-0 executing...
//        Thread-0 executing...
//        Thread-4 executing...
//        Thread-3 executing...
//        read number : 0,ready=true
//        in main(),number=0,ready=true
//        Thread-2 executing...
//        Thread-1 executing...
//        read number : 31,ready=true
//        read number : 0,ready=true
//        read number : 0,ready=true
//        Thread-0 executing...
//        read number : 31,ready=true
//        read number : 31,ready=true
        
        /**
         * 2016年11月27日写：为什么隔一段时间可以看到变量（隔几个时钟周期）：
         * volatile的可见性只是把回写方式保存缓存一致性的架构变成直写的效果——缓存修改了立即能回映到内存；
         * 而不加volatile的则是CUP在执行该线程任务一段时间后（也许是几个时钟周期），或者切换任务后，将就修改过的缓存映射回内存。所以其他CPU对这块内存修改后的读取会有较大的延迟~~而不是完全读不到。
         * FIXME 学习总线的相关知识，基础知识的重要性呀<br>
         */
    }

    /**
     * static class 才能引用static 变量,即如果ready变成 非static的话 ReaderThread也应该变成非static.<br>
     * @author shawn
     *
     */
    private static class ReaderThread extends Thread {
        @Override
        public void run() {
            while (!ready) {
            	System.out.println(Thread.currentThread().getName() + " executing...");
            }

            System.out.println("read number : " + number + ",ready=" + ready);
        }
    }
}
