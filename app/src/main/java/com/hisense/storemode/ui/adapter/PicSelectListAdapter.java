package com.hisense.storemode.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hisense.artmode.ui.view.BoundsImageView;
import com.hisense.storemode.R;
import com.hisense.storemode.utils.GlideUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by xuchongxiang on 2020年07月03日.
 */
public class PicSelectListAdapter extends RecyclerView.Adapter<PicSelectListAdapter.PicSelectViewHolder> {

    private List<String> mPicList;
    private Context mContext;
    private PicSelectListener mListener;

    public PicSelectListAdapter(Context context, List<String> picList) {
        mContext = context;
        mPicList = picList;
    }

    public void setPicSelectListener(PicSelectListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public PicSelectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_pic_select, parent, false);
        return new PicSelectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PicSelectViewHolder holder, int position) {
        GlideUtils.loadImageForMallLogo(mPicList.get(position), holder.mIvPicSelect);
        holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View view, boolean hasFocus) {
                if (hasFocus) {
                    ViewCompat.animate(view).scaleX(1.1f).scaleY(1.1f).start();
                    holder.mIvPicSelect.setBorderColor(mContext.getResources().getColor(R.color.white),
                            mContext.getResources().getColor(R.color.black),
                            mContext.getResources().getColor(R.color.black));
                } else {
                    ViewCompat.animate(view).scaleX(1.0f).scaleY(1.0f).start();
                    holder.mIvPicSelect.setBorderColor(mContext.getResources().getColor(R.color.trans),
                            mContext.getResources().getColor(R.color.trans),
                            mContext.getResources().getColor(R.color.trans));
                }
            }
        });
        holder.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onPicSelected(mPicList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mPicList != null) {
            return mPicList.size();
        }
        return 0;
    }

    public void updatePicList(List<String> list) {
        if (mPicList != null) {
            mPicList.clear();
        }
        mPicList = list;
        notifyDataSetChanged();
    }

    static class PicSelectViewHolder extends RecyclerView.ViewHolder {

        BoundsImageView mIvPicSelect;

        public PicSelectViewHolder(@NonNull View itemView) {
            super(itemView);
            mIvPicSelect = itemView.findViewById(R.id.iv_pic_select);
        }
    }

    public interface PicSelectListener {
        void onPicSelected(String path);
    }

}
