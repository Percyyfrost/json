package com.atc.vuyaninxele.sight3;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{

    private EditText Email;
    private EditText Password;
    private Button LoginBtn;
    private FirebaseAuth Auth;
    private FirebaseAuth .AuthStateListener AuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Auth = FirebaseAuth.getInstance();
        AuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null)
                {
                    startActivity(new Intent(LoginActivity.this, SplashActivity.class));
                    finish();
                }
            }
        };

        Email = (EditText) findViewById(R.id.email);
        Password = (EditText) findViewById(R.id.password);
        LoginBtn = (Button) findViewById(R.id.loginbtn);

        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSignIn();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        Auth.addAuthStateListener(AuthListener);
    }

    @Override
    protected void onStop(){
        super.onStop();

        Auth.removeAuthStateListener(AuthListener);
    }

    private void startSignIn() {
        String email = Email.getText().toString();
        String password = Password.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password))
        {
            //Toast.makeText(LoginActivity.this, "Email or Password Fields Cannot Be Empty", Toast.LENGTH_LONG).show();
        }
        else
        {
            Auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful())
                    {
                       // Toast.makeText(LoginActivity.this, "Invalid Email or Password", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}

