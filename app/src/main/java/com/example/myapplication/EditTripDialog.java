package com.example.myapplication;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditTripDialog extends DialogFragment {

    public MyDatabaseHelper db;
    private static final String TAG = "DialogFragment";

    public interface OnInputListener {
        void sendInput(String name,String dept,String dest,String date,String date_end,String risk,String desc);
    }
    public OnInputListener mOnInputListener;
    EditText dept_dialog,dest_dialog,desc_dialog;
    static EditText date_dialog,date_dialog_end;
    String dept_get,risk_get,name_get,dest_get,date_get,desc_get,date_end_get;
    int id;
    SwitchMaterial switch_dialog;
    AutoCompleteTextView autoCompleteTextView_dialog;
    String[] items = {"Vacation","Meeting","Hometown","Conference","Events","Offices","Others"};
    ArrayAdapter<String> adapterItems;

    Button update,cancel;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.layout_dialog_update, null);
        db = new MyDatabaseHelper(getActivity());
        getData();
        update = v.findViewById(R.id.update_btn_dialog);
        cancel = v.findViewById(R.id.cancel_btn_dialog);
        date_dialog = v.findViewById(R.id.date_dialog);
        dest_dialog = v.findViewById(R.id.dest_dialog);
        desc_dialog = v.findViewById(R.id.desc_dialog);
        dept_dialog = v.findViewById(R.id.dept_dialog);
        switch_dialog = v.findViewById(R.id.risk_dialog);
        date_dialog_end = v.findViewById(R.id.date_dialog_end_txt);
        autoCompleteTextView_dialog = v.findViewById(R.id.auto_complete_txt_dialog);
        adapterItems = new ArrayAdapter<String>(getContext(), R.layout.list_name_trip, items);
        autoCompleteTextView_dialog.setAdapter(adapterItems);

        setText();
        autoCompleteTextView_dialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                name_get = adapterView.getItemAtPosition(i).toString();
            }
        });
        date_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTruitonDatePickerDialog(view);
            }
        });
        date_dialog_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToDatePickerDialog(view);
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkAllField()){
                    String name = autoCompleteTextView_dialog.getText().toString();
                    String dept = dept_dialog.getText().toString();
                    String dest = dest_dialog.getText().toString();
                    String date = date_dialog.getText().toString();
                    String risk = risk_get;
                    String desc = desc_dialog.getText().toString();
                    String date_end = date_dialog_end.getText().toString();

                    db.updateData(id,name,dept,dest,date,date_end,risk,desc);
                    mOnInputListener.sendInput(name,dept,dest,date,date_end,risk,desc);
                    getDialog().dismiss();
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.custom_toasts,
                            (ViewGroup) view.findViewById(R.id.toast_type));
                    TextView tv = layout.findViewById(R.id.toast_text);
                    tv.setText("Updated the trip");
                    Toast toast = new Toast(getActivity().getApplicationContext());
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);

                    toast.show();
                }


            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });
        switch_dialog.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    risk_get = "YES";
                } else {
                    risk_get = "NO";
                }
            }
        });
        builder.setView(v);
        return builder.create();
    }

    private boolean checkAllField() {

            boolean check = true;
            if (dept_dialog.getText().toString().length() == 0){
                dept_dialog.setError("Empty");
                check = false;
            }
            if (dest_dialog.getText().toString().length() == 0){
                dest_dialog.setError("Empty");
                check = false;
            }
            return check;


    }

    private void getData() {
        Bundle bundle = getArguments();
        id = bundle.getInt("id",0);
        name_get = bundle.getString("name","");
        dept_get = bundle.getString("dept","");
        dest_get = bundle.getString("dest","");
        date_get = bundle.getString("datefrom","");
        date_end_get = bundle.getString("dateto","");
        risk_get = bundle.getString("risk","");
        desc_get = bundle.getString("desc","");
        desc_get = bundle.getString("desc","");

    }

    private void setText() {
        autoCompleteTextView_dialog.setText(name_get,false);
        dept_dialog.setText(dept_get);
        dest_dialog.setText(dest_get);
        date_dialog.setText(date_get);
        if (risk_get.equals("YES")){
            switch_dialog.setChecked(true);
        } else {
            switch_dialog.setChecked(false);
        }
        desc_dialog.setText(desc_get);
        date_dialog_end.setText(date_end_get);
    }

    public void showTruitonDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    public void showToDatePickerDialog(View v) {
        DialogFragment newFragment = new ToDatePickerFragment();
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
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
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            int monthSelected = month + 1;


            date_dialog.setText(day + "-" + monthSelected  + "-" + year);
            try {
                Date startDate = sdf.parse(date_dialog.getText().toString());
                Date endDate = sdf.parse(date_dialog_end.getText().toString());
                if (startDate.after(endDate)) {
                    Calendar c = Calendar.getInstance();
                    c.set(year,month,day+1);

                    date_dialog_end.setText(sdf.format(c.getTime()));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
    public static class ToDatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {
        // Calendar startDateCalendar=Calendar.getInstance();
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            String getfromdate = date_dialog.getText().toString().trim();
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

            date_dialog_end.setText(day + "-" + month  + "-" + year);
        }
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mOnInputListener
                    = (OnInputListener)getActivity();
        }
        catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ClassCastException: "
                    + e.getMessage());
        }
    }
}
