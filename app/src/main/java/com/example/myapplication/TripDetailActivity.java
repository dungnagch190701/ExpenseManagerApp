package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class TripDetailActivity extends AppCompatActivity implements EditTripDialog.OnInputListener{

    ImageView image,empty;
    TextView date_txt,dept_txt,dest_txt,risk_txt,desc_txt,date_end_txt,empty_txt;
    int id_details;
    String name_trip,dept,dest,date,risk,desc,dateEnd;
    Toolbar toolbar;
    FloatingActionButton addExpense;
    MyDatabaseHelper mydb;
    Cursor cursor;
    ArrayList<ExpenseEntity> expenseArrayList = new ArrayList<>();
    ExpenseAdapter expenseAdapter;
    RecyclerView recyclerView;
    ScrollView scv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_trip_detail);
        findView();
        setSupportActionBar(toolbar);
        getAndSetIntentData();
        getSupportActionBar().setTitle(name_trip);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TripDetailActivity.this, AddExpenseActivity.class);
                intent.putExtra("id",id_details);
                intent.putExtra("datefrom",date_txt.getText().toString());
                intent.putExtra("dateto",date_end_txt.getText().toString());
                startActivityForResult(intent,100);
            }
        });
        mydb = new MyDatabaseHelper(TripDetailActivity.this);
        cursor = mydb.getExpenseByTripId(id_details);
        if (cursor.getCount() == 0 ) {
            empty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            empty_txt.setVisibility(View.VISIBLE);
        }else{
            empty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            empty_txt.setVisibility(View.GONE);
            while (cursor.moveToNext()){
                int expense_id = cursor.getInt(0);
                String type = cursor.getString(1);
                String amount = cursor.getString(2);
                String time = cursor.getString(3);
                String address = cursor.getString(4);
                String cmt = cursor.getString(5);
                expenseArrayList.add(new ExpenseEntity(expense_id,type,amount,time,address,cmt,id_details));
            }
        }
        expenseAdapter = new ExpenseAdapter(TripDetailActivity.this,this,expenseArrayList);
        recyclerView.setAdapter(expenseAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(TripDetailActivity.this));
        recyclerView.setHasFixedSize(true);
        scv.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    addExpense.hide();
                } else {
                    addExpense.show();
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK){
            id_details = data.getIntExtra("id",0);
            cursor = mydb.getExpenseByTripId(id_details);
            if (cursor.getCount() == 0 ) {
                empty.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                empty_txt.setVisibility(View.VISIBLE);
            }else {
                empty.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                empty_txt.setVisibility(View.GONE);
            }
            cursor.moveToLast();
            int expense_id = cursor.getInt(0);
            String type = cursor.getString(1);
            String amount = cursor.getString(2);
            String time = cursor.getString(3);
            String address = cursor.getString(4);
            String cmt = cursor.getString(5);
            expenseArrayList.add(new ExpenseEntity(expense_id, type, amount, time,address, cmt, id_details));
            expenseAdapter.notifyDataSetChanged();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.edit:
                EditTripDialog customDialog = new EditTripDialog();
                Bundle bundle = new Bundle();
                bundle.putInt("id",id_details);
                bundle.putString("name",name_trip);
                bundle.putString("dept",dept_txt.getText().toString());
                bundle.putString("dest",dest_txt.getText().toString());
                bundle.putString("datefrom",date_txt.getText().toString());
                bundle.putString("dateto",date_end_txt.getText().toString());
                bundle.putString("risk",risk);
                bundle.putString("desc",desc_txt.getText().toString());

                customDialog.setArguments(bundle);
                customDialog.show(getSupportFragmentManager(),"dialog");
                break;
            case R.id.delete:
                confirmDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void getAndSetIntentData() {
        if(getIntent().hasExtra("id") &&
                getIntent().hasExtra("name") &&
                getIntent().hasExtra("dept") &&
                getIntent().hasExtra("dest") &&
                getIntent().hasExtra("datefrom") &&
                getIntent().hasExtra("dateto") &&
                getIntent().hasExtra("risk") &&
                getIntent().hasExtra("desc") &&
                getIntent().hasExtra("img")
        ) {
            id_details   = getIntent().getIntExtra("id",0);
            dept   = getIntent().getStringExtra("dept");
            name_trip = getIntent().getStringExtra("name");
            dest = getIntent().getStringExtra("dest");
            date = getIntent().getStringExtra("datefrom");
            dateEnd = getIntent().getStringExtra("dateto");
            risk = getIntent().getStringExtra("risk");
            desc = getIntent().getStringExtra("desc");
            Bitmap bitmap = (Bitmap) getIntent().getParcelableExtra("img");
            dept_txt.setText(dept);
            dest_txt.setText(dest);
            date_txt.setText(date);
            date_end_txt.setText(dateEnd);
            if (risk.equals("NO")){
                risk_txt.setText("NO RISK");
            } else { risk_txt.setText("HAVE RISK");}
            desc_txt.setText(desc);
            desc_txt.setText(desc);
            image.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "No data!", Toast.LENGTH_SHORT).show();
        }
    }
    private void findView() {
        toolbar = findViewById(R.id.toolbarTripDetails);
        date_txt = findViewById(R.id.date_from);
        date_txt = findViewById(R.id.date_from);
        date_end_txt = findViewById(R.id.date_to);
        dept_txt = findViewById(R.id.departure_details);
        dest_txt = findViewById(R.id.destination_details);
        risk_txt = findViewById(R.id.risk_details);
        desc_txt = findViewById(R.id.desc_details);
        image = findViewById(R.id.image_details);
        addExpense = findViewById(R.id.addExpense);
        empty = findViewById(R.id.empty_expense_img);
        empty_txt = findViewById(R.id.empty_text);
        recyclerView = findViewById(R.id.expense_recyclerview);
        scv = findViewById(R.id.scv);
    }
    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete" + name_trip + "?");
        builder.setMessage("Are you sure you want to delete " + name_trip + "?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(TripDetailActivity.this);
                myDB.deleteOneRow(id_details);
                finish();
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toasts,
                        (ViewGroup) findViewById(R.id.toast_type));
                TextView tv = layout.findViewById(R.id.toast_text);
                tv.setText("Deleted the trip");
                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);

                toast.show();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    public void sendInput(String name, String dept, String dest, String date,String date_end, String risk, String desc) {
        name_trip = name;
        getSupportActionBar().setTitle(name);
        dept_txt.setText(dept);
        dest_txt.setText(dest);
        date_txt.setText(date);
        if (risk.equals("NO")){
            risk_txt.setText("NO RISK");
        } else { risk_txt.setText("HAVE RISK");}
        desc_txt.setText(desc);
        if (name.equals("Vacation")) {
            image.setImageResource(R.drawable.vacation);
        } else if (name.equals("Meeting")){
            image.setImageResource(R.drawable.meeting);
        } else if (name.equals("Events")) {
            image.setImageResource(R.drawable.events);
        } else if (name.equals("Conference")) {
            image.setImageResource(R.drawable.conference);
        }else if (name.equals("Offices")) {
            image.setImageResource(R.drawable.offices);
        }else if (name.equals("Hometown")) {
            image.setImageResource(R.drawable.house);
        }else {
            image.setImageResource(R.drawable.other);
        }
        date_end_txt.setText(date_end);
    }
}