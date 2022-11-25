package minh.tdtu.todolistapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Action_add extends AppCompatActivity {

    private EditText edtTitle,edtDate,edtStatus,edtContent,edtTime;
    private Button bntSave,bntCancel;
    private int id;
    private Note note;

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
        edtStatus = findViewById(R.id.edtStatus);
        edtContent = findViewById(R.id.edtContent);
        bntSave = findViewById(R.id.bntSave);
        bntCancel = findViewById(R.id.bntCancel);

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
        String date = this.edtDate.getText().toString();
        String time = this.edtTime.getText().toString();
        String status = this.edtStatus.getText().toString();
        String content = this.edtContent.getText().toString();

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
                edtTime.setText(hourOfDay + ":" + minute );
                hour24hrs = hourOfDay;
                minutes = minute;
            }
        };

        // Create TimePickerDialog:
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                timeSetListener, hour24hrs, minutes, is24HView);

        // Show
        timePickerDialog.show();
    }
}