package com.study.basejava.a1_thread_status;

import java.util.concurrent.locks.LockSupport;

/**
 * 三种线程协作通信的方式：suspend/resume、wait/notify、park/unpark
 */
public class Demo5 {
    // 包子店
    public static Object baozidian = null ;

    /**
     *  正常的 suspend/resume
     */
    public void suspendResumeTest() throws InterruptedException {
        // 启动线程
        Thread consumerThread = new Thread(()->{
            if (baozidian == null){     // 如果没有包子，则进入等待状态
                System.out.println("1. 进入等待状态");
                Thread.currentThread().suspend();
            }
            System.out.println("2. 买到包子，回家");
        });
        consumerThread.start();
        // 等待3秒 生产一个包子
        Thread.sleep(3000L);
        baozidian = new Object();
        consumerThread.resume();
        System.out.println("3. 通知消费者去消费");
    }

    /**
     * 死锁的suspend/resume。 suspend并不会像wait一样释放锁，故此容易写出死锁代码
     */
    public void suspendResumeDeadLockTest() throws InterruptedException {
        // 启动线程
        Thread consumeThread = new Thread(() -> {
            if (baozidian == null) {
                System.out.println("1. 进入等待状态");
                synchronized (this) {  // 线程被挂起... 没人能够再次拿到锁  故进入死锁状态
                    Thread.currentThread().suspend();
                }
            }
            System.out.println("2. 买到包子，回家");
        });
        consumeThread.start();
        // 等待3秒 生产一个包子
        Thread.sleep(3000L);
        baozidian = new Object();
        // 争取到锁以后，再恢复consumerThread
        synchronized (this) {
            consumeThread.resume();
        }
        System.out.println("3、通知消费者");
    }
    /**
    * 导致程序永久挂起的suspend/resume
    */
    public void suspendResumeDeadLockTest2() throws InterruptedException {
        // 启动线程
        Thread consumerThread = new Thread(() -> {
            if (baozidian == null) {
                System.out.println("1、没包子，进入等待");
                try { // 为这个线程加上一点延时
                    Thread.sleep(5000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 这里的挂起执行在resume后面
                Thread.currentThread().suspend();
            }
            System.out.println("2、买到包子，回家");
        });
        consumerThread.start();
        // 3秒之后，生产一个包子
        Thread.sleep(3000L);
        baozidian = new Object();
        consumerThread.resume();
        System.out.println("3、通知消费者");
        consumerThread.join();
    }
    /**
     * 正常的wait、notify
     * @throws InterruptedException
     */
    public void waitNotifyTest() throws InterruptedException {
        new Thread(()->{
            synchronized (this){
                while (baozidian == null ){
                    System.out.println("1. 进入等待状态");
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println("2. 买到包子，回家");
        }).start();
        // 3秒之后，生产一个包子
        Thread.sleep(3000L);
        baozidian = new Object();
        synchronized (this){
            this.notify();
//            this.notifyAll();
            System.out.println("3、通知消费者");
        }
    }

    /**
     * 会导致程序永久等待的wait/notify
     */
    public void waitNotifyDeadLockTest() throws InterruptedException {
        new Thread(()->{
            if (baozidian == null ){
                try {
                    Thread.sleep(5000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (this){
                    System.out.println("1、进入等待");
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println("2、买到包子，回家");
        }).start();
        // 3秒之后，生产一个包子
        Thread.sleep(3000L);
        baozidian = new Object();
        synchronized (this){
            this.notify();
//            this.notifyAll();
            System.out.println("3、通知消费者");
        }
    }

    /**
     * 正常的park/unpark
     * @throws InterruptedException
     */
    public void parkUnparkTest() throws InterruptedException {
        Thread consumerThread = new Thread(()->{
            while (baozidian == null){
                System.out.println("1、进入等待");
                LockSupport.park();
            }
            System.out.println("2、买到包子，回家");
        });
        consumerThread.start();
        Thread.sleep(3000L);
        baozidian = new Object();
        LockSupport.unpark(consumerThread);
        System.out.println("3、通知消费者");
    }

    /**
     * 死锁的park/unpark
     */
    public void parkUnparkDeadLockTest() throws InterruptedException {
        Thread consumerThread = new Thread(()->{
           if (baozidian == null){
               System.out.println("1、进入等待");
               // 当前线程拿到锁，然后挂起
               synchronized (this){
                   LockSupport.park();
               }
           }
        });
        consumerThread.start();
        Thread.sleep(3000L);
        baozidian = new Object();
        synchronized (this){
            LockSupport.unpark(consumerThread);
        }
        System.out.println("3、通知消费者");
    }

    public static void main(String[] args) throws InterruptedException {
        Demo5 demo5 = new Demo5();
        // 对调用顺序有要求，也要开发自己注意锁的释放。这个被弃用的API， 容易死锁，也容易导致永久挂起。
//        demo5.suspendResumeTest();
//        demo5.suspendResumeDeadLockTest();
//        demo5.suspendResumeDeadLockTest2();

        // wait/notify要求再同步关键字里面使用，免去了死锁的困扰，但是一定要先调用wait，再调用notify，否则永久等待了
//        demo5.waitNotifyTest();
//        demo5.waitNotifyDeadLockTest();

        // park/unpark没有顺序要求，但是park并不会释放锁，所有再同步代码中使用要注意
//        demo5.parkUnparkTest();
        demo5.parkUnparkDeadLockTest();
    }

}
