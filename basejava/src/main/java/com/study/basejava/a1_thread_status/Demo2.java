package com.study.basejava.a1_thread_status;

/**
 * 示例2 - 多线程运行状态切换示例
 */
public class Demo2 {
    public static Thread thread1;
    public static Demo2 obj;

    public static void main(String[] args) throws InterruptedException {
//        第一种状态切换 - 新建 -> 运行 -> 终止
        threadRunTimeStatus1();
        System.out.println();
        System.out.println("-----------------");
        System.out.println();
//        第二种状态切换 - 新建 -> 运行 -> 等待 -> 运行 -> 终止(sleep方式)
        threadRunTimeStatus2();
        System.out.println();
        System.out.println("-----------------");
        System.out.println();
//        第三种状态切换 - 新建 -> 运行 -> 阻塞 -> 运行 -> 终止
        threadRunTimeStatus3();
    }

    public static void threadRunTimeStatus1() throws InterruptedException {
        System.out.println("#######第一种状态切换  -> 新建 -> 运行 -> 终止################################");
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("thread1当前状态："+Thread.currentThread().getState().toString());
                System.out.println("thread1执行了");
            }
        });
        System.out.println("没调用start方法，thread1当前状态："+thread1.getState().toString());
        thread1.start();
        Thread.sleep(2000L); // 等待thread1执行完成后，再看线程状态
        System.out.println("等待thread1执行完成后，再看线程状态："+thread1.getState().toString());
        // thread1.start(); TODO 注意，线程终止之后，再进行调用，会抛出IllegalThreadStateException异常
    }

    public static void threadRunTimeStatus2() throws InterruptedException {
        System.out.println("############第二种：新建 -> 运行 -> 等待 -> 运行 -> 终止(sleep方式)###########################");
            Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {   // 将线程thread2置位等待状态 1500毫秒后唤醒
                    Thread.sleep(1500L);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }finally {
                    System.out.println("thread2当前状态："+Thread.currentThread().getState().toString());
                    System.out.println("thread2执行了");
                }
            }
        });
        System.out.println("没调用start方法，thread2当前状态："+thread2.getState().toString());
        thread2.start();
        Thread.sleep(200L); // 等待 200ms  再看thread2的状态
        System.out.println("等待200毫秒，再看thread2当前状态："+thread2.getState().toString());
        Thread.sleep(3000L); // 等待 3000ms  再看thread2的状态
        System.out.println("等待3秒，再看thread2当前状态：" + thread2.getState().toString());
    }

    public static void threadRunTimeStatus3() throws InterruptedException {
        System.out.println("############第三种：新建 -> 运行 -> 阻塞 -> 运行 -> 终止###########################");
            Thread thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (Demo2.class) {
                    System.out.println("thread3当前状态：" + Thread.currentThread().getState().toString());
                    System.out.println("thread3 执行了");
                }
            }
        });
        synchronized (Demo2.class){
            System.out.println("没调用start方法，thread2当前状态："+thread3.getState().toString());
            thread3.start();
            System.out.println("调用start方法，thread3当前状态：" + thread3.getState().toString());
            Thread.sleep(200L); // 等待 200ms  再看thread3的状态
            System.out.println("等待200ms，thread3当前状态：" + thread3.getState().toString());
        }
        Thread.sleep(3000L); // 等待 3000ms  再看thread3的状态
        System.out.println("等待3秒，让thread3抢到锁，再看thread3当前状态：" + thread3.getState().toString());
    }


}
