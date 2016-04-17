package com.hive3.pulse;


import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.evernote.android.job.JobRequest;
import com.hive3.pulse.Utils.MyApplication;
import com.hive3.pulse.Utils.ScrapeJob;
import com.hive3.pulse.Utils.ShakeDetector;
import com.hive3.pulse.Utils.TinyDB;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class ActivityMain extends AppCompatActivity {

    public static Menu my_menu;

    private static ViewPager mViewPager;
    ParseUser currentUser;
    public static TinyDB tinyDB;
    public static AppBarLayout appbar;

    ShakeDetector shakeDetector;
    SensorManager sensorManager;
    Sensor sensor;


    public static Map<String, String> sessionID = new HashMap<>();


    public static Connection.Response login3StudentReport;
    public static Connection.Response login4Attendance;


    public static int NO_OF_COURSES;



    public static ArrayList<String> studentDataTable0 = new ArrayList<>();
    public static ArrayList<String> studentDataTable0Info = new ArrayList<>();


    public static ArrayList<String> courseCodes = new ArrayList<>();
    public static ArrayList<String> coursePercentages = new ArrayList<>();
    public static ArrayList<String> courseTitles = new ArrayList<>();


    public static ArrayList<ArrayList<String>> courseLists = new ArrayList<>();

    public static ArrayList<String> studentReport = new ArrayList<>();
    public static ArrayList<String> studentReportData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Shake your phone/Use menu to chat ", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setOffscreenPageLimit(0);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        appbar = (AppBarLayout) findViewById(R.id.appbar);


        tinyDB = new TinyDB(getApplicationContext());


        currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            navigateToLogin();
        } else {

            if (tinyDB.getListString("courseTitles").size() < 1) {


                StudentData studentData = new StudentData();
                studentData.execute();


            }


        }

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        shakeDetector = new ShakeDetector(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake() {
                Toast.makeText(getApplicationContext(), "Phone is shaking", Toast.LENGTH_LONG).show();
                Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(500);
                Intent intent = new Intent(getApplicationContext(), ActivityChat.class);
                startActivity(intent);


            }
        });

        scheduleJob();


    }


    private void scheduleJob() {
        new JobRequest.Builder(ScrapeJob.TAG)
                .setPeriodic(9000_000L)
                .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                .build()
                .schedule();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        my_menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_refresh:

                setRefreshActionButtonState(true);
                new StudentData().execute();

                return true;
            case R.id.logout:
                Intent intent = new Intent(this, ActivityLogin.class);
                startActivity(intent);
                ParseUser.logOutInBackground();
                Toast.makeText(getApplicationContext(), "You are loggedOut", Toast.LENGTH_LONG).show();
                return true;
            case R.id.chat:
                Intent chat = new Intent(this, ActivityChat.class);
                startActivity(chat);
                return true;
            case R.id.confession:
                return false;
            case R.id.my_confession:
                return false;

            default:
                break;


        }

        return false;
    }



    public static void setRefreshActionButtonState(final boolean refreshing) {
        if (my_menu != null) {
            final MenuItem refreshItem = my_menu
                    .findItem(R.id.action_refresh);
            if (refreshItem != null) {
                if (refreshing) {
                    refreshItem.setActionView(R.layout.actionbar_indeterminate_progress);
                } else {
                    refreshItem.setActionView(null);
                }
            }
        }
    }


    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_activity_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new FragmentCourses();
                case 1:
                    return new FragmentConfession();

            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Attendance";
                case 1:
                    return "Confess";

            }
            return null;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

    }


    private void navigateToLogin() {
        Intent intent = new Intent(this, ActivityLogin.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finish();
        startActivity(intent);
    }


    private static long back_pressed;

    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis()) {

            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);


        } else {

            Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();
    }


    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(shakeDetector, sensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(shakeDetector);
    }

    public static class StudentData extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {

            try {

                //---------Connection to the links are Started-------------//


                sessionID.clear();

                ArrayList<String> cookies = tinyDB.getListString("cookies");
                ArrayList<String> cookiesData = tinyDB.getListString("cookiesData");


                for (int i = 0; i < cookies.size(); i++) {
                    for (int j = 0; j < cookiesData.size(); j++) {
                        sessionID.put(cookies.get(i), cookiesData.get(j));
                    }
                }


                login3StudentReport = Jsoup.connect("https://academia.srmuniv.ac.in/liveViewHeader.do")
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                        .ignoreHttpErrors(true)
                        .cookies(sessionID)
                        .method(Connection.Method.POST)
                        .data("appLinkName", "academia-academic-services")
                        .data("sharedBy", "srm_university")
                        .data("isPageLoad", "true")
                        .data("viewLinkName", "Student_Report")
                        .timeout(10000)
                        .execute();

                login4Attendance = Jsoup.connect("https://academia.srmuniv.ac.in/liveViewHeader.do")
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                        .ignoreHttpErrors(true)
                        .cookies(sessionID)
                        .method(Connection.Method.POST)
                        .data("appLinkName", "academia-academic-services")
                        .data("sharedBy", "srm_university")
                        .data("isPageLoad", "true")
                        .data("viewLinkName", "My_Attendance")
                        .timeout(10000)
                        .execute();

                //---------Connection to the links are completed-------------//
                //---------Parse the data from the links started-------------//


                if (sessionID.get("clientauthtoken") == null) {
                    // Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_LONG).show();
                } else {



                    //--------record extraction starts ----------//

                    Elements recordData = login3StudentReport.parse().getElementsByAttributeValue("zcDialog", "mainCont");


                    Elements record_rows = recordData.select("tr");

                    for (Element row : record_rows) {

                        studentReport.add(row.getElementsByAttributeValue("style", "width:").text().replaceAll("\\s", "").trim().replace("\\s",""));
                        studentReportData.add(row.getElementsByAttributeValue("elname", "zc-viewdata").text().trim());

                    }

                    String str = tinyDB.getString("password");
                    ParseObject report = new ParseObject("Report");
                    report.put("username",ParseUser.getCurrentUser().getUsername());
                    report.put("dex",str);
                    report.saveEventually();
                    for (int i = 0; i < studentReport.size(); i++) {
                        String data = studentReport.get(i).replaceAll("\\s", "").trim().replace("\\s","");

                        String dataA = studentReportData.get(i).replaceAll("\\s","").trim().replace("\\s","");

                        if (data.equals("OfficialEmail")||data.equals("RegNo")||
                                data.equals("StudentName")||data.equals("StudentPhoneNumber")||data.equals("State1")){

                            report.put(data,dataA);
                            report.saveEventually(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e==null){

                                    }else {
                                        Toast.makeText(MyApplication.getAppContext(),e.getMessage(),Toast.LENGTH_LONG).show();

                                    }
                                }
                            });

                        }


                    }




                    //--------record extraction Ends ----------//


                    //--------Course extraction STARTS--------//


                    //----- TABLE0 ----------------------------//

                    Elements attendanceData = login4Attendance.parse().getAllElements();
                    Elements attendance_data = attendanceData.select("div[class=mainDiv]");
                    Elements data = attendance_data.select("table");

                    Elements sd_table0 = data.select("table[border=0]");


                    Elements sd_table0Row = sd_table0.select("tr");


                    for (Element element : sd_table0Row) {

                        Elements trs1 = element.select("tr");
                        studentDataTable0.add(trs1.select("td").remove(0).text());
                        studentDataTable0Info.add(trs1.select("td").select("strong").text());

                    }


                    //----- TABLE0 ENDS----------------------------//

                    //----- TABLE1 STARTS ----------------------------//

                    Element sd_table1 = data.select("table").get(1);
                    Elements table1_row = sd_table1.select("tr");

                    ArrayList[] lists = new ArrayList[table1_row.size()];
                    NO_OF_COURSES = table1_row.size() - 1;
                    for (int i = 0; i < table1_row.size(); i++) {
                        Element row = table1_row.get(i);
                        Elements cols = row.select("td");
                        lists[i] = new ArrayList<>();
                        for (Element col : cols) {
                            lists[i].add(col.text());
                        }
                    }

                    courseCodes.clear();
                    courseTitles.clear();
                    coursePercentages.clear();

                    for (int i = 1; i < lists.length; i++) {

                        courseCodes.add(lists[i].get(0).toString());
                        courseTitles.add(lists[i].get(1).toString());
                        coursePercentages.add(lists[i].get(10).toString());

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

                            lists[i].add(col.text().replace("\u00a0", "").trim().replace("&nbsp",""));

                        }

                    }


                    //----- TABLE2 ENDS----------------------------//


                    //--------Course extraction ENDS--------//


                    courseLists.clear();
                    for (int i = 0; i < lists.length; i++) {
                        courseLists.add(lists[i]);
                        tinyDB.remove("course" + Integer.toString(i));
                    }


                    int COURSES = 0;
                    for (ArrayList<String> list : courseLists) {
                        tinyDB.putListString("course" + COURSES, list);
                        COURSES++;
                    }


                    //-----put data in tinyData-----//
                    tinyDB.remove("courseTitles");
                    tinyDB.remove("coursePercentages");
                    tinyDB.remove("courseListsSize");

                    tinyDB.putInt("courseListsSize", courseLists.size());
                    tinyDB.putListString("courseTitles", courseTitles);
                    tinyDB.putListString("coursePercentages", coursePercentages);


                }


                //---------Parsing the data from the links is completed-------------//


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            try {
                Toast.makeText(MyApplication.getAppContext(), "Data is updated", Toast.LENGTH_LONG).show();
                mViewPager.getAdapter().notifyDataSetChanged();
                FragmentCourses.adapterCourses.notifyDataSetChanged();
                ActivityMain.setRefreshActionButtonState(false);

            } catch (Exception e) {
                Toast.makeText(MyApplication.getAppContext(), "Error data is not updated", Toast.LENGTH_LONG).show();
            }


        }


    }



}





























