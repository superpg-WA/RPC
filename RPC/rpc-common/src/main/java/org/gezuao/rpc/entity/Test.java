package org.gezuao.rpc.entity;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

//基于以下代码完成工具类BoundedBlockingQueue，能支持多线程的同时写入，和阻塞取数，要求尽可能地高效
class BoundedBlockingQueue<T>{
    //limit: 队列最大长度，如果在达到最大长度时插入元素，则阻塞在push函数中，直到队列中有空位为止。
    //waitMs: 队列为空时最长等待时长，单位ms
    private int waitMs = 5;
    private  int limit = 10;

    LinkedList<T> list = new LinkedList<>();
    ReentrantLock lock = new ReentrantLock();

    Condition full = lock.newCondition();

    Condition empty = lock.newCondition();

    public BoundedBlockingQueue(int limit, int waitMs){
        this.limit = limit;
        this.waitMs = waitMs;
    }
    //插入元素到队列尾部
    public void push(T obj){
        lock.lock();
        try{
            while(list.size() == limit){
                full.await();
            }
            list.add(obj);
            empty.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
    //从队列头部取出元素，如果没有元素等待timeoutMs后弹出null
    public T pop(){
        T t;
        lock.lock();
        try{
            while(list.size() == 0 && empty.await(waitMs, TimeUnit.MILLISECONDS)){

            }
            if(list.size() != 0){
                t = list.removeFirst();
                full.signal();
                return t;
            } else return null;

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return null;
    }
}
//编写一段范例代码，实现十个线程同时插入20个元素，另一个线程取数并打印200个元素。

class Test{
    public static void main(String[] args) {
        BoundedBlockingQueue<Integer> q = new BoundedBlockingQueue<Integer>(10,5000);
        Thread t = new Thread(() -> {
            for(int j = 0; j < 20; j++){
                q.push(j);
            }
        });
        t.start();

        Thread t1 = new Thread(() -> {
            int cnt = 0;
            for(int i = 0; i < 22; i++){
                cnt++;
                Integer res = q.pop();
                if(res != null)
                    System.out.println(res);
                else
                    System.out.println("-1");
            }
            System.out.println(cnt + "OK");
        });
        t1.start();
    }
}