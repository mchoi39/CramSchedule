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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;


public class User_Fragment extends Fragment {

    private Button createEvent;
    private RecyclerView mRecyclerView;
    private FirebaseFirestore firebaseFirestore;
    private SavedEventAdapter eventAdapter;
    private ClassAdapter classAdapter;

    private DocumentReference myClassesDocRef;
    private RecyclerView mClassRecyclerView;


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


        mRecyclerView = v.findViewById(R.id.saved_events);
        mRecyclerView.setNestedScrollingEnabled(false);
        //setUpRecyclerView(mRecyclerView);
        mClassRecyclerView = v.findViewById(R.id.classes_recycler_view);
        setUpClassesRecycler(mClassRecyclerView);


//        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mClassRecyclerView.setHasFixedSize(true);
        mClassRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //mRecyclerView.setAdapter(eventAdapter);


        return v;
    }


    private void setUpRecyclerView(RecyclerView aRecyclerView){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        assert user != null;
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

    public void setUpClassesRecycler(final RecyclerView recycler){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        Query query = firebaseFirestore.collection("Users").document(user.getUid()).collection("MyClassesForFragment");
        //Recycler options
        FirestoreRecyclerOptions < ClassModel > options = new FirestoreRecyclerOptions.Builder < ClassModel > ()
                .setQuery(query, ClassModel.class)
                .build();

        classAdapter = new ClassAdapter(options);
        classAdapter.startListening();
        classAdapter.notifyDataSetChanged();
        recycler.setAdapter(classAdapter);
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
                        classAdapter.deleteItem(viewHolder.getLayoutPosition());
                    }
                })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                classAdapter.notifyItemChanged(viewHolder.getLayoutPosition());
                            }
                        }).create().show();
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
        }).attachToRecyclerView(recycler);

    }


    @Override
    public void onStart() {
        super.onStart();
        //eventAdapter.startListening();
        classAdapter.startListening();


//        myClassesDocRef = firebaseFirestore.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                .collection("MyClasses").document("classes");
//
//        myClassesDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//                if (error != null) {
//                    return;
//                }
//                if (value != null && value.exists()){
//                    String class1 = value.getString("class1");
//                    String class2 = value.getString("class2");
//                    String class3 = value.getString("class3");
//                    String class4 = value.getString("class4");
//                    String class5 = value.getString("class5");
//                    String class6 = value.getString("class6");
////                    if (class1 != null && class1.trim().length() > 0){
////                        classTextView.setText(class1);
////                        eraseClass1.setVisibility(View.VISIBLE);
////                    }
////                    if (class2 != null && class2.trim().length() > 0){
////                        classTextView2.setText(class2);
////                        eraseClass2.setVisibility(View.VISIBLE);
////                    }
////                    if (class3 != null && class3.trim().length() > 0){
////                        classTextView3.setText(class3);
////                        eraseClass3.setVisibility(View.VISIBLE);
////                    }
////                    if (class4 != null && class4.trim().length() > 0){
////                        classTextView4.setText(class4);
////                        eraseClass4.setVisibility(View.VISIBLE);
////                    }
////                    if (class5 != null && class5.trim().length() > 0){
////                        classTextView5.setText(class5);
////                        eraseClass5.setVisibility(View.VISIBLE);
////                    }
////                    if (class6 != null && class6.trim().length() > 0){
////                        classTextView6.setText(class6);
////                        eraseClass6.setVisibility(View.VISIBLE);
////                    }
//
//                }
//
//
//            }
//        });
    }
}
