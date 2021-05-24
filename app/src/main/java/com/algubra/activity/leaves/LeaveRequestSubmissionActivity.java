ypackage com.algubra.activity.leaves;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.algubra.R;
import com.algubra.activity.home.HomeActivity;
import com.algubra.activity.login.LoginActivity;
import com.algubra.constants.JsonTagConstants;
import com.algubra.constants.StausCodes;
import com.algubra.constants.URLConstants;
import com.algubra.manager.AppPreferenceManager;
import com.algubra.manager.AppUtilityMethods;
import com.algubra.manager.HeaderManager;
import com.algubra.volleymanager.VolleyAPIManager;
import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by gayatri on 15/5/17.
 */
public class LeaveRequestSubmissionActivity extends Activity implements JsonTagConstants, StausCodes, URLConstants {
    Button submitBtn;
    RelativeLayout relativeHeader;
    RelativeLayout headerStartDate;
    RelativeLayout headerEndDate;
    HeaderManager headermanager;
    ImageView back;
    ImageView dismissStartDatePicker;
    ImageView dismissEndDatePicker;
    Context mContext = this;
    EditText enterMessage, enterSubject;
    TextView enterStratDate, enterEndDate;
    LinearLayout submitLayout;
    Calendar c;
    int mYear;
    int mMonth;
    int mDay;
    SimpleDateFormat df;
    String formattedDate;
    Calendar calendar;
    String fromDate, toDate;
    String tomorrowAsString;
    Date sdate, edate;
    long elapsedDays;
    SingleDateAndTimePicker singleDateAndTimePicker;
    SingleDateAndTimePicker singleDateAndTimePickerEndDate;
    RelativeLayout pickerIOS;
    RelativeLayout pickerIOSEndDate;
    TextView done;
    TextView doneEndDate;
    String entryDate = "";
    String mEndDate = "";
    Date mEntryDate=new Date(System.currentTimeMillis());
     Date mEndDates=new Date(System.currentTimeMillis());
    String outputText;
    String outputTextend;
    SimpleDateFormat outputFormats;
    SimpleDateFormat outputFormat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_report_issues);
        initUI();
        resetDisconnectTimer();

       /* if(AppUtilityMethods.isNetworkConnected(mContext)) {
            submitLeave(URL_GET_LEAVEREQUESTSUBMISSION);
        }else{
            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
        }*/
    }

    private void initUI() {
        calendar = Calendar.getInstance();
         outputFormat =
                new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
         outputFormats =
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        headerStartDate = (RelativeLayout) findViewById(R.id.headerStartDate);
        headerEndDate = (RelativeLayout) findViewById(R.id.headerEndDate);
        pickerIOS = (RelativeLayout) findViewById(R.id.pickerIOS);
        pickerIOSEndDate = (RelativeLayout) findViewById(R.id.pickerIOSEndDate);
        enterSubject = (EditText) findViewById(R.id.enterSubject);
        enterMessage = (EditText) findViewById(R.id.enterMessage);
        enterStratDate = (TextView) findViewById(R.id.enterStratDate);
        done = (TextView) findViewById(R.id.done);
        dismissStartDatePicker = (ImageView) findViewById(R.id.dismissStartDatePicker);
        dismissEndDatePicker = (ImageView) findViewById(R.id.dismissEndDatePicker);
        doneEndDate = (TextView) findViewById(R.id.doneEndDate);
        enterEndDate = (TextView) findViewById(R.id.enterEndDate);
        submitLayout = (LinearLayout) findViewById(R.id.submitLayout);
        submitBtn = (Button) findViewById(R.id.submitBtn);

        headermanager = new HeaderManager(LeaveRequestSubmissionActivity.this, "New Leave Request");//changed on november 2,2017
        if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")) {
            headermanager.getHeader(relativeHeader, 1);
            submitBtn.setBackgroundResource(R.drawable.rounded_corner_loginbtn_bg);
            headerEndDate.setBackgroundResource(R.color.login_button_bg);
            headerStartDate.setBackgroundResource(R.color.login_button_bg);
        } else {
            headermanager.getHeader(relativeHeader, 3);
            submitBtn.setBackgroundResource(R.drawable.rounded_button_bg_blue);
            headerEndDate.setBackgroundResource(R.color.isg_int_blue);
            headerStartDate.setBackgroundResource(R.color.isg_int_blue);
        }
        back = headermanager.getLeftButton();


        headermanager.setButtonLeftSelector(R.drawable.backbtn,
                R.drawable.backbtn);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopDisconnectTimer();

                finish();
            }
        });
        enterSubject.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    pickerIOS.setVisibility(View.GONE);
                    pickerIOSEndDate.setVisibility(View.GONE);
                    enterMessage.clearFocus();
//                    Toast.makeText(getApplicationContext(), "Got the focus", Toast.LENGTH_LONG).show();
                } else {
//                    Toast.makeText(getApplicationContext(), "Lost the focus", Toast.LENGTH_LONG).show();
                }
            }
        });
        enterMessage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    pickerIOS.setVisibility(View.GONE);
                    pickerIOSEndDate.setVisibility(View.GONE);
                    enterSubject.clearFocus();

//                    Toast.makeText(getApplicationContext(), "Got the focus", Toast.LENGTH_LONG).show();
                } else {
//                    Toast.makeText(getApplicationContext(), "Lost the focus", Toast.LENGTH_LONG).show();
                }
            }
        });
        singleDateAndTimePicker = (SingleDateAndTimePicker) findViewById(R.id.single_day_picker);
        singleDateAndTimePicker.setStepMinutes(1);
        singleDateAndTimePicker.setMustBeOnFuture(true);
        singleDateAndTimePicker.setListener(new SingleDateAndTimePicker.Listener() {
            @Override
            public void onDateChanged(String displayed, Date date) {
//                display(displayed);

                outputText = outputFormat.format(date);
                mEntryDate = new Date(date.getTime());

                if (System.currentTimeMillis() < date.getTime()) {
                    if (enterEndDate.getText().toString().equalsIgnoreCase("")) {

                        entryDate = outputFormats.format(date);
                        enterStratDate.setText(outputText);
                    } else if (date.getTime() < mEndDates.getTime()) {
                        entryDate = outputFormats.format(date);
                        enterStratDate.setText(outputText);
                    }

                }


//                System.out.println("output::" + outputText);
//                System.out.println("Datee::" + date);
//                System.out.println("entryDate::" + entryDate);
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (System.currentTimeMillis() < mEntryDate.getTime()) {
                    if (enterEndDate.getText().toString().equalsIgnoreCase("")) {
                        enterStratDate.setText(outputText);
                        pickerIOS.setVisibility(View.GONE);
                    }
                    else if ( mEntryDate.getTime()< mEndDates.getTime())
                    {
                        enterStratDate.setText(outputText);
                        pickerIOS.setVisibility(View.GONE);
                    }
                    else
                    {
                        AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), "Choose start date less than selected end date.", R.drawable.infoicon, R.drawable.roundblue);

                    }

                }
                else if (System.currentTimeMillis()==mEntryDate.getTime())
                {
                    mEntryDate = new Date(System.currentTimeMillis());
                    entryDate = outputFormats.format(mEntryDate);
                    enterStratDate.setText(outputText);
                    pickerIOS.setVisibility(View.GONE);

                }
                else
                {
                    pickerIOS.setVisibility(View.VISIBLE);
                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), "Choose start time greater than current/past time.", R.drawable.infoicon, R.drawable.roundblue);

                }
            }
        });

        //End Date

        singleDateAndTimePickerEndDate = (SingleDateAndTimePicker) findViewById(R.id.single_day_pickerEndDate);
        singleDateAndTimePickerEndDate.setMustBeOnFuture(true);
        singleDateAndTimePickerEndDate.setStepMinutes(1);
        singleDateAndTimePickerEndDate.setListener(new SingleDateAndTimePicker.Listener() {
            @Override
            public void onDateChanged(String displayed, Date date) {
//                display(displayed);


                outputTextend = outputFormat.format(date);
                mEndDates = new Date(date.getTime());
                if (mEntryDate.getTime() < mEndDates.getTime()) {
                    if (System.currentTimeMillis() < date.getTime()) {
                        mEndDate = outputFormats.format(date);
                        enterEndDate.setText(outputTextend);
                    }
                }

             /*   else {
                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), "Choose end date greater than selected start date", R.drawable.infoicon, R.drawable.roundblue);

                }*/
//                System.out.println("output::" + outputText);
//                System.out.println("Datee::" + date);
//                System.out.println("mEndDate::" + mEndDate);
            }
        });
        doneEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEntryDate.getTime() < mEndDates.getTime()) {
                    enterEndDate.setText(outputTextend);
                    pickerIOSEndDate.setVisibility(View.GONE);
                } else if (mEntryDate.getTime() > mEndDates.getTime()) {
                    pickerIOSEndDate.setVisibility(View.VISIBLE);
                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), "Choose end date greater than selected start date.", R.drawable.infoicon, R.drawable.roundblue);

                } else {
                    pickerIOSEndDate.setVisibility(View.GONE);

                }
            }
        });
        dismissEndDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerIOSEndDate.setVisibility(View.GONE);

            }
        });
        dismissStartDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerIOS.setVisibility(View.GONE);

            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* if (enterSubject.getText().toString().trim().equals("")) {
                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.enter_subject), R.drawable.infoicon, R.drawable.roundblue);
                } else*/ if (enterStratDate.getText().toString().equals("")) {
                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.enter_startdate), R.drawable.infoicon, R.drawable.roundblue);
                } else if (enterMessage.getText().toString().trim().equals("")) {
                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.enter_reason), R.drawable.infoicon, R.drawable.roundblue);

                } else {
                    if (AppUtilityMethods.isNetworkConnected(mContext)) {
                        submitLeave(URL_GET_LEAVEREQUESTSUBMISSION);
                    } else {
                        AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
                    }
                }
                //AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.success), "Successfully submitted your leave requst", R.drawable.tick,  R.drawable.roundblue);
            }
        });

        enterStratDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("strDate---" + AppUtilityMethods.getCurrentDateToday());
                pickerIOS.setVisibility(View.VISIBLE);
                pickerIOSEndDate.setVisibility(View.GONE);

AppUtilityMethods.hideKeyBoard(mContext);
                enterSubject.clearFocus();
                enterMessage.clearFocus();
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                Date strDate = null;
//                String[] items1 = AppUtilityMethods.getCurrentDateToday().split("-");
//                String date1=items1[0];
//                //if()
//                String month=items1[1];
//                String year=items1[2];
//                try {
//                    strDate = sdf.parse(AppUtilityMethods.getCurrentDateToday());
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//               DatePickerDialog strDatePicker = new DatePickerDialog(mContext, startDate, Integer.parseInt(year),
//                        Integer.parseInt(month) - 1, Integer.parseInt(date1));
//                strDatePicker.getDatePicker().setMinDate(strDate.getTime());
//                strDatePicker.show();
            }
        });

        enterEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*System.out.println("strDate---"+AppUtilityMethods.getCurrentDateToday());

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date strDate = null;
                String[] items1 = AppUtilityMethods.getCurrentDateToday().split("-");
                String date1=items1[0];
                //if()
                String month=items1[1];
                String year=items1[2];
                try {
                    strDate = sdf.parse(AppUtilityMethods.getCurrentDateToday());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                DatePickerDialog endDatePicker = new DatePickerDialog(mContext, endDate, Integer.parseInt(year),
                        Integer.parseInt(month) - 1, Integer.parseInt(date1));
                endDatePicker.getDatePicker().setMinDate(strDate.getTime());
                endDatePicker.show();*/
                AppUtilityMethods.hideKeyBoard(mContext);
                enterSubject.clearFocus();
                enterMessage.clearFocus();
                if (enterStratDate.getText().toString().equals("")) {
                    Toast.makeText(mContext, "Please select start date first.", Toast.LENGTH_SHORT).show();
                } else {
                    pickerIOSEndDate.setVisibility(View.VISIBLE);
                    pickerIOS.setVisibility(View.GONE);

                }

                //System.out.println("From date---"+fromDate);
//                String[] items1 = AppUtilityMethods.getCurrentDateToday().split("-");
//                String date1=items1[2];
//                //if()
//                String month=items1[1];
//                String year=items1[0];
//                            /*new DatePickerDialog(context, checkoutDate, Integer.parseInt(year),
//                                    Integer.parseInt(month)-1, Integer.parseInt(date1)).show();*/
//                try {
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                    Date strDate = sdf.parse(AppUtilityMethods.getCurrentDateToday());
//                    DatePickerDialog checkoutDatepicker = new DatePickerDialog(mContext, endDate, Integer.parseInt(year),
//                            Integer.parseInt(month) - 1, Integer.parseInt(date1));
//                    checkoutDatepicker.getDatePicker().setMinDate(strDate.getTime());
//                    checkoutDatepicker.show();
//                    /*SimpleDateFormat dateFormatt = new SimpleDateFormat("dd MMM yyyy");
//
//                     sdate = sdf.parse(enterStratDate.getText().toString());
//                     edate = sdf.parse(enterEndDate.getText().toString());*/
//                }catch (Exception e){
//
//                }

            }
        });



        enterSubject.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                  /* Write your logic here that will be executed when user taps next button */
                    pickerIOS.setVisibility(View.VISIBLE);
                    pickerIOSEndDate.setVisibility(View.GONE);
                    AppUtilityMethods.hideKeyBoard(mContext);
                    enterSubject.clearFocus();
                    handled = true;
                }
                return handled;
            }
        });
    }

    private void display(String toDisplay) {
        Toast.makeText(this, toDisplay, Toast.LENGTH_SHORT).show();

    }

    private void submitLeave(String URL_API) {

        VolleyAPIManager volleyWrapper = new VolleyAPIManager(URL_API);
        String[] name = {"access_token", "studentId", "parentId", "from_date", "to_date", "reason"};
        fromDate=entryDate;
        if (enterEndDate.getText().toString().equals("")) {
//            toDate=fromDate;
            toDate = entryDate;
        }
        else
        {
            toDate=mEndDate;
        }
        String[] value = {AppPreferenceManager.getAccessToken(mContext), AppPreferenceManager.getStudentId(mContext), AppPreferenceManager.getUserId(mContext),
                fromDate, toDate, enterMessage.getText().toString().trim()};

        //String[] value={PreferenceManager.getAccessToken(mContext),mStaffList.get(pos).getStaffEmail(),JTAG_USERS_ID_VALUE,text_dialog.getText().toString(),text_content.getText().toString()};
        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyAPIManager.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("The response is" + successResponse);
                try {
                    JSONObject obj = new JSONObject(successResponse);
                    String response_code = obj.getString(JTAG_RESPONSECODE);
                    if (response_code.equalsIgnoreCase(RESPONSE_SUCCESS)) {
                        JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                        String status_code = secobj.getString(JTAG_STATUSCODE);
                        if (status_code.equalsIgnoreCase(STATUSCODE_SUCCESS)) {
                            showDialogSuccess((Activity) mContext, "Success", getString(R.string.succ_leave_submission), R.drawable.tick, R.drawable.roundblue);


                            // AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Success", getString(R.string.frgot_success_alert), R.drawable.tick,  R.drawable.roundblue);
                        } else if (status_code.equals(STATUSCODE_ALLREADYEXISTS)) {
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), "Already exists", R.drawable.infoicon, R.drawable.roundblue);

                        } else {
                            AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.common_error), R.drawable.infoicon, R.drawable.roundblue);

                        }
                    } else if (response_code.equalsIgnoreCase(RESPONSE_INTERNALSERVER_ERROR)) {
                        AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.internal_server_error), R.drawable.exclamationicon, R.drawable.roundblue);

                    } else if (response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) || response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) || response_code.equalsIgnoreCase(RESPONSE_INVALID_ACCESSTOKEN)) {
                        AppUtilityMethods.getToken(mContext, new AppUtilityMethods.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        submitLeave(URL_GET_LEAVEREQUESTSUBMISSION);

                    } else {
                        /*CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
								, getResources().getString(R.string.ok));
						dialog.show();*/
                        AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.roundblue);

                    }
                } catch (Exception ex) {
                    System.out.println("The Exception in edit profile is" + ex.toString());
                }

            }

            @Override
            public void responseFailure(String failureResponse) {
				/*CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
						, getResources().getString(R.string.ok));
				dialog.show();*/
                AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.roundblue);

            }
        });


    }

    public void showDialogSuccess(final Activity activity, String msgHead, String msg, int ico, int bgIcon) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_dialog_ok_layout);
        ImageView icon = (ImageView) dialog.findViewById(R.id.iconImageView);
        icon.setBackgroundResource(bgIcon);
        icon.setImageResource(ico);
        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        TextView textHead = (TextView) dialog.findViewById(R.id.alertHead);
        text.setText(msg);
        textHead.setText(msgHead);

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_Ok);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                /*Intent intent = new Intent(mContext, LeavesActivity.class);
                startActivity(intent);*/
                finish();
            }
        });

        dialog.show();

    }

    DatePickerDialog.OnDateSetListener startDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            fromDate = String.valueOf(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            enterStratDate.setText(AppUtilityMethods.dateConversionY(fromDate));
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date strDate = sdf.parse(fromDate);
                c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                df = new SimpleDateFormat("yyyy-MM-dd");
                formattedDate = df.format(c.getTime());
                Date endDate = sdf.parse(formattedDate);

                SimpleDateFormat dateFormatt = new SimpleDateFormat("dd MMM yyyy");
                Date convertedDate = new Date();
                try {
                    convertedDate = dateFormatt.parse(enterStratDate.getText().toString());
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                calendar.setTime(convertedDate);
                calendar.add(Calendar.DAY_OF_YEAR, 1);

                Date tomorrow = calendar.getTime();


                tomorrowAsString = dateFormatt.format(tomorrow);

                //System.out.println(todayAsString);
                System.out.println("Tomorrow--" + tomorrowAsString);
                //  enterEndDate.setText(tomorrowAsString);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


    };
    DatePickerDialog.OnDateSetListener endDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            toDate = String.valueOf(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

            if (!toDate.equals("")) {
                SimpleDateFormat dateFormatt = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    sdate = dateFormatt.parse(fromDate);
                    edate = dateFormatt.parse(toDate);
                    printDifference(sdate, edate);
                } catch (Exception e) {

                }
                if (elapsedDays < 0) {
                    AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), "Choose end date greater than selected start date", R.drawable.infoicon, R.drawable.roundblue);
                    //break;
                } else {
                    enterEndDate.setText(AppUtilityMethods.dateConversionY(toDate));

                }

                //AppUtilityMethods.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.enter_enddate), R.drawable.infoicon,  R.drawable.roundblue);
            }
/*
            enterEndDate.setText(AppUtilityMethods.dateConversionY(toDate));
*/
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date strDate = sdf.parse(toDate);
                c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                df = new SimpleDateFormat("yyyy-MM-dd");
                formattedDate = df.format(c.getTime());
                Date endDate = sdf.parse(formattedDate);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


    };

    public void printDifference(Date startDate, Date endDate) {

        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : " + endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays,
                elapsedHours, elapsedMinutes, elapsedSeconds);

    }

    /*public static final long DISCONNECT_TIMEOUT = 300000;; // 5 min = 5 * 60 * 1000 ms

    private Handler disconnectHandler = new Handler(){
        public void handleMessage(Message msg) {
        }
    };

    private Runnable disconnectCallback = new Runnable() {
        @Override
        public void run() {
            // Perform any required operation on disconnect
            AppPreferenceManager.setIsGuest(LeaveRequestSubmissionActivity.this, false);
            AppPreferenceManager.setUserId(LeaveRequestSubmissionActivity.this, "");
            AppPreferenceManager.setIsUserAlreadyLoggedIn(LeaveRequestSubmissionActivity.this, false);
            AppPreferenceManager.setSchoolSelection(LeaveRequestSubmissionActivity.this, "ISG");
            if (AppUtilityMethods.isAppInForeground(mContext)) {
                Intent mIntent = new Intent(LeaveRequestSubmissionActivity.this, LoginActivity.class);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mIntent);
                finish();
            }
        }
    };
*/
    public void resetDisconnectTimer(){
        HomeActivity.disconnectHandler.removeCallbacks(HomeActivity.disconnectCallback);
        HomeActivity.disconnectHandler.postDelayed(HomeActivity.disconnectCallback, HomeActivity.DISCONNECT_TIMEOUT);
    }

    public void stopDisconnectTimer(){
        HomeActivity.disconnectHandler.removeCallbacks(HomeActivity.disconnectCallback);
    }

    @Override
    public void onUserInteraction(){
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

  @Override protected void onPostResume() { super.onPostResume();
        if (AppPreferenceManager.getUserId(mContext).equalsIgnoreCase("")) {
            Intent mIntent = new Intent(LeaveRequestSubmissionActivity.this, LoginActivity.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mIntent);
            finish();
        }
    }


}
