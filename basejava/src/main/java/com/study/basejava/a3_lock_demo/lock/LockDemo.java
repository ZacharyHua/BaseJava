package com.study.basejava.a3_lock_demo.lock;

import java.util.concurrent.atomic.AtomicInteger;

// 创建两个线程 对变量 i 进行递增操作
public class LockDemo {
    // volatile int i = 0; // 保证线程的可见性 不能保证原子性
    AtomicInteger i = new AtomicInteger();
    public void add(){
        i.incrementAndGet(); // 三个步骤  读  加一  赋值  使用 javap -p -v LockDemo.class 查看相关步骤
    }

    public static void main(String[] args) throws InterruptedException {
        LockDemo lockDemo = new LockDemo();
        for (int i = 0 ; i < 2 ; i ++ ){
            new Thread(()->{
                for (int j = 0 ; j < 10000 ; j++){
                    lockDemo.add();
                }
            }).start();
        }
        Thread.sleep(2000L);
        System.out.println(lockDemo.i);
    }
}
