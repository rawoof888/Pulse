package com.hive3.pulse.Utils;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.hive3.pulse.ActivityMain;

public class UpdateTask extends AsyncTask<Void, Void, Void> {



    private Context mCon;

    public UpdateTask(Context con)
    {
        mCon = con;
    }

    @Override
    protected Void doInBackground(Void... nope) {
        try {
            // Set a time to simulate a long update process.
            Thread.sleep(4000);

            return null;

        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Void nope) {
        // Give some feedback on the UI.
        Toast.makeText(mCon, "Finished complex background function!",
                Toast.LENGTH_LONG).show();

        // Change the menu back
       // ((ActivityMain) mCon).resetUpdating();
        ((ActivityMain) mCon).setRefreshActionButtonState(false);


    }
}






