package com.network.protobuf;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TaskAction<R> extends ArrayList<Task<R>> {
	private static final long serialVersionUID = 1L;
	// 同步汇总线程是否进入结果集等待状态
	boolean waitResult = false;

	Lock lock = new ReentrantLock();
	// Condition效率比obj.wait()、obj.notify()高
	Condition complementCondition = lock.newCondition();
	Condition waitResultCondition = lock.newCondition();

	// 最终汇总的结果集
	Vector<R> result = new Vector<R>();
	// 子线程已完成数量，AtomicInteger是原子性操作，线程安全
	AtomicInteger cc = new AtomicInteger(0);

	/**
	 * 为了可以addTask(t0).addTask(t1).addTask(t2)...
	 * 
	 * @param task
	 * @return
	 */
	public TaskAction<R> addTask(Task<R> task) {
		super.add(task);
		return this;
	}

	public List<R> doTasks() {

		for (int i = 0; i < this.size(); i++) {
			Task<R> task = get(i);
			task.ta = this;
			new Thread(task).start();
		}

		/**
		 * 标号001, 模拟同步汇总线程比较倒霉，一直未得到cpu时间片，还未进入结果集等待状态，而所有任务子线程已经执行完毕了。 *
		 * (标号001和标号002的地方请放开一块，另一块注释掉，分别模拟两种不同情况)
		 */
		/*
		 * try { Thread.sleep(20000); } catch (InterruptedException e1) {
		 * e1.printStackTrace(); }
		 */

		try {
			lock.lock();
			waitResult = true;
			waitResultCondition.signal();
			complementCondition.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
		return result;
	}

}
