package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddExpenseActivity extends AppCompatActivity {
    EditText time,desc,amount,address;
    int id;
    String datefrom,dateto;
    private int lastSelectedHour = -1;
    private int lastSelectedMinute = -1;
    String itemSelected;
    String[] items = {"Clothes","Transport","Food","Gift","Parking","Coffee"};
    ArrayAdapter<String> adapterItems;
    AutoCompleteTextView auto_complete_txt;
    FloatingActionButton add_btn;
    SimpleDateFormat sdf;
    Calendar myCalendar;
    Date endDate;
    Date startDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_expense);
        id = getIntent().getIntExtra("id",0);
        datefrom = getIntent().getStringExtra("datefrom");
        dateto = getIntent().getStringExtra("dateto");

        myCalendar = Calendar.getInstance();
        sdf = new SimpleDateFormat("dd-MM-yyyy");
        endDate = null;
        try {
            endDate = sdf.parse(dateto);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            startDate = sdf.parse(datefrom);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            myCalendar.setTime(sdf.parse(datefrom));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                view.setMinDate(myCalendar.getTimeInMillis());
                view.setMaxDate(endDate.getTime());
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };

        time = findViewById(R.id.date_expense);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(AddExpenseActivity.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMinDate(startDate.getTime());
                dialog.getDatePicker().setMaxDate(endDate.getTime());
                dialog.show();
            }
        });
        auto_complete_txt = findViewById(R.id.auto_complete_txt);
        adapterItems = new ArrayAdapter<String>(this, R.layout.list_name_trip, items);
        auto_complete_txt.setAdapter(adapterItems);
        auto_complete_txt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemSelected = adapterView.getItemAtPosition(i).toString();
            }
        });
        desc = findViewById(R.id.description);
        address = findViewById(R.id.address);
        amount = findViewById(R.id.number);
        add_btn = findViewById(R.id.addExpense);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkAllField()){
                    MyDatabaseHelper db = new MyDatabaseHelper(AddExpenseActivity.this);
                    db.addExpense(itemSelected,
                            amount.getText().toString().trim(),
                            time.getText().toString().trim(),
                            address.getText().toString().trim(),
                            desc.getText().toString().trim(),
                            id
                    );
                    Intent intent = new Intent(AddExpenseActivity.this, TripDetailActivity.class);
                    intent.putExtra("id",id);
                    setResult(RESULT_OK,intent);
                    finish();
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.custom_toasts,
                            (ViewGroup) view.findViewById(R.id.toast_type));
                    TextView tv = layout.findViewById(R.id.toast_text);
                    tv.setText("Added new expense");
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
                }

            }
        });




    }

    private boolean checkAllField() {
        boolean check = true;
        if (amount.getText().toString().length() == 0){
            amount.setError("Empty");
            check = false;
        }
        if (time.getText().toString().length() == 0){
            time.setError("Empty");

            check = false;
        }


        return check;
    }

    private void updateLabel() {
        time.setText(sdf.format(myCalendar.getTime()));
    }


}
