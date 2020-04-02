package com.eng.momen.ncitlogin;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecuters {

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static AppExecuters sInstance;
    private final Executor diskIO;
    private final Executor mainThread;
    private final Executor networkIO;


    public AppExecuters(Executor diskIO, Executor mainThread, Executor networkIO) {
        this.diskIO = diskIO;
        this.mainThread = mainThread;
        this.networkIO = networkIO;
    }

    public static AppExecuters getsInstance() {
        if (sInstance == null){
            synchronized (LOCK){
                sInstance = new AppExecuters(Executors.newSingleThreadExecutor(),
                        Executors.newFixedThreadPool(3),
                        new MainThreadExecuter());
            }
        }
        return sInstance;
    }

    private static class MainThreadExecuter implements Executor {

        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());
        @Override
        public void execute(Runnable command) {
            mainThreadHandler.post(command);
        }
    }
    public Executor diskIO(){return diskIO;}
    public Executor mainThread(){return mainThread;}
    public Executor networkIO(){return networkIO;}

}
