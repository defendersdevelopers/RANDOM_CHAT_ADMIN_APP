package com.kyle.admin.Reports;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kyle.admin.R;

import java.util.ArrayList;
public class ReportsViewer extends AppCompatActivity {
    ArrayList<Report> tempReportsList;
    RecyclerView recyclerView;
    ReportsAdapter ReportsAdapter;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reports_viewer);
        tempReportsList = new ArrayList<>();
        ReportsList =new ArrayList();
        recyclerView = findViewById(R.id.recyclerView);
        ReportsAdapter=new ReportsAdapter(this,ReportsList);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(ReportsAdapter);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.id_7));
        loadReports();
    }
    public void loadReports() {
        progressDialog.show();
        FirebaseDatabase.getInstance().getReference("reports").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot reports) {
                if (reports.exists()){
                    tempReportsList.clear();
                    for (DataSnapshot reportItem : reports.getChildren()) {
                        String reportedUid = String.valueOf(reportItem.getKey());
                        int improperLanguageReportsCount = 0;
                        int improperBehaviorReportsCount = 0;
                        ArrayList<String> images=new ArrayList<>();
                        for (DataSnapshot report : reportItem.getChildren()) {
                            String reportType = String.valueOf(report.child("type").getValue());
                            String reportImage = String.valueOf(report.child("image").getValue());
                            if (reportType.equals("improperLanguage")) {
                                improperLanguageReportsCount++;
                            }
                            if (reportType.equals("improperBehavior")) {
                                improperBehaviorReportsCount++;
                            }
                            images.add(reportImage);
                        }


                        Report report = new Report(improperLanguageReportsCount, improperBehaviorReportsCount, reportedUid, "", "",images);
                        tempReportsList.add(report);
                    }
                        loadReportsData();

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
    }
    ArrayList<Report> ReportsList;
    public void loadReportsData() {
        FirebaseDatabase.getInstance().getReference("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot users) {
                ReportsList.clear();
                if (!tempReportsList.isEmpty()){
                    for (int i = 0; i < tempReportsList.size(); i++) {
                        Report report = tempReportsList.get(i);
                        String name = String.valueOf(users.child(report.reportedUid).child("name").getValue());
                        String profileImage = String.valueOf(users.child(report.reportedUid).child("profileImage").getValue());
                        boolean suspended;
                        try {
                            suspended   = (boolean) users.child(report.reportedUid).child("suspended").getValue();
                        }catch (Exception e){
                            suspended=false;
                        }


                        if (suspended){
                          //  tempReportsList.remove(i);
                        }else {
                            tempReportsList.get(i).image = profileImage;
                            tempReportsList.get(i).name = name;
                            ReportsList.add(tempReportsList.get(i));
                        }


                    }
                }
                ReportsAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}