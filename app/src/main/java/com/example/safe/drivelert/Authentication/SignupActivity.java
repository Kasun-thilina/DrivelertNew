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

import com.example.safe.drivelert.R;
import com.example.safe.drivelert.Utility.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    EditText et_userName, et_email, et_password, et_confirmPassword;
    Button btn_signUp;
    TextView tv_signIn;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private String userNamePattern = "[a-zA-Z0-9]*";

    FirebaseDatabase database;
    DatabaseReference myRef;

    boolean isUniqueEntry;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getViews();
        performActions();
    }

    private void getViews() {
        et_userName = findViewById(R.id.signup_et_username);
        et_email = findViewById(R.id.signup_et_email);
        et_password = findViewById(R.id.signup_et_password);
        et_confirmPassword = findViewById(R.id.signup_et_confirm_password);
        btn_signUp = findViewById(R.id.signup_btn_signup);
        tv_signIn = findViewById(R.id.signup_tv_signin);
    }

    private void performActions() {
        btn_signUp.setOnClickListener(this);
        tv_signIn.setOnClickListener(this);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("all-users");
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.signup_btn_signup:


                if (et_userName.getText().toString().length() < 4 || et_userName.getText().toString().length() > 16) {
                    et_userName.setError(getResources().getString(R.string.username_size));
                }

                if (!et_userName.getText().toString().matches(userNamePattern)) {
                    et_userName.setError(getResources().getString(R.string.contains_special_char));
                }

                if (!et_email.getText().toString().matches(emailPattern)) {
                    et_email.setError(getResources().getString(R.string.invalid_email));
                }
                if (!et_password.getText().toString().equals(et_confirmPassword.getText().toString())) {
                    et_confirmPassword.setError(getResources().getString(R.string.password_not_match));
                }
                if (et_password.getText().toString().length() < 4) {
                    et_password.setError(getResources().getString(R.string.password_size));

                }
                if (et_confirmPassword.getText().toString().length() < 4) {
                    et_confirmPassword.setError(getResources().getString(R.string.password_size));
                }

                if ((et_userName.getText().toString().length() > 3 && et_userName.getText().toString().length() < 17) && et_userName.getText().toString().matches(userNamePattern)
                        && et_email.getText().toString().matches(emailPattern) && et_password.getText().toString().equals(et_confirmPassword.getText().toString()) && (et_password.getText().toString().length() > 3 && et_confirmPassword.getText().toString().length() > 3)) {
                    // Toast.makeText(this, "signup", Toast.LENGTH_SHORT).show();
                    signUp(et_userName.getText().toString(), et_password.getText().toString(), et_email.getText().toString());
                }

                break;
            case R.id.signup_tv_signin:
                Intent signinIntent = new Intent(this, LoginActivity.class);
                startActivity(signinIntent);
                break;
        }
    }

    private void signUp(final String userName, final String password, final String email) {

        checkUser(userName , password , email);

    }

    private void checkUser(final String userName, final String password, final String email)
    {
        if (Utils.isInternetAvailable(this)) {

            Query queryRef = myRef.orderByChild("userName").startAt(userName);
            queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.hasChildren()) {

                        for (DataSnapshot model : dataSnapshot.getChildren()) {

                            SignupModel user = model.getValue(SignupModel.class);

                            if (user == null) {

                            }
                            if (user.getUserName().equals(userName)) {
                                Toast.makeText(SignupActivity.this, "user name already taken", Toast.LENGTH_SHORT).show();
                                isUniqueEntry = false;
                                return;
                            }
                            else
                            {
                                isEmailExist(userName , password , email);
                            }

                            break;

                        }
                    }
                    else
                    {
                        isEmailExist(userName , password , email);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }


            });

        }
        else {
            Toast.makeText(SignupActivity.this, getResources().getString(R.string.internet_prob), Toast.LENGTH_SHORT).show();

        }


    }

    private void isEmailExist(final String userName, final String password, final String email)
    {
        if (Utils.isInternetAvailable(this)) {

            Query queryRef = myRef.orderByChild("email").startAt(email);
            queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.hasChildren()) {

                        for (DataSnapshot model : dataSnapshot.getChildren()) {

                            SignupModel user = model.getValue(SignupModel.class);

                            if (user == null) {

                            }
                            if (user.getEmail().equals(email)) {
                                Toast.makeText(SignupActivity.this, "email already used", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            else
                            {
                                performSignup(userName , email , password);
                            }

                            break;
                        }

                    }
                    else
                    {
                        performSignup(userName , email , password);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }


            });





        }
        else {
            Toast.makeText(SignupActivity.this, getResources().getString(R.string.internet_prob), Toast.LENGTH_SHORT).show();

        }


    }

    private void performSignup(String userName , String email , String password)
    {

        myRef = database.getReference("all-users");

        SignupModel model1 = new SignupModel(userName, email, password);

        DatabaseReference childRef = database.getReference("all-users").child(userName);
        childRef.setValue(model1);

        childRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                SignupSuccessfull();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void SignupSuccessfull() {
        Toast.makeText(SignupActivity.this, "Sign Up Successfull", Toast.LENGTH_SHORT).show();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        }, 2000);
    }

}
