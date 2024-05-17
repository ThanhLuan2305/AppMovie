package com.example.appmovie.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.appmovie.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class ForgotPass extends AppCompatActivity {

    EditText edt_email_forgot;
    Button btn_send;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
        addControl();
        addEvent();
    }
    void addControl() {
        edt_email_forgot = findViewById(R.id.edt_email_forgot);
        progressBar = findViewById(R.id.progressBar_forgot);
        btn_send = findViewById(R.id.btn_send);
    }
    void addEvent(){
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickForgotPassword();
            }
        });

    }
    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isNotEmpty(String input) {
        return input != null && !input.trim().isEmpty();
    }
    void onClickForgotPassword() {
        String email = edt_email_forgot.getText().toString().trim();
        if (!isValidEmail(email)) {
            Toast.makeText(this, "Vui lòng nhập một email hợp lệ để đặt lại mật khẩu.", Toast.LENGTH_LONG).show();
            return;
        }

        FirebaseAuth auth = FirebaseAuth.getInstance();
        progressBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email)
                .addOnSuccessListener(aVoid -> {
                    progressBar.setVisibility(View.GONE);
                    Intent intent = new Intent(ForgotPass.this, SignIn.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Đã gửi email đặt lại mật khẩu. Vui lòng kiểm tra hộp thư của bạn.", Toast.LENGTH_LONG).show();
                }).addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    if (e instanceof FirebaseAuthInvalidUserException) {
                        Toast.makeText(ForgotPass.this, "Không có người dùng nào với email này trong hệ thống.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ForgotPass.this, "Lỗi khi gửi email đặt lại mật khẩu: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}