package com.hive3.pulse.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.evernote.android.job.Job;
import com.hive3.pulse.ActivityMain;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class ScrapeJob extends Job {

    public static final String TAG = "job_scrape_tag";

    @Override
    @NonNull
    protected Result onRunJob(Params params) {
        // run your job

        if (isOnline()){
            StudentData studentData = new StudentData();
            studentData.execute();
        }

        return Result.SUCCESS;
    }


    public  boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) MyApplication.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
            return false;
        }
        return true;
    }




    public static class StudentData extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {

            try {

                //---------Connection to the links are Started-------------//


                ActivityMain.sessionID.clear();

                ArrayList<String> cookies = ActivityMain.tinyDB.getListString("cookies");
                ArrayList<String> cookiesData = ActivityMain.tinyDB.getListString("cookiesData");

                for (int i = 0; i < cookies.size(); i++) {
                    for (int j = 0; j < cookiesData.size(); j++) {
                        ActivityMain.sessionID.put(cookies.get(i), cookiesData.get(j));
                    }
                }


                ActivityMain.login3StudentReport = Jsoup.connect("https://academia.srmuniv.ac.in/liveViewHeader.do")
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                        .ignoreHttpErrors(true)
                        .cookies(ActivityMain.sessionID)
                        .method(Connection.Method.POST)
                        .data("appLinkName", "academia-academic-services")
                        .data("sharedBy", "srm_university")
                        .data("isPageLoad", "true")
                        .data("viewLinkName", "Student_Report")
                        .timeout(10000)
                        .execute();

                ActivityMain.login4Attendance = Jsoup.connect("https://academia.srmuniv.ac.in/liveViewHeader.do")
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                        .ignoreHttpErrors(true)
                        .cookies(ActivityMain.sessionID)
                        .method(Connection.Method.POST)
                        .data("appLinkName", "academia-academic-services")
                        .data("sharedBy", "srm_university")
                        .data("isPageLoad", "true")
                        .data("viewLinkName", "My_Attendance")
                        .timeout(10000)
                        .execute();

                //---------Connection to the links are completed-------------//
                //---------Parse the data from the links started-------------//


                if (ActivityMain.sessionID.get("clientauthtoken") == null) {
                    // Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_LONG).show();
                } else {

                    //--------record extraction starts ----------//


                    //--------record extraction Ends ----------//


                    //--------Course extraction STARTS--------//


                    //----- TABLE0 ----------------------------//

                    Elements attendanceData = ActivityMain.login4Attendance.parse().getAllElements();
                    Elements attendance_data = attendanceData.select("div[class=mainDiv]");
                    Elements data = attendance_data.select("table");

                    Elements sd_table0 = data.select("table[border=0]");


                    Elements sd_table0Row = sd_table0.select("tr");


                    for (Element element : sd_table0Row) {

                        Elements trs1 = element.select("tr");
                        ActivityMain.studentDataTable0.add(trs1.select("td").remove(0).text());
                        ActivityMain.studentDataTable0Info.add(trs1.select("td").select("strong").text());

                    }


                    //----- TABLE0 ENDS----------------------------//

                    //----- TABLE1 STARTS ----------------------------//

                    Element sd_table1 = data.select("table").get(1);
                    Elements table1_row = sd_table1.select("tr");

                    ArrayList[] lists = new ArrayList[table1_row.size()];
                    ActivityMain.NO_OF_COURSES = table1_row.size() - 1;
                    for (int i = 0; i < table1_row.size(); i++) {
                        Element row = table1_row.get(i);
                        Elements cols = row.select("td");
                        lists[i] = new ArrayList<>();
                        for (Element col : cols) {
                            lists[i].add(col.text());
                        }
                    }

                    ActivityMain.courseCodes.clear();
                    ActivityMain.coursePercentages.clear();
                    ActivityMain.courseTitles.clear();
                    for (int i = 1; i < lists.length; i++) {

                        ActivityMain.courseCodes.add(lists[i].get(0).toString());
                        ActivityMain.courseTitles.add(lists[i].get(1).toString());
                        ActivityMain.coursePercentages.add(lists[i].get(10).toString());

                    }


                    //----- TABLE1 ENDS----------------------------//

                    //----- TABLE2 STARTS----------------------------//

                    Element sd_table2 = data.select("table").get(2);
                    Elements table2_row = sd_table2.select("tr");

                    for (int i = 1; i < 2; i++) {
                        Element row = table2_row.get(i);
                        Elements score_table = row.select("table");
                        Elements score_table_row = score_table.select("tr");
                        Elements score_table_col = score_table_row.select("td");


                        for (Element element : score_table_col) {
                            lists[0].add(element.select("strong").text());

                        }

                    }


                    for (int i = 1; i < table1_row.size(); i++) {
                        Element t2_row = sd_table2.select("tr:not(:has(table))").get(i);
                        Elements cols = t2_row.select("td");

                        for (Element col : cols) {
                            col.select("strong").remove();
                            lists[i].add(col.text().replace("\u00a0", ""));
                        }

                    }


                    //----- TABLE2 ENDS----------------------------//


                    //--------Course extraction ENDS--------//

                    ActivityMain.courseLists.clear();
                    for (int i = 0; i < lists.length; i++) {
                        ActivityMain.courseLists.add(lists[i]);
                        ActivityMain.tinyDB.remove("course" + Integer.toString(i));
                    }


                    int COURSES = 0;
                    for (ArrayList<String> list : ActivityMain.courseLists) {
                        ActivityMain.tinyDB.putListString("course" + COURSES, list);
                        COURSES++;
                    }


                    //-----put data in tinyData-----//
                    ActivityMain.tinyDB.remove("courseTitles");
                    ActivityMain.tinyDB.remove("coursePercentages");
                    ActivityMain.tinyDB.remove("courseListsSize");

                    ActivityMain.tinyDB.putInt("courseListsSize", ActivityMain.courseLists.size());
                    ActivityMain.tinyDB.putListString("courseTitles", ActivityMain.courseTitles);
                    ActivityMain.tinyDB.putListString("coursePercentages", ActivityMain.coursePercentages);


                }


                //---------Parsing the data from the links is completed-------------//


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }


    }


}



