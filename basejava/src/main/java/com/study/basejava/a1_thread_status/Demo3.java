package com.study.basejava.a1_thread_status;

/**
 * 示例3 - 线程stop强制性中止，破坏线程安全的示例
 */
public class Demo3 {
    public static void main(String[] args) throws InterruptedException {
        StopThread thread = new StopThread();
        thread.start();
        // 睡眠1s 确保变量自增成功
        Thread.sleep(1000L);
        // 暂停线程
        thread.stop(); // 错误的终止
//        thread.interrupt();
        while (thread.isAlive()){
            System.out.println("线程的状态"+thread.getState().toString());
            // 确保线程已经终止
        }
        // 输出结果
        thread.print();
    }
}
