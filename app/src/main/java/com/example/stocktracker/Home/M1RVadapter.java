package com.example.stocktracker.Home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class M1RVadapter extends RecyclerView.Adapter<M1RVadapter.ViewHolder> {

    private com.example.stocktracker.Home.Data mData;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView one, two, three, four;

        public ViewHolder(View v) {
            super(v);

            one = v.findViewById(R.id.textView1);
            two = v.findViewById(R.id.textView2);
            three = v.findViewById(R.id.textView3);
            four = v.findViewById(R.id.textView4);

            //리스너
        }

        public TextView getOne() {
            return one;
        }

        public TextView getTwo() {
            return two;
        }

        public TextView getThree() {
            return three;
        }

        public TextView getFour() {
            return four;
        }
    }

    public M1RVadapter(Context context, com.example.testmain.Data Data){
        this.mData = Data;
    }

    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int Viewtype){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getOne().setText(mData.name[position]);
        holder.getTwo().setText(mData.pra[position]);
        holder.getThree().setText(mData.name2[position]);
        holder.getFour().setText(mData.def[position]);
    }

    @Override
    public int getItemCount(){
        return mData.length();
    }

}
