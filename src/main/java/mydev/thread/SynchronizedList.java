package mydev.thread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SynchronizedList {

    public static void main(String[] args) throws InterruptedException {
        List<String> list = Collections.synchronizedList(new ArrayList<String>());
        
        Thread viewThread = new Thread(new IteratorViewRunnable(list));
        viewThread.start();
        
        TimeUnit.SECONDS.sleep(5);
        
        Thread addThread = new Thread(new IteratorAddRunnable(list));
        addThread.start();
    }
    
    /**
     * 向list 中添加
     *
     */
    static class IteratorAddRunnable implements Runnable {
        private List<String> list;

        public IteratorAddRunnable(List<String> list) {
            this.list = list;
        }

        public void run() {
            while(true){
                list.add("test"+System.currentTimeMillis());
            }
        }
    }

    /**
     * 遍历list
     *
     */
    static class IteratorViewRunnable implements Runnable {
        private List<String> list;

        public IteratorViewRunnable(List<String> list) {
            this.list = list;
        }

        public void run() {
            while(true){
            }
        }
    }
    
}
