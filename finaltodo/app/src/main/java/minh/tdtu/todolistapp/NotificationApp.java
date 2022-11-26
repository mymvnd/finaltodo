package minh.tdtu.todolistapp;


import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NotificationApp extends AppCompatActivity {
    TextView textView,textView2,textView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_message);
        Log.d(TAG, "onCreate: notification chay thoi gian roi ne");
        textView = findViewById(R.id.tv_message);
        textView2 = findViewById(R.id.tv_Content_Notification);
        textView3= findViewById(R.id.tv_Time_Notification);

        Bundle bundle = getIntent().getExtras();
        textView.setText(bundle.getString("message"));
        textView2.setText(bundle.getString("content"));
        textView3.setText(bundle.getString("date"));



    }
}
