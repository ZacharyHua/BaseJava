package com.study.basejava.a1_thread_status;

public class StopThread extends Thread {
    private int i = 0, j = 0;

    @Override
    public void run() {
        synchronized (this){
            // 增加同步锁，确保线程安全
            ++i;
            try {
                // 睡眠10秒，模拟耗时操作
                Thread.sleep(10000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            ++j;
        }
    }

    /**
     * 打印i和j
     */
    public void print() {
        System.out.println("i=" + i + " j=" + j);
    }
}
