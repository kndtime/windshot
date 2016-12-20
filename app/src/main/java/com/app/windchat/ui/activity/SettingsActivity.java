package com.app.windchat.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.app.windchat.R;
import com.app.windchat.Snap;
import com.app.windchat.Utils;
import com.app.windchat.api.model.User;
import com.app.windchat.api.rest.Api;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.google.gson.JsonObject;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView user_name, user_title, bar_text;
    private CircularImageView bar_img;
    private ImageView bar_mess;
    private TextView bar_edit;

    private FloatingActionButton fab;

    private EditText dial_firstname, dial_lastname;

    private LinearLayout img_container;
    private CircularImageView img;
    private TextView email;

    private LinearLayout name_container;
    private TextView name;

    private LinearLayout birth_container;
    private TextView birthday;

    private LinearLayout logout;


    private User current;

    private final int RESULT_LOAD_IMAGE = 14;
    private final int REQUEST_TAKE_PHOTO = 24;
    private static File file;

    SimpleDateFormat simpleDateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        current = Snap.getCurrent();
        simpleDateFormat = new SimpleDateFormat("EEE d MMM", Locale.getDefault());
        initViews();
        initProfile(current);
    }

    public void initViews(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        user_name = (TextView) findViewById(R.id.user_name);
        user_title = (TextView) findViewById(R.id.user_title);
        bar_text = (TextView) findViewById(R.id.bar_text);
        bar_img = (CircularImageView) findViewById(R.id.bar_img);
        bar_mess = (ImageView) findViewById(R.id.bar_mess);
        bar_edit = (TextView) findViewById(R.id.bar_edit);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        logout = (LinearLayout) findViewById(R.id.logout);

        img_container = (LinearLayout) findViewById(R.id.img_container);
        img = (CircularImageView) findViewById(R.id.img);
        email = (TextView) findViewById(R.id.email);

        name_container = (LinearLayout) findViewById(R.id.name_container);
        name = (TextView) findViewById(R.id.name);

        birth_container = (LinearLayout) findViewById(R.id.birth_container);
        birthday = (TextView) findViewById(R.id.birthday);

        initClick(this);
    }

    public void initClick(final Context context){
        img_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(context)
                        .title(R.string.dialog_picture_profile)
                        .items(R.array.img_change)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                switch (which) {
                                    case 0:
                                        Intent i = new Intent(
                                                Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                        startActivityForResult(i, RESULT_LOAD_IMAGE);
                                        break;
                                    case 1:
                                        startCamera();
                                    default:
                                        break;
                                }
                            }
                        })
                        .show();
            }
        });

        name_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean wrapInScrollView = true;
                MaterialDialog dialog = new MaterialDialog.Builder(context)
                        .title(R.string.edit_name)
                        .customView(R.layout.dialog_edit_name_layout, wrapInScrollView)
                        .positiveText(R.string.save_profile)
                        .negativeText(R.string.cancel)
                        .positiveColorRes(R.color.w_red)
                        .negativeColorRes(R.color.red)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                current.setLastname(dial_lastname.getText().toString());
                                current.setFirstname(dial_firstname.getText().toString());
                                user_name.setText(current.getCompleteName());
                                name.setText(current.getCompleteName());
                                dialog.hide();
                            }
                        })
                        .build();
                dial_firstname = (EditText) dialog.findViewById(R.id.name);
                dial_lastname = (EditText) dialog.findViewById(R.id.lname);
                dial_lastname.setText(current.getLastname());
                dial_firstname.setText(current.getFirstname());
                initProfile(current);
                dialog.show();
            }
        });

        birth_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SingleDateAndTimePickerDialog.Builder(context)
                        .bottomSheet()
                        .curved()
                        .title("Your birthday")
                        .listener(new SingleDateAndTimePickerDialog.Listener() {
                            @Override
                            public void onDateSelected(Date date) {
                                birthday.setText(simpleDateFormat.format(date));
                            }
                        })
                        .display();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
    }

    private void startCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        }
    }

    public void initProfile(User user){
        user_name.setText(user.getCompleteName());
        user_title.setText(user.getUsername());
        email.setText(user.getEmail());
        bar_text.setText(user.getCompleteName());
        name.setText(user.getCompleteName());
        String date_after =
                Utils.formateDateFromstring(Utils.dateFormat, "dd, MMM yyyy", user.getBirthday());
        birthday.setText(date_after);
        if (!user.getPictureUrl().isEmpty())
            Picasso.with(this).load(user.getPictureUrl()).fit().centerCrop().into(bar_img);
        if (!user.getPictureUrl().isEmpty())
            Picasso.with(this).load(user.getPictureUrl()).fit().centerCrop().into(img);
    }

    public void update(){
        Toast.makeText(this, "Updating...", Toast.LENGTH_SHORT).show();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("email", current.getEmail());
        jsonObject.addProperty("birthday", current.getBirthday());
        jsonObject.addProperty("password", current.getPassword());
        jsonObject.addProperty("userName", current.getUsername());
        jsonObject.addProperty("firstName", current.getFirstname());
        jsonObject.addProperty("lastName", current.getLastname());
        jsonObject.addProperty("imageStr64", current.getImageStr64());

        Call<User> call = new Api().getRestClient().update(jsonObject);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()){
                    Snap.setCurrent(response.body());
                    Toast.makeText(getBaseContext(), "Updated", Toast.LENGTH_SHORT).show();
                    Utils.startMainIntent();
                }else{
                    Toast.makeText(SettingsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

    }

    public void logout(){
        Snap.setCurrent(new User());
        Intent i = new Intent(this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_basic, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == -1 && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);

            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            file = new File(picturePath);
            Picasso.with(this).load(selectedImage).fit().centerCrop().into(bar_img);
            Picasso.with(this).load(selectedImage).fit().centerCrop().into(img);
            current.setImageStr64(Utils.imgTo64(picturePath));
        }

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == -1 && null != data) {
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            try {
                file = Utils.createImageFile();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                assert bitmap != null;
                bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                byte[] bitmapdata = bos.toByteArray();

                FileOutputStream fos = new FileOutputStream(file);
                fos.write(bitmapdata);
                fos.flush();
                fos.close();
                Picasso.with(this).load(file).fit().centerCrop().into(bar_img);
                Picasso.with(this).load(file).fit().centerCrop().into(img);
                current.setImageStr64(Utils.imgTo64(bitmapdata));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
