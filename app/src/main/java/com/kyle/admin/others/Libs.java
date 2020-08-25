package com.kyle.admin.others;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kyle.admin.R;
import com.kyle.admin.models.Country;
import com.kyle.admin.ui.MainActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import es.dmoral.toasty.Toasty;

import static android.content.Context.MODE_PRIVATE;
public class Libs {
    Context context;
    SharedPreferences prefs;

    public void sendNotification(final String to){
        LayoutInflater factory = LayoutInflater.from(context);
        final View dialogView = factory.inflate(R.layout.dialog_send_notification, null);
        final AlertDialog dialog = new AlertDialog.Builder(context).create();

        final EditText title = dialogView.findViewById(R.id.title);
        final EditText content = dialogView.findViewById(R.id.content);



        dialogView.findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleStr=title.getText().toString();
                String contentStr=content.getText().toString();
                if (titleStr.isEmpty()||contentStr.isEmpty()) {
                    Toasty.info(context, R.string.id_1, Toast.LENGTH_SHORT, true).show();
                }else {
                    HashMap<String,Object> hashMap =new HashMap<>();
                    hashMap.put("to",to );
                    hashMap.put("title",titleStr);
                    hashMap.put("content",contentStr);


                    FirebaseDatabase.getInstance().getReference("notifications").push().updateChildren(hashMap);
                    Toasty.success(context, R.string.id_2, Toast.LENGTH_SHORT, true).show();
                }
                dialog.dismiss();
            }
        });

        dialogView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setView(dialogView);
        dialog.show();
    }
    public Libs(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences("data", MODE_PRIVATE);
    }
    public boolean isLoggedDone() {
        return !getUserId().isEmpty() && !getUserData("username").isEmpty() && !getUserData("Uid").isEmpty();
    }
    public void saveData(String name, String value) {
        prefs.edit().putString(name, value).apply();
    }
    public String getUserData(String key) {
        return prefs.getString(key, "");
    }
    public static String getUserId() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            return FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        return "";
    }
    public void setVipMode(Boolean vipMode) {
        ShowActivity("vip", vipMode);
        ShowActivity("classic", !vipMode);
    }
    public void ShowActivity(String activity_name, Boolean show) {
        context.getPackageManager().setComponentEnabledSetting(new ComponentName(context.getPackageName(), context.getPackageName() + "." + activity_name), show ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED : PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }
    public static long getTimeNow() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
        return cal.getTimeInMillis();
    }
    public void addGemsToUser(final int gems, final String UID) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(UID).child("gems");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int oldGems = Integer.parseInt(String.valueOf(dataSnapshot.getValue()));
                int newGems = gems + oldGems;
                FirebaseDatabase.getInstance().getReference("users").child(UID).child("gems").setValue(newGems);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    public String getAge(DataSnapshot user) {
        return String.valueOf(getAge(Long.parseLong(String.valueOf(user.child("birthdayDate").getValue()))));
    }
    public Integer getAge(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(time));
        int year, month, day;
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
        return getAge(year, month, day);
    }
    public Integer getAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        dob.set(year, month, day);
        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        return age;
    }


    public interface OnLoadListener {
        public void OnLoad(ArrayList<Country> countriesList);
    }
    public void loadCountries(final OnLoadListener onLoadListener) {
        final ArrayList<Country> countriesList = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("countriesList").addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot countriesListDataSnapshot) {
                for (DataSnapshot currentC : countriesListDataSnapshot.getChildren()) {
                    String code, name, flag;
                    code = currentC.child("code").getValue().toString();
                    if (currentC.child("localized").child(getLanguage(true)).exists()) {
                        name = String.valueOf(currentC.child("localized").child(getLanguage(true)).getValue());
                    } else {
                        name = String.valueOf(currentC.child("localized").child("Default").getValue());
                    }
                    flag = currentC.child("flag").getValue().toString();
                    countriesList.add(new Country(code, name, flag));
                }
                onLoadListener.OnLoad(countriesList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    public String getLanguage(boolean upCases) {
        String str = Locale.getDefault().getLanguage();
        return upCases ? str.toUpperCase() : str.toLowerCase();
    }
    public String getCountryCodeLib() {
        String result;
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            result = tm.getNetworkCountryIso();
        } catch (Exception e) {
            result = Locale.getDefault().getCountry();
        }
        if (result == null || result.equals("")) {
            result = Locale.getDefault().getCountry();
        }
        return result;
    }
    public String getCountryFlagWithCountryCode(ArrayList<Country> countriesList, String CountryCode) {
        String CountryFlag = "https://i.pinimg.com/originals/9c/94/60/9c94604be48438303581d788f06267e1.jpg";
        for (Country country : countriesList) {
            if (country.CountryCode.toLowerCase().equals(CountryCode.toLowerCase())) {
                CountryFlag = country.CountryFlag;
            }
        }

        return CountryFlag;
    }

    public String getCountryCodeWithCountryArabicName(ArrayList<Country> countriesList, String CountryArabicName) {
        String CountryCode = "";
        for (Country country : countriesList) {
            if (country.CountryName.equals(CountryArabicName)) {
                CountryCode = country.CountryFlag;
            }
        }
        return CountryCode;
    }
    public String getCountryNameWithCountryCode(ArrayList<Country> countriesList, String code) {
        String CountryName = "Other";
        for (Country country : countriesList) {
            if (country.CountryCode.equals(code)) {
                CountryName = country.CountryFlag;
            }
        }
        return CountryName;
    }

}
