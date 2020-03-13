package com.clipboard.log;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public DatabaseHelper myDB;
    private ListView cp_index;
    private ArrayList<String> sqlid = new ArrayList<>();
    private ArrayList<String> sqltime = new ArrayList<>();
    private ArrayList<String> sqldata = new ArrayList<>();
    private ArrayList<String> sqldate = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
        initializeLogic();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }

    private void initialize() {

        cp_index = (ListView) findViewById(R.id.cp_index);
        startService(new Intent(getBaseContext(), cp_monitor.class));
        //startActivity(new Intent(getApplicationContext(), view_data.class));
    }

    private void initializeLogic() {

        customAdapter ca = new customAdapter();

        cp_index.setAdapter(ca);

        cp_index.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {

                    //Toast.makeText(getApplicationContext(), "" + sqlid.get(position), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(), view_data.class);
                    intent.putExtra("data", sqldata.get(position));
                    intent.putExtra("id", sqlid.get(position));

                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        customAdapter c = new customAdapter();
        cp_index.setAdapter(c);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // The activity has become visible (it is now "resumed").

        customAdapter c = new customAdapter();
        cp_index.setAdapter(c);
    }

    class customAdapter extends BaseAdapter {

        private CheckBox checkBox;
        private TextView id;
        private TextView date;
        private TextView data;

        @Override
        public int getCount() {

            myDB = new DatabaseHelper(getApplicationContext());
            Cursor cursor = myDB.getAllData();
            return cursor.getCount();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.customlayout, null);

            checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
            id = (TextView) convertView.findViewById(R.id.id);
            date = (TextView) convertView.findViewById(R.id.date);
            data = (TextView) convertView.findViewById(R.id.data);

            myDB = new DatabaseHelper(getApplicationContext());
            Cursor cursor = myDB.getAllData();
            if (cursor.getCount() == 0) {

                Toast.makeText(getApplicationContext(), "No text is copied yet!", Toast.LENGTH_SHORT).show();
            } else {
                while (cursor.moveToNext()) {
                    try {
                        sqlid.add("" + cursor.getInt(0));
                        sqldate.add(cursor.getString(1));
                        sqltime.add(cursor.getString(2));
                        sqldata.add(cursor.getString(3));
                    } catch (Exception e) {

                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }

            }

            if (id.length() > 0) {
                String truncateData = sqldata.get(position);
                if(truncateData.length() > 38) {
                    truncateData = truncateData.substring(0, 37);
                    truncateData += "...";
                }
                truncateData=truncateData.replace("\n"," ");
                id.setText(sqltime.get(position));
                data.setText(truncateData);
                date.setText(sqldate.get(position));
            }
             checkBox.setVisibility(View.INVISIBLE);

            return convertView;
        }


    }
}