package com.trung.test2;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        EditText e = findViewById(R.id.e), p = findViewById(R.id.p);
        Button btnReg = findViewById(R.id.btnReg), btnLogin = findViewById(R.id.btnLogin);

        //Đăng ký
        btnReg.setOnClickListener(v -> {
            auth.createUserWithEmailAndPassword(e.getText().toString(), p.getText().toString())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) Toast.makeText(this, "Đăng ký Thành Công!", Toast.LENGTH_SHORT).show();
                        else Toast.makeText(this, "Lỗi: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        //Đăng nhập
        btnLogin.setOnClickListener(v -> {
            auth.signInWithEmailAndPassword(e.getText().toString(), p.getText().toString())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) Toast.makeText(this, "NGON LUÔN!", Toast.LENGTH_SHORT).show();
                        else Toast.makeText(this, "Sai tài khoản/mật khẩu!", Toast.LENGTH_SHORT).show();
                    });
        });
    }
}