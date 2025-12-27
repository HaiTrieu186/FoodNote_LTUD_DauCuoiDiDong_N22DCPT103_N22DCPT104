package com.example.foodnote_n22dcpt103_n22dcpt104.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.foodnote_n22dcpt103_n22dcpt104.R;
import com.example.foodnote_n22dcpt103_n22dcpt104.adapter.home_fragment_apdapter.CuisineAdapter;
import com.example.foodnote_n22dcpt103_n22dcpt104.adapter.home_fragment_apdapter.RecommendRecipeAdapter;
import com.example.foodnote_n22dcpt103_n22dcpt104.database.AppDatabase;
import com.example.foodnote_n22dcpt103_n22dcpt104.database.entities.Recipe;
import com.example.foodnote_n22dcpt103_n22dcpt104.models.Cuisine;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    private RecyclerView rcv_home_recommend, rcv_home_cuisine;
    private RecommendRecipeAdapter recommendRecipeAdapter;
    private CuisineAdapter cuisineAdapter;
    private List<Recipe> recommendRecipes;


//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    public HomeFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment HomeFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static HomeFragment newInstance(String param1, String param2) {
//        HomeFragment fragment = new HomeFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ánh xạ
        rcv_home_recommend= view.findViewById(R.id.rcv_home_recommend);
        rcv_home_cuisine= view.findViewById(R.id.rcv_home_cuisine);


        // Thiết lập layout cho recycler view
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rcv_home_recommend.setLayoutManager(layoutManager1);
        LinearLayoutManager linearManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rcv_home_cuisine.setLayoutManager(linearManager2);
        rcv_home_cuisine.setNestedScrollingEnabled(false); // Để cuộn mượt trong ScrollView

        // Thiết lập adapter
        recommendRecipeAdapter = new RecommendRecipeAdapter();
        rcv_home_recommend.setAdapter(recommendRecipeAdapter);
        cuisineAdapter = new CuisineAdapter();
        rcv_home_cuisine.setAdapter(cuisineAdapter);
        
        loadRecommendRecipes();
        loadCuisine();

    }

    private void loadCuisine() {
        List<Cuisine> list = new ArrayList<>();

        list.add(new Cuisine("Món Việt Nam", "cuisine_vietnam", "Vietnamese"));
        list.add(new Cuisine("Món Hàn Quốc", "cuisine_korea", "Korean"));
        list.add(new Cuisine("Món Thái", "cuisine_thai", "Thai"));
        list.add(new Cuisine("Món Âu Mỹ", "cuisine_euro", "European"));
        list.add(new Cuisine("Món Nhật Bản", "cuisine_japan", "Japanese"));

        cuisineAdapter.setData(list, new CuisineAdapter.ICuisineClickListener() {
            @Override
            public void onClickItem(Cuisine cuisine) {
                Toast.makeText(getContext(), "Bạn chọn: " + cuisine.getNameDisplay(), Toast.LENGTH_SHORT).show();
                // Intent intent = new Intent(getActivity(), ListRecipeActivity.class);
                // intent.putExtra("KEY_SEARCH", cuisine.getDataKey());
                // startActivity(intent);
            }
        });
    }

    private void loadRecommendRecipes() {
        new Thread(() -> {
            // 1. Lấy dữ liệu từ DB
            AppDatabase db = AppDatabase.getInstance(getContext());
            recommendRecipes = db.recipeDAO().getRecommendRecipe();

            // 2. Cập nhật lên giao diện
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    recommendRecipeAdapter.setData(recommendRecipes, new RecommendRecipeAdapter.IClickRecommendListener() {
                        @Override
                        public void onClickItem(Recipe recipe) {
                            Toast.makeText(getContext(), "Chọn: " + recipe.getName(), Toast.LENGTH_SHORT).show();
                            // Intent intent = new Intent(getActivity(), RecipeDetailActivity.class);
                            // intent.putExtra("RECIPE_ID", recipe.getId());
                            // startActivity(intent);
                        }
                    });
                });
            }
        }).start();
    }


}