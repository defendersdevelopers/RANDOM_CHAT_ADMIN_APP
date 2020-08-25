package com.kyle.admin.ui;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.kyle.admin.R;
import com.kyle.admin.Reports.ReportsViewer;
import com.kyle.admin.Users.UsersViewer;
import com.kyle.admin.login;
import com.kyle.admin.models.Statistics;
import com.kyle.admin.others.Libs;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;
public class MainActivity extends AppCompatActivity {
    EditText email;
    TextView about_us_text, privacy_text, terms_text;
    Statistics statistics;
    View navView;
    TextView usersNumberTextView, onlineUsersNumberTextView, offlineUsersNumberTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email = findViewById(R.id.email);
        about_us_text = findViewById(R.id.about_us_text);
        privacy_text = findViewById(R.id.privacy_text);
        terms_text = findViewById(R.id.terms_text);
        navView = findViewById(R.id.navView);
        usersNumberTextView = findViewById(R.id.usersNumberTextView);
        onlineUsersNumberTextView = findViewById(R.id.onlineUsersNumberTextView);
        offlineUsersNumberTextView = findViewById(R.id.offlineUsersNumberTextView);
        loadStatistics();
        loadData();
        final CheckBox notify = findViewById(R.id.notify);
        final SharedPreferences data = getSharedPreferences("data", 0);
        notify.setChecked(data.getBoolean("random_notify", false));
        notify.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    FirebaseMessaging.getInstance().subscribeToTopic("random_notify");
                } else {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("random_notify");
                }
                data.edit().putBoolean("random_notify", checked).apply();
            }
        });
    }
    @Override
    public void onBackPressed() {
        boolean v = navView.getVisibility() == View.VISIBLE;
        if (v) {
            navView.setVisibility(View.GONE);
            return;
        }
        super.onBackPressed();
    }
    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }
    public void loadData() {
        FirebaseDatabase.getInstance().getReference("data").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot data) {
                String data_email = String.valueOf(data.child("email").getValue());
                String data_about_us = String.valueOf(data.child("about_us").getValue());
                String data_privacy = String.valueOf(data.child("privacy").getValue());
                String data_terms = String.valueOf(data.child("terms").getValue());
                if (!data_email.isEmpty() && !data_email.equals("null")) {
                    email.setText(data_email);
                }
                if (!data_terms.isEmpty() && !data_terms.equals("null")) {
                    String[] arr = data_terms.split("\\s+");
                    int N = 50; // NUMBER OF WORDS THAT YOU NEED
                    if (countWords(data_terms) < 50) {
                        terms_text.setText(data_terms.replace("\\n", "\n"));
                    } else {
                        String nWords = "";
                        for (int i = 0; i < N; i++) {
                            nWords = nWords + " " + arr[i];
                        }
                        terms_text.setText(nWords.replace("\\n", "\n") + "...");
                    }
                }
                //  ========
                if (!data_about_us.isEmpty() && !data_about_us.equals("null")) {
                    String[] arr = data_about_us.split("\\s+");
                    int N = 50; // NUMBER OF WORDS THAT YOU NEED
                    if (countWords(data_about_us) < 50) {
                        about_us_text.setText(data_about_us.replace("\\n", "\n"));
                    } else {
                        String nWords = "";
                        for (int i = 0; i < N; i++) {
                            nWords = nWords + " " + arr[i];
                        }
                        about_us_text.setText(nWords.replace("\\n", "\n") + "...");
                    }
                }
                if (!data_privacy.isEmpty() && !data_privacy.equals("null")) {
                    String[] arr = data_privacy.split("\\s+");
                    int N = 50; // NUMBER OF WORDS THAT YOU NEED
                    if (countWords(data_privacy) < 50) {
                        privacy_text.setText(data_privacy.replace("\\n", "\n"));
                    } else {
                        String nWords = "";
                        for (int i = 0; i < N; i++) {
                            nWords = nWords + " " + arr[i];
                        }
                        privacy_text.setText(nWords.replace("\\n", "\n") + "...");
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    public static int countWords(String s) {
        int wordCount = 0;
        boolean word = false;
        int endOfLine = s.length() - 1;
        for (int i = 0; i < s.length(); i++) {
            // if the char is a letter, word = true.
            if (Character.isLetter(s.charAt(i)) && i != endOfLine) {
                word = true;
                // if char isn't a letter and there have been letters before,
                // counter goes up.
            } else if (!Character.isLetter(s.charAt(i)) && word) {
                wordCount++;
                word = false;
                // last word of String; if it doesn't end with a non letter, it
                // wouldn't count without this.
            } else if (Character.isLetter(s.charAt(i)) && i == endOfLine) {
                wordCount++;
            }
        }
        return wordCount;
    }
    public void edit_about_us(View view) {
        showEditDialog("about_us", getString(R.string.id_8));
    }
    public void edit_privacy(View view) {
        showEditDialog("privacy", getString(R.string.id_9));
    }
    public void save_changes(View view) {
        String s_email = email.getText().toString();
        if (s_email.isEmpty()) {
            Toasty.info(this, R.string.id_1, Toast.LENGTH_SHORT, true).show();
        } else {
            FirebaseDatabase.getInstance().getReference("data").child("email").setValue(s_email);
            Toasty.success(this, R.string.id_2, Toast.LENGTH_SHORT, true).show();
        }
    }
    public void showEditDialog(final String key, String title) {
        LayoutInflater factory = LayoutInflater.from(this);
        final View dialogView = factory.inflate(R.layout.dialog_edit, null);
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        TextView titleView = dialogView.findViewById(R.id.title);
        titleView.setText(title);
        final EditText enter = dialogView.findViewById(R.id.enter);
        TextView ok = dialogView.findViewById(R.id.ok);
        final TextView cancel = dialogView.findViewById(R.id.cancel);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = enter.getText().toString();
                if (value.isEmpty()) {
                    Toasty.info(MainActivity.this, R.string.id_1, Toast.LENGTH_SHORT, true).show();
                } else {
                    FirebaseDatabase.getInstance().getReference("data").child(key).setValue(value);
                    Toasty.success(MainActivity.this, R.string.id_2, Toast.LENGTH_SHORT, true).show();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setView(dialogView);
        dialog.show();
    }
    public void reports(View view) {
        startActivity(new Intent(this, ReportsViewer.class));
    }
    public void users(View view) {
        startActivity(new Intent(this, UsersViewer.class));
    }
    public void map(View view) {
        startActivity(new Intent(this, UsersViewer.class));
    }
    public void loadStatistics() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.id_7));
        progressDialog.setCancelable(false);
        progressDialog.show();
        statistics = new Statistics();
        FirebaseDatabase.getInstance().getReference("users").addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot users) {
                progressDialog.dismiss();
                int FemaleUsersNumber = 0, maleUsersNumber, usersNumber, reactionsNumber, lovesNumber = 0, likesNumber = 0, inCallLikesNumber = 0;
                ArrayList<Integer> deviceList = new ArrayList<>();
                ArrayList<String> onlineUsers = new ArrayList<>();
                ArrayList<String> messagesList = new ArrayList<>();
                usersNumber = (int) users.getChildrenCount();
                for (DataSnapshot user : users.getChildren()) {
                    try {
                        if (user.child("gender").getValue().equals("female")) {
                            FemaleUsersNumber++;
                        }
                    } catch (Exception e) {
                    }
                    for (DataSnapshot friend : user.child("friends").getChildren()) {
                        for (DataSnapshot message : friend.child("chat").getChildren()) {
                            messagesList.add(String.valueOf(message.child("kind").getValue()));
                        }
                    }
                    long lastActiveDateTime = Long.parseLong(String.valueOf(user.child("last_active").getValue()));
                    String timeAgo = getTimeAgo(lastActiveDateTime);
                    if (timeAgo.equals("Online")) {
                        onlineUsers.add(user.getKey());
                    }
                    likesNumber += (int) user.child("likes").getChildrenCount();
                    lovesNumber += (int) user.child("love").getChildrenCount();
                    inCallLikesNumber += (int) user.child("likes_in_call").getChildrenCount();
                    try {
                        deviceList.add(Integer.parseInt(String.valueOf(user.child("android_version").getValue())));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                reactionsNumber = likesNumber + lovesNumber + inCallLikesNumber;
                maleUsersNumber = usersNumber - FemaleUsersNumber;
                //todo  onlineUsers
                statistics.setUsersNumber(usersNumber);
                statistics.setFemaleUsersNumber(FemaleUsersNumber);
                statistics.setMaleUsersNumber(maleUsersNumber);
                statistics.setLikesNumber(likesNumber);
                statistics.setLovesNumber(lovesNumber);
                statistics.setInCallLikesNumber(inCallLikesNumber);
                statistics.setReactionsNumber(reactionsNumber);
                statistics.setDeviceList(deviceList);
                statistics.setMessagesList(messagesList);
                usersNumberTextView.setText(usersNumber + " " + getString(R.string.id_13) + " ");
                onlineUsersNumberTextView.setText(onlineUsers.size() + " " + getString(R.string.id_13) + " ");
                offlineUsersNumberTextView.setText(usersNumber - onlineUsers.size() + " " + getString(R.string.id_13) + " ");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    public static long getTimeNow() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
        return cal.getTimeInMillis();
    }
    public String getTimeAgo(long lastActive) {
        String time_ago = "";
        try {
            long now = getTimeNow();
            long seconds = TimeUnit.MILLISECONDS.toSeconds(now - lastActive);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(now - lastActive);
            long hours = TimeUnit.MILLISECONDS.toHours(now - lastActive);
            long days = TimeUnit.MILLISECONDS.toDays(now - lastActive);
            if (seconds < 60) {
                time_ago = seconds + getString(R.string.id_14);
            } else if (minutes < 60) {
                time_ago = minutes + getString(R.string.id_15);
            } else if (hours < 24) {
                time_ago = hours + getString(R.string.id_16);
            } else {
                time_ago = days + getString(R.string.id_17);
            }
            if (seconds < 7) {
                return getString(R.string.id_18);
            }
        } catch (Exception j) {
            j.printStackTrace();
            time_ago = "";
        }
        return getString(R.string.id_19) + time_ago;
    }
    public void reactions(View view) {
        startActivity(new Intent(this, StatisticsViewer.class).putExtra("statistics", statistics).putExtra("type", "reactions"));
    }
    public void nav(View view) {
        navView.setVisibility(View.VISIBLE);
    }
    public void hideNav(View view) {
        navView.setVisibility(View.GONE);
    }
    public void devices(View view) {
        startActivity(new Intent(this, StatisticsViewer.class).putExtra("statistics", statistics).putExtra("type", "devices"));
    }
    public void gender(View view) {
        startActivity(new Intent(this, StatisticsViewer.class).putExtra("statistics", statistics).putExtra("type", "gender"));
    }
    public void messages(View view) {
        startActivity(new Intent(this, StatisticsViewer.class).putExtra("statistics", statistics).putExtra("type", "messages"));
    }
    public void send_notification(View view) {
        new Libs(this).sendNotification("notifications");
    }
    public void edit_terms(View view) {
        showEditDialog("terms", getString(R.string.id_131));
    }
    public void logOut(View view) {
        finish();
        getSharedPreferences("data", MODE_PRIVATE).edit().putBoolean("RememberMe", false).apply();
        startActivity(new Intent(MainActivity.this, login.class));
    }
}