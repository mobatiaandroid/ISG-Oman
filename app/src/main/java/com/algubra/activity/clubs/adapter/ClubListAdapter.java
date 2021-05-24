package com.algubra.activity.clubs.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.algubra.R;
import com.algubra.activity.clubs.model.ClubModels;
import com.algubra.manager.AppPreferenceManager;

import java.util.ArrayList;

/**
 * Created by gayatri on 3/5/17.
 */
public class ClubListAdapter extends RecyclerView.Adapter<ClubListAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<ClubModels> mClubArrayList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView clubName;
        LinearLayout layoutList,lineLinear;
        ImageView eventsbg;
        public MyViewHolder(View view) {
            super(view);

            clubName = (TextView) view.findViewById(R.id.clubName);
            eventsbg= (ImageView) view.findViewById(R.id.eventsbg);

        }
    }


    public ClubListAdapter(Context mContext, ArrayList<ClubModels> mClubArrayList) {
        this.mContext = mContext;
        this.mClubArrayList = mClubArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_adapter_clublist_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        if(AppPreferenceManager.getSchoolSelection(mContext).equals("ISG")){
            holder.eventsbg.setBackgroundResource(R.drawable.evnticon);
        }else{
            holder.eventsbg.setBackgroundResource(R.drawable.evnticonisg);

        }
//        holder.phototakenDate.setText(mPhotosModelArrayList.get(position).getMonth() + " " + mPhotosModelArrayList.get(position).getDay() + "," + mPhotosModelArrayList.get(position).getYear());
        holder.clubName.setText(mClubArrayList.get(position).getClub_name());

    }



    @Override
    public int getItemCount() {
        return mClubArrayList.size();
    }
}
