package com.study.basejava.a3_lock_demo.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockDemo2 {
    volatile int i = 0; // 保证线程的可见性 不能保证原子性

    Lock lock = new ReentrantLock();

    public void add(){
        lock.lock();
        try {
            i++; // 三个步骤  读  加一  赋值  使用 javap -p -v LockDemo.class 查看相关步骤
        }finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        LockDemo2 lockDemo = new LockDemo2();
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
