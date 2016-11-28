package com.concurrent.thread;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/**
 * User: shijingui
 * Date: 2016/2/3
 */
public class ThreadMXBeanExample {

    public static void main(String... args) {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(true, true);
        int i=0;
        for (ThreadInfo threadInfo : threadInfos) {
        	i++;
            long threadId = threadInfo.getThreadId();
            String threadName = threadInfo.getThreadName();
            Thread.State state = threadInfo.getThreadState();
            System.out.println("thread" + i + " status, " + threadInfo.toString());
//            System.out.println("Thread id:" + threadId + " ;Thread name:" + threadName + "; Thread state:" + state.name());
        }
        
        /**
         * 2016-11-27 执行结果：
         */
//              thread1 status, "Signal Dispatcher" Id=4 RUNNABLE
//
//        		thread2 status, "Finalizer" Id=3 WAITING on java.lang.ref.ReferenceQueue$Lock@489bb457
//        			at java.lang.Object.wait(Native Method)
//        			-  waiting on java.lang.ref.ReferenceQueue$Lock@489bb457
//        			at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:135)
//        			at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:151)
//        			at java.lang.ref.Finalizer$FinalizerThread.run(Finalizer.java:209)
//
//        		thread3 status, "Reference Handler" Id=2 WAITING on java.lang.ref.Reference$Lock@665ce0fe
//        			at java.lang.Object.wait(Native Method)
//        			-  waiting on java.lang.ref.Reference$Lock@665ce0fe
//        			at java.lang.Object.wait(Object.java:503)
//        			at java.lang.ref.Reference$ReferenceHandler.run(Reference.java:133)
//
//        		thread4 status, "main" Id=1 RUNNABLE
//        			at sun.management.ThreadImpl.dumpThreads0(Native Method)
//        			at sun.management.ThreadImpl.dumpAllThreads(ThreadImpl.java:446)
//        			at com.concurrent.thread.ThreadMXBeanExample.main(ThreadMXBeanExample.java:15)
    }
}
