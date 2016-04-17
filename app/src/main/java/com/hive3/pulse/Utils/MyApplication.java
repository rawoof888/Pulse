package com.hive3.pulse.Utils;


import android.app.Application;
import android.content.Context;

import com.evernote.android.job.JobManager;
import com.parse.Parse;


public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "x4wsOqibu7E7Eq9F0xZhk9XZ3yY4Mnn86ni1qlJd", "laN0pawbK1whTkRi89fomP82EHMzVh8u7zbL6zjE");



        JobManager.create(this).addJobCreator(new DemoJobCreator());

        MyApplication.context = getApplicationContext();



    }

    public static Context getAppContext() {
        return MyApplication.context;
    }





}
