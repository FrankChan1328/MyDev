/**
 * @(#)SynchronizedListTest.java 2017-12-08
 * 
 * Copyright 2000-2017 by ChinanetCenter Corporation.
 *
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * ChinanetCenter Corporation ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with ChinanetCenter.
 * 
 */
package mydev.thread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import com.alibaba.fastjson.JSON;

/**
 * 
 * @author chenms
 * @date 2017-12-08
 * @version 1.0.0
 */
public class SynchronizedListTest {

    private static List<String> managers;
    
    @PostConstruct
    private void init() {
        run();
    }
    
    public static void add(String str) {
        if (null != str) {
            managers.add(str);
        }
    }
    
    private static void run(){
        if (null == managers) {
            managers = Collections.synchronizedList(new ArrayList<String>());
        }
        
        Thread thread = new Thread(new Runnable() {
            public void run() {
                while(true){
                    managersRun();
                }
            }
        });
        thread.start();
    }
    
    private static void managersRun() {
        synchronized (managers) {
            if(null != managers && managers.size() >0){
                ExecutorService exec = Executors.newFixedThreadPool(10);
                // execcomp 管理内部完成队列
                CompletionService<String> execcomp = new ExecutorCompletionService<>(exec);
                for (final String mgr : managers) {
                    execcomp.submit(new Callable<String>() {
                        @Override
                        public String call() throws Exception {
                            System.out.println("mgr:"+mgr);
                            // do sth
                            return mgr;
                        }
                    });
                }
                
                //检索并移除表示下一个已完成任务的 Future
                List<String> manager2Del = new ArrayList<>();
                for (final String mgr : managers) {
                    try{
                        Future<String> future = execcomp.take();
                        String mgr_exed = future.get();
                        //
                        if(mgr.equals("i:4") || mgr.equals("i:12")){
                            System.out.println("============================sleeping=======================");
                            Thread.sleep(5000);
                        }
                        if (null != mgr_exed) {
                            // do sth
                            System.out.println("mgr_exed:"+mgr_exed);
                        }
                        manager2Del.add(mgr_exed);
                    }
                    catch(Exception e){
                        
                    }
                }
                
                // 去掉执行完成的任务
                System.out.println("begin to remove:"+JSON.toJSONString(manager2Del));
                managers.removeAll(manager2Del);
            }
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        SynchronizedListTest.run();
        
        for(int i =0 ;i< 10; i++){
            SynchronizedListTest.add("i:"+i);
        }
//        TimeUnit.SECONDS.sleep(10);
        for(int i =11 ;i< 20; i++){
            SynchronizedListTest.add("i:"+i);
        }
        
    }

}
