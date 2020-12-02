package com.example.kenji.freelancemarketplace;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    private RecyclerView recyclerView;
    private JobsAdapter adapter;
    private ArrayList<Jobs> JobsArrayList;
    private FloatingActionButton fab;
    private TextView account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refresh();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
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
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        addData();
        try{
            if(mAuth.getCurrentUser().getEmail().isEmpty() == false){
                setContentView(R.layout.activity_main_in);
            }
            else{
                setContentView(R.layout.activity_main_out);
            }
        }
        catch (Exception e){
            setContentView(R.layout.activity_main_out);
        }
        dl = (DrawerLayout)findViewById(R.id.activity_menu);
        t = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);
        dl.addDrawerListener(t);
        t.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nv = (NavigationView)findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                try {
                    if (mAuth.getCurrentUser().getEmail().isEmpty() == false) {
                        switch (id) {
                            case R.id.post:
                                startActivity(new Intent(getApplicationContext(), JobsPostedActivity.class));
                                return true;
                            case R.id.current:
                                startActivity(new Intent(getApplicationContext(), JobsTakenActivity.class));
                                return true;
                            case R.id.logout:
                                logout();
                                return true;
                            default:
                                return true;
                        }
                    } else {
                        switch (id) {
                            case R.id.login:
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                return true;
                            case R.id.register:
                                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                                return true;
                            default:
                                return true;
                        }
                    }
                }
                catch (Exception e){
                    switch (id) {
                        case R.id.login:
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            return true;
                        case R.id.register:
                            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                            return true;
                        default:
                            return true;
                    }
                }

            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        adapter = new JobsAdapter(JobsArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void addData(){
        JobsArrayList = new ArrayList<Jobs>();
        db.collection("JobsList").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                for (DocumentChange documentChange : documentSnapshots.getDocumentChanges())
                {
                    String name =  documentChange.getDocument().getData().get("Name").toString();
                    String phone   =  documentChange.getDocument().getData().get("Phone").toString();
                    String email = documentChange.getDocument().getData().get("Email").toString();
                    String jobsname = documentChange.getDocument().getData().get("JobsName").toString();
                    String jobsdetail = documentChange.getDocument().getData().get("JobsDetail").toString();
                    String taken = documentChange.getDocument().getData().get("Taken").toString();
                    JobsArrayList.add(new Jobs(name, phone, email, jobsname, jobsdetail, taken));
                }
            }
        });

    }

    private void logout(){
        if(mAuth.getCurrentUser().getEmail().isEmpty() == false){
            mAuth.signOut();
            Intent i = new Intent(MainActivity.this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            Toast.makeText(MainActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
            startActivity(i);
        }
    }
}