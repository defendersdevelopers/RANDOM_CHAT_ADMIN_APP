package com.kyle.admin.Reports;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;
import com.kyle.admin.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
public class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.MyViewHolder> {
   public Activity context;
    ArrayList<Report> Reports;
    public ReportsAdapter(Activity context, ArrayList<Report> Reports) {
        this.context = context;
        this.Reports = Reports;
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profileImage;
        View block;
        RecyclerView imagesViewer;
        TextView name, improperLanguageReportsCount, improperBehaviorReportsCount;
        public MyViewHolder(View itemView,Context context) {
            super(itemView);
            this.profileImage = itemView.findViewById(R.id.profileImage);
            this.block = itemView.findViewById(R.id.block);
            this.improperLanguageReportsCount = itemView.findViewById(R.id.improperLanguageReportsCount);
            this.improperBehaviorReportsCount = itemView.findViewById(R.id.improperBehaviorReportsCount);
            this.name = itemView.findViewById(R.id.name);
            this.imagesViewer = itemView.findViewById(R.id.imagesViewer);
           imagesViewer.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            imagesViewer.setHasFixedSize(true);
        }
    }
    @Override
    public ReportsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report, parent, false);
        return new ReportsAdapter.MyViewHolder(view,context);
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Report report = Reports.get(position);
        Picasso.get().load(report.image).placeholder(R.drawable.loading).into(holder.profileImage);
        holder.name.setText(report.name);
        holder.improperBehaviorReportsCount.setText( report.improperBehaviorReportsCount +" "+context.getString(R.string.id_129));
        holder.improperLanguageReportsCount.setText(report.improperLanguageReportsCount +" "+ context.getString(R.string.id_129));
        holder.block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase.getInstance().getReference("users").child(report.reportedUid).child("suspended").setValue(true);
                Toasty.success(context, R.string.id_6, Toast.LENGTH_SHORT, true).show();
                Reports.remove(position);
                notifyDataSetChanged();
            }
        });


        holder.imagesViewer.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                return new RecyclerView.ViewHolder(inflater.inflate(R.layout.item_image_no_rmove, parent, false)) {
                    @Override
                    public String toString() {
                        return super.toString();
                    }
                };
            }
            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                ImageView imageView = holder.itemView.findViewById(R.id.image);
                Picasso.get().load(report.reportsImages.get(position)).placeholder(R.drawable.loading).into(imageView);
            }
            @Override
            public int getItemCount() {
                return report.reportsImages.size();
            }
        });
    }
    @Override
    public int getItemCount() {
        return Reports.size();
    }
}
