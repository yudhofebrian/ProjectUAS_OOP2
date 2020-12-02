package com.example.kenji.freelancemarketplace;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class JobsAddActivity extends AppCompatActivity {
    private static final String NAME_KEY = "Name";
    private static final String EMAIL_KEY = "Email";
    private static final String PHONE_KEY = "Phone";
    private static final String JOBS_NAME_KEY = "JobsName";
    private static final String JOBS_DETAIL_KEY = "JobsDetail";
    private static final String TAKEN = "Taken";
    FirebaseFirestore db;
    TextView textDisplay;
    TextView message;
    String name, email, phone, jobsname, jobsdetail;
    Button save;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs_add);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        textDisplay = findViewById(R.id.textDisplay);
        save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewJobs();
            }
        });
    }
    private void addNewJobs() {
        EditText temp;
        save = findViewById(R.id.save);
        temp = (EditText) findViewById(R.id.name);
        name = temp.getText().toString();
        email = user.getEmail();
        temp = (EditText) findViewById(R.id.phone);
        phone = temp.getText().toString();
        temp = (EditText) findViewById(R.id.jobsname);
        jobsname = temp.getText().toString();
        temp = (EditText) findViewById(R.id.jobsdetail);
        jobsdetail = temp.getText().toString();
        Map<String, Object> newJobs = new HashMap<>();
        newJobs.put(NAME_KEY, name);
        newJobs.put(EMAIL_KEY, email);
        newJobs.put(PHONE_KEY, phone);
        newJobs.put(JOBS_NAME_KEY, jobsname);
        newJobs.put(JOBS_DETAIL_KEY, jobsdetail);
        newJobs.put(TAKEN, "");
        try {
            db.collection("JobsList").document(email + "_" +jobsname).set(newJobs);
            Toast.makeText(JobsAddActivity.this, "Jobs Added", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(JobsAddActivity.this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
        catch(Exception e){
            Toast.makeText(JobsAddActivity.this, "Jobs Not Added", Toast.LENGTH_SHORT).show();
        }
    }
}
