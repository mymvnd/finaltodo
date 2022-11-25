package minh.tdtu.todolistapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Activity_Edit extends AppCompatActivity {
    private EditText edtTitle2,edtDate2,edtStatus2,edtContent2,edtTime2;
    private Button bntEdit2,bntCancel2;
    private static final int MODE_CREATE = 1;
    private static final int MODE_EDIT = 2;
    private boolean needRefresh;
    private int mode;
    private Note note;

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

        bntEdit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
    public void buttonCancelClicked()  {
        this.onBackPressed();
    }

}