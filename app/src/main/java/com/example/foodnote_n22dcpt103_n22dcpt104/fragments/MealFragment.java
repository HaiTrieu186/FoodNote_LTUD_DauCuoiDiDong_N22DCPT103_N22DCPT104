package com.example.foodnote_n22dcpt103_n22dcpt104.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodnote_n22dcpt103_n22dcpt104.CreateListActivity;
import com.example.foodnote_n22dcpt103_n22dcpt104.ListRecipeActivity;
import com.example.foodnote_n22dcpt103_n22dcpt104.R;
import com.example.foodnote_n22dcpt103_n22dcpt104.RecipeDetailActivity;
import com.example.foodnote_n22dcpt103_n22dcpt104.adapter.meal_fragment_adapter.MealRecipeAdapter;
import com.example.foodnote_n22dcpt103_n22dcpt104.database.AppDatabase;
import com.example.foodnote_n22dcpt103_n22dcpt104.database.entities.Meal_plan;
import com.example.foodnote_n22dcpt103_n22dcpt104.database.models.RecipeForMealPlan;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class MealFragment extends Fragment implements MealRecipeAdapter.IMealItemListener {

    private ImageView btnPrevDate, btnNextDate;
    private LinearLayout layoutPickDate;
    private TextView tvCurrentDate;

    private TextView btnAddBreakfast, btnAddLunch, btnAddDinner;
    private RecyclerView rcvBreakfast, rcvLunch, rcvDinner;
    private TextView tvEmptyBreakfast, tvEmptyLunch, tvEmptyDinner;

    private EditText edtMealNote;
    private Button btnCreateShoppingList;

    private MealRecipeAdapter adapterBreakfast, adapterLunch, adapterDinner;
    private AppDatabase db;
    private Calendar currentCal;
    private SimpleDateFormat sdf;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_meal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = AppDatabase.getInstance(getContext());
        currentCal = Calendar.getInstance();
        sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        initUI(view);
        loadDataForDate();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDataForDate(); // Load lại khi quay về từ màn hình khác
    }

    @Override
    public void onPause() {
        super.onPause();
        saveNote(); // Tự động lưu note khi thoát
    }

    @Override
    public void onDeleteClick(RecipeForMealPlan item) {
        // Xử lý logic xóa
        new Thread(() -> {
            db.mealPlanDao().deleteRecipeFromMealPlan(item.getRecipe_session_id());
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "Đã xóa: " + item.getRecipe_name(), Toast.LENGTH_SHORT).show();
                    loadDataForDate(); // Load lại dữ liệu
                });
            }
        }).start();
    }

    @Override
    public void onItemClick(RecipeForMealPlan item) {
        // Xử lý logic chuyển trang
        Intent intent = new Intent(getActivity(), RecipeDetailActivity.class);
        intent.putExtra("RECIPE_ID", item.getRecipe_id());
        startActivity(intent);
    }

    private void loadDataForDate() {
        String dateStr = sdf.format(currentCal.getTime());

        new Thread(() -> {
            long mealId = db.mealPlanDao().getIDMealPlanByDate(dateStr);
            List<RecipeForMealPlan> listSang = db.mealPlanDao().getRecipesFromMealPlanBySession((int) mealId, 1);
            List<RecipeForMealPlan> listTrua = db.mealPlanDao().getRecipesFromMealPlanBySession((int) mealId, 2);
            List<RecipeForMealPlan> listToi = db.mealPlanDao().getRecipesFromMealPlanBySession((int) mealId, 3);

            String noteContent = db.mealPlanDao().getNoteByDate(dateStr);
            String finalNote = noteContent == null ? "" : noteContent;

            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    updateAdapter(adapterBreakfast, listSang, rcvBreakfast, tvEmptyBreakfast);
                    updateAdapter(adapterLunch, listTrua, rcvLunch, tvEmptyLunch);
                    updateAdapter(adapterDinner, listToi, rcvDinner, tvEmptyDinner);

                    if (!edtMealNote.hasFocus()) edtMealNote.setText(finalNote);
                });
            }
        }).start();
    }

    private void updateAdapter(MealRecipeAdapter adapter, List<RecipeForMealPlan> list, RecyclerView rcv, TextView tvEmpty) {
        adapter.setData(list, this); // 'this' chính là Fragment này (đã implement Interface)

        if (list == null || list.isEmpty()) {
            rcv.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            rcv.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);
        }
    }

    private void initUI(View view) {
        btnPrevDate = view.findViewById(R.id.btn_prev_date);
        btnNextDate = view.findViewById(R.id.btn_next_date);
        layoutPickDate = view.findViewById(R.id.layout_pick_date);
        tvCurrentDate = view.findViewById(R.id.tv_current_date);
        updateDateDisplay();

        btnPrevDate.setOnClickListener(v -> changeDate(-1));
        btnNextDate.setOnClickListener(v -> changeDate(1));
        layoutPickDate.setOnClickListener(v -> showDatePicker());

        // Setup 3 Adapter
        setupRecyclerView(view, R.id.rcv_breakfast, R.id.btn_add_breakfast, R.id.tv_empty_breakfast, 1);
        setupRecyclerView(view, R.id.rcv_lunch, R.id.btn_add_lunch, R.id.tv_empty_lunch, 2);
        setupRecyclerView(view, R.id.rcv_dinner, R.id.btn_add_dinner, R.id.tv_empty_dinner, 3);

        edtMealNote = view.findViewById(R.id.edt_meal_note);
        edtMealNote.setOnFocusChangeListener((v, hasFocus) -> { if (!hasFocus) saveNote(); });

        btnCreateShoppingList = view.findViewById(R.id.btn_create_shopping_list);
        btnCreateShoppingList.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CreateListActivity.class);
            // Gửi ngày đang hiển thị (currentCal) sang Activity tạo list
            // key là "PRE_SELECTED_DATE", value là chuỗi ngày "dd/MM/yyyy"
            intent.putExtra("PRE_SELECTED_DATE", sdf.format(currentCal.getTime()));
            startActivity(intent);
        });
    }

    // Hàm phụ để setUpRecyclerview
    private void setupRecyclerView(View view, int rcvId, int btnAddId, int emptyId, int session) {
        RecyclerView rcv = view.findViewById(rcvId);
        TextView btnAdd = view.findViewById(btnAddId);
        TextView tvEmpty = view.findViewById(emptyId);

        MealRecipeAdapter adapter = new MealRecipeAdapter();
        rcv.setLayoutManager(new LinearLayoutManager(getContext()));
        rcv.setAdapter(adapter);

        if (session == 1) {
            adapterBreakfast = adapter;
            tvEmptyBreakfast = tvEmpty;
            rcvBreakfast = rcv; }
        else if (session == 2) {
            adapterLunch = adapter;
            tvEmptyLunch = tvEmpty;
            rcvLunch = rcv; }
        else {
            adapterDinner = adapter;
            tvEmptyDinner = tvEmpty;
            rcvDinner = rcv; }

        btnAdd.setOnClickListener(v -> openListRecipe(session));
    }

    private void changeDate(int amount) {
        saveNote();
        currentCal.add(Calendar.DAY_OF_YEAR, amount);
        updateDateDisplay();
        loadDataForDate();
    }

    private void showDatePicker() {
        saveNote();
        new DatePickerDialog(getContext(), (view, year, month, day) -> {
            currentCal.set(year, month, day);
            updateDateDisplay();
            loadDataForDate();
        }, currentCal.get(Calendar.YEAR), currentCal.get(Calendar.MONTH), currentCal.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void openListRecipe(int session) {
        Intent intent = new Intent(getActivity(), ListRecipeActivity.class);
        intent.putExtra("TARGET_DATE", sdf.format(currentCal.getTime()));
        intent.putExtra("TARGET_SESSION", session);
        startActivity(intent);
    }

    private void saveNote() {
        String dateStr = sdf.format(currentCal.getTime());
        String content = edtMealNote.getText().toString().trim();
        new Thread(() -> {
            long mealId = db.mealPlanDao().getIDMealPlanByDate(dateStr);
            if (mealId == 0 && !content.isEmpty()) {
                db.mealPlanDao().insertMealPlan(new Meal_plan(null, content, dateStr));
            } else if (mealId != 0) {
                db.mealPlanDao().updateNote(dateStr, content);
            }
        }).start();
    }

    private void updateDateDisplay() {
        tvCurrentDate.setText(sdf.format(currentCal.getTime()));
    }
}