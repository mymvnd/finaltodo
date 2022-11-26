package minh.tdtu.todolistapp;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Activity_Edit extends AppCompatActivity {
    private EditText edtTitle2,edtDate2,edtStatus2,edtContent2,edtTime2;
    private Button bntEdit2,bntCancel2;
    private static final int MODE_CREATE = 1;
    private static final int MODE_EDIT = 2;
    private boolean needRefresh;
    private int mode;
    private Note note;
    private Spinner sp2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        edtTitle2 = findViewById(R.id.edtNoteTitle2);
        edtDate2 = findViewById(R.id.edtDate2);
        edtTime2 = findViewById(R.id.edtTime2);
        edtStatus2 = findViewById(R.id.edtStatus2);
        edtContent2 = findViewById(R.id.edtContent2);
        bntEdit2 = findViewById(R.id.bntEdit2);
        bntCancel2 = findViewById(R.id.bntCancel2);
        sp2 = findViewById(R.id.spinner2);
        ArrayList<String> ar = new ArrayList<>();
        ar.add("Wait");
        ar.add("Done");
        ar.add("Late");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                ar);

        // Layout for All ROWs of Spinner.  (Optional for ArrayAdapter).
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp2.setAdapter(adapter);


        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
            return;
        }
        Note note = (Note) bundle.get("NoteDetails");

        edtTitle2.setText(note.getTitle());
        edtDate2.setText(note.getDate());
        edtTime2.setText(note.getTime());
        edtStatus2.setText(note.getStatus());
        edtContent2.setText(note.getContent());
        String title = edtTitle2.getText().toString();
        String date = edtDate2.getText().toString();
        String time = edtTime2.getText().toString();

        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String status2 = adapter.getItem(i);
                edtStatus2.setText(status2);


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        bntEdit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAlarm(title,date,time);
                buttonSaveClicked(note);
                finish();
            }
        });
        bntCancel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonCancelClicked();
                finish();
            }
        });


    }
    public void buttonSaveClicked(Note note)  {
        Database db = new Database(this);

        String title = this.edtTitle2.getText().toString();
        String date = this.edtDate2.getText().toString();
        String time = this.edtTime2.getText().toString();
        String status = this.edtStatus2.getText().toString();
        String content = this.edtContent2.getText().toString();

        if(title.equals("") || content.equals("")) {
            Toast.makeText(getApplicationContext(),
                    "Please enter title & content", Toast.LENGTH_LONG).show();

        }


        note.setTitle(title);
        note.setDate(date);
        note.setTime(time);
        note.setStatus(status);
        note.setContent(content);
        db.updateNote(note);


        this.needRefresh = true;
        Intent data = new Intent();
        data.putExtra("needRefresh", needRefresh);
        this.setResult(Activity_Edit.RESULT_OK, data);

        //this.onBackPressed();
    }
    public void choiceDate(View view) {
        Calendar c = Calendar.getInstance();
        int selectedDayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        int selectedMonth = c.get(Calendar.MONTH);
        int selectedYear = c.get(Calendar.YEAR);

        String date = selectedDayOfMonth + "/" + (selectedMonth+1) + "/" + selectedYear;
        // Date Select Listener.

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {

                Activity_Edit.this.edtDate2.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
            }
        };

        // Create DatePickerDialog (Spinner Mode):
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                dateSetListener, selectedYear, selectedMonth, selectedDayOfMonth);

        // Show
        datePickerDialog.show();
    }

    //private int lastSelectedHour = -1;
    //private int lastSelectedMinute = -1;
    Calendar calendar = Calendar.getInstance();
    int hour24hrs = calendar.get(Calendar.HOUR_OF_DAY);
    int hour12hrs = calendar.get(Calendar.HOUR);
    int minutes = calendar.get(Calendar.MINUTE);
    String timeTonotify;
    public void choiceTime(View view) {

        boolean is24HView = true;
        // Time Set Listener.
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String formattedMinute;
                String formattedHour;

                if (minute <10) {
                    formattedMinute = "0" + minute;
                } else {
                    formattedMinute = "" + minute;
                }

                if(hourOfDay <10){
                    formattedHour = "0" + hourOfDay;
                }else {
                    formattedHour = "" + hourOfDay;
                }

                edtTime2.setText(formattedHour + ":" + formattedMinute );
                timeTonotify = formattedHour + ":" + formattedMinute;
                hour24hrs = Integer.parseInt(formattedHour);
                minutes = Integer.parseInt(formattedMinute);
            }
        };

        // Create TimePickerDialog:
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                timeSetListener, hour24hrs, minutes, is24HView);

        // Show
        timePickerDialog.show();
    }

    public void buttonCancelClicked()  {
        this.onBackPressed();
    }

    private void setAlarm(String title, String date, String time) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent2 =new Intent(Activity_Edit.this,Alarm_Broadcast.class);
        intent2.putExtra("event", title);
        intent2.putExtra("time", time);
        intent2.putExtra("date", date);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(Activity_Edit.this, 0, intent2, PendingIntent.FLAG_ONE_SHOT);
        String dateandtime = date + " " + timeTonotify;
        DateFormat formatter = new SimpleDateFormat("d-M-yyyy hh:mm");

        Log.d(TAG, "setAlarm:  set alarm ne");
        try {
            Date date1 = formatter.parse(dateandtime);
            alarmManager.set(AlarmManager.RTC_WAKEUP, date1.getTime(), pendingIntent);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        finish();

    }
}