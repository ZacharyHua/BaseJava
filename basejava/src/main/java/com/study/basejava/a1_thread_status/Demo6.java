package com.study.basejava.a1_thread_status;

/**
 * 线程封闭
 */
public class Demo6 {
    /**
     * threadLocal变量，每个线程都有一个副本，互不干扰
     */
    public static ThreadLocal<String> value = new ThreadLocal<>();

    /**
     * threadLocal测试
     */
    public void threadLocalTest() throws InterruptedException {
        // threadlocal线程封闭示例
        value.set("这是主线程设置的123");
        String v = value.get();
        System.out.println("线程1执行之前，主线程取到的值：" + v);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String v = value.get();
                System.out.println("线程1取到的值：" + v);
                value.set("这是线程1设置的456");
                v = value.get();
                System.out.println("重新设置之后，线程1取到的值：" + v);
                System.out.println("线程1执行结束");
            }
        }).start();
        Thread.sleep(5000L);
        v = value.get();
        System.out.println("线程1执行之后，主线程取到的值：" + v);
    }

    public static void main(String[] args) throws InterruptedException {
        new Demo6().threadLocalTest();
    }
}
