package com.techuva.blackcard.new_changes.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.techuva.blackcard.R;
import com.techuva.blackcard.contusfly.utils.Utils;
import com.techuva.blackcard.new_changes.models.ComplaintDetailModel;

import java.util.List;

public class ComplaintsAdapter extends RecyclerView.Adapter<ComplaintsAdapter.ViewHolder> {

    private final List<ComplaintDetailModel> mValues;
    Context context;

    public ComplaintsAdapter(List<ComplaintDetailModel> items, Context context) {
        mValues = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_complaint, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.tv_title.setText(mValues.get(position).getTitle());
        holder.tv_content.setText(mValues.get(position).getComplaint());

        if(!mValues.get(position).getImageUrl().equals(""))
        {
            holder.iv_image.setVisibility(View.VISIBLE);
            Log.e("URL", mValues.get(position).getImageUrl());
            Utils.loadImageWithGlide(context, mValues.get(position).getImageUrl(), holder.iv_image, R.drawable.splash_screen_new_crop);
        }
        else {
            holder.iv_image.setVisibility(View.GONE);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView tv_title;
        public final TextView tv_content;
        public ComplaintDetailModel mItem;
        ImageView iv_image;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_content = (TextView) view.findViewById(R.id.tv_content);
            iv_image = (ImageView) view.findViewById(R.id.iv_image);
        }

     /*   @Override
        public String toString() {

            return super.toString() + " '" + mContentView.getText() + "'";
        }*/
    }
}
