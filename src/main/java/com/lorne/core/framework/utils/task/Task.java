package com.lorne.core.framework.utils.task;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by yuliang on 2016/4/28.
 */
public  class Task {

    private Lock lock;
    private Condition condition;
    private IBack back;


    /**
     * 是否被唤醒
     */
    private boolean isNotify = false;
    /**
     * 唯一标示key
     */
    private String key;

    /**
     * 数据状态用于业务处理
     */
    private int state = 0;


    /**
     * 是否被唤醒
     * @return true 是，false，否
     */
    public boolean isNotify() {
        return isNotify;
    }


    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    public IBack getBack() {
        return back;
    }

    public void setBack(IBack back) {
        this.back = back;
    }

    public Task() {
        lock = new ReentrantLock();
        condition = lock.newCondition();
    }

    public void remove(){
        ConditionUtils.getInstance().removeKey(getKey());
    }


    public void signalTask() {
        try {
            lock.lock();
            //notify();
            condition.signal();
        } finally {
            lock.unlock();
        }
        isNotify= true;
    }

    public void awaitTask() {
        try {
            lock.lock();
            condition.await();
        } catch (Exception e) {
        } finally {
            lock.unlock();
        }
    }

    public void awaitTask(IBack back) {
        try {
            lock.lock();
            back.doing();
            condition.await();
        } catch (Throwable e) {
        } finally {
            lock.unlock();
        }
    }
}
