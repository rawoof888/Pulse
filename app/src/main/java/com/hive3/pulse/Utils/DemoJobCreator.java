package com.hive3.pulse.Utils;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;


public class DemoJobCreator implements JobCreator {
    @Override
    public Job create(String tag) {
        switch (tag) {
            case ScrapeJob.TAG:
                return new ScrapeJob();
            default:
                return null;
        }
    }


}


