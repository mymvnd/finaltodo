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

public class Action_add extends AppCompatActivity {

    private EditText edtTitle,edtDate,edtStatus,edtContent,edtTime;
    private Button bntSave,bntCancel;
    private int id;
    private Note note;
    private Spinner sp;
    Database database;

    private static final int MODE_CREATE = 1;
    private static final int MODE_EDIT = 2;
    private boolean needRefresh;
    private int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_add);
        edtTitle = findViewById(R.id.edtNoteTitle);
        edtDate = findViewById(R.id.edtDate);
        edtTime = findViewById(R.id.edtTime);
        sp = findViewById(R.id.spinner);
        edtStatus = findViewById(R.id.edtStatus);
        edtContent = findViewById(R.id.edtContent);
        bntSave = findViewById(R.id.bntSave);
        bntCancel = findViewById(R.id.bntCancel);
        ArrayList<String> ar = new ArrayList<>();
        ar.add("Wait");
        ar.add("Done");
        ar.add("Late");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                ar);

        // Layout for All ROWs of Spinner.  (Optional for ArrayAdapter).
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String status2 = adapter.getItem(i);
                edtStatus.setText(status2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        bntSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                buttonSaveClicked();
                finish();
            }
        });

        bntCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonCancelClicked();
                finish();
            }
        });


        Intent intent = this.getIntent();
        this.note = (Note) intent.getSerializableExtra("note");
        if(note== null)  {
            this.mode = MODE_CREATE;
        } else  {
            this.mode = MODE_EDIT;
            this.edtTitle.setText(note.getTitle());
            this.edtDate.setText(note.getDate());
            this.edtTime.setText(note.getTime());
            this.edtStatus.setText(note.getStatus());
            this.edtContent.setText(note.getContent());
        }

    }

    public void buttonSaveClicked()  {
        Database db = new Database(this);

        String title = this.edtTitle.getText().toString();
        String date = this.edtDate.getText().toString().trim();
        String time = this.edtTime.getText().toString().trim();
        String status = this.edtStatus.getText().toString();
        String content = this.edtContent.getText().toString();
        setAlarm(title,date,time);
        if(title.equals("") || content.equals("")) {
            Toast.makeText(getApplicationContext(),
                    "Please enter title & content", Toast.LENGTH_LONG).show();
            return;
        }
        if(mode == MODE_CREATE ) {
            this.note= new Note(id,title,date,time,status,content);


            db.addNote(note);
        } else  {
            this.note.setTitle(title);
            this.note.setDate(date);
            this.note.setTime(time);
            this.note.setStatus(status);
            this.note.setContent(content);


            db.updateNote(note);
            //setAlarm(title,date,time);

        }

        this.needRefresh = true;
        Intent data = new Intent();
        data.putExtra("needRefresh", needRefresh);
        this.setResult(Action_add.RESULT_OK, data);

        //this.onBackPressed();
    }



    public void buttonCancelClicked()  {
        this.onBackPressed();
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

                Action_add.this.edtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
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
    String timeTonotify2;
    public void choiceTime(View view) {

        //int seconds = calendar.get(Calendar.SECOND);
        //String date = sdf.format(c.getTime());
        boolean is24HView = true;
        int selectedHour = 10;
        int selectedMinute = 20;
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





                edtTime.setText(formattedHour + ":" + formattedMinute );
                timeTonotify2 = formattedHour + ":" + formattedMinute;
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
    private void setAlarm(String title, String date, String time) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent2 =new Intent(Action_add.this,Alarm_Broadcast.class);
        intent2.putExtra("event", title);
        intent2.putExtra("time", time);
        intent2.putExtra("date", date);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(Action_add.this, 0, intent2, PendingIntent.FLAG_ONE_SHOT);
        String dateandtime = date + " " + timeTonotify2;
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