package com.app.windchat.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.app.windchat.R;
import com.app.windchat.Snap;
import com.app.windchat.Utils;
import com.app.windchat.api.model.User;
import com.app.windchat.api.rest.Api;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by banal_a on 07/12/2016.
 */

public class LoginActivity extends AppCompatActivity {

    private Button login;
    private TextView btn_new_acc;
    private EditText edit_email;
    private EditText edit_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = (Button) findViewById(R.id.login);
        edit_email = (EditText) findViewById(R.id.email);
        edit_password = (EditText) findViewById(R.id.password);
        final User user = new User();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login.setEnabled(false);
                user.setEmail(edit_email.getText().toString());
                user.setPassword(edit_password.getText().toString());
                if (valide()) {
                    Call<User> call = new Api().getRestClient().login(user);
                    call.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.isSuccessful()) {
                                Snap.setCurrent(response.body());
                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(i);
                                Toast.makeText(LoginActivity.this, "Received", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginActivity.this, "Error..", Toast.LENGTH_SHORT).show();
                            }
                            login.setEnabled(true);
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {

                        }
                    });
                }
            }
        });

        btn_new_acc = (TextView) findViewById(R.id.btn_new_acc);
        btn_new_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplication(), SignUpActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });
    }

    private boolean valide(){
        boolean valid = true;
        String email = edit_email.getText().toString();
        String password = edit_password.getText().toString();

        if (password.isEmpty() || password.length() < 6){
            valid = false;
            edit_password.setError(Utils.getString(R.string.pass_invalid));
        }

        if (email.isEmpty()){
            valid = false;
            edit_email.setError(Utils.getString(R.string.email_empty));
            return valid;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            valid = false;
            edit_email.setError(Utils.getString(R.string.email_invalid));
            return valid;
        }

        if (!isAvailable(email)){
            valid = false;
            edit_email.setError(Utils.getString(R.string.email_taken));
            return valid;
        }

        return valid;
    }

    private boolean isAvailable(String email){
        return true;
    }
}
