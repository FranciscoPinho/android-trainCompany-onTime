package com.example.holykael.ontime_travellers;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ServiceCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.UUID;

import static android.content.ContentValues.TAG;

/**
 * Created by Holykael on 4/20/2017.
 */

public class QRCodeGenerator extends Service{
    // Binder given to clients
    private final IBinder mBinder = new QRCodeBinder();
    private int currentTicket = 0;

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class QRCodeBinder extends Binder {
        QRCodeGenerator getService() {
            // Return this instance of QRCodeGenerator so clients can call public methods
            return QRCodeGenerator.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        JodaTimeAndroid.init(this);
        loadSharedPreferences();
        return mBinder;
    }

    private void loadSharedPreferences() {
        // This will get you an instance of your applications shared preferences.
        SharedPreferences preferences = this.getSharedPreferences("Data",Context.MODE_PRIVATE);
        int uuid = preferences.getInt("currentTicket",-1);
        SharedPreferences.Editor editor = preferences.edit();
        if(uuid==-1) {
            editor.putInt("currentTicket",0);
            editor.apply();
        }
        else currentTicket=uuid;
        currentTicket++;
    }

    public void generateQRCode(final String payload){
        Thread t = new Thread(new Runnable() {  // do the creation in a new thread to avoid ANR Exception
            public void run() {
                final Bitmap bitmap;
                try {
                    bitmap = encodeAsBitmap(payload);
                    storeImage(bitmap,payload);
                }
                catch (WriterException e) {
                    Log.d("QRCode",e.getMessage());
                }
            }
        });
        t.start();
    }

    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, 500, 500, null);
        }
        catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? Color.parseColor("#001f3f"):Color.WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
        return bitmap;
    }
    private void updateSharedPreferences(String payload){
        SharedPreferences preferences = this.getSharedPreferences("Data",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String processed = process(payload);
        editor.putInt("currentTicket",currentTicket);
        editor.putString("Ticket"+currentTicket,processed);
        editor.apply();
    }
    private String process(String payload){
        StringTokenizer tokenizer = new StringTokenizer(payload,";");
        String finals="";
        Log.d("D",""+tokenizer.countTokens());
        tokenizer.nextToken();
        tokenizer.nextToken();
        finals+=tokenizer.nextToken()+" ";
        finals+=tokenizer.nextToken()+" ";
        finals+=tokenizer.nextToken()+" ";
        finals+=tokenizer.nextToken()+" ";
        finals+=tokenizer.nextToken()+" ";
        finals+=tokenizer.nextToken()+" ";
        finals+=tokenizer.nextToken();
        return finals;
    }
    private void storeImage(Bitmap image,String payload) {
        File dir = mkFolder();
        if(dir==null)
            return;
        try {
            FileOutputStream fos = new FileOutputStream(new File(dir.getPath()+ "/" + currentTicket + "-" + LocalDate.now().toString() + ".png"));
            Boolean result =image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
            updateSharedPreferences(payload);
            currentTicket++;
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
        catch (Exception e) {
            Log.d(TAG, "Unknown Exception: " + e.getMessage());
        }

    }


    public File mkFolder(){
        Log.d("myAppName","folder exist:"+getCacheDir());
        File folder = new File(getCacheDir(),"Tickets");
        if (folder.exists()) {
            Log.d("myAppName","folder exist:"+folder.toString());
            return folder;
        }else{
            try {
                if (folder.mkdirs()) {
                    return folder;
                } else {
                    return null;
                }
            }catch (Exception ecp){
                ecp.printStackTrace();
            }
        }
        return null;
    }



    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }



}


