package com.example.foodnote_n22dcpt103_n22dcpt104.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodnote_n22dcpt103_n22dcpt104.CreateListActivity;
import com.example.foodnote_n22dcpt103_n22dcpt104.R;
import com.example.foodnote_n22dcpt103_n22dcpt104.ShoppingDetailActivity;
import com.example.foodnote_n22dcpt103_n22dcpt104.adapter.list_fragment_adapter.ShoppingListAdapter;
import com.example.foodnote_n22dcpt103_n22dcpt104.database.AppDatabase;
import com.example.foodnote_n22dcpt103_n22dcpt104.database.entities.Meal_plan;
import com.example.foodnote_n22dcpt103_n22dcpt104.database.entities.Shopping_list;

import java.util.ArrayList;
import java.util.List;


public class ListFragment extends Fragment {

    private RecyclerView rcv;
    private ShoppingListAdapter adapter;
    private AppDatabase db;

    @Override
    public void onResume() {
        super.onResume();
        loadShoppingLists();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = AppDatabase.getInstance(getContext());
        initUI(view);
        loadShoppingLists();
    }

    private void initUI(View view) {
        db = AppDatabase.getInstance(getContext());
        rcv = view.findViewById(R.id.rcv_shopping_lists);
        View btnCreate = view.findViewById(R.id.btn_create_list);

        adapter = new ShoppingListAdapter();
        rcv.setLayoutManager(new LinearLayoutManager(getContext()));
        rcv.setAdapter(adapter);

        //  Chuyển sang trang tạo list
        btnCreate.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), CreateListActivity.class));
        });
    }


    private void loadShoppingLists() {
        new Thread(() -> {
            List<Shopping_list> list = db.shoppingListDao().getAllShoppingLists();
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    adapter.setData(list, item -> {
                        // Chuyển sang màn hình Chi tiết (Check món)
                        Intent intent = new Intent(getActivity(), ShoppingDetailActivity.class);
                        intent.putExtra("LIST_ID", item.getId());
                        intent.putExtra("LIST_NAME", item.getName());
                        startActivity(intent);
                    });
                });
            }
        }).start();
    }

}
