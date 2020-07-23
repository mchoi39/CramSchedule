package com.example.myapplication;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EventAdapter extends FirestoreRecyclerAdapter<EventModel, EventAdapter.EventsViewHolder> {
    Dialog dialog;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public EventAdapter(@NonNull FirestoreRecyclerOptions<EventModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull EventsViewHolder holder, int position, @NonNull EventModel model) {
        holder.className.setText(model.getClassName());
        holder.location.setText(model.getLocation());
        holder.time.setText(model.getTime());


    }

    @NonNull
    @Override
    public EventsViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_list_item, parent, false);
        EventsViewHolder vh = new EventsViewHolder(view);


        return vh;
    }

    public class EventsViewHolder extends RecyclerView.ViewHolder{
        private CardView pop;
        private TextView className;
        private TextView location;
        private TextView time;
        public EventsViewHolder(@NonNull final View itemView) {
            super(itemView);
            pop = itemView.findViewById(R.id.event_item_id);
            className = itemView.findViewById(R.id.classNameSLI);
            location = itemView.findViewById(R.id.locationSLI);
            time = itemView.findViewById(R.id.timeSLI);

            pop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog = new Dialog(pop.getContext());
                    dialog.setContentView(R.layout.event_click_popup);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();

                    TextView popup_className = (TextView) dialog.findViewById(R.id.class_name_popup);
                    TextView popup_Location = (TextView) dialog.findViewById(R.id.location_popup);
                    popup_className.setText(className.getText());
                    popup_Location.setText(location.getText());

                    dialog.getWindow().findViewById(R.id.save_to_profile).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int position = getLayoutPosition();

                            DocumentSnapshot snapshot = getSnapshots().getSnapshot(position);
                            String id = snapshot.getId();
                            String className = snapshot.getString("className");
                            String date = snapshot.getString("date");
                            String location = snapshot.getString("location");
                            String time = snapshot.getString("time");

                            Map<String, Object> RSVPDoc = new HashMap<>();
                            RSVPDoc.put("className", className);
                            RSVPDoc.put("date", date);
                            RSVPDoc.put("location", location);
                            RSVPDoc.put("time", time);

                            //ystem.out.println(id);
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user != null) {
                                System.out.println("click working");
                                DocumentReference mUserDocRef = FirebaseFirestore.getInstance().collection("Users").document(user.getUid()).collection("SavedEvents").document(id);
                                mUserDocRef.set(RSVPDoc);
                            } else {
                                System.out.println("not working");
                            }
                        }
                    });

                }
            });

        }

    }






}
