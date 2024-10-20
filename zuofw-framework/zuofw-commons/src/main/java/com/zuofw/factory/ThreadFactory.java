package com.zuofw.factory;



import com.zuofw.factory.core.FutureOperation;
import com.zuofw.factory.core.ThreadOperation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 线程工厂类
 * 使用方法：
 * 1. ThreadFactory.newThread(() -> {});
 * 2. ThreadFactory.addThreadPool(() -> {});
 * 3. FutureTask<T> futureTask = ThreadFactory.init().submitOne(() -> {});
 * 4. List<FutureTask<T>> futureTaskList = ThreadFactory.init().submitMany(() -> {}).submitMany(() -> {}).submitMany(() -> {}).getFutureTaskList();
 * 5. List<FutureTask<T>> futureTaskList = ThreadFactory.init().submitMany(() -> {}).submitMany(() -> {}).submitMany(() -> {}).shutdown().getFutureTaskList();
 * 6. FutureTask<T> futureTask = ThreadFactory.init().executeOne(() -> {});
 * 7. List<FutureTask<T>> futureTaskList = ThreadFactory.init().executeMany(() -> {}).executeMany(() -> {}).executeMany(() -> {}).getFutureTaskList();
 * 8. List<FutureTask<T>> futureTaskList = ThreadFactory.init().executeMany(() -> {}).executeMany(() -> {}).executeMany(() -> {}).shutdown().getFutureTaskList();
 *
 * @author tangchao
 */
public class ThreadFactory<T> {

    /**
     * 核心线程数
     */
    private static final int corePoolSize = 5;

    /**
     * 最大线程数
     */
    private static final int maxPoolSize = 20;

    /**
     * 空闲线程等待时间
     */
    private static final int keepAliveTime = 10;

    /**
     * 空闲线程等待时间单位
     */
    private static final TimeUnit timeUnit = TimeUnit.SECONDS;

    /**
     * 工作队列大小
     */
    private static final int workQueueSize = 10000;

    /**
     * 初始化线程池的个数
     */
    private static final int initThreadPoolNum = 1;

    /**
     * 每个线程池中最大的排队数，计算方法是工作队列大小的90% + 线程池最大线程数的一半
     */
    private static final int maxQueueSize = (int) (workQueueSize * 0.9 + maxPoolSize / 2);

    /**
     * 线程池列表
     */
    private static List<ThreadPoolExecutor> threadPoolExecutorList = new ArrayList<>();
    
    /**
     * 定义http命令运行池
     */
    public static ThreadPoolExecutor HTTP_CALL_COMMAND_RUNPOOL = new ThreadPoolExecutor(
            corePoolSize, maxPoolSize, keepAliveTime, timeUnit,
            new ArrayBlockingQueue<>(workQueueSize), new ThreadPoolExecutor.DiscardPolicy());
    
    /**
     * 定义shell命令运行池
     */
    public static ThreadPoolExecutor SHELL_COMMAND_RUNPOOL = new ThreadPoolExecutor(
            corePoolSize, maxPoolSize, keepAliveTime, timeUnit,
            new ArrayBlockingQueue<>(workQueueSize), new ThreadPoolExecutor.DiscardPolicy());

    static {
        for (int i = 1; i <= initThreadPoolNum; i++) {
            threadPoolExecutorList.add(new ThreadPoolExecutor(
                    corePoolSize, maxPoolSize, keepAliveTime, timeUnit,
                    new ArrayBlockingQueue<>(workQueueSize), new ThreadPoolExecutor.DiscardPolicy()));
        }
    }

    /**
     * 创建一个兜底线程池
     * 如果线程池队列满了，走丢弃策略
     */
    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            corePoolSize, maxPoolSize, keepAliveTime, timeUnit,
            new ArrayBlockingQueue<>(workQueueSize), new ThreadPoolExecutor.DiscardPolicy());

    /**
     * 新建一个线程并启动执行
     *
     * @param threadOperation
     */
    public static void newThread(ThreadOperation threadOperation) {
        new Thread(() -> {
            threadOperation.accept();
        }).start();
    }

    /**
     * 往线程池中加一个执行任务
     *
     * @param threadOperation
     */
    public static void addThreadPool(ThreadOperation threadOperation) {
        boolean isInsert = false;
        for (ThreadPoolExecutor poolExecutor : threadPoolExecutorList) {
            // 判断线程池里面的队列是否小于最大排列队列
            if (poolExecutor.getQueue().size() < maxQueueSize) {
                poolExecutor.execute(() -> {
                    threadOperation.accept();
                });
                isInsert = true;
                break;
            }
        }
        if (!isInsert) {
            threadPoolExecutor.execute(() -> {
                threadOperation.accept();
            });
        }
    }

    private ExecutorService futureTaskPool;
    private List<CompletableFuture<T>> futureTaskList;

    /**
     * @param configMaxPoolSize   最大线程大小
     * @param configWorkQueueSize 队列大小
     */
    public ThreadFactory(Integer configMaxPoolSize, Integer configWorkQueueSize, Integer configCorePoolSize) {
        if (configMaxPoolSize == null) {
            configMaxPoolSize = maxPoolSize;
        }
        if (configWorkQueueSize == null) {
            configWorkQueueSize = workQueueSize;
        }
        if (configCorePoolSize == null) {
            configCorePoolSize = corePoolSize;
        }
        this.futureTaskPool = new ThreadPoolExecutor(
                configCorePoolSize, configMaxPoolSize, keepAliveTime, timeUnit,
                new ArrayBlockingQueue<>(configWorkQueueSize), new ThreadPoolExecutor.DiscardPolicy());
        this.futureTaskList = new ArrayList<>();
    }

    /**
     * 初始化一个等待响应的线程池
     *
     * @return
     */
    public static <T> ThreadFactory<T> init() {
        ThreadFactory<T> threadFactory = new ThreadFactory(null, null, null);
        return threadFactory;
    }

    /**
     * 初始化一个等待响应的线程池
     *
     * @param maxPoolSize 最大线程大小
     * @return
     */
    public static <T> ThreadFactory<T> init(int maxPoolSize) {
        ThreadFactory<T> threadFactory = new ThreadFactory(maxPoolSize, null, null);
        return threadFactory;
    }

    /**
     * 初始化一个等待响应的线程池
     *
     * @param maxPoolSize   最大线程大小
     * @param workQueueSize 队列大小
     * @return
     */
    public static <T> ThreadFactory<T> init(int maxPoolSize, int workQueueSize) {
        ThreadFactory<T> threadFactory = new ThreadFactory(maxPoolSize, workQueueSize, null);
        return threadFactory;
    }

    public static <T> ThreadFactory<T> init(int maxPoolSize, int workQueueSize, int corePoolSize) {
        ThreadFactory<T> threadFactory = new ThreadFactory(maxPoolSize, workQueueSize, corePoolSize);
        return threadFactory;
    }

    /**
     * 提交一个任务并响应
     *
     * @param futureOperation
     * @return
     */
    public CompletableFuture<T> submitOne(FutureOperation futureOperation) {
        CompletableFuture<T> futureTask = CompletableFuture.supplyAsync(() -> (T) futureOperation.accept(), this.futureTaskPool);
        this.futureTaskPool.shutdown();
        return futureTask;
    }

    /**
     * 提交多个任务
     *
     * @param futureOperation
     * @return
     */
    public ThreadFactory<T> submitMany(FutureOperation futureOperation) {
        CompletableFuture<T> futureTask = CompletableFuture.supplyAsync(() -> (T) futureOperation.accept(), this.futureTaskPool);
        this.futureTaskList.add(futureTask);
        return this;
    }

    /**
     * 立即执行一个任务并响应
     *
     * @param futureOperation
     * @return
     */
    public CompletableFuture<T> executeOne(FutureOperation futureOperation) {
        CompletableFuture<T> futureTask = new CompletableFuture<>();
        futureTask.complete((T) futureOperation.accept());
        this.futureTaskPool.shutdown();
        return futureTask;
    }

    /**
     * 立即执行多个任务
     *
     * @param futureOperation
     * @return
     */
    public ThreadFactory<T> executeMany(FutureOperation futureOperation) {
        CompletableFuture<T> futureTask = new CompletableFuture<>();
        futureTask.complete((T) futureOperation.accept());
        this.futureTaskList.add(futureTask);
        return this;
    }

    /**
     * 获取任务池
     *
     * @return
     */
    public ExecutorService taskPool() {
        return this.futureTaskPool;
    }

    /**
     * 多个任务添加完成的时候待响应
     *
     * @return
     */
    public ThreadFactory<T> shutdown() {
        this.futureTaskPool.shutdown();
        return this;
    }

    /**
     * 等待结束后获取响应结果
     *
     * @return
     */
    public List<CompletableFuture<T>> getFutureTaskList() {
        return this.futureTaskList;
    }

}
