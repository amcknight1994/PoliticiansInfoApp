package com.example.knowyourgov;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class official_adapter extends RecyclerView.Adapter<officialViewHolder>{

        private ArrayList<Official> electionList;
        private MainActivity mainActivity;

        official_adapter(ArrayList<Official> list, MainActivity mainActivity){

            electionList = list;
            this.mainActivity = mainActivity;
        }

        @NonNull
        @Override
        public officialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.official_holder, parent,false);
            itemView.setOnClickListener(mainActivity);
            return new officialViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull officialViewHolder holder, int position) {
            Official selectedOfficial= electionList.get(position);
            holder.Name.setText(selectedOfficial.title);
            holder.Title.setText(selectedOfficial.name + " (" + selectedOfficial.party +")");
        }

        @Override
        public int getItemCount() {
            return electionList.size();
        }

}
