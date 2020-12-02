package com.example.kenji.freelancemarketplace;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class JobsAdapter extends RecyclerView.Adapter<JobsAdapter.JobsViewHolder>  {
    private ArrayList<Jobs> dataList;

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    public JobsAdapter(ArrayList<Jobs> dataList) {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        this.dataList = dataList;
    }

    @Override
    public JobsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.activity_jobs_view, parent, false);
        return new JobsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(JobsViewHolder holder, int position) {
        holder.name.setText(dataList.get(position).name);
        holder.phone.setText(dataList.get(position).phone);
        holder.email.setText(dataList.get(position).email);
        holder.jobsname.setText(dataList.get(position).jobsname);
        if(dataList.get(position).taken.length() > 0) {
            holder.taken.setText("Taken by " + dataList.get(position).taken);
        }
        else{
            holder.taken.setText("Not Yet Taken");
        }
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class JobsViewHolder extends RecyclerView.ViewHolder{
        private TextView name, phone, email, jobsname, taken;
        public JobsViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            phone = (TextView) itemView.findViewById(R.id.phone);
            email = (TextView) itemView.findViewById(R.id.email);
            jobsname = (TextView) itemView.findViewById(R.id.jobsname);
            taken = (TextView) itemView.findViewById(R.id.taken);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = null;
                    int post = getAdapterPosition();
                    String element = dataList.get(post).name;
                    String element2 = dataList.get(post).phone;
                    String element3 = dataList.get(post).email;
                    String element4 = dataList.get(post).jobsname;
                    String element5 = dataList.get(post).jobsdetail;
                    String element6 = dataList.get(post).taken;
                    intent = new Intent(v.getContext(), JobsDetailActivity.class);
                    intent.putExtra("Name", element);
                    intent.putExtra("Phone", element2);
                    intent.putExtra("Email", element3);
                    intent.putExtra("JobsName", element4);
                    intent.putExtra("JobsDetail", element5);
                    intent.putExtra("Taken", element6);
                    v.getContext().startActivity(intent);
                }
            });
        }


    }
}
