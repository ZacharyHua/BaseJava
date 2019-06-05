package com.study.basejava.a3_lock_demo.lock;

public class LockDemo1 {
    volatile int i = 0; // 保证线程的可见性 不能保证原子性

    public void add(){
        synchronized (this) {
            i++; // 三个步骤  读  加一  赋值  使用 javap -p -v LockDemo.class 查看相关步骤
        }
    }

    public static void main(String[] args) throws InterruptedException {
        LockDemo1 lockDemo = new LockDemo1();
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
