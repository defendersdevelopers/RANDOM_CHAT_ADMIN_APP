package com.kyle.admin.Users;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;
import com.kyle.admin.R;
import com.kyle.admin.others.Libs;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
public class UsersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Activity context;
    ArrayList<User> Users;
    Libs libs;
    public UsersAdapter(Activity context, ArrayList<User> Users) {
        this.context = context;
        this.Users = Users;
        libs = new Libs(context);
    }
    private ViewGroup mParent;
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mParent = parent;
        return new RecyclerView.ViewHolder(context.getLayoutInflater().inflate(R.layout.item_user, parent, false)) {
            @Override
            public String toString() {
                return super.toString();
            }
        };
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        View view = holder.itemView;
        final User user = Users.get(position);
        ImageView profileImageViewer, countryFlag2Viewer;
        TextView nameViewer, briefViewer, country2Viewer, likesViewer, loveViewer;
        profileImageViewer = view.findViewById(R.id.profileImage);
        countryFlag2Viewer = view.findViewById(R.id.countryFlag2);

        final TextView gems = view.findViewById(R.id.gemsViewer);
        View block = view.findViewById(R.id.block);
        view.findViewById(R.id.add_gems).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater factory = LayoutInflater.from(context);
                final View dialogView = factory.inflate(R.layout.dialog_add_gems, null);
                final AlertDialog dialog = new AlertDialog.Builder(context).create();
                final EditText enter = dialogView.findViewById(R.id.enter);
                TextView ok = dialogView.findViewById(R.id.ok);
                final TextView cancel = dialogView.findViewById(R.id.cancel);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String value = enter.getText().toString();
                        if (value.isEmpty()) {
                            Toasty.info(context, R.string.id_1, Toast.LENGTH_SHORT, true).show();
                        } else {
                            user.gems = value;
                            gems.setText(value);
                            FirebaseDatabase.getInstance().getReference("users").child(user.userId).child("gems").setValue(value);
                            Toasty.success(context, R.string.id_46, Toast.LENGTH_SHORT, true).show();
                        }
                        dialog.dismiss();
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
        });
        view.findViewById(R.id.notify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Libs(context).sendNotification(user.userId);
            }
        });
        gems.setText(user.gems);



        final Switch vip = view.findViewById(R.id.vip);
        vip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                user.heIsVIP = b;
                FirebaseDatabase.getInstance().getReference("users").child(user.userId).child("VIP").setValue(b);
            }
        });
        vip.setChecked(user.heIsVIP);



        final Switch matchPlus = view.findViewById(R.id.matchPlus);
        matchPlus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                user.heIsUsingMatchPlus = b;
                FirebaseDatabase.getInstance().getReference("users").child(user.userId).child("iamUsingMatchPlus").setValue(b);
            }
        });
        matchPlus.setChecked(user.heIsUsingMatchPlus);








        TextView blockText = view.findViewById(R.id.blockText);
        blockText.setText(user.suspended ? context.getString(R.string.id_47) : context.getString(R.string.id_48));
        block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("users").child(user.userId).child("suspended").setValue(!user.suspended);
                user.suspended = !user.suspended;
                notifyDataSetChanged();
            }
        });
        nameViewer = view.findViewById(R.id.name);
        briefViewer = view.findViewById(R.id.bio);
        country2Viewer = view.findViewById(R.id.country2);
        likesViewer = view.findViewById(R.id.likes);
        loveViewer = view.findViewById(R.id.love);
        View placeView = view.findViewById(R.id.placeView);
        String finalName;
        if (!user.fakeName.equals("null") && user.fakeName != null && !user.fakeName.isEmpty()) {
            finalName = user.fakeName;
        } else {
            finalName = user.name;
        }
        if (user.hideAge) {
            nameViewer.setText(finalName);
        } else {
            nameViewer.setText(finalName + "/" + user.age);
        }
        Picasso.get().load(user.profileImage).placeholder(R.drawable.loading).into(profileImageViewer);
        briefViewer.setText(user.brief);
        if (user.hideLocation) {
            country2Viewer.setVisibility(View.GONE);
            countryFlag2Viewer.setVisibility(View.GONE);
            placeView.setVisibility(View.GONE);
        } else {
            placeView.setVisibility(View.VISIBLE);
            String path = libs.getCountryFlagWithCountryCode(user.countriesList, user.userCountryCode);
            if (!user.fakeCountry.equals("null") && user.fakeCountry != null) {
                country2Viewer.setText(user.fakeCountry);
            } else {
                country2Viewer.setText(user.country);
            }
            Picasso.get().load(path).into(countryFlag2Viewer);
            likesViewer.setText(user.likes);
            loveViewer.setText(user.love);
        }
    }
    @Override
    public int getItemCount() {
        return Users.size();
    }
}
