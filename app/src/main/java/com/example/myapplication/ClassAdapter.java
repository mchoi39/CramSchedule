package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;

public class ClassAdapter extends FirestoreRecyclerAdapter<ClassModel, ClassAdapter.ClassViewHolder> {


    public ClassAdapter(@NonNull FirestoreRecyclerOptions<ClassModel> options) {
        super(options);
    }


    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_class_list, parent,  false);

        return new ClassViewHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull ClassViewHolder holder, int position, @NonNull ClassModel model) {
        holder.className.setText(model.getClassName());

    }
    public void deleteItem(int position){
        getSnapshots().getSnapshot(position).getReference().delete();

    }


    public class ClassViewHolder extends RecyclerView.ViewHolder {
        TextView className;
        public ClassViewHolder(@NonNull View itemView) {
            super(itemView);
            className = itemView.findViewById(R.id.classNameSLI);

        }
    }









}
