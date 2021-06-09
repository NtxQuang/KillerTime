package com.ntxq.btl_ptudpm.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.ntxq.btl_ptudpm.R;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtEmail;
    private EditText edtPass;
    private EditText edtPassConfirm;
    private Button btnSignUp;
    private TextView btnBackToSignIn;

    protected FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        auth= FirebaseAuth.getInstance();
        initView();
    }

    private void initView() {
        edtEmail= findViewById(R.id.edt_register_email);
        edtPass= findViewById(R.id.edt_register_password);
        edtPassConfirm= findViewById(R.id.edt_register_confirmPassword);
        btnSignUp= findViewById(R.id.btn_register_signUp);
        btnBackToSignIn= findViewById(R.id.btn_backToSignIn);
        btnBackToSignIn.setOnClickListener(this);

        btnSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.btn_backToSignIn){
            finish();
            return;
        }
        String email= edtEmail.getText().toString().trim();
        String pass= edtPass.getText().toString();
        String passConfirm= edtPassConfirm.getText().toString();
        if(email.isEmpty()){
            edtEmail.setError("Empty!");
            return;
        }
        if(pass.isEmpty()){
            edtPass.setError("Empty!");
            return;
        }
        if(passConfirm.isEmpty()){
            edtPassConfirm.setError("Empty!");
        }
        if(pass.length()<6){
            edtPass.setError("Password is more than 6 character!");
            return;
        }
        if (!pass.equals(passConfirm)){
            edtPass.setError("Check you password!");
            return;
        }
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "Successfully!", Toast.LENGTH_SHORT).show();
                    Intent intentLogin= new Intent(getApplicationContext(), LoginActivity.class);
                    intentLogin.putExtra("email",  email);
                    intentLogin.putExtra("pass", pass);
                    setResult(RESULT_OK, intentLogin);
                    finish();
                } else{
                    Toast.makeText(RegisterActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}