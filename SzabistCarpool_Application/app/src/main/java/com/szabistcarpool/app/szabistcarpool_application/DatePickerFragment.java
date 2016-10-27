package com.szabistcarpool.app.szabistcarpool_application;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by ttwyf on 4/24/2016.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return  new DatePickerDialog(getActivity(),this, year,month,day);
    }
    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        MainActivity.DateEdit.setText(day + "/" + (month + 1) + "/" + year);
    }
}
