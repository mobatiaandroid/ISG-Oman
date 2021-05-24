package com.algubra.activity.reportissues;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.algubra.R;
import com.algubra.activity.home.HomeActivity;
import com.algubra.activity.login.LoginActivity;
import com.algubra.activity.reportissues.adapter.IssuesAdapter;
import com.algubra.activity.reportissues.model.IssueModel;
import com.algubra.manager.AppPreferenceManager;
import com.algubra.manager.HeaderManager;
import com.algubra.recyclerviewmanager.ItemOffsetDecoration;

import java.util.ArrayList;

/**
 * Created by gayatri on 24/4/17.
 */
public class ReportIssuesListActivity extends Activity {
    RecyclerView listViewIssues;
    ArrayList<IssueModel> listIssue;
    Activity mContext;
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back,addReport;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_issue_list);
        mContext = this;
        initUI();
        resetDisconnectTimer();

    }

    private void initUI() {

        listIssue = new ArrayList<>();
        IssueModel model = new IssueModel();
        model.setIssue("Lorem ipsum dolor sit amet, consectetur");
        model.setDate("31-Jan-2017");
        model.setIssue("Lorem ipsum dolor sit amet, consectetur");
        model.setDate("31-Jan-2017");

        listIssue.add(model);

        listViewIssues = (RecyclerView) findViewById(R.id.recyclerIssues);
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        listViewIssues.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listViewIssues.setLayoutManager(llm);
        int spacing = 10; // 50px
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext,spacing);
        listViewIssues.addItemDecoration(itemDecoration);
        headermanager = new HeaderManager(ReportIssuesListActivity.this, getString(R.string.report_issues));
        if (AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")) {
            headermanager.getHeader(relativeHeader, 1);
        } else {
            headermanager.getHeader(relativeHeader, 3);
        }
        back = headermanager.getLeftButton();
        headermanager.setButtonLeftSelector(R.drawable.backbtn,
                R.drawable.backbtn);
        addReport=headermanager.getRightButton();
        headermanager.setButtonRightSelector(R.drawable.add_report, R.drawable.add_report);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopDisconnectTimer();

                finish();
            }
        });
        addReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopDisconnectTimer();

                Intent intent = new Intent(ReportIssuesListActivity.this, AddReportActivity.class);
                startActivity(intent);
            }
        });
        IssuesAdapter adapter = new IssuesAdapter(mContext,listIssue);
        listViewIssues.setAdapter(adapter);

        /*listViewIssues.addOnItemTouchListener(new RecyclerItemListener(getApplicationContext(), listViewIssues,
                new RecyclerItemListener.RecyclerTouchListener() {
                    public void onClickItem(View v, int position) {
                        Intent intent = new Intent(ReportIssuesListActivity.this, AddReportActivity.class);
                        startActivity(intent);
                    }

                    public void onLongClickItem(View v, int position) {
                        System.out.println("On Long Click Item interface");
                    }
                }));*/
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
            AppPreferenceManager.setIsGuest(ReportIssuesListActivity.this, false);
            AppPreferenceManager.setUserId(ReportIssuesListActivity.this, "");
            AppPreferenceManager.setIsUserAlreadyLoggedIn(ReportIssuesListActivity.this, false);
            AppPreferenceManager.setSchoolSelection(ReportIssuesListActivity.this, "ISG");
            if (AppUtilityMethods.isAppInForeground(mContext)) {

                Intent mIntent = new Intent(ReportIssuesListActivity.this, LoginActivity.class);
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
            Intent mIntent = new Intent(ReportIssuesListActivity.this, LoginActivity.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mIntent);
            finish();
        }
    }
}
