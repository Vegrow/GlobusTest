package com.litvinov.globustest.util;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutors {

    public static final int THREAD_COUNT = 3;
    private static AppExecutors instance;
    private final Executor diskIO;
    private final Executor networkIO;
    private final Executor mainThread;

    private AppExecutors(Executor diskIO, Executor networkIO, Executor mainThread) {
        this.diskIO = diskIO;
        this.networkIO = networkIO;
        this.mainThread = mainThread;
    }

    private AppExecutors() {
        this(Executors.newSingleThreadExecutor(), Executors.newSingleThreadExecutor(),
                new MainThreadExecutor());
    }

    public static AppExecutors getInstance() {
        AppExecutors localInstance = instance;
        if (localInstance == null) {
            synchronized (AppExecutors.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new AppExecutors();
                }
            }
        }
        return localInstance;
    }

    public Executor getDiskIO() {
        return diskIO;
    }

    public Executor getNetworkIO() {
        return networkIO;
    }

    public Executor getMainThread() {
        return mainThread;
    }

    public static class MainThreadExecutor implements Executor{

        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable runnable) {
            mainThreadHandler.post(runnable);
        }
    }
}
