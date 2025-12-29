package com.example.foodnote_n22dcpt103_n22dcpt104;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodnote_n22dcpt103_n22dcpt104.adapter.IngredientDetailAdapter;
import com.example.foodnote_n22dcpt103_n22dcpt104.database.AppDatabase;
import com.example.foodnote_n22dcpt103_n22dcpt104.database.entities.Meal_plan;
import com.example.foodnote_n22dcpt103_n22dcpt104.database.entities.Recipe;
import com.example.foodnote_n22dcpt103_n22dcpt104.database.entities.Recipe_MealPlan;
import com.example.foodnote_n22dcpt103_n22dcpt104.database.models.IngredientsByRecipe;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RecipeDetailActivity extends AppCompatActivity {

    // Biến dữ liệu
    private int recipeId;
    private AppDatabase db;
    private Recipe currentRecipe;
    private IngredientDetailAdapter ingredientAdapter;

    // Khai báo View
    private ImageView imgRecipe, btnBack, btnFav;
    private TextView tvName, tvTime, tvServings, tvDesc;
    private RecyclerView rcvIngredients;
    private LinearLayout layoutSteps;
    private Button btnAddMeal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe_detail);

        // Lấy dữ liệu trong intent gửi qua.
        recipeId = getIntent().getIntExtra("RECIPE_ID", -1);
        if (recipeId == -1) {
            Toast.makeText(this, "Lỗi: Không tìm thấy món ăn!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        db = AppDatabase.getInstance(this);
        initUI();
        loadData();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    private void initUI() {
        imgRecipe = findViewById(R.id.img_recipe_detail);
        btnBack = findViewById(R.id.btn_back_detail);
        btnFav = findViewById(R.id.btn_fav_detail);
        tvName = findViewById(R.id.tv_name_detail);
        tvTime = findViewById(R.id.tv_time_detail);
        tvServings = findViewById(R.id.tv_servings_detail);
        tvDesc = findViewById(R.id.tv_desc_detail);

        rcvIngredients = findViewById(R.id.rcv_ingredients_detail);
        layoutSteps = findViewById(R.id.layout_steps_container);
        btnAddMeal = findViewById(R.id.btn_add_meal_detail);


        ingredientAdapter = new IngredientDetailAdapter();
        rcvIngredients.setLayoutManager(new LinearLayoutManager(this));
        rcvIngredients.setAdapter(ingredientAdapter);
        rcvIngredients.setNestedScrollingEnabled(false);

        btnBack.setOnClickListener(v -> finish());
        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newStatus = (currentRecipe.isFavorite() == 1) ? 0 : 1;
                v.setActivated(newStatus == 1);
                currentRecipe.setFavorite(newStatus);

                if (newStatus == 1) {
                    Toast.makeText(v.getContext(), "Đã thêm vào yêu thích ❤️", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(v.getContext(), "Đã bỏ khỏi yêu thích 💔", Toast.LENGTH_SHORT).show();
                }

                // Cập nhật DB
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        AppDatabase db = AppDatabase.getInstance(v.getContext());
                        db.recipeDAO().updateFavorite(currentRecipe.getId(), newStatus );
                    }
                }).start();
            }
        });

        // Sự kiện: Thêm vào lịch ăn
        btnAddMeal.setOnClickListener(v -> showAddMealDialog());
    }

    private void loadData() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                // Lấy danh sách nguyên liệu dựa trên ID truyền vào
                currentRecipe = db.recipeDAO().getRecipeById(recipeId);
                List<IngredientsByRecipe> listIngredients = db.recipeDAO().getAllIngredientsByRecipe(recipeId);

                // Cập nhật UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (currentRecipe != null) {
                            displayRecipeInfo(currentRecipe);
                            ingredientAdapter.setData(listIngredients);
                            displayInstructions(currentRecipe.getInstruction());
                        }
                    }
                });
            }
        }).start();
    }

    private void displayInstructions(String instruction) {
        layoutSteps.removeAllViews();
        if (instruction == null || instruction.isEmpty()) return;

        String[] steps = instruction.split("\n");
        for (String step : steps) {
            String content = step.trim();
            if (content.isEmpty()) continue;

            // Tạo TextView cho mỗi bước
            TextView tvStep = new TextView(this);
            tvStep.setText(content);
            tvStep.setTextColor(Color.parseColor("#444444"));
            tvStep.setTextSize(15);
            tvStep.setLineSpacing(0, 1.3f); // Giãn dòng

            // Tạo Background: Bo góc 12dp, Màu nền xám nhạt (#F8F8F8)
            GradientDrawable shape = new GradientDrawable();
            shape.setCornerRadius(24); // Tương đương 12dp
            shape.setColor(Color.parseColor("#F8F8F8"));
            tvStep.setBackground(shape);

            // Căn chỉnh: Padding bên trong và Margin giữa các ô
            int padding = (int) (16 * getResources().getDisplayMetrics().density);
            tvStep.setPadding(padding, padding, padding, padding);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
            params.setMargins(0, 0, 0, 24); // Khoảng cách giữa các ô bước làm
            tvStep.setLayoutParams(params);

            // Add vào màn hình
            layoutSteps.addView(tvStep);
        }
    }

    private void displayRecipeInfo(Recipe currentRecipe) {
        tvName.setText(currentRecipe.getName());
        tvTime.setText(currentRecipe.getReadyInMinutes() + " phút");
        tvServings.setText(currentRecipe.getServings() + " người");
        tvDesc.setText(currentRecipe.getDescription());
        btnFav.setActivated(currentRecipe.isFavorite() == 1);

        String imgPath = "file:///android_asset/images/" + currentRecipe.getImg() + ".webp";
        Glide.with(this)
                .load(imgPath)
                .centerCrop()
                .placeholder(R.drawable.ic_loading)
                .error(R.drawable.ic_default_ingredient)
                .into(imgRecipe);
    }

    private void showAddMealDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_recipe_detail_dialog_add_meal_plan);

        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.getAttributes().gravity = Gravity.CENTER;
        }

        TextView tvDate = dialog.findViewById(R.id.tv_date_dialog);
        Spinner spnSession = dialog.findViewById(R.id.spinner_session_dialog);
        Button btnConfirm = dialog.findViewById(R.id.btn_confirm_dialog);
        Button btnCancel = dialog.findViewById(R.id.btn_cancel_dialog);

        // 1. Setup Spinner trước
        String[] sessions = {"Bữa Sáng", "Bữa Trưa", "Bữa Tối"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, sessions);
        spnSession.setAdapter(adapter);

        // 2. Khởi tạo Calendar mặc định là HÔM NAY
        final Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()); // Format chuẩn

        // 3. Kiểm tra Intent (Nếu có thì cập nhật lại Calendar và Spinner)
        Intent intent = getIntent();
        if (intent.hasExtra("TARGET_DATE")) {
            String tDate = intent.getStringExtra("TARGET_DATE");
            int tSession = intent.getIntExtra("TARGET_SESSION", 0);

            try {
                Date dateFromIntent = sdf.parse(tDate);
                if (dateFromIntent != null) {
                    cal.setTime(dateFromIntent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Chọn buổi trên Spinner
            if (tSession > 0) {
                spnSession.setSelection(tSession - 1);
            }
        }

        // 4. Hiển thị ngày lên TextView
        tvDate.setText(sdf.format(cal.getTime()));

        // Sự kiện chọn ngày từ DatePicker
        tvDate.setOnClickListener(v -> {
            new DatePickerDialog(this, (view, y, m, d) -> {
                cal.set(y, m, d);
                tvDate.setText(sdf.format(cal.getTime()));
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
        });

        // 5. Logic xử lý Database
        btnConfirm.setOnClickListener(v -> {
            // Lấy ngày từ biến 'cal' (đã được xử lý chuẩn ở trên)
            String selectedDate = sdf.format(cal.getTime());
            int sessionOrder = spnSession.getSelectedItemPosition() + 1;

            new Thread(() -> {
                try {
                    // Kiểm tra ngày có chưa
                    long mealId = db.mealPlanDao().getIDMealPlanByDate(selectedDate);

                    if (mealId == 0) {
                        Meal_plan newPlan = new Meal_plan(null, "", selectedDate);
                        mealId = db.mealPlanDao().insertMealPlan(newPlan);
                    }

                    Recipe_MealPlan link = new Recipe_MealPlan(sessionOrder, recipeId, (int) mealId);
                    db.mealPlanDao().insertRecipeToMeal(link);

                    runOnUiThread(() -> {
                        Toast.makeText(this, "Đã thêm vào thực đơn ngày " + selectedDate, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    });

                } catch (Exception e) {
                    runOnUiThread(() -> Toast.makeText(this, "Món này đã có trong bữa ăn này rồi!", Toast.LENGTH_LONG).show());
                }
            }).start();
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

}