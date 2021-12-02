package com.nrlm.lakhpatikisaan.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppExecutor {

    private static AppExecutor instance;

    public static AppExecutor getInstance(){
        if(instance == null){
            instance = new AppExecutor();
        }
        return instance;
    }

    private final ExecutorService executorService = Executors.newFixedThreadPool(4);


    public ExecutorService threadExecutor(){
        return executorService;
    }
}
