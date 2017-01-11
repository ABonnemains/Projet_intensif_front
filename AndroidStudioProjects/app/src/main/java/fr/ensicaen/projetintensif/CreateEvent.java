package fr.ensicaen.projetintensif;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CreateEvent extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_event, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        final Button button = (Button) getView().findViewById(R.id.buttonCreateEvent);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText editTextName = (EditText) getView().findViewById(R.id.name);
                String name = editTextName.getText().toString();
                EditText editTextPlace = (EditText) getView().findViewById(R.id.place);
                String place = editTextPlace.getText().toString();
                EditText editTextDate = (EditText) getView().findViewById(R.id.date);
                String date = editTextDate.getText().toString();
                EditText editTextDescription = (EditText) getView().findViewById(R.id.description);
                String description = editTextDescription.getText().toString();

                String text = name + " " + place + " " + date + " " + description;

                Toast toast = Toast.makeText(getActivity().getApplicationContext(), text, Toast.LENGTH_SHORT);
                toast.show();
                if (!name.equals("") && !place.equals("") && !date.equals("") && !description.equals("")) {
                    dismiss();
                } else {
                    Toast toastError = Toast.makeText(getActivity().getApplicationContext(), "Informations non complètes", Toast.LENGTH_SHORT);
                    toastError.show();
                }
            }
        });

        final EditText editTextDate = (EditText) getView().findViewById(R.id.date);
        editTextDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                Calendar calendar = Calendar.getInstance();
                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedYear, int selectedMonth, int selectedDay) {
                        String format = "dd/MM/yy";
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
                        Calendar myCalendar = Calendar.getInstance();
                        myCalendar.set(Calendar.YEAR, selectedYear);
                        myCalendar.set(Calendar.MONTH, selectedMonth);
                        myCalendar.set(Calendar.DAY_OF_MONTH, selectedDay);
                        editTextDate.setText(simpleDateFormat.format(myCalendar.getTime()));
                    }
                },mYear, mMonth, mDay);

                mDatePicker.setTitle("Select date");
                mDatePicker.show();  }
        });

        final EditText editTextHour = (EditText) getView().findViewById(R.id.hour);
        editTextHour.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                Calendar calendar = Calendar.getInstance();
                int mHour = calendar.get(Calendar.HOUR);
                int mMinute = calendar.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
                        Calendar myCalendar = Calendar.getInstance();
                        myCalendar.set(Calendar.HOUR, selectedHour);
                        myCalendar.set(Calendar.MINUTE, selectedMinute);
                        editTextHour.setText( simpleDateFormat.format(myCalendar.getTime()));
                    }
                }, mHour, mMinute, true);

                mTimePicker.setTitle("Select time");
                mTimePicker.show();  }
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
