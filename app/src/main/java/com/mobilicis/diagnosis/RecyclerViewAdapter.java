package com.mobilicis.diagnosis;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private final List<RecyclerViewModel> mData;
    private final LayoutInflater mInflater;

    RecyclerViewAdapter(Context context, List<RecyclerViewModel> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView myTextView;
        ImageView myImageView;
        ProgressBar progressBar;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.tvTest);
            myImageView = itemView.findViewById(R.id.status_iv);
            progressBar = itemView.findViewById(R.id.progressBar1);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.rv_items, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecyclerViewModel test = mData.get(position);

        holder.myImageView.setImageURI(test.getmImage());
        holder.myTextView.setText(test.getMtest());
        Log.d("Rupak", "onBindViewHolder: "+test.getmImage());

        holder.progressBar.setVisibility(test.ismStatus() ? 0 : 8);
        holder.myImageView.setVisibility(test.ismStatus() ? 8 : 0);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

}
