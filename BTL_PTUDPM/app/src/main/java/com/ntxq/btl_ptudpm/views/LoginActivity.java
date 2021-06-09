package com.ntxq.btl_ptudpm.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.ntxq.btl_ptudpm.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REGISTER_REQUEST_CODE = 1;
    protected FirebaseAuth auth;

    private EditText edtEmail;
    private EditText edtPassword;
    private Button btnSignIn;
    private TextView btnForgotPass;
    private TextView btnSignUp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth= FirebaseAuth.getInstance();
        initView();
    }

    private void initView() {
        edtEmail= findViewById(R.id.edt_login_email);
        edtPassword= findViewById(R.id.edt_number);
        btnSignIn= findViewById(R.id.btn_login_signIn);
        btnForgotPass= findViewById(R.id.btn_login_forgotPassword);
        btnSignUp= findViewById(R.id.btn_login_signUp);

        btnSignIn.setOnClickListener(this);
        btnForgotPass.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login_forgotPassword:
                Toast.makeText(this, "Under construction...\nJust register new account! ", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_login_signUp:
                Intent intentRegister= new Intent(this, RegisterActivity.class);
                startActivityForResult(intentRegister, REGISTER_REQUEST_CODE);
                break;
            case R.id.btn_login_signIn:
                String email= edtEmail.getText().toString().trim();
                String password= edtPassword.getText().toString();
                if(email.isEmpty() || password.isEmpty()){
                    Toast.makeText(this, "Email or Password is empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Intent intentMain= new Intent(getApplicationContext(), MainActivity.class);
                            String[] arr= email.split("@");
                            intentMain.putExtra("headerEmail", arr[0]);
                            startActivity(intentMain);
                            finish();
                        } else{
                            Toast.makeText(LoginActivity.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("TAG", "onFailure: " + e.toString() );
                    }
                });
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REGISTER_REQUEST_CODE){
            if (resultCode==RESULT_OK){
                edtEmail.setText(data.getStringExtra("email"));
                edtPassword.setText(data.getStringExtra("pass"));
            }
        }
    }
}
