package com.clipboard.log;

import android.app.Service;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class cp_monitor extends Service {
    public String date;
    public String text;
    public String time;
    private DatabaseHelper myDb;

    public cp_monitor() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        myDb = new DatabaseHelper(this);

        final ClipboardManager clipboard = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);

        assert clipboard != null;
        clipboard.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            public void onPrimaryClipChanged() {
                //ClipData clip = clipboard.getPrimaryClip();
                boolean result;
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                SimpleDateFormat sdt = new SimpleDateFormat("HH:mm", Locale.US);

                Date now = new Date();
                time = sdt.format(now);
                date = sdf.format(now);

                text = clipboard.getText().toString();
                if (text.trim().length() > 0) {
                    result = myDb.insertData(date, time, text);
                }

                //if(result)
                //  Toast.makeText(getBaseContext(),date+" "+" "+text,Toast.LENGTH_LONG).show();

            }
        });

        //Toast.makeText(getBaseContext(),"Hello From service",Toast.LENGTH_LONG).show();
    }
}
