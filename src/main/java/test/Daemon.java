package test;

/**
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/10/16
 * Time: 15:40
 */
public class Daemon {

    public static void main(String[] args) {
        Thread t = new Thread(new DaemonRunner());
        t.setDaemon(true);
        t.setPriority(10);
        t.setName("Daemon thread");
        t.start();
        
        Thread t1 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				System.out.println("execute thread t1...s");
			}
		});
        t1.start();
    }

    static class DaemonRunner implements Runnable {

        @Override
        public void run() {
            try {
            	System.out.println("bein execute DaemonRunner");
                Thread.sleep(2000);
            	System.out.println("end execute DaemonRunner");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println("Daemon thread run.");
            }
        }
    }
}
