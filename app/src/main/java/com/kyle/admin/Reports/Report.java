package com.kyle.admin.Reports;
import java.util.ArrayList;
public class Report {
    public int improperLanguageReportsCount, improperBehaviorReportsCount;
    public String reportedUid,name,image;
ArrayList<String> reportsImages;
    public Report(int improperLanguageReportsCount, int improperBehaviorReportsCount, String reportedUid
    ,String name,String image,ArrayList<String> reportsImages) {
        this.improperLanguageReportsCount = improperLanguageReportsCount;
        this.improperBehaviorReportsCount = improperBehaviorReportsCount;
        this.reportedUid = reportedUid;
        this.name=name;
        this.image=image;
        this.reportsImages=reportsImages;

    }
}
