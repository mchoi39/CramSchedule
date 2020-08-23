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
import android.widget.EditText;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Objects;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class Calendar_Fragment extends Fragment {
    private RecyclerView mRecyclerView;
    private FirebaseFirestore firebaseFirestore;
    private ClassAdapter classAdapter;
    FirebaseAuth rAuth;
    private EditText etNameOfClass;


    View v;
    Button addClass;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_calendar, container, false);


        addClass = (Button) v.findViewById(R.id.add_btn);
        etNameOfClass = (EditText) v.findViewById(R.id.classToAdd);
        addClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String classToAdd = etNameOfClass.getText().toString();
                if (classToAdd.trim().length() > 0) {
                    rAuth = FirebaseAuth.getInstance();
                    String userID = Objects.requireNonNull(rAuth.getCurrentUser()).getUid();

                    DocumentReference ref = firebaseFirestore.collection("Users").document(userID).collection("MyClassesForFragment").document();
                    HashMap<String, Object> toPut = new HashMap<>();
                    toPut.put("className", classToAdd);
                    ref.set(toPut);



                    DocumentReference ogClass = firebaseFirestore.collection("Users").document(userID).collection("MyClasses").document("classes");
                    DocumentReference random = firebaseFirestore.collection("random").document();
                    HashMap<String, Object> toPutToOG = new HashMap<>();
                    toPutToOG.put(random.getId(), classToAdd);
                    ogClass.set(toPutToOG, SetOptions.merge());
                }
            }
        });

        firebaseFirestore = FirebaseFirestore.getInstance();


        mRecyclerView = v.findViewById(R.id.recycler_classes);
        mRecyclerView.setNestedScrollingEnabled(false);
        setUpRecyclerView(mRecyclerView);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(classAdapter);
        return v;
    }
    private void setUpRecyclerView(RecyclerView aRecyclerView){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        Query query = firebaseFirestore.collection("Users").document(user.getUid()).collection("MyClassesForFragment");
        FirestoreRecyclerOptions< ClassModel > options = new FirestoreRecyclerOptions.Builder < ClassModel > ()
                .setQuery(query, ClassModel.class)
                .build();

        classAdapter = new ClassAdapter(options);
        classAdapter.startListening();
        classAdapter.notifyDataSetChanged();
        aRecyclerView.setAdapter(classAdapter);

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
        }).attachToRecyclerView(mRecyclerView);

    }

    @Override
    public void onStop() {
        super.onStop();
        classAdapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        classAdapter.startListening();
    }
}
