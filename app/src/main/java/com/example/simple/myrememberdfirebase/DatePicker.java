package com.example.simple.myrememberdfirebase;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.EditText;
import java.util.Calendar;

public class DatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, yy, mm, dd);
    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int day) {
        populateSetDate(year, month + 1, day);
    }


    public void populateSetDate(int year, int month, int day) {
        EditText data_date= getActivity(). findViewById(R.id.data_date);
        data_date.setText(day+"/"+month+"/"+year);
    }

}
