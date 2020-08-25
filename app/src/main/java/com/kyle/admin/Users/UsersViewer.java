package com.kyle.admin.Users;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kyle.admin.models.Country;
import com.kyle.admin.R;
import com.kyle.admin.others.Libs;

import java.util.ArrayList;

public class UsersViewer extends AppCompatActivity {
    ArrayList<User> UsersList;
    RecyclerView recyclerView;
    UsersAdapter UsersAdapter;
    ProgressDialog progressDialog;
    ArrayList<String> images;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users_viewer);
        UsersList = new ArrayList<>();
        UsersList =new ArrayList();
        recyclerView = findViewById(R.id.recyclerView);
        images = new ArrayList<>();

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.id_7));
       new Libs(this).loadCountries(new Libs.OnLoadListener() {
            @Override
            public void OnLoad(ArrayList<Country> countriesList) {

                loadUsers(countriesList);

            }
        });

    }
    public void loadUsers(final ArrayList<Country> countriesList) {
        FirebaseDatabase.getInstance().getReference("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot users) {
                UsersList.clear();
                for (DataSnapshot user : users.getChildren()) {
                    try {
                        String brief = String.valueOf(user.child("brief").getValue());
                        String age = "";
                        try {
                            age =  new Libs(UsersViewer.this).getAge(user);
                        } catch (Exception e) {
                            age = "";
                        }


                        String city = String.valueOf(user.child("city").getValue());
                        String country = String.valueOf(user.child("country").getValue());
                        String name = String.valueOf(user.child("name").getValue());
                        String profileImage = String.valueOf(user.child("profileImage").getValue());
                        String likes = String.valueOf(user.child("likes").getChildrenCount());
                        String love = String.valueOf(user.child("love").getChildrenCount());
                        boolean match_plus = (boolean) user.child("match_plus").getValue();
                        String fakeCountry = String.valueOf(user.child("fakeCountry").getValue());
                        String userCountryCode = "";
                        try {
                            userCountryCode = String.valueOf(user.child("CountryCode").getValue());
                        } catch (Exception e) {
                            userCountryCode = "";
                        }
                        boolean heIsVIP = false;
                        try {
                            heIsVIP = (boolean) user.child("VIP").getValue();
                        } catch (Exception e) {
                            heIsVIP = false;
                        }
                        boolean suspended = false;
                        try {
                            suspended = (boolean) user.child("suspended").getValue();
                        } catch (Exception e) {
                            suspended = false;
                        }
                        String gems = String.valueOf(user.child("gems").getValue());

                        images.clear();
                        for (DataSnapshot image : user.child("images").getChildren()) {
                            images.add(String.valueOf(image.getValue()));
                        }
                        StringBuilder  interests = new StringBuilder();
                        if (user.child("hobbies").getChildrenCount() == 0) {
                            interests.append("");
                        } else {
                            for (DataSnapshot interest : user.child("hobbies").getChildren()) {
                                interests.append(" #");
                                interests.append(interest.getValue());
                            }
                        }
                        String fakeName = String.valueOf(user.child("fakeName").getValue());
                        UsersList.add(new User(suspended,gems,user.getKey(), countriesList, fakeCountry, brief, country, city, name, profileImage, likes, love
                                , userCountryCode, 0, age, false, false, false, match_plus,
                                heIsVIP, images, String.valueOf(interests), fakeName));
                    }catch (Exception e){

                    }

                }
                UsersAdapter=new UsersAdapter(UsersViewer.this,UsersList);
                recyclerView.setAdapter(UsersAdapter);
                UsersAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

}