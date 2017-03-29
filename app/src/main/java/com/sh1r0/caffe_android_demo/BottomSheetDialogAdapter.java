package com.sh1r0.caffe_android_demo;

/**
 * Created by Even on 2017/3/28.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Even on 2017/3/27.
 */


public class BottomSheetDialogAdapter extends RecyclerView.Adapter<BottomSheetDialogAdapter.ViewHolder> {

    private List<BottomSheetItem> mItems;
    private ItemListener mListener;

    public BottomSheetDialogAdapter(List<BottomSheetItem> items, ItemListener listener) {
        mItems = items;
        mListener = listener;
    }

    public void setListener(ItemListener listener) {
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.buttonsheetdialog_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textView;
        public BottomSheetItem item;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textView = (TextView) itemView.findViewById(R.id.addapterItem);
        }

        public void setData(BottomSheetItem item) {
            this.item = item;
            textView.setText(item.getTitle());
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(item);
            }
        }
    }

    public interface ItemListener {
        void onItemClick(BottomSheetItem item);
    }
}

