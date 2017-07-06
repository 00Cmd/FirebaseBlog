package com.example.macbookair.firebaseblog.Activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.macbookair.firebaseblog.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private EditText name,email,pass,passConfirmation;
    private Button btnCreate;
    private ProgressDialog mDialog;
    private FirebaseAuth mAuth;
    private DatabaseReference mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mDb = FirebaseDatabase.getInstance().getReference().child("Users");
        find();
        onClick();
    }

    private void find() {
        name = (EditText) findViewById(R.id.signUpName);
        email = (EditText) findViewById(R.id.signUpEmail);
        pass = (EditText) findViewById(R.id.signUpPass);
//        passConfirmation = (EditText) findViewById(R.id.signUpPassConfirmation);
        btnCreate = (Button) findViewById(R.id.signUpCreate);
    }

    private void onClick() {
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegistration();
            }
        });
    }

    private void startRegistration() {
        final String uName = name.getText().toString().trim();
        String uMail = email.getText().toString().trim();
        String uPass = pass.getText().toString().trim();
//        String uPassConf = passConfirmation.getText().toString().trim();

        if(!TextUtils.isEmpty(uName) && !TextUtils.isEmpty(uMail) && !TextUtils.isEmpty(uPass) ) {

                    mDialog.setMessage("Signing up....");
                    mDialog.show();

                    mAuth.createUserWithEmailAndPassword(uMail, uPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String uId = mAuth.getCurrentUser().getUid();
                                DatabaseReference currentUserDbRef = mDb.child(uId);
                                currentUserDbRef.child("name").setValue(uName);
                                currentUserDbRef.child("image").setValue("default");

                                mDialog.dismiss();
                                Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                            }
                        }
                    });
        }
    }
}
