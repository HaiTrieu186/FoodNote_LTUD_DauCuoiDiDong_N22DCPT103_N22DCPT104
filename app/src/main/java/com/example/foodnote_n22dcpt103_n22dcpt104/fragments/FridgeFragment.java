package com.example.foodnote_n22dcpt103_n22dcpt104.fragments;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodnote_n22dcpt103_n22dcpt104.R;
import com.example.foodnote_n22dcpt103_n22dcpt104.RecipeDetailActivity;
import com.example.foodnote_n22dcpt103_n22dcpt104.adapter.fridge_fragment_adapter.FridgeIngredientAdapter;
import com.example.foodnote_n22dcpt103_n22dcpt104.adapter.fridge_fragment_adapter.RecipeSuggestionAdapter;
import com.example.foodnote_n22dcpt103_n22dcpt104.database.AppDatabase;
import com.example.foodnote_n22dcpt103_n22dcpt104.database.entities.Ingredient;
import com.example.foodnote_n22dcpt103_n22dcpt104.database.entities.Recipe;
import com.example.foodnote_n22dcpt103_n22dcpt104.database.models.RecipeSuggestion;

import java.util.List;

// 1. Implements View.OnClickListener tại đây
public class FridgeFragment extends Fragment implements View.OnClickListener {

    // View
    private SearchView sv_fridge;
    private Button btn_all, btn_1, btn_2, btn_3, btn_4;
    private RecyclerView rcv_ingredients, rcv_suggestions;

    // Adapter và Data
    private FridgeIngredientAdapter fridgeAdapter;
    private RecipeSuggestionAdapter suggestAdapter;
    private AppDatabase db;
    private int currentCategory = 0; // 0: All, 1: Thịt, 2: Rau...

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fridge, container, false);
        initUI(view);

        // Load dữ liệu lần đầu
        filterData("", 0);
        reloadSuggestions();

        return view;
    }

    private void initUI(View view) {
        sv_fridge = view.findViewById(R.id.sv_fridge);
        btn_all = view.findViewById(R.id.btn_filter_all);
        btn_1 = view.findViewById(R.id.btn_filter_1);
        btn_2 = view.findViewById(R.id.btn_filter_2);
        btn_3 = view.findViewById(R.id.btn_filter_3);
        btn_4 = view.findViewById(R.id.btn_filter_4);

        rcv_ingredients = view.findViewById(R.id.rcv_ingredients);
        rcv_suggestions = view.findViewById(R.id.rcv_suggestions);

        db = AppDatabase.getInstance(getContext());

        // Setup Adapter Nguyên liệu
        fridgeAdapter = new FridgeIngredientAdapter();
        rcv_ingredients.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rcv_ingredients.setAdapter(fridgeAdapter);

        // Setup Adapter Gợi ý
        suggestAdapter = new RecipeSuggestionAdapter();
        rcv_suggestions.setLayoutManager(new LinearLayoutManager(getContext()));
        rcv_suggestions.setAdapter(suggestAdapter);

        // Cài đặt sự kiện
        setupEvents();
    }

    private void setupEvents() {
        // Tìm kiếm
        sv_fridge.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterData(newText, currentCategory);
                return true;
            }
        });

        // 2. Gán sự kiện click cho 'this' (chính là Fragment này)
        btn_all.setOnClickListener(this);
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        btn_3.setOnClickListener(this);
        btn_4.setOnClickListener(this);
    }

    // Hàm onClick chung cho tất cả các nút
    @Override
    public void onClick(View v) {
        // Reset màu tất cả nút về mặc định (Xám)
        resetButtonColors();

        // Đổi màu nút vừa bấm (Đỏ)
        v.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E23E3E")));
        ((Button) v).setTextColor(Color.WHITE);

        int id = v.getId();

        if (id == R.id.btn_filter_all) {
            currentCategory = 0;
        } else if (id == R.id.btn_filter_1) {
            currentCategory = 1; // Thịt/Trứng
        } else if (id == R.id.btn_filter_2) {
            currentCategory = 2; // Rau/Củ
        } else if (id == R.id.btn_filter_3) {
            currentCategory = 3; // Gia vị
        } else if (id == R.id.btn_filter_4) {
            currentCategory = 4; // Đồ khô
        }

        // Gọi hàm lọc dữ liệu với category vừa chọn
        filterData(sv_fridge.getQuery().toString(), currentCategory);
    }

    // Hàm reset màu nút
    private void resetButtonColors() {
        Button[] buttons = {btn_all, btn_1, btn_2, btn_3, btn_4};
        for (Button btn : buttons) {
            btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F0F0F0")));
            btn.setTextColor(Color.parseColor("#555555"));
        }
    }

    // Hàm lọc dữ liệu từ DB (Dùng Thread)
    private void filterData(String keyword, int category) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Gọi hàm Search trong DAO
                List<Ingredient> result = db.ingredientDao().searchIngredients(keyword, category);

                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Cập nhật Adapter
                            fridgeAdapter.setData(result, new FridgeIngredientAdapter.IClickIngredientListener() {
                                @Override
                                public void onClickItem() {
                                    // Khi chọn item -> Load lại list gợi ý
                                    reloadSuggestions();
                                }
                            });
                        }
                    });
                }
            }
        }).start();
    }

    // Hàm load list gợi ý từ DB
    private void reloadSuggestions() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<RecipeSuggestion> suggestions = db.ingredientDao().getSuggestedRecipes();

                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            suggestAdapter.setData(suggestions, new RecipeSuggestionAdapter.IClickSuggestionListener() {
                                @Override
                                public void onClickItem(Recipe recipe) {
                                    // Chuyển màn hình
                                    Intent intent = new Intent(getActivity(), RecipeDetailActivity.class);
                                    intent.putExtra("RECIPE_ID", recipe.getId());
                                    startActivity(intent);
                                }
                            });
                        }
                    });
                }
            }
        }).start();
    }
}