package com.network.protobuf;

public abstract class Task<R> implements Runnable {
	TaskAction<R> ta;

	abstract R execure();

	@Override
	public void run() {
		try {
			ta.result.add(execure());
		} catch (Exception e) {
			// 不管成功或失败，最后是一定要有汇总结果返回的
			e.printStackTrace();
		}
		if (ta.cc.incrementAndGet() == ta.size()) {
			// 所有任务子线程已完成
			/**
			 * 标号002, 模拟任务子线程比较耗时，同步汇总线程早已进入等待结果集状态。 (标号001和标号002的地方请放开一块，另一块注释掉，分别模拟两种不同情况)
			 */
			/*
			 * try { Thread.sleep(10000); } catch (InterruptedException e1) {
			 * e1.printStackTrace(); }
			 */
			try {
				ta.lock.lock();
				if (!ta.waitResult) {
					// 同步汇总线程未进入等待结果的状态,等待同步汇总线程进入结果集等待状态
					ta.waitResultCondition.await();
				}
				ta.complementCondition.signal();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				ta.lock.unlock();
			}
		}
	}
}