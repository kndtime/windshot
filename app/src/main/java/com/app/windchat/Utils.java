package com.app.windchat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.text.format.DateUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;

import com.app.windchat.ui.activity.MainActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by banal_a on 07/12/2016.
 */

public class Utils {

    public static String dateFormat = "yyyy-MM-dd'T'HH:mm:ss";


    public static void startMainIntent(){
        Intent i = new Intent(Snap.getInstance(), MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Snap.getInstance().startActivity(i);
    }

    public static Drawable getDrawable(int resource){
        return Snap.getInstance().getResources().getDrawable(resource);
    }

    public static String getString(int resource){
        return Snap.getInstance().getResources().getString(resource);
    }

    public static File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        //mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    public static String imgTo64(String path){
        Bitmap bm = BitmapFactory.decodeFile(path);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static String imgTo64(Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate){

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {
        }

        return outputDate;

    }

    public static Bitmap combineImages(Bitmap background, Bitmap foreground, WindowManager window) {

        int width = 0, height = 0;
        Bitmap cs;

        width = window.getDefaultDisplay().getWidth();
        height = window.getDefaultDisplay().getHeight();

        cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas comboImage = new Canvas(cs);
        background = Bitmap.createScaledBitmap(background, width, height, true);
        comboImage.drawBitmap(background, 0, 0, null);
        comboImage.drawBitmap(foreground, new Matrix(), null);
        return cs;
    }

    public static String imgTo64(byte[] b){
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static Bitmap combineImages(Bitmap background, Bitmap foreground, Activity activity, int arr[]) {

        int width = 0, height = 0;
        Bitmap cs;

        width = activity.getWindowManager().getDefaultDisplay().getWidth();
        height = activity.getWindowManager().getDefaultDisplay().getHeight();

        cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas comboImage = new Canvas(cs);
        background = Bitmap.createScaledBitmap(background, width, height, true);
        comboImage.drawBitmap(background, 0, 0, null);
        comboImage.drawBitmap(foreground, arr[0], arr[1], null);

        return cs;
    }

    public static File savebitmap(String filename, Bitmap bitmap) {
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        OutputStream outStream = null;

        File file = new File(filename + ".png");
        if (file.exists()) {
            file.delete();
            file = new File(extStorageDirectory, filename + ".png");
            Log.e("file exist", "" + file + ",Bitmap= " + filename);
        }
        try {
            // make a new bitmap from your file
            bitmap = BitmapFactory.decodeFile(file.getName());

            outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("file", "" + file);
        return file;

    }

    public static void animateError(Context context, View view){
        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake_error));
    }

    public static CharSequence getTimeSpan(String time) {
        CharSequence tmp = "";
        if (time!=""){
            try {
                DateFormat df2 = new SimpleDateFormat(dateFormat);
                Date date = df2.parse(time);
                Calendar c = Calendar.getInstance();
                long now = c.getTimeInMillis();
                long mills = date.getTime();
                CharSequence elapsedtime = DateUtils.getRelativeTimeSpanString(mills, now, 0L, DateUtils.FORMAT_ABBREV_ALL);
                return elapsedtime;
            } catch (ParseException e) {
                e.printStackTrace();
            }}
        return tmp;
    }

    public static String getNowToString(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(calendar.getTimeInMillis());
        return simpleDateFormat.format(calendar.getTime());
    }
}
