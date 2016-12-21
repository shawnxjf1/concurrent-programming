package com.gc;

import com.concurrent.util.SleepUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 配置一：
 * 1G的堆（-Xms1g -Xmx1g）
 * 使用CMS来清理老年代（-XX:+UseConcMarkSweepGC）使用并行回收器清理新生代（-XX:+UseParNewGC）
 * 将堆的12.5%（-Xmn128m）分配给新生代，并将Eden区和Survivor区的大小限制为一样的。
 * -Xms1g -Xmx1g -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -Xmn128m
 * 配置二：
 * 512m的堆（-Xms512m -Xmx512m）
 * 新生代和老年代都使用Parellel GC(-XX:+UseParallelGC)
 * 将堆的75%分配给新生代（-Xmn384）
 * <p/>
 * 第一个配置（大堆，大的老年代，CMS GC）每秒能吞食8.2头猪
 * 第二个配置（小堆，大的新生代，Parellel GC）每秒可以吞食9.2头猪
 * FIXME 不太懂这个意思?
 * -Xms512m -Xmx512m  -XX:+UseParallelGC -Xmn384m
 * 使用jstat 命令，jstat -gc -t -h10 pid 1s
 * User: shijingui
 * Date: 2016/11/3
 */
public class GCTypeSelectOptimize {
    private static volatile List objectList = new ArrayList<>();
    private static volatile int objAddCount = 0;
    private static final int MAX_Object = 1000;
    private static int bytes = 1 * 1024 * 1024;//1M

    public static void main(String[] args) {
        new Add().start();
        new Clean().start();
        /**
         * 2016-12-01 没看懂这个代码的意思?
         */
    }

    static class Add extends Thread {
        public void run() {
            while (true) {
                objectList.add(new byte[bytes]);
                if (objAddCount > MAX_Object) return;
                SleepUtil.millisecond(100);
            }
        }
    }

    static class Clean extends Thread {
        public void run() {
            long st = System.currentTimeMillis();
            while (true) {
                SleepUtil.millisecond(2000);//休息2秒
                objAddCount += objectList.size();
                objectList = new ArrayList();
                if (objAddCount > MAX_Object) {
                    System.out.format("Clean %d objectList in %d ms.%n", objAddCount, System.currentTimeMillis() - st);
                    return;
                }
            }
        }
    }
    /**
     * 参考:http://27091497.blog.163.com/blog/static/1180625020111156222150/ <br>
     * java -Xmx3550m -Xms3550m -Xmn2g -Xss128k 
-Xmx3550m:设置JVM最大可用内存为3550M. 
-Xms3550m:设置JVM促使内存为3550m.此值可以设置与-Xmx相同,以避免每次垃圾回收完成后JVM重新分配内存. 
-Xmn2g:设置年轻代大小为2G.整个堆大小=年轻代大小 + 年老代大小 + 持久代大小.持久代一般固定大小为64m,所以增大年轻代后,将会减小年老代大小.此值对系统性能影响较大,Sun官方推荐配置为整个堆的3/8. 
-Xss128k: 设置每个线程的堆栈大小.JDK5.0以后每个线程堆栈大小为1M,以前每个线程堆栈大小为256K.更具应用的线程所需内存大小进行 调整.在相同物理内存下,减小这个值能生成更多的线程.但是操作系统对一个进程内的线程数还是有限制的,不能无限生成,经验值在3000~5000左右. 
     */
    
    /**
     * tomcat 后台查询到的:
     * stlm     25463     1  1 11:40 ?        00:04:18 /opt/jdk1.6.0_34/bin/java -Djava.util.logging.config.file=/home/stlm/exec/rfss/console/liq_tomcat/conf/logging.properties -Djava.util.logging.manager=org.apache.juli.ClassLoaderLogManager -Xms256m -Xmx512m -Xss1024K -XX:PermSize=128m -XX:MaxPermSize=256m -Djava.rmi.server.hostname=10.7.111.153 -Djava.endorsed.dirs=/home/stlm/exec/rfss/console/liq_tomcat/endorsed -classpath /home/stlm/exec/rfss/console/liq_tomcat/bin/bootstrap.jar:/home/stlm/exec/rfss/console/liq_tomcat/bin/tomcat-juli.jar -Dcatalina.base=/home/stlm/exec/rfss/console/liq_tomcat -Dcatalina.home=/home/stlm/exec/rfss/console/liq_tomcat -Djava.io.tmpdir=/home/stlm/exec/rfss/console/liq_tomcat/temp org.apache.catalina.startup.Bootstrap start
     */
}
