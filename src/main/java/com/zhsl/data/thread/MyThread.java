package com.zhsl.data.thread;

/**
 * @author xiangjg
 * @date 2018/7/4 13:12
 */
public class MyThread extends Thread {
    private String name;
    public MyThread(String name)
    {
        this.name = name;
    }
    public void run()
    {
        System.out.println("hello " + name);
    }
    public static void main(String[] args)
    {
        String str= "world";
        Thread thread = new MyThread("world");
        str = "aaa";
        thread.start();

    }
}
