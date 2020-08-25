package com.kyle.admin.ui;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.db.williamchart.view.BarChartView;
import com.db.williamchart.view.HorizontalBarChartView;
import com.kyle.admin.R;
import com.kyle.admin.login;
import com.kyle.admin.models.Statistics;
import com.kyle.admin.others.RingStatisticsView;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;

import es.dmoral.toasty.Toasty;
public class StatisticsViewer extends AppCompatActivity {
    Statistics statistics;
    String type;
    TextView counter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reactions);
        statistics = (Statistics) getIntent().getSerializableExtra("statistics");
        type = getIntent().getStringExtra("type");
        HorizontalBarChartView horizontalBarChartView = findViewById(R.id.horizontalBarChart);
        BarChartView barChart = findViewById(R.id.barChart);
        RingStatisticsView ringStatisticsView = findViewById(R.id.id_rsv);
        LinkedHashMap<String, Float> dataSet = new LinkedHashMap<>();
        TextView title = findViewById(R.id.title2);
        scroll = findViewById(R.id.ScrollView);
        counter = findViewById(R.id.counter);

        // todo add more here
        int[] baseColors = new int[]{Color.parseColor("#F9AA28"), Color.parseColor("#009752"), Color.parseColor("#2EC1FB"), Color.parseColor("#FA6723"), Color.parseColor("#8379b8"), Color.parseColor("#f4306d"), Color.parseColor("#01ba87"), Color.parseColor("#B8B8B8")};
        switch (type) {
            case "reactions":
                if ( statistics.getReactionsNumber() ==0){
                    Toasty.info(this,R.string.id_130, Toast.LENGTH_SHORT, true).show();

                    finish();
                    return;
                }
                title.setText(R.string.id_20);
                ringStatisticsView.setCenterText(getString(R.string.id_21));
                counter.setText(String.valueOf(statistics.getReactionsNumber()));
                dataSet.put(getString(R.string.id_22), (float) statistics.getInCallLikesNumber());
                dataSet.put(getString(R.string.id_23), (float) statistics.getLikesNumber());
                dataSet.put(getString(R.string.id_24), (float) statistics.getLovesNumber());
                ringStatisticsView.setCenterNumber(String.valueOf(statistics.getReactionsNumber()));
                float inCall = (float) statistics.getInCallLikesNumber() / (float) statistics.getReactionsNumber();
                float likes = (float) statistics.getLikesNumber() / (float) statistics.getReactionsNumber();
                float love = (float) statistics.getLovesNumber() / (float) statistics.getReactionsNumber();
                ringStatisticsView.setRatio(21);
                ringStatisticsView.setPercentAndColors(new String[]{getString(R.string.id_22),getString(R.string.id_23), getString(R.string.id_24)}, new float[]{inCall, likes, love}, new int[]{Color.parseColor("#009752"), Color.parseColor("#2EC1FB"), Color.parseColor("#FA6723")});
                ringStatisticsView.refresh();
                break;
            case "messages":
                if ( statistics.getMessagesList().size() ==0){
                    Toasty.info(this,R.string.id_130, Toast.LENGTH_SHORT, true).show();

                    finish();
                    return;
                }
                title.setText(R.string.id_25);
                ringStatisticsView.setCenterText(getString(R.string.id_26));
                counter.setText(String.valueOf(statistics.getMessagesList().size()));
                for (String type : statistics.getMessagesList()) {
                    type = getMessageRealKey(type, false);
                    if (dataSet.containsKey(type)) {
                        int oldCount = Math.round(dataSet.get(type));
                        dataSet.put(type, (float) oldCount + 1);
                    } else {
                        dataSet.put(type, 1f);
                    }
                }
                ringStatisticsView.setCenterText(getString(R.string.id_26));
                LinkedHashMap<String, Float> sortDataSet = new LinkedHashMap<>();
                for (String type : statistics.getMessagesList()) {
                    type = getMessageRealKey(type, true);
                    if (sortDataSet.containsKey(type)) {
                        int oldCount = Math.round(sortDataSet.get(type));
                        sortDataSet.put(type, (float) oldCount + 1);
                    } else {
                        sortDataSet.put(type, 1f);
                    }
                }
                int current_message = 0;
                String[] messages_titles = new String[dataSet.size()];
                float[] messages_percent = new float[dataSet.size()];
                int[] messages_colors = new int[dataSet.size()];
                for (String key : sortDataSet.keySet()) {
                    messages_titles[current_message] = key;
                    messages_percent[current_message] = sortDataSet.get(key) / (float) statistics.getMessagesList().size();
                    messages_colors[current_message] = baseColors[(current_message % baseColors.length)];
                    current_message++;
                }
                ringStatisticsView.setCenterNumber(String.valueOf(statistics.getMessagesList().size()));
                ringStatisticsView.setPercentAndColors(messages_titles, messages_percent, messages_colors);
                ringStatisticsView.refresh();
                break;
            case "devices":
                if ( statistics.getDeviceList().size() ==0){
                    Toasty.info(this,R.string.id_130, Toast.LENGTH_SHORT, true).show();

                    finish();
                    return;
                }
                title.setText(R.string.id_28);
                ringStatisticsView.setCenterText(getString(R.string.id_29));
                counter.setText(String.valueOf(statistics.getDeviceList().size()));

                for (int i : statistics.getDeviceList()) {
                    String os = getOsVersionName(i - 1);
                    if (dataSet.containsKey(os)) {
                        int oldCount = Math.round(dataSet.get(os));
                        dataSet.put(os, (float) oldCount + 1);
                    } else {
                        dataSet.put(os, 1f);
                    }
                }
                ringStatisticsView.setCenterNumber(String.valueOf(statistics.getDeviceList().size()));
                String[] devices_titles = new String[dataSet.size()];
                float[] devices_percent = new float[dataSet.size()];
                int[] devices_colors = new int[dataSet.size()];
                int current_device = 0;
                for (String key : dataSet.keySet()) {
                    devices_titles[current_device] = key;
                    devices_percent[current_device] = dataSet.get(key) / (float) statistics.getDeviceList().size();
                    devices_colors[current_device] = baseColors[(current_device % baseColors.length)];
                    current_device++;
                }
                ringStatisticsView.setRatio(21);
                ringStatisticsView.setPercentTextSize(dpToPx(10));
                ringStatisticsView.setPercentAndColors(devices_titles, devices_percent, devices_colors);
                ringStatisticsView.refresh();
                break;
            case "gender":
                if ( statistics.getUsersNumber() ==0){
                    Toasty.info(this, R.string.id_130, Toast.LENGTH_SHORT, true).show();

                    finish();
                    return;
                }
                title.setText(R.string.id_30);
                ringStatisticsView.setCenterText(getString(R.string.id_31));
                counter.setText(String.valueOf(statistics.getUsersNumber()));
                dataSet.put("", 0f);
                dataSet.put(getString(R.string.id_32), (float) statistics.getMaleUsersNumber());
                dataSet.put(getString(R.string.id_33), (float) statistics.getFemaleUsersNumber());
                dataSet.put(" ", 0f);
                ringStatisticsView.setCenterNumber(String.valueOf(statistics.getUsersNumber()));
                float man = (float) statistics.getMaleUsersNumber() / (float) statistics.getUsersNumber();
                float woman = (float) statistics.getFemaleUsersNumber() / (float) statistics.getUsersNumber();
                ringStatisticsView.setPercentAndColors(new String[]{getString(R.string.id_34), getString(R.string.id_35)}, new float[]{man, woman}, new int[]{Color.parseColor("#2EC1FB"), Color.parseColor("#FA6723")});
                ringStatisticsView.refresh();
                break;
        }
        barChart.getAnimation().setDuration(animationDuration);
        barChart.animate(dataSet);
        horizontalBarChartView.getAnimation().setDuration(animationDuration);
        horizontalBarChartView.animate(dataSet);
    }
    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
    static String getOsVersionName(int sdk) {
        String osName = "";
        String[] mapper = new String[]{
                "ANDROID BASE", "ANDROID BASE", "CUPCAKE", "DONUT",
                "ECLAIR", "ECLAIR", "ECLAIR", "FROYO",
                "GINGERBREAD", "GINGERBREAD", "HONEYCOMB", "HONEYCOMB",
                "HONEYCOMB", "SANDWICH", "SANDWICH", "JELLYBEAN",
                "JELLYBEAN", "JELLYBEAN", "KITKAT", "KITKAT",
                "LOLLIPOP", "LOLLIPOP", "MARSHMALLOW", "NOUGAT",
                "NOUGAT", "OREO", "OREO", "ANDROID P"};
        int index = sdk - 1;
        if (index < mapper.length) {
            osName = mapper[index];
        }
        if (osName.isEmpty()) {
            Field[] fields = Build.VERSION_CODES.class.getFields();
            for (Field field : fields) {
                String fieldName = field.getName();
                int fieldValue = -1;
                try {
                    fieldValue = field.getInt(new Object());
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                if (fieldValue == sdk) {
                    osName = fieldName;
                    break;
                }
            }
        }
        if (osName.isEmpty()) {
            osName = "UNKNOWN_VERSION";
        }
        return osName;
    }
    public String getMessageRealKey(String type, boolean shorten) {
        switch (type) {
            case "voice":
                return shorten ? getString(R.string.id_36) : getString(R.string.id_37);
            case "image":
                return getString(R.string.id_38);
            case "video_call":
                return shorten ? getString(R.string.id_39) : getString(R.string.id_40);
            case "voice_call":
                return shorten ? getString(R.string.id_39) : getString(R.string.id_41);
            case "text":
                return shorten ? getString(R.string.id_42) : getString(R.string.id_43);
            default:
                return getString(R.string.id_44);
        }
    }
    Long animationDuration = 1000L;
    ScrollView scroll;
    public void more(View view) {
        scroll.smoothScrollTo(0, scroll.getBottom());
    }
}