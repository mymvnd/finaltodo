package minh.tdtu.todolistapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class MainActivity extends AppCompatActivity{

    private NotificationManagerCompat notificationManagerCompat;
    private ImageView imageViewUploadDB;
    private RecyclerView mRecylerview;
    private ArrayList<Note> noteList;
    Database database;
    NoteAdapter noteAdapter;
    private Note note;

    private FloatingActionButton bnt_add;
    private int draggedItemIndex;

    private static final int MENU_ITEM_VIEW = 111;
    private static final int MENU_ITEM_EDIT = 222;
    private static final int MENU_ITEM_CREATE = 333;
    private static final int MENU_ITEM_DELETE = 444;

    private static final int MY_REQUEST_CODE = 9;

    private ImageView img_v;
    private EditText editTextTitleNotify, editTextMessageNotify;

    public Calendar calendar = Calendar.getInstance();
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    //final Intent intent2 = new Intent(MainActivity.this,NotificationApp.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bnt_add = findViewById(R.id.bntAdd);
        imageViewUploadDB = findViewById(R.id.imageViewUploadDB);

        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        mRecylerview = findViewById(R.id.RecylerView);
        mRecylerview.setLayoutManager(new LinearLayoutManager(this));
        mRecylerview.setHasFixedSize(true);
        //noteList = Note.generate10Note();

        Database database = new Database(this);
        //database.createDefaultNotesIfNeed();

        noteList =  database.getAllNotes();
        //noteList.addAll(noteList);

        noteAdapter = new NoteAdapter(noteList, this);
        mRecylerview.setAdapter(noteAdapter);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        mRecylerview.addItemDecoration(itemDecoration);

        bnt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Action_add.class);
                int REQUEST_CODE = 9;
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(
                        ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                        ItemTouchHelper.END | ItemTouchHelper.START );
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                draggedItemIndex = viewHolder.getAdapterPosition();
                int targetIndex = target.getAdapterPosition();
                Collections.swap(noteAdapter.getNoteList(), draggedItemIndex, targetIndex);
                noteAdapter.notifyItemMoved(draggedItemIndex,targetIndex);
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                switch (direction){
                    case ItemTouchHelper.END:
                        Note note = noteAdapter.getNote(viewHolder.getAdapterPosition());
                        noteAdapter.removeNote(note);
                        deleteNote(note);
                        break;

                    case ItemTouchHelper.START:
                        Note nte = noteAdapter.getNote(viewHolder.getAdapterPosition());
                        noteAdapter.removeNote(viewHolder.getAdapterPosition());
                        deleteNote(nte);
                        break;
                }
            }
        });
        helper.attachToRecyclerView(mRecylerview);

        imageViewUploadDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //noteAdapter.setNoteList(database.sortDB());
                noteList.clear();
                Database db = new Database(MainActivity.this);
                ArrayList<Note> list = db.sortDB();
                noteList.addAll(list);
                noteAdapter.notifyDataSetChanged();
            }
        });

        int currentDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentdYear = calendar.get(Calendar.YEAR);


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("onStart", "onStart: Chay roi nhe' ");
        noteList.clear();
        Database db = new Database(this);
        ArrayList<Note> list = db.getAllNotes();
        noteList.addAll(list);
        noteAdapter.notifyDataSetChanged();
    }

    private void deleteNote(Note note)  {
        Database db = new Database(this);
        db.deleteNote(note);
        this.noteList.remove(note);
        // Refresh ListView.
        this.noteAdapter.notifyDataSetChanged();
    }

    public void GetNoteinDB(){
        Cursor Notedata = database.GetData("SELECT * FROM NOTES");
        while(Notedata.moveToNext()){
            String ten = Notedata.getString(1);
            String date = Notedata.getString(2);
            String time = Notedata.getString(3);
            String status = Notedata.getString(4);
            String content = Notedata.getString(5);
            int id = Notedata.getInt(0);
            Note note = new Note(id,ten,date,time,status,content);
            noteList.add(note);
        }
        noteAdapter.notifyDataSetChanged();
    }
    /*
    public Note getRecord(String s_Des){

        Cursor Notedata2 =database.QueryData("SELECT * FROM NOTES WHERE NOTE_TITLE = '"+ s_Des +"'");

        String ten_Notedata2 = Notedata2.getString(1);
        String date_Notedata2 = Notedata2.getString(2);
        String time_Notedata2 = Notedata2.getString(3);
        String status_Notedata2 = Notedata2.getString(4);
        String content_Notedata2 = Notedata2.getString(5);
        int id = Notedata2.getInt(0);

        Note nte = new Note(id,ten_Notedata2,date_Notedata2,time_Notedata2,status_Notedata2,content_Notedata2);

        return nte;
    }

     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Action_add.RESULT_OK && requestCode == MY_REQUEST_CODE) {
            boolean needRefresh = data.getBooleanExtra("needRefresh", true);
            // Refresh ListView
            if (needRefresh) {
                this.noteList.clear();
                Database db = new Database(this);
                ArrayList<Note> list = db.getAllNotes();
                this.noteList.addAll(list);

            // Notify the data change (To refresh the RecylerView).
            this.noteAdapter.notifyDataSetChanged();
            }
        }else if (resultCode == Activity_Edit.RESULT_OK) {
            boolean needRefresh = data.getBooleanExtra("needRefresh", true);
            // Refresh ListView
            if (needRefresh) {
                this.noteList.clear();
                Database db = new Database(this);
                ArrayList<Note> listNote = db.getAllNotes();
                this.noteList.addAll(listNote);
                this.noteAdapter.notifyDataSetChanged();
            }
            // Notify the data change (To refresh the RecylerView).\
        }

        //pendingIntent = PendingIntent.getBroadcast(MainActivity.this,0,intent2,PendingIntent.FLAG_UPDATE_CURRENT);
        //alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);

    }

    @Override
    protected void onStop() {
        super.onStop();
        this.noteList.clear();
        Database db = new Database(this);
        ArrayList<Note> list = db.getAllNotes();
        this.noteList.addAll(list);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.noteList.clear();
        Database db = new Database(this);
        ArrayList<Note> list = db.getAllNotes();
        this.noteList.addAll(list);
    }

    private void sendOnChannel1()  {
        String title = this.note.getTitle();
        String message = "It's time to prepare every thing";

        Notification notification = new NotificationCompat.Builder(MainActivity.this, NotificationApp.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_notify)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        int notificationId = 1;
        this.notificationManagerCompat.notify(notificationId, notification);
    }


}