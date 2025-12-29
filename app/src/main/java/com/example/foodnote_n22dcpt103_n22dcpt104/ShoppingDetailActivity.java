package com.example.foodnote_n22dcpt103_n22dcpt104;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodnote_n22dcpt103_n22dcpt104.adapter.list_fragment_adapter.ShoppingItemAdapter;
import com.example.foodnote_n22dcpt103_n22dcpt104.database.AppDatabase;
import com.example.foodnote_n22dcpt103_n22dcpt104.database.models.ShoppingListIngredient;

import java.util.List;

public class ShoppingDetailActivity extends AppCompatActivity {
    private int listId;
    private AppDatabase db;
    private ShoppingItemAdapter adapter;
    private TextView tvTitle;
    private  ImageView btnDelete;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shopping_detail);
        
        initUI();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initUI() {
        // Nhận dữ liệu từ Fragment
        listId = getIntent().getIntExtra("LIST_ID", -1);
        String listName = getIntent().getStringExtra("LIST_NAME");

        if (listId == -1) { finish(); return; }

        db = AppDatabase.getInstance(this);

        // Ánh xạ
        ImageView btnBack = findViewById(R.id.btn_back_detail_list);
        tvTitle = findViewById(R.id.tv_detail_list_title);
        RecyclerView rcv = findViewById(R.id.rcv_shopping_items);
        btnDelete= findViewById(R.id.btn_delete_list);

        
        btnDelete.setOnClickListener(v -> {
            // Hiện dialog xác nhận xóa
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Xóa danh sách?")
                    .setMessage("Bạn có chắc chắn muốn xóa danh sách đi chợ này không? Các ngày ăn sẽ được mở lại để tạo danh sách mới.")
                    .setPositiveButton("Xóa", (dialog, which) -> deleteCurrentList())
                    .setNegativeButton("Hủy", null)
                    .show();
        });


        tvTitle.setText(listName);
        btnBack.setOnClickListener(v -> finish());

        adapter = new ShoppingItemAdapter();
        rcv.setLayoutManager(new LinearLayoutManager(this));
        rcv.setAdapter(adapter);

        loadItems();
    }

    private void deleteCurrentList() {
        new Thread(() -> {
            // 1. Gỡ List ID khỏi các Meal Plan (để Meal Plan đó có thể được chọn lại)
            db.mealPlanDao().removeShoppingListFromMeals(listId);

            // 2. Xóa Shopping List (Item con sẽ tự xóa do)
            db.shoppingListDao().deleteShoppingList(listId);

            runOnUiThread(() -> {
                Toast.makeText(this, "Đã xóa danh sách!", Toast.LENGTH_SHORT).show();
                finish(); // Đóng Activity quay về list
            });
        }).start();
    }

    private void loadItems() {
        new Thread(() -> {
            List<ShoppingListIngredient> items = db.shoppingListDao().getShoppingListWithItems(listId);
            runOnUiThread(() -> {
                adapter.setData(items, (item, isChecked) -> {
                    updateStatus(item, isChecked);
                    // Cập nhật UI ngay lập tức
                    item.setIs_bought(isChecked);
                    adapter.notifyDataSetChanged();
                });
            });
        }).start();
    }

    private void updateStatus(ShoppingListIngredient item, boolean isChecked) {
        new Thread(() -> {
            //  Update trạng thái món hàng
            db.shoppingListDao().updateItemStatus(item.getItem_id(), isChecked);

            //  Kiểm tra xem list đã xong hết chưa để update trạng thái Hoàn thành
            int remaining = db.shoppingListDao().countUnboughtItems(listId);
            db.shoppingListDao().updateShoppingListStatus(listId, remaining == 0);
        }).start();
    }
}