package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton add_button;
    Toolbar toolbar;
    MyDatabaseHelper myDB;
    ArrayList<TripEntity> tripArrayList;
    TripAdapter customAdapter;
    SearchView searchView;
    TextView totalMonth,totalMonthTxt,totalDay;
    int totalMonthInt,totalDayInt;
    ImageView cloud;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_home);
        recyclerView = findViewById(R.id.recyclerView);
        TextView emptyView = findViewById(R.id.empty_view);
        ImageView empty_img = findViewById(R.id.no_img);
        totalMonth = findViewById(R.id.money);
        totalDay = findViewById(R.id.money_day);
        totalMonthTxt = findViewById(R.id.money_txt);
        Calendar cal=Calendar.getInstance();
        SimpleDateFormat month_year = new SimpleDateFormat("MMMM yyyy");
        SimpleDateFormat today = new SimpleDateFormat("MM/dd/yy");
        String todayStr = today.format(cal.getTime());
        String month_name = month_year.format(cal.getTime());
        totalMonthTxt.setText(month_name);
        myDB = new MyDatabaseHelper(MainActivity.this);
        tripArrayList = new ArrayList<>();
        storeDataInArrays();
        cloud = findViewById(R.id.cloud);
        cloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ApiActivity.class);
                intent.putExtra("BUNDLE",tripArrayList);
                startActivity(intent);
            }
        });
        add_button = findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddTripActivity.class);
                startActivityForResult(intent,1);
//                Intent intent = new Intent(MainActivity.this,MapActivity.class);
//                startActivity(intent);

            }
        });
        totalDayInt = myDB.getSumAmountOfToday();
        totalDay.setText(String.valueOf(totalDayInt));
        totalMonthInt = myDB.getSumAmountOfExpenseByTrip();
        totalMonth.setText(String.valueOf(totalMonthInt));
        customAdapter = new TripAdapter(MainActivity.this,this,tripArrayList);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        if (tripArrayList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            empty_img.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.VISIBLE);
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            empty_img.setVisibility(View.GONE);
        }
        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                customAdapter.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                customAdapter.getFilter().filter(s);
                return false;
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            recreate();
        }
    }
    void storeDataInArrays(){
        Cursor cursor = myDB.readAllData();

            while (cursor.moveToNext()){
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String dept = cursor.getString(2);
                String dest = cursor.getString(3);
                String datefrom = cursor.getString(4);
                String dateto = cursor.getString(5);
                String risk = cursor.getString(6);
                String desc = cursor.getString(7);
                tripArrayList.add(new TripEntity(id,name, dept, dest,datefrom,dateto,risk,desc));
            }
        }
}