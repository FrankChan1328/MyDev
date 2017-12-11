package mydev.thread;

import java.util.Vector;
import java.util.concurrent.TimeUnit;

public class VectorTest {
    public static void main(String[] args) throws InterruptedException {
        Vector<Integer> vector = new Vector<Integer>();
        // �ȴ��1000��ֵ��iterator��ֵ���Ա���
        for (int i = 0; i < 1000; i++) {
            vector.add(i);
        }

        Thread iteratorThread = new Thread(new IteratorRunnable(vector));
        iteratorThread.start();

        // ���߳�����5�룬��iteratorThread�ܹ���������������ʱ���ǲ���������ġ�
        TimeUnit.SECONDS.sleep(5);

        // ���߳�����֮�󣬻�ṹ���޸�Vector��Ȼ��ͻ��׳�ConcurrentModificationException�쳣
        Thread modifyVectorThread = new Thread(new ModifyVectorRunnable(vector));
        modifyVectorThread.start();
    }

    /**
     *  ���Runnable�᲻��ʹ�õ�����(for-each���)����Vector
     */
    private static class IteratorRunnable implements Runnable {

        private Vector<Integer> vector;

        public IteratorRunnable(Vector<Integer> vector) {
            this.vector = vector;
        }
        
        public void run() {
            while(true) {
                for (Integer i : vector) {

                }
            }
        }
        
        // ���´���Ų������ ConcurrentModificationException �쳣
/*        public void run() {
            while(true) {
                // �Ե������̼���
                synchronized (vector) {
                    for (Integer i : vector) {

                    }
                }
            }
        }*/
    }

    /**
     * ���Runnable�᲻�������Ԫ�أ�Ҳ���ǻ�ṹ���޸�Vector
     */
    private static class ModifyVectorRunnable implements Runnable {
        private Vector<Integer> vector;

        public ModifyVectorRunnable(Vector<Integer> vector) {
            this.vector = vector;
        }

        public void run() {
            while(true) {
                vector.add(1);
            }
        }
    }
}
