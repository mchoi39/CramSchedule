package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;
import java.util.concurrent.Executor;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;


public class User_Fragment extends Fragment {

    private Button createEvent;
    private RecyclerView mRecyclerView;
    private FirebaseFirestore firebaseFirestore;
    private SavedEventAdapter eventAdapter;
    private DocumentReference myClassesDocRef;
    private TextView classTextView;
    View v;
    Button logout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_user, container, false);

        logout = (Button) v.findViewById(R.id.logout_btn);

        logout.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(v.getContext(), Login.class));
            }
        });

        firebaseFirestore = FirebaseFirestore.getInstance();

        classTextView = v.findViewById(R.id.my_classes_textview_display);
        mRecyclerView = v.findViewById(R.id.saved_events);
        mRecyclerView.setNestedScrollingEnabled(false);
        setUpRecyclerView(mRecyclerView);


        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //mRecyclerView.setAdapter(eventAdapter);


        return v;
    }


    private void setUpRecyclerView(RecyclerView aRecyclerView){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        assert user != null;
        System.out.println(user.getUid());
        Query query = firebaseFirestore.collection("Users").document(user.getUid()).collection("SavedEvents");
        //Recycler options
        FirestoreRecyclerOptions < EventModel > options = new FirestoreRecyclerOptions.Builder < EventModel > ()
                .setQuery(query, EventModel.class)
                .build();

        eventAdapter = new SavedEventAdapter(options);
        eventAdapter.startListening();
        eventAdapter.notifyDataSetChanged();
        aRecyclerView.setAdapter(eventAdapter);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                new AlertDialog.Builder(viewHolder.itemView.getContext()).setMessage("Are you sure?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        eventAdapter.deleteItem(viewHolder.getLayoutPosition());
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        eventAdapter.notifyItemChanged(viewHolder.getLayoutPosition());
                    }
                }).create().show();
                //eventAdapter.deleteItem(viewHolder.getLayoutPosition());
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addBackgroundColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorAccent))
                        .addActionIcon(R.drawable.ic_baseline_delete_sweep_24)
                        .create()
                        .decorate();

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }).attachToRecyclerView(mRecyclerView);



    }
    @Override
    public void onStop() {
        super.onStop();
        eventAdapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        eventAdapter.startListening();

        myClassesDocRef = firebaseFirestore.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("MyClasses").document("classes");

        myClassesDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    return;
                }
                if (value != null && value.exists()){

                    String class1 = value.getString("class1");
                    String class2 = value.getString("class2");
                    String class3 = value.getString("class3");
                    String class4 = value.getString("class4");
                    String class5 = value.getString("class5");
                    String class6 = value.getString("class6");
                    classTextView.setText(class1 + "\n" + class2 + "\n"+ class3 + "\n" + class4+ "\n"+class5 + "\n" + class6 + "\n");
                }

            }
        });


    }



}
