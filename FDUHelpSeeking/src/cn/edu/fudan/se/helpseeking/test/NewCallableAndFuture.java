package cn.edu.fudan.se.helpseeking.test;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

//使用Callable和Future，通过ExecutorService的submit方法执行Callable，并返回Future，代码如下：
//ExecutorService继承自Executor，它的目的是为我们管理Thread对象，从而简化并发编程，
//Executor使我们无需显示的去管理线程的生命周期，是JDK 5之后启动任务的首选方式。

public class NewCallableAndFuture {
	
		public static void main(String[] args) {
			ExecutorService threadPool = Executors.newSingleThreadExecutor();
			Future<Integer> future = threadPool.submit(new Callable<Integer>() {
				public Integer call() throws Exception {
					return new Random().nextInt(100);
				}
			});
			try {
				Thread.sleep(5000);// 可能做一些事情
				System.out.println(future.get());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}

}
