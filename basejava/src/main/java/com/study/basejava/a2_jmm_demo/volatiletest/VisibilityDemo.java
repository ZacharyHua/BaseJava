package com.study.basejava.a2_jmm_demo.volatiletest;

import java.util.concurrent.TimeUnit;
// 1、 jre/bin/server  放置hsdis动态链接库
//  测试代码 将运行模式设置为-server， 变成死循环   。 没加默认就是client模式，就是正常（可见性问题）
// 2、 通过设置JVM的参数，打印出jit编译的内容 （这里说的编译非class文件），通过可视化工具jitwatch进行查看
// -server -XX:+UnlockDiagnosticVMOptions -XX:+PrintAssembly -XX:+LogCompilation -XX:LogFile=jit.log
//  关闭jit优化-Djava.compiler=NONE
public class VisibilityDemo {
    private volatile boolean flag = true;

    public static void main(String[] args) throws InterruptedException {
        VisibilityDemo demo = new VisibilityDemo();
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                // class -> 运行时jit编译 -> 汇编指令 -> 重排序
                while (demo.flag){  // 指令重排序
                    i++;
                }
                // while 变成这样  针对热点代码进行优化
//                if (demo.flag){
//                    while (true){
//                        i++;
//                    }
//                }
                System.out.println(i);
            }
        });
        thread1.start();
        TimeUnit.SECONDS.sleep(2);
        // 设置is为false，使上面的线程结束while循环
        demo.flag = false;
        System.out.println("被置位false了");
    }
}
