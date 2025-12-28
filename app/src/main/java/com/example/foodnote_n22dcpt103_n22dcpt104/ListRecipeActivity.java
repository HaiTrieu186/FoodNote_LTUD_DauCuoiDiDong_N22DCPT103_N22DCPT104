package com.example.foodnote_n22dcpt103_n22dcpt104;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodnote_n22dcpt103_n22dcpt104.R;
import com.example.foodnote_n22dcpt103_n22dcpt104.adapter.ListRecipeAdapter;
import com.example.foodnote_n22dcpt103_n22dcpt104.database.AppDatabase;
import com.example.foodnote_n22dcpt103_n22dcpt104.database.entities.Recipe;

import java.util.ArrayList;
import java.util.List;

public class ListRecipeActivity extends AppCompatActivity {

    // Khai báo View
    private ImageView btnBack;
    private SearchView searchView;
    private Spinner spinnerCategory, spinnerCuisine;
    private RecyclerView rcvRecipe;
    private TextView tvResultCount;

    // Khai báo Adapter và Database
    private ListRecipeAdapter adapter;
    private AppDatabase db;

    // Các biến lưu trạng thái lọc hiện tại
    private String currentKeyword = "";
    private int currentCategory = 0;       // 0 = Tất cả
    private String currentCuisine = "All"; // "All" = Tất cả

    // Key để nhận Intent từ Home
    public static final String KEY_TYPE = "TYPE";
    public static final String KEY_DATA = "DATA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_recipe);

        initUI();
        setupSpinners();

        db = AppDatabase.getInstance(this);
        handleIntentData();
    }

    private void initUI() {
        btnBack = findViewById(R.id.btn_back_list_recipe);
        searchView = findViewById(R.id.sv_recipe);
        spinnerCategory = findViewById(R.id.spinner_category);
        spinnerCuisine = findViewById(R.id.spinner_cuisine);
        rcvRecipe = findViewById(R.id.rcv_list_recipe);
        tvResultCount = findViewById(R.id.tv_result_count);

        // Cài đặt RecyclerView
        adapter = new ListRecipeAdapter();
        rcvRecipe.setLayoutManager(new LinearLayoutManager(this));
        rcvRecipe.setAdapter(adapter);

        // Sự kiện nút Quay lại
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Sự kiện SearchView (Tìm kiếm real-time)
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                currentKeyword = newText;
                loadDataFromDB();
                return true;
            }
        });
    }

    private void setupSpinners() {
        // --- 1. SPINNER CATEGORY ---
        List<String> categories = new ArrayList<>();
        categories.add("Tất cả loại món");
        categories.add("Món chính");
        categories.add("Canh - Rau");
        categories.add("Ăn vặt / Giải khát");
        categories.add("Món chay / Tương");

        ArrayAdapter<String> catAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(catAdapter);

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (currentCategory != position) {
                    currentCategory = position;
                    loadDataFromDB();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });


        // --- 2. SPINNER CUISINE ---
        List<String> cuisineDisplay = new ArrayList<>();
        cuisineDisplay.add("Tất cả vùng miền");
        cuisineDisplay.add("Món Việt Nam");
        cuisineDisplay.add("Món Hàn Quốc");
        cuisineDisplay.add("Món Thái");
        cuisineDisplay.add("Món Âu Mỹ");
        cuisineDisplay.add("Món Nhật Bản");

        List<String> cuisineValues = new ArrayList<>();
        cuisineValues.add("All");
        cuisineValues.add("Vietnamese");
        cuisineValues.add("Korean");
        cuisineValues.add("Thai");
        cuisineValues.add("European");
        cuisineValues.add("Japanese");

        ArrayAdapter<String> cuisineAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cuisineDisplay);
        cuisineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCuisine.setAdapter(cuisineAdapter);

        spinnerCuisine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedValue = cuisineValues.get(position);
                if (!currentCuisine.equals(selectedValue)) { // Chỉ load lại nếu có thay đổi
                    currentCuisine = selectedValue;
                    loadDataFromDB();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }


    private void loadDataFromDB() {
        new Thread(() -> {
            List<Recipe> result = db.recipeDAO().searchRecipesCombined(currentKeyword, currentCategory, currentCuisine);
            runOnUiThread(() -> {
                updateRecyclerView(result);
            });
        }).start();
    }

    // Cập nhật giao diện sau khi có dữ liệu
    private void updateRecyclerView(List<Recipe> list) {
        if (adapter != null) {
            adapter.setData(list, new ListRecipeAdapter.IClickItemListener() {
                @Override
                public void onClickItem(Recipe recipe) {
                    Toast.makeText(ListRecipeActivity.this, "Bạn chọn: " + recipe.getName(), Toast.LENGTH_SHORT).show();

                     Intent intent = new Intent(ListRecipeActivity.this, RecipeDetailActivity.class);
                     intent.putExtra("RECIPE_ID", recipe.getId());
                     startActivity(intent);
                }
            });
        }

        if (list != null && !list.isEmpty()) {
            tvResultCount.setText("Tìm thấy " + list.size() + " món ăn");
        } else {
            tvResultCount.setText("Không tìm thấy món nào phù hợp");
        }
    }

    // Xử lý Intent từ màn hình Home gửi sang
    private void handleIntentData() {
        Intent intent = getIntent();
        String type = intent.getStringExtra(KEY_TYPE);
        String data = intent.getStringExtra(KEY_DATA);

        if (type != null) {
            if ("CUISINE".equals(type)) {
                // Ví dụ data = "Vietnamese"
                currentCuisine = data;
                setSpinnerCuisineSelection(data);

            } else if ("CATEGORY".equals(type)) {
                // Ví dụ data = "1"
                try {
                    int catId = Integer.parseInt(data);
                    currentCategory = catId;
                    if(catId >=0 && catId < spinnerCategory.getAdapter().getCount()){
                        spinnerCategory.setSelection(catId);
                    }
                } catch (Exception e) { e.printStackTrace(); }
            }
        }
        loadDataFromDB();
    }

    // Hàm phụ để set Spinner Cuisine đúng vị trí
    private void setSpinnerCuisineSelection(String cuisineCode) {
        if (cuisineCode == null) return;
        switch (cuisineCode) {
            case "Vietnamese": spinnerCuisine.setSelection(1); break;
            case "Korean":     spinnerCuisine.setSelection(2); break;
            case "Thai":       spinnerCuisine.setSelection(3); break;
            case "European":   spinnerCuisine.setSelection(4); break;
            case "Japanese":   spinnerCuisine.setSelection(5); break;
            default:           spinnerCuisine.setSelection(0); break;
        }
    }
}