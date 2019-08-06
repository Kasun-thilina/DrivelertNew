package com.example.safe.drivelert.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.safe.drivelert.MainActivity;
import com.example.safe.drivelert.R;
import com.example.safe.drivelert.Utility.Const;
import com.example.safe.drivelert.Utility.TinyDB;
import com.example.safe.drivelert.Utility.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText et_username, et_password;
    Button btn_login;
    TextView tv_signup;
    TinyDB tinyDbUserId, tinyDbUserToken;


    FirebaseDatabase database;
    DatabaseReference myRef;

    TinyDB tinyDB ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getViews();
        performActions();
    }

    private void getViews() {
        et_username = findViewById(R.id.login_et_username);
        et_password = findViewById(R.id.login_et_password);
        btn_login = findViewById(R.id.login_btn_login);
        tv_signup = findViewById(R.id.login_tv_signup);
    }

    private void performActions() {
        btn_login.setOnClickListener(this);
        tv_signup.setOnClickListener(this);
        tinyDbUserId = new TinyDB(this);
        tinyDbUserToken = new TinyDB(this);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("all-users");
        tinyDB = new TinyDB(getApplicationContext());

        if(tinyDB.getBoolean(Const.IS_SIGNED_IN))
        {
            Intent login = new Intent(LoginActivity.this, MainActivity.class);
            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(login);
        }
    }

    private void signIn(final String userName, final String password) {


        if (Utils.isInternetAvailable(this)) {

            Query queryRef = myRef.orderByChild("userName").startAt(userName);
            queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.hasChildren()) {

                        for (DataSnapshot model : dataSnapshot.getChildren()) {

                            SignupModel user = model.getValue(SignupModel.class);
                            if (user == null) {
                                showMessage(false);
                            } else {
                                if (user.getPassword().equals(password)) {
                                    showMessage(true);
                                    LoginSuccessfull(userName);
                                } else {
                                    showMessage(false);

                                }
                            }
                            return;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }


            });
        } else {
            Toast.makeText(this, getResources().getString(R.string.internet_prob), Toast.LENGTH_SHORT).show();
        }
    }

    private void showMessage(boolean success) {


        if (success)
            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(LoginActivity.this, "Incorrect username or password", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.login_btn_login:


                signIn(et_username.getText().toString(), et_password.getText().toString());
                break;
            case R.id.login_tv_signup:
                Intent signupIntent = new Intent(this, SignupActivity.class);
                startActivity(signupIntent);
                break;

        }

    }

    private void LoginSuccessfull(final String userName) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                tinyDB.putBoolean(Const.IS_SIGNED_IN ,true);
                tinyDB.putString(Const.USER_NAME , userName);

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);




            }
        }, 1000);
    }
}
