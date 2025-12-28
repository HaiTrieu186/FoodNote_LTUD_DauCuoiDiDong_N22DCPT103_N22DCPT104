package com.example.foodnote_n22dcpt103_n22dcpt104.adapter.fridge_fragment_adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodnote_n22dcpt103_n22dcpt104.R;
import com.example.foodnote_n22dcpt103_n22dcpt104.database.entities.Recipe;
import com.example.foodnote_n22dcpt103_n22dcpt104.database.models.RecipeSuggestion;

import java.util.List;

public class RecipeSuggestionAdapter extends RecyclerView.Adapter<RecipeSuggestionAdapter.RecipeSuggestionViewHolder> {

    private List<RecipeSuggestion> mList;
    private IClickSuggestionListener iClickSuggestionListener;
    public interface IClickSuggestionListener {
        void onClickItem(Recipe recipe);
    }

    public void setData(List<RecipeSuggestion> list, IClickSuggestionListener listener) {
        this.mList = list;
        this.iClickSuggestionListener = listener;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeSuggestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_fridge_recyclerview_item_recipe_suggestion, parent, false);
        return new RecipeSuggestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeSuggestionViewHolder holder, int position) {
        RecipeSuggestion item = mList.get(position);
        if (item == null) return;

        //  Gán dữ liệu
        holder.tv_name.setText(item.recipe.getName());
        holder.tv_percent.setText("Phù hợp: " + item.getMatchPercentage() + "%");

        // Logic đổi màu chữ trạng thái (Đủ/Thiếu)
        if (item.missingIngredients == 0) {
            holder.tv_missing.setText("Đủ nguyên liệu! Nấu ngay");
            holder.tv_missing.setTextColor(Color.parseColor("#4CAF50")); // Màu xanh lá
        } else {
            holder.tv_missing.setText("Thiếu " + item.missingIngredients + " nguyên liệu");
            holder.tv_missing.setTextColor(Color.parseColor("#757575")); // Màu xám
        }

        // Load ảnh
        String imgPath = "file:///android_asset/images/" + item.recipe.getImg() + ".webp";
        Glide.with(holder.itemView.getContext())
                .load(imgPath)
                .placeholder(R.drawable.ic_loading)
                .error(R.drawable.ic_default_recipe)
                .centerCrop()
                .into(holder.imv_img);

        // Sự kiện Click --> chuyển sang trang Chi tiết
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iClickSuggestionListener != null) {
                    iClickSuggestionListener.onClickItem(item.recipe);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mList != null) return mList.size();
        return 0;
    }

    public class RecipeSuggestionViewHolder extends RecyclerView.ViewHolder {
        ImageView imv_img;
        TextView tv_name, tv_percent, tv_missing;

        public RecipeSuggestionViewHolder(@NonNull View itemView) {
            super(itemView);
            imv_img = itemView.findViewById(R.id.img_recipe_suggest);
            tv_name = itemView.findViewById(R.id.tv_name_suggest);
            tv_percent = itemView.findViewById(R.id.tv_match_percent);
            tv_missing = itemView.findViewById(R.id.tv_missing_count);
        }
    }
}
