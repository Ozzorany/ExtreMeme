package com.example.extrememe.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.extrememe.R;
import com.example.extrememe.model.Meme;

import java.util.List;

public class MyMemesAdapter extends RecyclerView.Adapter<MyMemesViewHolder>{
    public List<Meme> data;
    public boolean isEditAvailable;
    LayoutInflater inflater;
    private OnItemClickListener onItemClickListener;
    private OnMemeRemoveListener onRemoveListener;

    public MyMemesAdapter(LayoutInflater inflater){
        this.inflater = inflater;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public interface OnMemeRemoveListener{
        void onItemRemove(Meme meme);
    }

    public void setOnClickListener(OnItemClickListener listener){
        this.onItemClickListener = listener;
    }

    public void setOnRemoveListener(OnMemeRemoveListener onRemoveListener) {
        this.onRemoveListener = onRemoveListener;
    }

    @NonNull
    @Override
    public MyMemesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_meme,parent,false);
        MyMemesViewHolder holder = new MyMemesViewHolder(view, this.isEditAvailable);
        holder.onItemClickListener = onItemClickListener;
        holder.onMemeRemoveListener = onRemoveListener;
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyMemesViewHolder holder, int position) {
        Meme meme = data.get(position);
        holder.bindData(meme,position);
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }
}