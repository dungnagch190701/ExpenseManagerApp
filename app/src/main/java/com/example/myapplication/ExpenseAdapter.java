package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.MyViewHolder> {
    Context context;
    ArrayList<ExpenseEntity> expenseArrayList = new ArrayList<>();
    Activity activity;
    TripDetailActivity mActivity;
    Date startDate,endDate;
    public ExpenseAdapter(Activity activity,Context context,ArrayList<ExpenseEntity> expenseArrayList) {
        this.context = context;
        this.activity = activity;
        this.expenseArrayList = expenseArrayList;
        mActivity = (TripDetailActivity) activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.expense_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final ExpenseEntity expenseEntity = expenseArrayList.get(position);
        holder.type.setText(String.valueOf(expenseEntity.getType()));
        holder.amount.setText(String.valueOf(expenseEntity.getAmount()));
        holder.time.setText(String.valueOf(expenseEntity.getTime()));
        holder.cmt.setText(String.valueOf(expenseEntity.getCmt()));
        if (expenseEntity.getType().equals("Food")){
            holder.img.setImageResource(R.drawable.food);
        } else if (expenseEntity.getType().equals("Transport")){
            holder.img.setImageResource(R.drawable.travel);
        } else if (expenseEntity.getType().equals("Parking")){
            holder.img.setImageResource(R.drawable.parking);
        } else if (expenseEntity.getType().equals("Gift")){
            holder.img.setImageResource(R.drawable.gift);
        } else if (expenseEntity.getType().equals("Coffee")){
            holder.img.setImageResource(R.drawable.coffee);
        } else if (expenseEntity.getType().equals("Clothes")){
            holder.img.setImageResource(R.drawable.cloth);
        } else {
            holder.img.setImageResource(R.drawable.cart);
        }
        holder.address.setText(String.valueOf(expenseEntity.getAddress()));
        if (expenseEntity.getAddress().isEmpty()){

            holder.map.setVisibility(View.GONE);

        } else {
            holder.map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context,MapActivity.class);
                    intent.putExtra("location",expenseEntity.getAddress());
                    context.startActivity(intent);
                }
            });
        }



        holder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MyDatabaseHelper db = new MyDatabaseHelper(context);
                db.deleteExpense(expenseArrayList.get(position).getId());
                expenseArrayList.remove(position);
                notifyDataSetChanged();
                if (getItemCount() == 0 ) {
                    mActivity.empty.setVisibility(View.VISIBLE);
                    mActivity.recyclerView.setVisibility(View.GONE);
                    mActivity.empty_txt.setVisibility(View.VISIBLE);
                }else {
                    mActivity.empty.setVisibility(View.GONE);
                    mActivity.recyclerView.setVisibility(View.VISIBLE);
                    mActivity.empty_txt.setVisibility(View.GONE);
                }
                LayoutInflater inflater = mActivity.getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toasts,
                        (ViewGroup) view.findViewById(R.id.toast_type));
                TextView tv = layout.findViewById(R.id.toast_text);
                tv.setText("Deleted the expense");
                Toast toast = new Toast(mActivity.getApplicationContext());
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();

            }
        });
        holder.update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.update_expense_dialog);
                Spinner spinner = dialog.findViewById(R.id.spinner);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                        android.R.layout.simple_spinner_item,context.getResources().getStringArray(R.array.expense_name)
                        );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);


                String type = String.valueOf(expenseEntity.getType());
                int typeSelect = adapter.getPosition(type);
                spinner.setSelection(typeSelect);
                EditText amount = dialog.findViewById(R.id.amount);
                EditText date = dialog.findViewById(R.id.date);
                EditText desc = dialog.findViewById(R.id.desc);
                EditText adr = dialog.findViewById(R.id.adr);
                amount.setText(String.valueOf(expenseEntity.getAmount()));
                date.setText(String.valueOf(expenseEntity.getTime()));
                desc.setText(String.valueOf(expenseEntity.getCmt()));
                adr.setText(String.valueOf(expenseEntity.getAddress()));
                Button update = dialog.findViewById(R.id.update);
                String datefrom = mActivity.date_txt.getText().toString();
                String dateto= mActivity.date_end_txt.getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");



                try {
                    startDate = sdf.parse(datefrom);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    endDate = sdf.parse(dateto);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar myCalendar = Calendar.getInstance();
                try {
                    myCalendar.setTime(sdf.parse(datefrom));
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
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
                        DatePickerDialog dialog = new DatePickerDialog(context,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH));
                        dialog.getDatePicker().setMinDate(startDate.getTime());
                        dialog.getDatePicker().setMaxDate(endDate.getTime());
                        dialog.show();
                    }

                    private void updateLabel() {
                        date.setText(sdf.format(myCalendar.getTime()));
                    }
                });
                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (checkAllField()){
                            int trip_id = expenseEntity.getTrip_id();
                            int id = expenseEntity.getId();
                            String name = spinner.getSelectedItem().toString();
                            String amount_get = amount.getText().toString().trim();
                            String date_get = date.getText().toString().trim();
                            String desc_get = desc.getText().toString().trim();
                            String adr_get = adr.getText().toString().trim();
                            MyDatabaseHelper db = new MyDatabaseHelper(context);
                            db.updateExpense(id,name,amount_get,date_get,adr_get,desc_get);
                            expenseArrayList.set(position, new ExpenseEntity(id,name,amount_get,date_get,adr_get,desc_get,trip_id));
                            notifyItemChanged(position);
                            dialog.dismiss();
                            LayoutInflater inflater = mActivity.getLayoutInflater();
                            View layout = inflater.inflate(R.layout.custom_toasts,
                                    (ViewGroup) view.findViewById(R.id.toast_type));
                            TextView tv = layout.findViewById(R.id.toast_text);
                            tv.setText("Updated the expense");
                            Toast toast = new Toast(mActivity.getApplicationContext());
                            toast.setDuration(Toast.LENGTH_LONG);
                            toast.setView(layout);
                            toast.show();

                        }

                    }

                    private boolean checkAllField() {

                            boolean check = true;
                            if (amount.getText().toString().length() == 0){
                                amount.setError("Empty");
                                check = false;
                            }
                            if (date.getText().toString().length() == 0){
                                date.setError("Empty");

                                check = false;
                            }


                            return check;
                        }

                });



                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.show();
                dialog.getWindow().setAttributes(lp);
            }


        });



    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView id,type,amount,time,cmt,trip_id,address;
        ImageView img,map;
        Button delete_btn,update_btn;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.type);
            amount = itemView.findViewById(R.id.amount);
            time = itemView.findViewById(R.id.time);
            cmt = itemView.findViewById(R.id.comment);
            img = itemView.findViewById(R.id.imageView);
            address = itemView.findViewById(R.id.address);
            update_btn = itemView.findViewById(R.id.edit_expense);
            delete_btn = itemView.findViewById(R.id.delete_expense);
            map = itemView.findViewById(R.id.map);



        }
    }


    @Override
    public int getItemCount() {
        return expenseArrayList.size();
    }


}
