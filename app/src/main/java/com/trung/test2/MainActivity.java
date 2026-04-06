package com.trung.test2;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth; // Thêm thư viện Firebase Auth
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase; // Thêm thư viện Firebase Realtime Database



// ... các phần import giữ nguyên ...

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // Lấy thêm 2 ô nhập liệu mới từ giao diện
        EditText e = findViewById(R.id.e), p = findViewById(R.id.p);
        EditText editName = findViewById(R.id.editName); // Ô nhập Tên
        EditText editAge = findViewById(R.id.editAge);   // Ô nhập Tuổi

        Button btnReg = findViewById(R.id.btnReg), btnLogin = findViewById(R.id.btnLogin);

        // --- PHẦN ĐĂNG KÝ (Lấy dữ liệu người dùng tự nhập) ---
        btnReg.setOnClickListener(v -> {
            String email = e.getText().toString();
            String password = p.getText().toString();
            String name = editName.getText().toString(); // Lấy tên người dùng vừa nhập
            String age = editAge.getText().toString();   // Lấy tuổi người dùng vừa nhập

            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String uid = auth.getCurrentUser().getUid();
                            DatabaseReference userRef = database.getReference("users").child(uid);

                            // LƯU DỮ LIỆU TỪ NGƯỜI DÙNG NHẬP LÊN DATABASE
                            userRef.child("email").setValue(email);
                            userRef.child("name").setValue(name); // Không còn ghi đè tên Trung nữa
                            userRef.child("age").setValue(age);   // Lưu tuổi thực tế người dùng nhập

                            Toast.makeText(this, "Đăng ký & Lưu thông tin xong!", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // --- PHẦN ĐĂNG NHẬP (Giữ nguyên logic lấy dữ liệu như mình đã hướng dẫn ở câu trước) ---
        btnLogin.setOnClickListener(v -> {
            auth.signInWithEmailAndPassword(e.getText().toString(), p.getText().toString())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String uid = auth.getCurrentUser().getUid();
                            database.getReference("users").child(uid).get()
                                    .addOnCompleteListener(dbTask -> {
                                        if (dbTask.isSuccessful()) {
                                            // Lấy dữ liệu đã lưu từ server về
                                            String nameFromDB = String.valueOf(dbTask.getResult().child("name").getValue());
                                            String ageFromDB = String.valueOf(dbTask.getResult().child("age").getValue());

                                            // Hiển thị lời chào cá nhân hóa
                                            Toast.makeText(this, "Chào " + nameFromDB + ", " + ageFromDB + " tuổi!", Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                    });
        });
    }
}