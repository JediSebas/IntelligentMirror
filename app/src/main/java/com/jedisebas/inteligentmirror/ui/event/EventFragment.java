package com.jedisebas.inteligentmirror.ui.event;

import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.jedisebas.inteligentmirror.Loggeduser;
import com.jedisebas.inteligentmirror.R;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

/**
 * Class that let user create an event.
 */

public class EventFragment extends Fragment {

    private EventViewModel eventViewModel;
    private TextView dateTv, hourTv;
    private EditText eventNameEt, howManyDaysEt;
    private Button saveEventBtn;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private TimePickerDialog.OnTimeSetListener onTimeSetListener;

    private String dateS, hourS;
    static boolean saveComplete;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        eventViewModel =
                new ViewModelProvider(this).get(EventViewModel.class);

        View root = inflater.inflate(R.layout.fragment_event, container, false);
        dateTv = root.findViewById(R.id.dateTv);
        hourTv = root.findViewById(R.id.hourTv);
        eventNameEt = root.findViewById(R.id.eventEt);
        saveEventBtn = root.findViewById(R.id.eventBtn);
        howManyDaysEt = root.findViewById(R.id.howmanydaysEt);

        dateTv.setOnClickListener(v -> chooseDate());
        hourTv.setOnClickListener(v -> chooseHour());

        onDateSetListener = (view, year, month, dayOfMonth) -> {
            month++;
            String mon = getMonthName(month);
            String date = dayOfMonth + " " + mon + " " + year;
            dateTv.setText(date);
            dateS = year + "-" + month + "-" + dayOfMonth;
        };

        onTimeSetListener = (view, hourOfDay, minute) -> {
            String h = getHour(hourOfDay);
            String m = getMinute(minute);

            String time = h + ":" + m;
            hourTv.setText(time);
            hourS = time + ":00";
        };

        saveEventBtn.setOnClickListener(v -> {
            String eventName = eventNameEt.getText().toString().trim();
            String howManyDayss = howManyDaysEt.getText().toString().trim();
            if (eventName.isEmpty() || howManyDayss.isEmpty()) {
                Toast.makeText(getContext(), "Not all data", Toast.LENGTH_SHORT).show();
            } else {
                int howManyDays = Integer.parseInt(howManyDayss);
                //TODO save Event in database
                String entireDate = dateS + " " + hourS;
                SaveEvent saveEvent = new SaveEvent(eventName, howManyDays, entireDate);
                saveEvent.t.start();
                System.out.println("Started saving");
                Toast.makeText(getContext(), "Saving event...", Toast.LENGTH_LONG).show();
                try {
                    saveEvent.t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (saveComplete) {
                    Toast.makeText(getContext(), "Event saving complete", Toast.LENGTH_LONG).show();
                }
            }
        });

        return root;
    }

    String getMinute(int minute) {
        if (minute < 10) {
            return "0" + minute;
        } else {
            return "" + minute;
        }
    }

    String getHour(int hour) {
        if (hour < 10) {
            return "0" + hour;
        } else {
            return "" + hour;
        }
    }

    String getMonthName(int month) {
        switch (month) {
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
            default:
                return  "Month";
        }
    }

    void chooseDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        Context context = getContext();

        DatePickerDialog dialog = new DatePickerDialog(context, onDateSetListener, year, month, day);
        dialog.show();
    }

    void chooseHour() {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);

        Context context = getContext();

        TimePickerDialog dialog = new TimePickerDialog(context, onTimeSetListener, hour, minute, true);
        dialog.show();
    }
}