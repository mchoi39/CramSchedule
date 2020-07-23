package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.text.BreakIterator;

public class SavedEventAdapter extends FirestoreRecyclerAdapter<EventModel, SavedEventAdapter.SavedEventViewHolder> {

    public SavedEventAdapter(@NonNull FirestoreRecyclerOptions<EventModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull SavedEventViewHolder holder, int position, @NonNull EventModel model) {
        holder.className.setText(model.getClassName());
        holder.location.setText(model.getLocation());
        holder.time.setText(model.getTime());


    }


    @NonNull
    @Override
    public SavedEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_list_item, parent,  false);

        return new SavedEventViewHolder(v);
    }

    public void deleteItem(int position){
        getSnapshots().getSnapshot(position).getReference().delete();

    }


    public class SavedEventViewHolder extends RecyclerView.ViewHolder{

        private TextView className;
        private TextView location;
        private TextView time;

        public SavedEventViewHolder(@NonNull View itemView) {
            super(itemView);
            className = itemView.findViewById(R.id.classNameSLI);
            location = itemView.findViewById(R.id.locationSLI);
            time = itemView.findViewById(R.id.timeSLI);
        }
    }
}
