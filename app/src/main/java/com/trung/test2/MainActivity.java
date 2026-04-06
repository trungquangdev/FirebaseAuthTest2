package com.trung.test2;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth; // Thêm thư viện Firebase Auth
import com.google.firebase.database.FirebaseDatabase; // Thêm thư viện Firebase Realtime Database



public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Khởi tạo Firebase Auth
        FirebaseAuth auth = FirebaseAuth.getInstance();
        EditText e = findViewById(R.id.e), p = findViewById(R.id.p);
        Button btnReg = findViewById(R.id.btnReg), btnLogin = findViewById(R.id.btnLogin);

//        //Đăng ký
//        btnReg.setOnClickListener(v -> {
//            auth.createUserWithEmailAndPassword(e.getText().toString(), p.getText().toString())
//                    .addOnCompleteListener(task -> {
//                        if (task.isSuccessful()) Toast.makeText(this, "Đăng ký Thành Công!", Toast.LENGTH_SHORT).show();
//                        else Toast.makeText(this, "Lỗi: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                    });
//        });

        // ĐĂNG KÝ (Có lưu thêm vào Database)
        btnReg.setOnClickListener(v -> {
            String email = e.getText().toString();
            String password = p.getText().toString();

            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // 1. Lấy ID của thằng vừa đăng ký xong
                            String uid = auth.getCurrentUser().getUid();

                            // 2. Đẩy cái Email này sang Realtime Database để lưu trữ lâu dài
                            FirebaseDatabase.getInstance().getReference("users")
                                    .child(uid) // Tạo thư mục riêng cho nó theo UID
                                    .child("email").setValue(email);

                            Toast.makeText(this, "Đăng ký & Lưu DB xong!", 0).show();
                        }
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