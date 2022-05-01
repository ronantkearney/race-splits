package com.racesplits.android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.racesplits.R;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private List<Model> modelList;

    public Adapter (List<Model> modelList) {this.modelList = modelList;}

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {

        int imageView1 = modelList.get(position).getImageView1();
        String textView1 = modelList.get(position).getTextView1();
        String textView2 = modelList.get(position).getTextView2();
        String textView3 = modelList.get(position).getTextView3();
        String textView4 = modelList.get(position).getTextView4();

        holder.setData(imageView1, textView1, textView2, textView3, textView4);
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView holderImageView;
        private TextView holderTextView1;
        private TextView holderTextView2;
        private TextView holderTextView3;
        private TextView holderTextView4;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            holderImageView = itemView.findViewById(R.id.imageview);
            holderTextView1 = itemView.findViewById(R.id.textview1);
            holderTextView2 = itemView.findViewById(R.id.textview2);
            holderTextView3 = itemView.findViewById(R.id.textview3);
            holderTextView4 = itemView.findViewById(R.id.textview4);
        }

        public void setData(int imageView1, String textView1, String textView2, String textView3, String textView4) {
            holderImageView.setImageResource(imageView1);
            holderTextView1.setText(textView1);
            holderTextView2.setText(textView2);
            holderTextView3.setText(textView3);
            holderTextView4.setText(textView4);
        }
    }
}
