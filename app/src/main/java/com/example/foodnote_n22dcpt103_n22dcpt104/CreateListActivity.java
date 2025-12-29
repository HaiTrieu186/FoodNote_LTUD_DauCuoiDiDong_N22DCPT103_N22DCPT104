package com.example.foodnote_n22dcpt103_n22dcpt104;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.foodnote_n22dcpt103_n22dcpt104.database.AppDatabase;
import com.example.foodnote_n22dcpt103_n22dcpt104.database.entities.Meal_plan;
import com.example.foodnote_n22dcpt103_n22dcpt104.database.entities.Shopping_list;

import java.util.ArrayList;
import java.util.List;

public class CreateListActivity extends AppCompatActivity {

    private EditText edtName;
    private LinearLayout container; // Nơi chứa checkbox
    private AppDatabase db;
    private List<CheckBox> checkBoxes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_list);
        
        initUI();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initUI() {
        db = AppDatabase.getInstance(this);

        edtName = findViewById(R.id.edt_create_list_name);
        container = findViewById(R.id.layout_checkbox_container);
        Button btnSave = findViewById(R.id.btn_save_new_list);
        ImageView btnBack = findViewById(R.id.btn_back_create);

        btnBack.setOnClickListener(v -> finish());
        btnSave.setOnClickListener(v -> handleSave());

        // Tự động load danh sách ngày ăn chưa mua sắm
        loadAvailablePlans();
    }

    private void handleSave() {
        String name = edtName.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên danh sách!", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Integer> selectedIds = new ArrayList<>();
        for (CheckBox cb : checkBoxes) {
            if (cb.isChecked()) {
                Meal_plan p = (Meal_plan) cb.getTag();
                selectedIds.add(p.getId());
            }
        }

        if (selectedIds.isEmpty()) {
            Toast.makeText(this, "Chọn ít nhất 1 ngày để tạo danh sách!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lưu vào DB
        saveToDb(name, selectedIds);
    }

    private void saveToDb(String name, List<Integer> selectedIds) {
        new Thread(() -> {
            // 1. Tạo  List
            Shopping_list newList = new Shopping_list(name, false, System.currentTimeMillis());
            long listId = db.shoppingListDao().insertShoppingList(newList);

            // 2. Thêm các meal vào List
            db.mealPlanDao().addMealsToShoppingList((int) listId, selectedIds);

            // 3. Tạo chi tiết món ăn (Items)
            db.shoppingListDao().insertShoppingListItems((int) listId);

            runOnUiThread(() -> {
                Toast.makeText(this, "Tạo thành công!", Toast.LENGTH_SHORT).show();
                finish(); // Đóng Activity, quay về Fragment
            });
        }).start();
    }



    private void loadAvailablePlans() {
        // Nhận dữ liệu ngày được gửi từ MealFragment (nếu có)
        String targetDate = getIntent().getStringExtra("PRE_SELECTED_DATE");

        new Thread(() -> {
            List<Meal_plan> plans = db.shoppingListDao().getAvailableMealPlans();

            runOnUiThread(() -> {
                if (plans.isEmpty()) {
                    Toast.makeText(this, "Không có ngày ăn nào mới để tạo list!", Toast.LENGTH_SHORT).show();
                }

                // Tạo checkbox động
                for (Meal_plan p : plans) {
                    CheckBox cb = new CheckBox(this);
                    cb.setText("Ngày " + p.getPlanDate());
                    cb.setTag(p);
                    cb.setTextColor(Color.parseColor("#333333"));
                    cb.setTextSize(16);
                    cb.setPadding(0, 16, 0, 16);
                    cb.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#E23E3E")));

                    // Sự kiện khi check / uncheck
                    cb.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        if (isChecked && edtName.getText().toString().isEmpty()) {
                            edtName.setText("Mua cho ngày " + p.getPlanDate());
                        }
                    });

                    if (targetDate != null && targetDate.equals(p.getPlanDate())) {
                        cb.setChecked(true); // Đánh dấu chọn

                        // Tự động điền tên danh sách luôn cho tiện
                        if (edtName.getText().toString().isEmpty()) {
                            edtName.setText("Mua cho ngày " + p.getPlanDate());
                        }
                    }


                    container.addView(cb);
                    checkBoxes.add(cb);
                }
            });
        }).start();
    }
}