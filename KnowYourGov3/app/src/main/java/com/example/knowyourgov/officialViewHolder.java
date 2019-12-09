package com.example.knowyourgov;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class officialViewHolder extends RecyclerView.ViewHolder {
    TextView Name;
    TextView Title;

    public officialViewHolder(View view){
        super(view);
        Name =      view.findViewById(R.id.Name);
        Title =     view.findViewById(R.id.officeTitle);
    }
}