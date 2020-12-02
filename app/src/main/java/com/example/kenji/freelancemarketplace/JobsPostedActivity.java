package com.example.kenji.freelancemarketplace;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class JobsPostedActivity extends AppCompatActivity {
    FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private JobsAdapter adapter;
    private ArrayList<Jobs> JobsArrayList;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refresh();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            refresh();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            refresh();
        }
    }

    public void refresh(){
        setContentView(R.layout.activity_jobs_posted);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                startActivity(new Intent(getApplicationContext(),
                        JobsAddActivity.class));
            }
        });
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        addData();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        adapter = new JobsAdapter(JobsArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(JobsPostedActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void addData(){
        JobsArrayList = new ArrayList<Jobs>();
        db.collection("JobsList").whereEqualTo("Email", mAuth.getCurrentUser().getEmail()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                for (DocumentChange documentChange : documentSnapshots.getDocumentChanges())
                {
                    String name =  documentChange.getDocument().getData().get("Name").toString();
                    String phone   =  documentChange.getDocument().getData().get("Phone").toString();
                    String email = documentChange.getDocument().getData().get("Email").toString();
                    String jobsname = documentChange.getDocument().getData().get("JobsName").toString();
                    String jobsdetail = documentChange.getDocument().getData().get("JobsDetail").toString();
                    String taken;
                    if(documentChange.getDocument().getData().get("Taken").toString().length() < 1){
                        taken = "Not Yet Taken";
                    }
                    else {
                        taken = documentChange.getDocument().getData().get("Taken").toString();
                    }
                    JobsArrayList.add(new Jobs(name, phone, email, jobsname, jobsdetail, taken));
                }
            }
        });

    }
}
