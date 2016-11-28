package test;

import org.junit.Test;
/**
 * 参考：<br>
 * 1. Volatile变量修饰符如果使用恰当的话，它比synchronized的使用和执行成本会更低，因为它不会引起线程上下文的切换和调度。<br>
 * 
 * 2.volatile变量修饰的共享变量进行写操作的时候会多第二行汇编代码，通过查IA-32架构软件开发者手册可知，
 * lock前缀的指令在多核处理器下会引发了两件事情。
   将当前处理器缓存行的数据会写回到系统内存。
   这个写回内存的操作会引起在其他CPU里缓存了该内存地址的数据无效。
   处理器为了提高处理速度，不直接和内存进行通讯，而是先将系统内存的数据读到内部缓存（L1,L2或其他）后再进行操作，
   但操作完之后不知道何时会写到内存，如果对声明了Volatile变量进行写操作，JVM就会向处理器发送一条Lock前缀的指令，
   将这个变量所在缓存行的数据写回到系统内存。但是就算写回到内存，如果其他处理器缓存的值还是旧的，再执行计算操作就会有问题，<br>
   3.所以在多处理器下，为了保证各个处理器的缓存是一致的，就会实现缓存一致性协议，每个处理器通过嗅探在总线上传播的数据来检查自己缓存的值是不是过期了，当处理器发现自己缓存行对应的内存地址被修改，就会将当前处理器的缓存行设置成无效状态，当处理器要对这个数据进行修改操作的时候，会强制重新从系统内存里把数据读到处理器缓存里。
 */
/**
 * User: shijingui
 * Date: 2016/10/21
 */
public class VolatilePerformanceTest {
    private long num;
    private volatile long volatileNum;
    private int size = 10000000;

    @Test
    public void test() {
        long time = System.nanoTime();

        for (int i = 0; i < size; i++)
            volatileNum = System.currentTimeMillis();
        System.out.println((-time + (time = System.nanoTime())) + "    volatile写+取系统时间");

        for (int i = 0; i < size; i++)
            num = System.currentTimeMillis();
        System.out.println((-time + (time = System.nanoTime())) + "    普通写+取系统时间");

        for (int i = 0; i < size; i++) {
            synchronized (VolatilePerformanceTest.class) {
            }
        }
        System.out.println((-time + (time = System.nanoTime())) + "    空的同步块(synchronized)");

        for (int i = 0; i < size; i++)
            volatileNum++;
        System.out.println((-time + (time = System.nanoTime())) + "     volatile变量自增");

        for (int i = 0; i < size; i++)
            volatileNum = i;
        System.out.println((-time + (time = System.nanoTime())) + "     volatile写");

        for (long i = 0; i < size; i++)
            i = volatileNum;
        System.out.println((-time + (time = System.nanoTime())) + "     volatile读");

        for (long i = 0; i < size; i++)
            num++;
        System.out.println((-time + (time = System.nanoTime())) + "     普通变量自增");

        for (long i = 0; i < size; i++)
            num = i;
        System.out.println((-time + (time = System.nanoTime())) + "     普通变量写");


        for (long i = 0; i < size; i++)
            i = num;
        System.out.println((-time + (time = System.nanoTime())) + "     普通变量读");
    }

}
