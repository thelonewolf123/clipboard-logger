package com.clipboard.log;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class view_data extends AppCompatActivity {

    public DatabaseHelper myDB;
    String data;
    String id;
    ArrayList<String> sqlid = new ArrayList<>();
    private Button copy;
    private Button delete;
    private TextView textView;
    private ClipboardManager clipboardManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data);

        initialize();
        initializeLogic();
    }

    private void initialize() {

        copy = (Button) findViewById(R.id.copy);
        delete = (Button) findViewById(R.id.delete);
        textView = (TextView) findViewById(R.id.editText);
        clipboardManager = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);

        data = this.getIntent().getStringExtra("data");
        id = this.getIntent().getStringExtra("id");
    }

    private void initializeLogic() {

        textView.setText(data);
        final String s = textView.getText().toString();
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClipData clip = ClipData.newPlainText("simple text", s);
                clipboardManager.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(), "copied", Toast.LENGTH_SHORT).show();

                myDB = new DatabaseHelper(getApplicationContext());
                Cursor cursor = myDB.getAllData();
                if (cursor.getCount() == 0) {

                    Toast.makeText(getApplicationContext(), "No text is copied yet.", Toast.LENGTH_SHORT).show();
                } else {
                    while (cursor.moveToNext()) {
                        try {
                            sqlid.add("" + cursor.getInt(0));
                            //sqldate.add(cursor.getString(1));
                            //sqltime.add(cursor.getString(2));
                            //sqldata.add(cursor.getString(3));
                        } catch (Exception e) {

                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }


                }

                String str = sqlid.get(sqlid.size() - 1);
                //Toast.makeText(getApplicationContext(),str, Toast.LENGTH_SHORT).show();

                myDB.deleteData(str);

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myDB = new DatabaseHelper(getApplicationContext());
                myDB.deleteData(id);

                //startActivity(new Intent(getApplicationContext(),MainActivity.class));

                finish();

            }
        });

    }

    //@Override
    //public void onBackPressed() {
    //super.onBackPressed();
    //startActivity(new Intent(getApplicationContext(),MainActivity.class));

    //finish();

    //}
}
