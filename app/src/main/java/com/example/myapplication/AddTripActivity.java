package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class AddTripActivity extends AppCompatActivity {
    static EditText date_txt;
    static EditText date_end_txt;
    Calendar calendar;
    String itemSelected,riskCheck = "NO";
    String[] items = {"Vacation","Meeting","Hometown","Conference","Events","Offices","Others"};
    ArrayAdapter<String> adapterItems;
    AutoCompleteTextView auto_complete_txt;
    SwitchMaterial switchMaterial;
    FloatingActionButton addDone;
    EditText dept,dest,desc;
    TextInputLayout dropdown,date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_trip);
        Toolbar toolbar = findViewById(R.id.toolbarAddTrip);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        date_txt = findViewById(R.id.date_txt);
        date_end_txt = findViewById(R.id.date_end_txt);
        date  =  findViewById(R.id.date);
        date_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTruitonDatePickerDialog(view);
            }
        });
        date_end_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (date_txt.getText().toString().isEmpty()){
                    date.setError("Please choose this first!");
                    return;
                }
                showToDatePickerDialog(view);
            }
        });
        switchMaterial = findViewById(R.id.risk);
        dept = findViewById(R.id.departure_txt);
        dest = findViewById(R.id.destination_txt);
        desc = findViewById(R.id.description_txt);
        addDone = findViewById(R.id.done_btn);
        dropdown = findViewById(R.id.dropdown_menu);

        //Dropdown menu
        auto_complete_txt = findViewById(R.id.auto_complete_txt);
        adapterItems = new ArrayAdapter<String>(this, R.layout.list_name_trip, items);
        auto_complete_txt.setAdapter(adapterItems);
        auto_complete_txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(editable)){
                    dropdown.setError(null);
                }
            }
        });
        date_txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(editable)){
                    date.setError(null);
                    date_txt.setError(null);
                }
            }
        });
        auto_complete_txt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemSelected = adapterView.getItemAtPosition(i).toString();
            }
        });
        /**/
        switchMaterial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b){
                    riskCheck = "YES";
                } else {
                    riskCheck = "NO";
                }
            }
        });
        addDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAllField();
                if (checkAllField()){

                    MyDatabaseHelper db = new MyDatabaseHelper(AddTripActivity.this);
                    db.addTrip(itemSelected,
                            dept.getText().toString().trim(),
                            dest.getText().toString().trim(),
                            date_txt.getText().toString().trim(),
                            date_end_txt.getText().toString().trim(),
                            riskCheck,desc.getText().toString().trim()
                    );
                    finish();
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.custom_toasts,
                            (ViewGroup) findViewById(R.id.toast_type));
                    TextView tv = layout.findViewById(R.id.toast_text);
                    tv.setText("Added new trip");
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
                }
            }
            private boolean checkAllField() {
                boolean check = true;
                if (dept.getText().toString().length() == 0){
                    dept.setError("Empty");
                    check = false;
                }
                if (dest.getText().toString().length() == 0){
                    dest.setError("Empty");
                    check = false;
                }
                if (auto_complete_txt.getText().toString().length() == 0){
                    dropdown.setError("Empty");
                    check =  false;
                }
                if (date_txt.getText().toString().length() == 0){
                    date_txt.setError("Empty");
                    check = false;
                }
                if (date_end_txt.getText().toString().length() == 0){
                    date_end_txt.setError("Empty");
                    check = false;
                }
                return check;

            }


        });
    }
    public void showTruitonDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
    public void showToDatePickerDialog(View v) {
        DialogFragment newFragment = new ToDatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog;
            datePickerDialog = new DatePickerDialog(getActivity(),this, year,
                    month,day);
            return datePickerDialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            int monthAdd = month + 1;
            date_txt.setText(day + "-" + monthAdd  + "-" + year);
        }
    }
    public static class ToDatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {
        // Calendar startDateCalendar=Calendar.getInstance();
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            String getfromdate = date_txt.getText().toString().trim();
            String getfrom[] = getfromdate.split("-");
            int year,month,day;
            year= Integer.parseInt(getfrom[2]);
            month = Integer.parseInt(getfrom[1]);
            day = Integer.parseInt(getfrom[0]);
            final Calendar c = Calendar.getInstance();
            c.set(year,month,day+1);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),this, year,month,day);
            datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
            return datePickerDialog;
        }
        public void onDateSet(DatePicker view, int year, int month, int day) {

            date_end_txt.setText(day + "-" + month  + "-" + year);
        }
    }
}