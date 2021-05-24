package com.algubra.activity.calendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.algubra.R;
import com.algubra.activity.calendar.adapter.CalendarListAdapter;
import com.algubra.activity.calendar.adapter.CustomCalendarSpinnerAdapter;
import com.algubra.activity.calendar.model.CalendarModel;
import com.algubra.activity.circulars.CircularActivity;
import com.algubra.activity.clubs.adapter.ClubListAdapter;
import com.algubra.activity.clubs.model.ClubModels;
import com.algubra.activity.home.HomeActivity;
import com.algubra.activity.login.LoginActivity;
import com.algubra.activity.pdf.PDFViewActivity;
import com.algubra.activity.pdf.PdfReader;
import com.algubra.constants.JsonTagConstants;
import com.algubra.constants.StausCodes;
import com.algubra.constants.URLConstants;
import com.algubra.manager.AppPreferenceManager;
import com.algubra.manager.AppUtilityMethods;
import com.algubra.manager.HeaderManager;
import com.algubra.volleymanager.VolleyAPIManager;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by gayatri on 5/5/17.
 */
public class CalendarActivity extends Activity implements JsonTagConstants, URLConstants, StausCodes {
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back;
    Context mContext = this;
    ArrayList<CalendarModel> calendarModelArrayList = new ArrayList<>();
    ListView dayListView;
    ListView monthListView;
    ListView yearListView;
    Activity activity;
    List<String> monthValues = new ArrayList<String>();
    List<String> monthFullStringValues = new ArrayList<String>();
    List<String> dayValues = new ArrayList<String>();
    int mPosMonth = 0;
    int mPosYear = 0;
    int dayPosition = -1;
    CustomCalendarSpinnerAdapter daydataAdapter;
    CustomCalendarSpinnerAdapter monthdataAdapter;
    CustomCalendarSpinnerAdapter dataAdapter;
    private TextView monthSpinner;
    private TextView yearSpinner;
    private TextView daySpinner;
    //    private TextView clearData;
    private ImageView clearData;
    private boolean monthSpinSelect = true;
    private boolean yearSpinSelect = true;
    private boolean daySpinSelect = true;
    private String selectedMonth;
    private String selectedMonthFullString;
    private int selectedMonthId;
    private String selectedYear;
    private RelativeLayout relMain;
    private RelativeLayout commonRelList;
    List<String> yearValues = new ArrayList<String>();
    ArrayList<CalendarModel> tempArrayList = new ArrayList<CalendarModel>();
    ArrayList<CalendarModel> modelArrayList;
    private TextView mDateTxt;
    private TextView monthText;
    private TextView yearText;
    private TextView mMnthTxt;
    private TextView mYearTxt;
    private ListView mCalendarList;
    int mnthId;
    private RelativeLayout mDateRelLayout;
    private int month;
    JSONArray events;
    private Calendar mCalendar;
    String myFormatCalender = "yyyy-MM-dd";
    SimpleDateFormat sdfcalender;
    RelativeLayout relDate;
    ImageView pdfDownload;
    String isgCalendarPdf = "";
    String isgIntCalendarPdf = "";
    boolean isAvailable = false;
    //calendar new changes
    ArrayList<CalendarModel> eventsArray;
    ArrayList<CalendarModel> dateArraylist;

    List<String> dates;
    ArrayList<String> datesFinal;
    int position;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        initUI();
        resetDisconnectTimer();
        myFormatCalender = "yyyy-MM-dd";
        sdfcalender = new SimpleDateFormat(myFormatCalender);
        if (AppUtilityMethods.isNetworkConnected(mContext)) {
            callCalendarListAPI(URL_GETCALENDAR);
        } else {
            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
        }


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initUI() {
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        headermanager = new HeaderManager(CalendarActivity.this, "Calendar");
        commonRelList = (RelativeLayout) findViewById(R.id.commonRelList);
        dayListView = (ListView) findViewById(R.id.dayListView);
        monthListView = (ListView) findViewById(R.id.monthListView);
        yearListView = (ListView) findViewById(R.id.yearListView);
        relDate = (RelativeLayout) findViewById(R.id.relDate);
        mCalendarList = (ListView) findViewById(R.id.calList);
        mDateRelLayout = (RelativeLayout) findViewById(R.id.dateRel);
        mDateTxt = (TextView) findViewById(R.id.dateText);
        monthText = (TextView) findViewById(R.id.monthText);
        yearText = (TextView) findViewById(R.id.yearText);
        mMnthTxt = (TextView) findViewById(R.id.mnthTxt);
        mYearTxt = (TextView) findViewById(R.id.yearTxt);
        monthSpinner = (TextView) findViewById(R.id.monthSpinner);
        yearSpinner = (TextView) findViewById(R.id.yearSpinner);
        daySpinner = (TextView) findViewById(R.id.daySpinner);
//        clearData = (TextView) findViewById(R.id.clearData);
        clearData = (ImageView) findViewById(R.id.clearData);
        //new calendar change
        eventsArray = new ArrayList<>();
        dateArraylist = new ArrayList<>();
        dates = new ArrayList<>();
        datesFinal = new ArrayList<>();

        if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")) {
            headermanager.getHeader(relativeHeader, 1);
            relDate.setBackgroundColor(getResources().getColor(R.color.login_button_bg));
            clearData.setBackgroundColor(getResources().getColor(R.color.calendar_selector));
            daySpinner.setBackgroundDrawable(getResources().getDrawable(R.drawable.dropnew));
            monthSpinner.setBackgroundDrawable(getResources().getDrawable(R.drawable.dropnew));
            yearSpinner.setBackgroundDrawable(getResources().getDrawable(R.drawable.dropnew));

        } else {
            headermanager.getHeader(relativeHeader, 3);
            relDate.setBackgroundColor(getResources().getColor(R.color.isg_int_blue));
            clearData.setBackgroundColor(getResources().getColor(R.color.calendar_blue_selector));
            daySpinner.setBackgroundDrawable(getResources().getDrawable(R.drawable.dropnewblue));
            monthSpinner.setBackgroundDrawable(getResources().getDrawable(R.drawable.dropnewblue));
            yearSpinner.setBackgroundDrawable(getResources().getDrawable(R.drawable.dropnewblue));
        }
        back = headermanager.getLeftButton();

        pdfDownload = headermanager.getRightButton();
        pdfDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")) {
//                    downloadPdf(isgCalendarPdf);
           /*         Intent intent = new Intent(CalendarActivity.this, PdfReader.class);
                    intent.putExtra("pdf_url",isgCalendarPdf);
                    startActivity(intent);*/
//                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(isgCalendarPdf));
//                    startActivity(browserIntent);
                    if (!(isgCalendarPdf.equalsIgnoreCase(""))) {
                        Intent browserIntent = new Intent(CalendarActivity.this, PDFViewActivity.class);
                        browserIntent.putExtra("pdf_url", isgCalendarPdf);
                        browserIntent.putExtra("title", "Calendar");
                        browserIntent.putExtra("filename", "Calendar");
                        startActivity(browserIntent);
                    } else {
                        AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.nomoreinfo), R.drawable.exclamationicon, R.drawable.roundblue);

                    }

                } else {
//                    downloadPdf(isgIntCalendarPdf);
                   /* Intent intent = new Intent(CalendarActivity.this, PdfReader.class);
                    intent.putExtra("pdf_url",isgIntCalendarPdf);
                    startActivity(intent);*/
                    if (!(isgIntCalendarPdf.equalsIgnoreCase(""))) {
                        Intent browserIntent = new Intent(CalendarActivity.this, PDFViewActivity.class);
                        browserIntent.putExtra("pdf_url", isgIntCalendarPdf);
                        browserIntent.putExtra("title", "Calendar");
                        browserIntent.putExtra("filename", "Calendar");
                        startActivity(browserIntent);

                    } else {
                        AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.nomoreinfo), R.drawable.exclamationicon, R.drawable.roundblue);

                    }
//                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(isgIntCalendarPdf));
//                    startActivity(browserIntent);
                }
            }
        });
        headermanager.setButtonLeftSelector(R.drawable.backbtn,
                R.drawable.backbtn);
        headermanager.setButtonRightSelector(R.drawable.pdf,
                R.drawable.pdf);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopDisconnectTimer();
                finish();
            }
        });


        selectedMonth = mContext.getResources().getString(R.string.month);
        selectedYear = mContext.getResources().getString(R.string.year);
        mDateTxt.setVisibility(View.INVISIBLE);
        monthText.setVisibility(View.INVISIBLE);
        yearText.setVisibility(View.INVISIBLE);
        populateMonthSpinner();
        populateYearSpinner();
        populateDaySpinner();
        clearData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthListView.setVisibility(View.GONE);
                yearListView.setVisibility(View.GONE);
                dayListView.setVisibility(View.GONE);
                commonRelList.setVisibility(View.GONE);
                mDateTxt.setVisibility(View.INVISIBLE);
                monthText.setVisibility(View.INVISIBLE);
                yearText.setVisibility(View.INVISIBLE);
                daySpinner.setText("DAY");
                monthSpinner.setText("MONTH");
                yearSpinner.setText("YEAR");

                selectedMonth = mContext.getResources().getString(R.string.month);
                selectedYear = mContext.getResources().getString(R.string.year);
                tempArrayList.clear();
                CalendarListAdapter CalendarListAdapter = new CalendarListAdapter(mContext, calendarModelArrayList);
                CalendarListAdapter.notifyDataSetChanged();
                mCalendarList.setAdapter(CalendarListAdapter);
            }
        });
        dayListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                daySpinSelect = true;
                commonRelList.setVisibility(View.GONE);
                dayListView.setVisibility(View.GONE);
                dayPosition = position;
                daySpinner.setText(dayValues.get(position).toString());

                if (!daySpinner.getText().toString().equalsIgnoreCase("DAY") && !monthSpinner.getText().toString().equalsIgnoreCase("MONTH") && !yearSpinner.getText().toString().equalsIgnoreCase("YEAR")) {
                    tempArrayList = new ArrayList<CalendarModel>();
                    for (int i = 0; i < calendarModelArrayList.size(); i++) {
                        if ((calendarModelArrayList.get(i).getDayDate().equalsIgnoreCase(daySpinner.getText().toString()) || calendarModelArrayList.get(i).getDayDate().equalsIgnoreCase("0" + daySpinner.getText().toString())) && calendarModelArrayList.get(i).getYearDate().equalsIgnoreCase(yearSpinner.getText().toString()) && calendarModelArrayList.get(i).getYearDate().equalsIgnoreCase(yearSpinner.getText().toString()) && calendarModelArrayList.get(i).getMonthDate().toLowerCase().contains(monthSpinner.getText().toString().toLowerCase())) {
                            tempArrayList.add(calendarModelArrayList.get(i));
                        }
                    }
                    CalendarListAdapter CalendarListAdapter = new CalendarListAdapter(mContext, tempArrayList);
                    CalendarListAdapter.notifyDataSetChanged();
                    mCalendarList.setAdapter(CalendarListAdapter);

                } else if (!monthSpinner.getText().toString().equalsIgnoreCase("MONTH") && !yearSpinner.getText().toString().equalsIgnoreCase("YEAR")) {
                    tempArrayList = new ArrayList<CalendarModel>();
                    for (int i = 0; i < calendarModelArrayList.size(); i++) {
                        if (calendarModelArrayList.get(i).getMonthDate().toLowerCase().contains(monthSpinner.getText().toString().toLowerCase()) && calendarModelArrayList.get(i).getYearDate().equalsIgnoreCase(yearSpinner.getText().toString())) {
                            tempArrayList.add(calendarModelArrayList.get(i));
                        }
                    }
                    CalendarListAdapter CalendarListAdapter = new CalendarListAdapter(mContext, tempArrayList);
                    CalendarListAdapter.notifyDataSetChanged();
                    mCalendarList.setAdapter(CalendarListAdapter);

                } else if (!monthSpinner.getText().toString().equalsIgnoreCase("MONTH") && !daySpinner.getText().toString().equalsIgnoreCase("DAY")) {
                    tempArrayList = new ArrayList<CalendarModel>();
                    for (int i = 0; i < calendarModelArrayList.size(); i++) {
                        if (calendarModelArrayList.get(i).getMonthDate().toLowerCase().contains(monthSpinner.getText().toString().toLowerCase()) && (calendarModelArrayList.get(i).getDayDate().equalsIgnoreCase(daySpinner.getText().toString()) || calendarModelArrayList.get(i).getDayDate().equalsIgnoreCase("0" + daySpinner.getText().toString()))) {
                            tempArrayList.add(calendarModelArrayList.get(i));
                        }
                    }
                    CalendarListAdapter CalendarListAdapter = new CalendarListAdapter(mContext, tempArrayList);
                    CalendarListAdapter.notifyDataSetChanged();
                    mCalendarList.setAdapter(CalendarListAdapter);

                } else if (!daySpinner.getText().toString().equalsIgnoreCase("DAY") && !yearSpinner.getText().toString().equalsIgnoreCase("YEAR")) {
                    tempArrayList = new ArrayList<CalendarModel>();
                    for (int i = 0; i < calendarModelArrayList.size(); i++) {
                        if (calendarModelArrayList.get(i).getDayDate().toLowerCase().contains(daySpinner.getText().toString().toLowerCase()) && calendarModelArrayList.get(i).getYearDate().equalsIgnoreCase(yearSpinner.getText().toString())) {
                            tempArrayList.add(calendarModelArrayList.get(i));
                        }
                    }
                    CalendarListAdapter CalendarListAdapter = new CalendarListAdapter(mContext, tempArrayList);
                    CalendarListAdapter.notifyDataSetChanged();
                    mCalendarList.setAdapter(CalendarListAdapter);

                } else if (!monthSpinner.getText().toString().equalsIgnoreCase("MONTH")) {
                    tempArrayList = new ArrayList<CalendarModel>();
                    for (int i = 0; i < calendarModelArrayList.size(); i++) {
                        if (calendarModelArrayList.get(i).getMonthDate().toLowerCase().contains(monthSpinner.getText().toString().toLowerCase())) {
                            tempArrayList.add(calendarModelArrayList.get(i));
                        }
                    }
                    CalendarListAdapter CalendarListAdapter = new CalendarListAdapter(mContext, tempArrayList);
                    CalendarListAdapter.notifyDataSetChanged();
                    mCalendarList.setAdapter(CalendarListAdapter);

                } else if (!yearSpinner.getText().toString().equalsIgnoreCase("YEAR")) {
                    tempArrayList = new ArrayList<CalendarModel>();
                    for (int i = 0; i < calendarModelArrayList.size(); i++) {
                        if (calendarModelArrayList.get(i).getYearDate().equalsIgnoreCase(yearSpinner.getText().toString())) {
                            tempArrayList.add(calendarModelArrayList.get(i));
                        }
                    }
                    CalendarListAdapter CalendarListAdapter = new CalendarListAdapter(mContext, tempArrayList);
                    CalendarListAdapter.notifyDataSetChanged();
                    mCalendarList.setAdapter(CalendarListAdapter);

                } else if (!daySpinner.getText().toString().equalsIgnoreCase("DAY")) {
                    tempArrayList = new ArrayList<CalendarModel>();
                    for (int i = 0; i < calendarModelArrayList.size(); i++) {
                        if (calendarModelArrayList.get(i).getDayDate().equalsIgnoreCase(daySpinner.getText().toString()) || calendarModelArrayList.get(i).getDayDate().equalsIgnoreCase("0" + daySpinner.getText().toString())) {
                            tempArrayList.add(calendarModelArrayList.get(i));
                        }
                    }
                    CalendarListAdapter CalendarListAdapter = new CalendarListAdapter(mContext, tempArrayList);
                    CalendarListAdapter.notifyDataSetChanged();
                    mCalendarList.setAdapter(CalendarListAdapter);

                }
                mDateTxt.setText(daySpinner.getText().toString());
                mDateTxt.setVisibility(View.VISIBLE);


            }
        });

        monthListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                monthSpinSelect = true;
                commonRelList.setVisibility(View.GONE);
                monthListView.setVisibility(View.GONE);
                monthListView.setVisibility(View.GONE);
                selectedMonth = monthValues.get(position).toString();
                monthSpinner.setText(monthValues.get(position).toString());
                mPosMonth = position;
                selectedMonthFullString = monthFullStringValues.get(position).toString();
                if (selectedMonth.equalsIgnoreCase(mContext.getResources().getString(
                        R.string.jan_short))) {
                    selectedMonthId = 1;
                } else if (selectedMonth.equalsIgnoreCase(mContext.getResources()
                        .getString(R.string.feb_short))) {
                    selectedMonthId = 2;
                } else if (selectedMonth.equalsIgnoreCase(mContext.getResources()
                        .getString(R.string.mar_short))) {
                    selectedMonthId = 3;
                } else if (selectedMonth.equalsIgnoreCase(mContext.getResources()
                        .getString(R.string.apr_short))) {
                    selectedMonthId = 4;
                } else if (selectedMonth.equalsIgnoreCase(mContext.getResources()
                        .getString(R.string.may_short))) {
                    selectedMonthId = 5;
                } else if (selectedMonth.equalsIgnoreCase(mContext.getResources()
                        .getString(R.string.jun_short))) {
                    selectedMonthId = 6;
                } else if (selectedMonth.equalsIgnoreCase(mContext.getResources()
                        .getString(R.string.jul_short))) {
                    selectedMonthId = 7;
                } else if (selectedMonth.equalsIgnoreCase(mContext.getResources()
                        .getString(R.string.aug_short))) {
                    selectedMonthId = 8;
                } else if (selectedMonth.equalsIgnoreCase(mContext.getResources()
                        .getString(R.string.sep_short))) {
                    selectedMonthId = 9;
                } else if (selectedMonth.equalsIgnoreCase(mContext.getResources()
                        .getString(R.string.oct_short))) {
                    selectedMonthId = 10;
                } else if (selectedMonth.equalsIgnoreCase(mContext.getResources()
                        .getString(R.string.nov_short))) {
                    selectedMonthId = 11;
                } else if (selectedMonth.equalsIgnoreCase(mContext.getResources()
                        .getString(R.string.dec_short))) {
                    selectedMonthId = 12;
                }
                selectedYear = yearValues.get(mPosYear).toString();
                System.out.println("Selected year----" + selectedYear);
                monthText.setText(selectedMonthFullString);
                monthText.setVisibility(View.VISIBLE);
                if ((!selectedMonth.equalsIgnoreCase(mContext.getResources().getString(
                        R.string.month)) && (!selectedYear.equalsIgnoreCase(mContext
                        .getResources().getString(R.string.year))))) {
//                    setSearchCalendarResult();
                    if (selectedYear.equals("YEAR")) {
                        selectedYear = yearValues.get(0).toString();
                    }
                    populateDaySpinner(selectedMonthId - 1, Integer.valueOf(selectedYear));
                    Log.e("Position1:", position + "");

                } else if ((!selectedMonth.equalsIgnoreCase(mContext.getResources().getString(
                        R.string.month)))) {
                    populateDaySpinner(selectedMonthId - 1, Calendar.YEAR - 1);
                    Log.e("Position2:", position + "");

                } else if ((!selectedYear.equalsIgnoreCase(mContext.getResources().getString(
                        R.string.year)))) {
//             if (selectedYear.equals("YEAR")) {
//                selectedYear = yearValues.get(0).toString();
//             }
                    populateDaySpinner(Calendar.MONTH, Integer.valueOf(selectedYear));
                    Log.e("Position3:", position + "");

                } else {
                    populateDaySpinner(Calendar.MONTH, Calendar.YEAR - 1);
                    Log.e("Position4:", position + "");

                }
            }
        });

        yearListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                yearSpinSelect = true;
                commonRelList.setVisibility(View.GONE);
                yearListView.setVisibility(View.GONE);
                mPosYear = position;

                yearSpinner.setText(yearValues.get(position).toString());

                selectedYear = yearValues.get(position).toString();
                yearText.setText(yearSpinner.getText().toString());
                yearText.setVisibility(View.VISIBLE);

                if ((!selectedMonth.equalsIgnoreCase(mContext.getResources().getString(R.string.month)) && (!selectedYear.equalsIgnoreCase(mContext
                        .getResources().getString(R.string.year))))) {
//                    setSearchCalendarResult();
                    if (selectedYear.equals("YEAR")) {
                        selectedYear = yearValues.get(0).toString();
                    }
                    populateDaySpinner(selectedMonthId - 1, Integer.valueOf(selectedYear));
                    Log.e("Position1:", position + "");

                } else if ((!selectedMonth.equalsIgnoreCase(mContext.getResources().getString(
                        R.string.month)))) {
                    populateDaySpinner(selectedMonthId - 1, Calendar.YEAR - 1);
                    Log.e("Position2:", position + "");

                }


                else if ((!daySpinner.getText().toString().equalsIgnoreCase(mContext.getResources().getString(R.string.day)) && (!selectedYear.equalsIgnoreCase(mContext.getResources().getString(R.string.year))))) {
//                    setSearchCalendarResult();
                    if (selectedYear.equals("YEAR")) {
                        selectedYear = yearValues.get(0).toString();
                    }
                    populateDaySpinner(selectedMonthId - 1, Integer.valueOf(selectedYear));
                    Log.e("Day Year Position1:", position + "");

                } else if ((!daySpinner.getText().toString().equalsIgnoreCase(mContext.getResources().getString(
                        R.string.day)))) {
                    populateDaySpinner(selectedMonthId - 1, Calendar.YEAR - 1);
                    Log.e("Day Year Position2:", position + "");

                }

                else if ((!selectedYear.equalsIgnoreCase(mContext.getResources().getString(
                        R.string.year)))) {
                    if (selectedYear.equals("YEAR")) {
                        selectedYear = yearValues.get(0).toString();
                    }
                    populateDaySpinner(Calendar.MONTH, Integer.valueOf(selectedYear));
                    Log.e("Position3:", Integer.valueOf(selectedYear) + "::" + selectedYear);


                } else {
                    populateDaySpinner(Calendar.MONTH, Calendar.YEAR - 1);
                    Log.e("Position4:", position + "");

                }
            }
        });
        selectedYear = mContext.getResources().getString(R.string.year);
        daySpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (daySpinSelect) {
                    commonRelList.setVisibility(View.VISIBLE);
                    dayListView.setVisibility(View.VISIBLE);
                    yearListView.setVisibility(View.GONE);
                    monthListView.setVisibility(View.GONE);

                    daySpinSelect = false;
                } else {
                    daySpinSelect = true;

                    commonRelList.setVisibility(View.GONE);
                    dayListView.setVisibility(View.GONE);
                }
            }
        });
        monthSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (monthSpinSelect) {
                    monthSpinSelect = false;
                    dayListView.setVisibility(View.GONE);
                    yearListView.setVisibility(View.GONE);

                    commonRelList.setVisibility(View.VISIBLE);
                    monthListView.setVisibility(View.VISIBLE);

                } else {
                    monthSpinSelect = true;

                    commonRelList.setVisibility(View.GONE);
                    monthListView.setVisibility(View.GONE);
                }
            }
        });
        yearSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yearSpinSelect) {
                    yearSpinSelect = false;
                    monthListView.setVisibility(View.GONE);
                    dayListView.setVisibility(View.GONE);

                    commonRelList.setVisibility(View.VISIBLE);
                    yearListView.setVisibility(View.VISIBLE);
                } else {
                    yearSpinSelect = true;
                    commonRelList.setVisibility(View.GONE);
                    yearListView.setVisibility(View.GONE);
                }
            }
        });
        mCalendar = Calendar.getInstance();
        mDateTxt.setText(Integer.toString(mCalendar.get(Calendar.DATE)));
        month = mCalendar.get(Calendar.MONTH);
        if (month == 0) {
            mMnthTxt.setText(mContext.getResources()
                    .getString(R.string.jan_short));
        } else if (month == 1) {
            mMnthTxt.setText(mContext.getResources().getString(
                    R.string.feb_short));
        } else if (month == 2) {
            mMnthTxt.setText(mContext.getResources().getString(R.string.mar_short));
        } else if (month == 3) {
            mMnthTxt.setText(mContext.getResources().getString(R.string.apr_short));
        } else if (month == 4) {
            mMnthTxt.setText(mContext.getResources().getString(R.string.may_short));
        } else if (month == 5) {
            mMnthTxt.setText(mContext.getResources().getString(R.string.jun_short));
        } else if (month == 6) {
            mMnthTxt.setText(mContext.getResources().getString(R.string.jul_short));
        } else if (month == 7) {
            mMnthTxt.setText(mContext.getResources().getString(R.string.aug_short));
        } else if (month == 8) {
            mMnthTxt.setText(mContext.getResources().getString(
                    R.string.sep_short));
        } else if (month == 9) {
            mMnthTxt.setText(mContext.getResources()
                    .getString(R.string.oct_short));
        } else if (month == 10) {
            mMnthTxt.setText(mContext.getResources().getString(
                    R.string.nov_short));
        } else if (month == 11) {
            mMnthTxt.setText(mContext.getResources().getString(
                    R.string.dec_short));
        }
        mYearTxt.setText(Integer.toString(mCalendar.get(Calendar.YEAR)));
    }

    private void callCalendarListAPI(final String URL) {
        eventsArray.clear();
        dates.clear();
        datesFinal.clear();


        VolleyAPIManager volleyWrapper = new VolleyAPIManager(URL);
        String[] name = {"access_token", "boardId", "studentId", "parentId"};
        String boardId = "";
        if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")) {
            boardId = "1";
        } else if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG-INT")) {
            boardId = "2";
        } else {
            boardId = "1";
        }
        String[] value = {AppPreferenceManager.getAccessToken(mContext), boardId,
                AppPreferenceManager.getStudentId(mContext), AppPreferenceManager.getUserId(mContext)};

        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyAPIManager.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("response" + successResponse);
                try {
                    JSONObject obj = new JSONObject(successResponse);
                    String response_code = obj.optString(JTAG_RESPONSECODE);
                    if (response_code.equalsIgnoreCase(RESPONSE_SUCCESS)) {
                        JSONObject secobj = obj.optJSONObject(JTAG_RESPONSE);
                        String status_code = secobj.optString(JTAG_STATUSCODE);
                        if (status_code.equalsIgnoreCase(STATUSCODE_SUCCESS)) {
                            JSONArray jsonArray = secobj.optJSONArray(JTAG_CALENDAR);
                            isgCalendarPdf = secobj.optString("isgCalendar");
                            isgIntCalendarPdf = secobj.optString("isgIntcalendar");
                            if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")) {
                                if (isgCalendarPdf.equalsIgnoreCase("")) {
                                    headermanager.getHeader(relativeHeader, 1);

                                } else {
                                    headermanager.getHeader(relativeHeader, 4);

                                }
                            } else {
                                if (isgIntCalendarPdf.equalsIgnoreCase("")) {
                                    headermanager.getHeader(relativeHeader, 3);

                                } else {
                                    headermanager.getHeader(relativeHeader, 5);

                                }
                            }
                            headermanager.setButtonLeftSelector(R.drawable.backbtn,
                                    R.drawable.backbtn);
                            headermanager.setButtonRightSelector(R.drawable.pdf,
                                    R.drawable.pdf);
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    CalendarModel calendarModel = new CalendarModel();
                                    calendarModel.setDateNTime(jsonObject.optString(JTAG_DATE));
                                    Date mNoticeDate = new Date();
                                    String mDate = jsonObject.optString(JTAG_DATE);
                                    try {
                                        mNoticeDate = sdfcalender.parse(mDate);
                                    } catch (ParseException ex) {
                                        Log.e("Date", "Parsing error");
                                    }
                                    String dayOfTheWeek; // Thursday
                                    String day; // 20
                                    String monthString; // Jun
                                    String monthNumber; // 06
                                    String year; // 2013
                                    dayOfTheWeek = (String) DateFormat.format("EEEE", mNoticeDate); // Thursday
                                    day = (String) DateFormat.format("dd", mNoticeDate); // 20
                                    monthString = (String) DateFormat.format("MMMM", mNoticeDate); // Jun
                                    monthNumber = (String) DateFormat.format("MM", mNoticeDate); // 06
                                    year = (String) DateFormat.format("yyyy", mNoticeDate); // 2013
                                    events = jsonObject.optJSONArray(JTAG_EVENTS);
                                    calendarModel.setDayStringDate(dayOfTheWeek);
                                    calendarModel.setDayDate(day);
                                    calendarModel.setMonthDate(monthString);
                                    calendarModel.setYearDate(year);
                                    calendarModel.setMontrhNo(monthNumber);
                                    modelArrayList = new ArrayList<CalendarModel>();
                                    for (int j = 0; j < events.length(); j++) {
                                        JSONObject detJsonObject = events.optJSONObject(j);
                                        CalendarModel model = new CalendarModel();
                                        model.setId(detJsonObject.optString(JTAG_ID));
                                        model.setEvent(detJsonObject.optString(JTAG_NAME));
                                        model.setDescription(detJsonObject.optString(JTAG_DESCRIPTION));
                                        model.setFromTime(detJsonObject.optString(JTAG_START_DATE));
                                        model.setToTime(detJsonObject.optString(JTAG_END_DATE));
                                        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        try {
                                            Date dateStart = format1.parse(detJsonObject.optString(JTAG_START_DATE));
                                            SimpleDateFormat format2 = new SimpleDateFormat("hh:mm a");
                                            String startTime = format2.format(dateStart);
                                            model.setStartTime(startTime);
                                            Date dateEndTime = format1.parse(detJsonObject.optString(JTAG_END_DATE));
                                            String endTime = format2.format(dateEndTime);
                                            model.setEndTime(endTime);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        //   modelArrayList.add(model);
                                        eventsArray.add(model);
                                        System.out.println("new calendar event array" + eventsArray.size());
                                    }
                                    //    calendarModel.setEventModels(eventsArray);
                                    // calendarModelArrayList.add(calendarModel);
                                }
                                for (int j = 0; j < eventsArray.size(); j++) {
                                    dates = getDatesBetween(eventsArray.get(j).getFromTime(), eventsArray.get(j).getToTime());
                                    for (String date : dates) {
                                        if (!datesFinal.contains(date))
                                            datesFinal.add(date);

                                    }
                                }
                                for (int j = 0; j < datesFinal.size(); j++) {
                                    CalendarModel calendarModel = new CalendarModel();
                                    calendarModel.setDateNTime(String.valueOf(datesFinal.get(j)));
                                    Date mNoticeDate = new Date();
                                    String mDate = String.valueOf(datesFinal.get(j));
                                    try {
                                        mNoticeDate = sdfcalender.parse(mDate);
                                    } catch (ParseException ex) {
                                        Log.e("Date", "Parsing error");
                                    }
                                    String dayOfTheWeek; // Thursday
                                    String day; // 20
                                    String monthString; // Jun
                                    String monthNumber; // 06
                                    String year; // 2013
                                    dayOfTheWeek = (String) DateFormat.format("EEEE", mNoticeDate); // Thursday
                                    day = (String) DateFormat.format("dd", mNoticeDate); // 20
                                    monthString = (String) DateFormat.format("MMMM", mNoticeDate); // Jun
                                    monthNumber = (String) DateFormat.format("MM", mNoticeDate); // 06
                                    year = (String) DateFormat.format("yyyy", mNoticeDate); // 2013
                                    calendarModel.setDayStringDate(dayOfTheWeek);
                                    calendarModel.setDayDate(day);
                                    calendarModel.setMonthDate(monthString);
                                    calendarModel.setYearDate(year);
                                    calendarModel.setMontrhNo(monthNumber);
                                    System.out.println(String.valueOf(datesFinal.get(j)));
                                    ArrayList<CalendarModel> finalEventsArray = new ArrayList<>();
                                    for (int e = 0; e < eventsArray.size(); e++) {
                                        String startdate = eventsArray.get(e).getFromTime();
                                        String endDate = eventsArray.get(e).getToTime();
                                        dates = getDatesBetween(startdate, endDate);
                                        CalendarModel xmodel = new CalendarModel();
                                        if (dates.contains(datesFinal.get(j))) {

                                            xmodel.setId(eventsArray.get(e).getId());
                                            xmodel.setEvent(eventsArray.get(e).getEvent());
                                            xmodel.setDescription(eventsArray.get(e).getDescription());
                                            xmodel.setFromTime(eventsArray.get(e).getFromTime());
                                            xmodel.setToTime(eventsArray.get(e).getToTime());
                                            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            try {
                                                Date dateStart = format1.parse(eventsArray.get(e).getFromTime());
                                                SimpleDateFormat format2 = new SimpleDateFormat("hh:mm a");
                                                String startTime = format2.format(dateStart);
                                                xmodel.setStartTime(startTime);
                                                Date dateEndTime = format1.parse(eventsArray.get(e).getToTime());
                                                String endTime = format2.format(dateEndTime);
                                                xmodel.setEndTime(endTime);
                                            } catch (ParseException ex) {
                                                ex.printStackTrace();
                                            }
                                            finalEventsArray.add(xmodel);
                                        }


                                    }
                                    calendarModel.setEventModels(finalEventsArray);
                                    calendarModelArrayList.add(calendarModel);
                                }

                                CalendarListAdapter CalendarListAdapter = new CalendarListAdapter(mContext, calendarModelArrayList);
                                mCalendarList.setAdapter(CalendarListAdapter);
                            } else {
                                AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.no_datafound), R.drawable.infoicon, R.drawable.roundblue);

                            }
                        } else {
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.common_error), R.drawable.infoicon, R.drawable.roundblue);
                        }
                    } else if (response_code.equalsIgnoreCase(RESPONSE_INTERNALSERVER_ERROR)) {

                        AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.internal_server_error), R.drawable.infoicon, R.drawable.roundblue);

                    } else if (response_code.equalsIgnoreCase(RESPONSE_INVALID_ACCESSTOKEN) || response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) || response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED)) {
                        AppUtilityMethods.getToken(mContext, new AppUtilityMethods.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        callCalendarListAPI(URL);
                    } else {
                        AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.roundblue);

                    }
                } catch (Exception ex) {
                    System.out.println("The Exception in edit profile is" + ex.toString());
                }
            }

            @Override
            public void responseFailure(String failureResponse) {
                AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.roundblue);

            }
        });


    }

    private static List<Date> getDates(String dateString1, String dateString2) throws ParseException {
        ArrayList<Date> dates = new ArrayList<Date>();
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");

        Date date1 = null;
        Date date2 = null;

        try {
            date1 = df1.parse(dateString1);
            date2 = df1.parse(dateString2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);


        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        while (!cal1.after(cal2)) {

            dates.add(df1.parse(String.valueOf(cal1.getTime())));
            cal1.add(Calendar.DATE, 1);
        }
        return dates;
    }

    public static List<String> getDatesBetween(String startDate, String endDate) {
        ArrayList<String> dates = new ArrayList<String>();
        Calendar calendar = new GregorianCalendar();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format.parse(startDate);
            calendar.setTime(date);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            Date date1 = format.parse(endDate);
            Calendar endCalendar = new GregorianCalendar();
            endCalendar.setTime(date1);
            System.out.println(date1);
            while (!calendar.after(endCalendar)) {
                SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
                sdf.setTimeZone(TimeZone.getDefault());
                String result = sdf.format(calendar.getTime());
                dates.add(result);
                calendar.add(Calendar.DATE, 1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return dates;
    }

    private void populateMonthSpinner() {
        int[] months = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
        monthValues = new ArrayList<String>();
        monthFullStringValues = new ArrayList<String>();
        for (int i = 0; i < months.length; i++) {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
            cal.set(Calendar.DATE, 5);
            cal.set(Calendar.MONTH, months[i]);
            String month_name = month_date.format(cal.getTime());
            monthFullStringValues.add(month_name);
            if (month_name.equalsIgnoreCase(mContext.getResources().getString(
                    R.string.january))) {
                month_name = mContext.getResources().getString(
                        R.string.jan_short);
            } else if (month_name.equalsIgnoreCase(mContext.getResources()
                    .getString(R.string.february))) {
                month_name = mContext.getResources().getString(
                        R.string.feb_short);
            } else if (month_name.equalsIgnoreCase(mContext.getResources()
                    .getString(R.string.march))) {
                month_name = mContext.getResources().getString(
                        R.string.mar_short);
            } else if (month_name.equalsIgnoreCase(mContext.getResources()
                    .getString(R.string.april))) {
                month_name = mContext.getResources().getString(
                        R.string.apr_short);
            } else if (month_name.equalsIgnoreCase(mContext.getResources()
                    .getString(R.string.may))) {
                month_name = mContext.getResources().getString(
                        R.string.may_short);
            } else if (month_name.equalsIgnoreCase(mContext.getResources()
                    .getString(R.string.june))) {
                month_name = mContext.getResources().getString(
                        R.string.jun_short);
            } else if (month_name.equalsIgnoreCase(mContext.getResources()
                    .getString(R.string.july))) {
                month_name = mContext.getResources().getString(
                        R.string.jul_short);
            } else if (month_name.equalsIgnoreCase(mContext.getResources()
                    .getString(R.string.august))) {
                month_name = mContext.getResources().getString(
                        R.string.aug_short);
            } else if (month_name.equalsIgnoreCase(mContext.getResources()
                    .getString(R.string.september))) {
                month_name = mContext.getResources().getString(
                        R.string.sep_short);
            } else if (month_name.equalsIgnoreCase(mContext.getResources()
                    .getString(R.string.october))) {
                month_name = mContext.getResources().getString(
                        R.string.oct_short);
            } else if (month_name.equalsIgnoreCase(mContext.getResources()
                    .getString(R.string.november))) {
                month_name = mContext.getResources().getString(
                        R.string.nov_short);
            } else if (month_name.equalsIgnoreCase(mContext.getResources()
                    .getString(R.string.december))) {
                month_name = mContext.getResources().getString(
                        R.string.dec_short);
            }
            monthValues.add(month_name);
        }

       /* monthdataAdapter = new CustomCalendarSpinnerAdapter(mContext,
                R.layout.spinner_textview_item, monthValues, -1);*/
        ArrayAdapter<String> monthdataAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_textview_item, R.id.title, monthValues) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                // Set the color here
                if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")) {
                    textView.setBackgroundColor(getResources().getColor(R.color.calendar_selector));
                } else {
                    textView.setBackgroundColor(getResources().getColor(R.color.calendar_blue_selector));
                }
                return textView;
            }
        };

        monthListView.setAdapter(monthdataAdapter);
    }

    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "yyyy-MM-dd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }


    private void populateDaySpinner(int month, int year) {
        Calendar selctedCalender = Calendar.getInstance();
        selctedCalender.set(Calendar.MONTH, month);
        selctedCalender.set(Calendar.YEAR, year);
        Log.e("Calendar.DAY_OF_MONTH", month + year + "");
        int noOfDays = selctedCalender.getActualMaximum(Calendar.DAY_OF_MONTH);
        Log.e("noofdays", noOfDays + "");
        dayValues = new ArrayList<String>();


        switch (noOfDays) {
            case 28:
                dayValues = new ArrayList<String>(Arrays.asList(mContext.getResources().getStringArray(R.array.month28)));
                break;
            case 29:
                dayValues = new ArrayList<String>(Arrays.asList(mContext.getResources().getStringArray(R.array.month29)));

                break;
            case 30:
                dayValues = new ArrayList<String>(Arrays.asList(mContext.getResources().getStringArray(R.array.month30)));

                break;
            case 31:
                dayValues = new ArrayList<String>(Arrays.asList(mContext.getResources().getStringArray(R.array.month31)));

                break;
            default:
                break;
        }


        /*dataAdapter = new CustomCalendarSpinnerAdapter(mContext,
                R.layout.spinner_textview_item, dayValues, -1);*/
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_textview_item, R.id.title, dayValues) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);

                textView.setGravity(Gravity.CENTER);
                // Set the color here
                if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")) {
                    textView.setBackgroundColor(getResources().getColor(R.color.calendar_selector));
                } else {
                    textView.setBackgroundColor(getResources().getColor(R.color.calendar_blue_selector));
                }
                return textView;
            }
        };

        dayListView.setAdapter(dataAdapter);
        if (dayPosition >= 0 && !daySpinner.getText().toString().equalsIgnoreCase("DAY")) {
            if (dayPosition < noOfDays) {
                daySpinner.setText(dayValues.get(dayPosition).toString());
                mDateTxt.setText(dayValues.get(dayPosition).toString());
            } else {
                mDateTxt.setText(dayValues.get(noOfDays - 1).toString());

                daySpinner.setText(dayValues.get(noOfDays - 1).toString());
            }
        } else {
            daySpinner.setText("DAY");

        }
        if (!daySpinner.getText().toString().equalsIgnoreCase("DAY") && !monthSpinner.getText().toString().equalsIgnoreCase("MONTH") && !yearSpinner.getText().toString().equalsIgnoreCase("YEAR")) {
            tempArrayList = new ArrayList<CalendarModel>();

            for (int i = 0; i < calendarModelArrayList.size(); i++) {
                if ((calendarModelArrayList.get(i).getDayDate().equalsIgnoreCase(daySpinner.getText().toString()) || calendarModelArrayList.get(i).getDayDate().equalsIgnoreCase("0" + daySpinner.getText().toString())) && calendarModelArrayList.get(i).getYearDate().equalsIgnoreCase(yearSpinner.getText().toString()) && calendarModelArrayList.get(i).getMonthDate().toLowerCase().contains(monthSpinner.getText().toString().toLowerCase())) {
                    tempArrayList.add(calendarModelArrayList.get(i));
                }
            }
            CalendarListAdapter CalendarListAdapter = new CalendarListAdapter(mContext, tempArrayList);
            CalendarListAdapter.notifyDataSetChanged();
            mCalendarList.setAdapter(CalendarListAdapter);
        } else if (!monthSpinner.getText().toString().equalsIgnoreCase("MONTH") && !yearSpinner.getText().toString().equalsIgnoreCase("YEAR")) {
            tempArrayList = new ArrayList<CalendarModel>();
            for (int i = 0; i < calendarModelArrayList.size(); i++) {
                if (calendarModelArrayList.get(i).getMonthDate().toLowerCase().contains(monthSpinner.getText().toString().toLowerCase()) && calendarModelArrayList.get(i).getYearDate().equalsIgnoreCase(yearSpinner.getText().toString())) {
                    tempArrayList.add(calendarModelArrayList.get(i));
                }
            }
            CalendarListAdapter CalendarListAdapter = new CalendarListAdapter(mContext, tempArrayList);
            CalendarListAdapter.notifyDataSetChanged();
            mCalendarList.setAdapter(CalendarListAdapter);
        } else if (!monthSpinner.getText().toString().equalsIgnoreCase("MONTH") && !daySpinner.getText().toString().equalsIgnoreCase("DAY")) {
            tempArrayList = new ArrayList<CalendarModel>();
            for (int i = 0; i < calendarModelArrayList.size(); i++) {
                if (calendarModelArrayList.get(i).getMonthDate().toLowerCase().contains(monthSpinner.getText().toString().toLowerCase()) && (calendarModelArrayList.get(i).getDayDate().equalsIgnoreCase(daySpinner.getText().toString()) || calendarModelArrayList.get(i).getDayDate().equalsIgnoreCase("0" + daySpinner.getText().toString()))) {
                    tempArrayList.add(calendarModelArrayList.get(i));
                }
            }
            CalendarListAdapter CalendarListAdapter = new CalendarListAdapter(mContext, tempArrayList);
            CalendarListAdapter.notifyDataSetChanged();
            mCalendarList.setAdapter(CalendarListAdapter);
        } else if (!daySpinner.getText().toString().equalsIgnoreCase("DAY") && !yearSpinner.getText().toString().equalsIgnoreCase("YEAR")) {
            tempArrayList = new ArrayList<CalendarModel>();
            for (int i = 0; i < calendarModelArrayList.size(); i++) {
                if (calendarModelArrayList.get(i).getDayDate().toLowerCase().contains(daySpinner.getText().toString().toLowerCase()) && calendarModelArrayList.get(i).getYearDate().equalsIgnoreCase(yearSpinner.getText().toString())) {
                    tempArrayList.add(calendarModelArrayList.get(i));
                }
            }
            CalendarListAdapter CalendarListAdapter = new CalendarListAdapter(mContext, tempArrayList);
            CalendarListAdapter.notifyDataSetChanged();
            mCalendarList.setAdapter(CalendarListAdapter);

        }
        else if (!monthSpinner.getText().toString().equalsIgnoreCase("MONTH")) {
            tempArrayList = new ArrayList<CalendarModel>();
            for (int i = 0; i < calendarModelArrayList.size(); i++) {

                if (calendarModelArrayList.get(i).getMonthDate().toLowerCase().contains(monthSpinner.getText().toString().toLowerCase())) {
                    tempArrayList.add(calendarModelArrayList.get(i));
                }
            }
            CalendarListAdapter CalendarListAdapter = new CalendarListAdapter(mContext, tempArrayList);
            CalendarListAdapter.notifyDataSetChanged();
            mCalendarList.setAdapter(CalendarListAdapter);
        } else if (!yearSpinner.getText().toString().equalsIgnoreCase("YEAR")) {
            tempArrayList = new ArrayList<CalendarModel>();
            for (int i = 0; i < calendarModelArrayList.size(); i++) {
                if (calendarModelArrayList.get(i).getYearDate().equalsIgnoreCase(yearSpinner.getText().toString())) {
                    tempArrayList.add(calendarModelArrayList.get(i));
                }
            }
            CalendarListAdapter CalendarListAdapter = new CalendarListAdapter(mContext, tempArrayList);
            CalendarListAdapter.notifyDataSetChanged();
            mCalendarList.setAdapter(CalendarListAdapter);
        } else if (!daySpinner.getText().toString().equalsIgnoreCase("DAY")) {
            tempArrayList = new ArrayList<CalendarModel>();
            for (int i = 0; i < calendarModelArrayList.size(); i++) {
                if (calendarModelArrayList.get(i).getDayDate().equalsIgnoreCase(daySpinner.getText().toString()) || calendarModelArrayList.get(i).getDayDate().equalsIgnoreCase("0" + daySpinner.getText().toString())) {
                    tempArrayList.add(calendarModelArrayList.get(i));
                }
            }
            CalendarListAdapter CalendarListAdapter = new CalendarListAdapter(mContext, tempArrayList);
            CalendarListAdapter.notifyDataSetChanged();
            mCalendarList.setAdapter(CalendarListAdapter);
        }
//        if (!daySpinner.getText().toString().equalsIgnoreCase("DAY") && !monthSpinner.getText().toString().equalsIgnoreCase("MONTH") && !yearSpinner.getText().toString().equalsIgnoreCase("YEAR")) {
//            tempArrayList = new ArrayList<CalendarModel>();
//
//            for (int i = 0; i < calendarModelArrayList.size(); i++) {
//                if (calendarModelArrayList.get(i).getDayDate().equalsIgnoreCase(daySpinner.getText().toString()) && calendarModelArrayList.get(i).getYearDate().equalsIgnoreCase(yearSpinner.getText().toString()) && calendarModelArrayList.get(i).getMonthDate().toLowerCase().contains(monthSpinner.getText().toString().toLowerCase())) {
//                    tempArrayList.add(calendarModelArrayList.get(i));
//                }
//            }
//            CalendarListAdapter CalendarListAdapter = new CalendarListAdapter(mContext, tempArrayList);
//            CalendarListAdapter.notifyDataSetChanged();
//            mCalendarList.setAdapter(CalendarListAdapter);
//        } else if (!monthSpinner.getText().toString().equalsIgnoreCase("MONTH") && !yearSpinner.getText().toString().equalsIgnoreCase("YEAR")) {
//            tempArrayList = new ArrayList<CalendarModel>();
//            for (int i = 0; i < calendarModelArrayList.size(); i++) {
//                if (calendarModelArrayList.get(i).getMonthDate().toLowerCase().contains(monthSpinner.getText().toString().toLowerCase()) && calendarModelArrayList.get(i).getYearDate().equalsIgnoreCase(yearSpinner.getText().toString())) {
//                    tempArrayList.add(calendarModelArrayList.get(i));
//                }
//            }
//            CalendarListAdapter CalendarListAdapter = new CalendarListAdapter(mContext, tempArrayList);
//            CalendarListAdapter.notifyDataSetChanged();
//            mCalendarList.setAdapter(CalendarListAdapter);
//        } else if (!monthSpinner.getText().toString().equalsIgnoreCase("MONTH") && !daySpinner.getText().toString().equalsIgnoreCase("DAY")) {
//            tempArrayList = new ArrayList<CalendarModel>();
//            for (int i = 0; i < calendarModelArrayList.size(); i++) {
//                if (calendarModelArrayList.get(i).getMonthDate().toLowerCase().contains(monthSpinner.getText().toString().toLowerCase()) && calendarModelArrayList.get(i).getDayDate().equalsIgnoreCase(daySpinner.getText().toString())) {
//                    tempArrayList.add(calendarModelArrayList.get(i));
//                }
//            }
//            CalendarListAdapter CalendarListAdapter = new CalendarListAdapter(mContext, tempArrayList);
//            CalendarListAdapter.notifyDataSetChanged();
//            mCalendarList.setAdapter(CalendarListAdapter);
//        } else if (!monthSpinner.getText().toString().equalsIgnoreCase("MONTH")) {
//            tempArrayList = new ArrayList<CalendarModel>();
//            for (int i = 0; i < calendarModelArrayList.size(); i++) {
//
//                if (calendarModelArrayList.get(i).getMonthDate().toLowerCase().contains(monthSpinner.getText().toString().toLowerCase())) {
//                    tempArrayList.add(calendarModelArrayList.get(i));
//                }
//            }
//            CalendarListAdapter CalendarListAdapter = new CalendarListAdapter(mContext, tempArrayList);
//            CalendarListAdapter.notifyDataSetChanged();
//            mCalendarList.setAdapter(CalendarListAdapter);
//        } else if (!yearSpinner.getText().toString().equalsIgnoreCase("YEAR")) {
//            tempArrayList = new ArrayList<CalendarModel>();
//            for (int i = 0; i < calendarModelArrayList.size(); i++) {
//                if (calendarModelArrayList.get(i).getYearDate().equalsIgnoreCase(yearSpinner.getText().toString())) {
//                    tempArrayList.add(calendarModelArrayList.get(i));
//                }
//            }
//            CalendarListAdapter CalendarListAdapter = new CalendarListAdapter(mContext, tempArrayList);
//            CalendarListAdapter.notifyDataSetChanged();
//            mCalendarList.setAdapter(CalendarListAdapter);
//        } else if (!daySpinner.getText().toString().equalsIgnoreCase("DAY")) {
//            tempArrayList = new ArrayList<CalendarModel>();
//            for (int i = 0; i < calendarModelArrayList.size(); i++) {
//                if (calendarModelArrayList.get(i).getDayDate().equalsIgnoreCase(daySpinner.getText().toString())) {
//                    tempArrayList.add(calendarModelArrayList.get(i));
//                }
//            }
//            CalendarListAdapter CalendarListAdapter = new CalendarListAdapter(mContext, tempArrayList);
//            CalendarListAdapter.notifyDataSetChanged();
//            mCalendarList.setAdapter(CalendarListAdapter);
//        }
    }

    private void populateDaySpinner() {
        Calendar selctedCalender = Calendar.getInstance();
        int noOfDays = selctedCalender.getActualMaximum(Calendar.DAY_OF_MONTH);
        Log.e("noofdays", noOfDays + "");
        dayValues = new ArrayList<String>();
        switch (noOfDays) {
            case 28:
                dayValues = new ArrayList<String>(Arrays.asList(mContext.getResources().getStringArray(R.array.month28)));
                break;
            case 29:
                dayValues = new ArrayList<String>(Arrays.asList(mContext.getResources().getStringArray(R.array.month29)));

                break;
            case 30:
                dayValues = new ArrayList<String>(Arrays.asList(mContext.getResources().getStringArray(R.array.month30)));

                break;
            case 31:
                dayValues = new ArrayList<String>(Arrays.asList(mContext.getResources().getStringArray(R.array.month31)));

                break;
            default:
                break;
        }

        /*daydataAdapter = new CustomCalendarSpinnerAdapter(mContext,
                R.layout.spinner_textview_item, dayValues, -1);*/
        ArrayAdapter<String> daydataAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_textview_item, R.id.title, dayValues) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setGravity(Gravity.CENTER);

                // Set the color here
                if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")) {
                    textView.setBackgroundColor(getResources().getColor(R.color.calendar_selector));
                } else {
                    textView.setBackgroundColor(getResources().getColor(R.color.calendar_blue_selector));
                }
                return textView;
            }
        };

        dayListView.setAdapter(daydataAdapter);
    }


    private void populateYearSpinner() {

        int yearInt = Calendar.getInstance().get(Calendar.YEAR) - 1;
        System.out.println("YEAR::" + yearInt);
//        yearValues.add(yearInt + "");
        yearValues.add(yearInt + 1 + "");
        yearValues.add(yearInt + 2 + "");
//        yearValues.add(yearInt + 3 + "");
//        yearValues.add(yearInt + 4 + "");
//        yearValues.add(yearInt + 5 + "");

        /*dataAdapter = new CustomCalendarSpinnerAdapter(mContext,
                R.layout.spinner_textview_item, yearValues, -1);
*/
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_textview_item, R.id.title, yearValues) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setGravity(Gravity.CENTER);

                // Set the color here
                if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")) {
                    textView.setBackgroundColor(getResources().getColor(R.color.calendar_selector));
                } else {
                    textView.setBackgroundColor(getResources().getColor(R.color.calendar_blue_selector));
                }
                return textView;
            }
        };
        yearListView.setAdapter(dataAdapter);
    }


   /* public static final long DISCONNECT_TIMEOUT = 300000;; // 5 min = 5 * 60 * 1000 ms 300000

    private Handler disconnectHandler = new Handler(){
        public void handleMessage(Message msg) {
        }
    };

    private Runnable disconnectCallback = new Runnable() {
        @Override
        public void run() {
            // Perform any required operation on disconnect
            AppPreferenceManager.setIsGuest(CalendarActivity.this, false);
            AppPreferenceManager.setUserId(CalendarActivity.this, "");
            AppPreferenceManager.setIsUserAlreadyLoggedIn(CalendarActivity.this, false);
            AppPreferenceManager.setSchoolSelection(CalendarActivity.this, "ISG");
            if (AppUtilityMethods.isAppInForeground(mContext)) {
                Intent mIntent = new Intent(CalendarActivity.this, LoginActivity.class);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mIntent);
                finish();
            }
        }
    };*/

    public void resetDisconnectTimer() {
        HomeActivity.disconnectHandler.removeCallbacks(HomeActivity.disconnectCallback);
        HomeActivity.disconnectHandler.postDelayed(HomeActivity.disconnectCallback, HomeActivity.DISCONNECT_TIMEOUT);
    }

    public void stopDisconnectTimer() {
        HomeActivity.disconnectHandler.removeCallbacks(HomeActivity.disconnectCallback);
    }

    @Override
    public void onUserInteraction() {
        resetDisconnectTimer();
    }

    @Override
    public void onResume() {
        super.onResume();
        resetDisconnectTimer();
    }

    @Override
    public void onStop() {
        super.onStop();
//        stopDisconnectTimer();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopDisconnectTimer();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("TestRestart:hshhs");
        if (AppPreferenceManager.getUserId(mContext).equalsIgnoreCase("")) {
            Intent mIntent = new Intent(CalendarActivity.this, LoginActivity.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mIntent);
            finish();
        }
    }

}