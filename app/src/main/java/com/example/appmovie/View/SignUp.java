package com.example.appmovie.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.appmovie.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class SignUp extends AppCompatActivity {

    EditText edt_email, edt_password, edt_confirm_password;
    Button btn_sign_up;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        addControl();
        addEvent();
    }
    void addControl(){
        edt_email = findViewById(R.id.edt_email_up);
        edt_password = findViewById(R.id.edt_password_up);
        edt_confirm_password = findViewById(R.id.edt_confirm_password_up);
        btn_sign_up = findViewById(R.id.btn_sign_up);
        progressBar = findViewById(R.id.progressBar_up);
    }
    void addEvent(){
        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onClickSignUp();
            }
        });
    }
    void onClickSignUp(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String email = edt_email.getText().toString().trim();
        String password = edt_password.getText().toString().trim();
        String confirmPassword = edt_confirm_password.getText().toString().trim();

        if (!isNotEmpty(email) || !isNotEmpty(password) || !isNotEmpty(confirmPassword)) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ các ô còn thiếu.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidEmail(email)) {
            Toast.makeText(this, "Email không hợp lệ.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isPasswordValid(password)) {
            Toast.makeText(this, "Mật khẩu phải có ít nhất 6 ký tự.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isPasswordMatch(password, confirmPassword)) {
            Toast.makeText(this, "Mật khẩu xác nhận không khớp.", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(SignUp.this, SignIn.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(SignUp.this, "Email đã tồn tại.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SignUp.this, "Đăng ký thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isNotEmpty(String input) {
        return input != null && !input.trim().isEmpty();
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }

    private boolean isPasswordMatch(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

}