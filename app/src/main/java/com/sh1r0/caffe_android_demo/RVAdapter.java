package com.sh1r0.caffe_android_demo;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Even on 2017/3/26.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.SnackViewHolder>{


    public static class SnackViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView snackName;
        TextView snackCalorie;
        ImageView snackImage;

        SnackViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            snackName = (TextView)itemView.findViewById(R.id.snack_name);
            snackCalorie = (TextView)itemView.findViewById(R.id.snack_calorie);
            snackImage = (ImageView)itemView.findViewById(R.id.snack_image);
        }

    }

    List<Snack> snacks;

    RVAdapter(List<Snack> snacks){
        this.snacks = snacks;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public SnackViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycle_item, viewGroup, false);
        SnackViewHolder pvh = new SnackViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(SnackViewHolder snackViewHolder, int i) {
        snackViewHolder.snackName.setText(snacks.get(i).getName());
        snackViewHolder.snackCalorie.setText("Calories:"+snacks.get(i).getCalorie()+"kJ");
        snackViewHolder.snackImage.setImageResource(snacks.get(i).getImgResId());
    }

    @Override
    public int getItemCount() {
        return snacks.size();
    }


}