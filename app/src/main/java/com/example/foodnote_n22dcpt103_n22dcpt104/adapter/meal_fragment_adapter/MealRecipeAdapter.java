package com.example.foodnote_n22dcpt103_n22dcpt104.adapter.meal_fragment_adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodnote_n22dcpt103_n22dcpt104.R;
import com.example.foodnote_n22dcpt103_n22dcpt104.database.models.RecipeForMealPlan;

import java.util.List;

public class MealRecipeAdapter extends RecyclerView.Adapter<MealRecipeAdapter.MealRecipeViewHolder> {

    private List<RecipeForMealPlan> mlist;
    private IMealItemListener iMealItemListener;

    public interface IMealItemListener {
        void onDeleteClick(RecipeForMealPlan item); // Sự kiện bấm nút Xóa
        void onItemClick(RecipeForMealPlan item);   // Sự kiện bấm vào món ăn (để xem chi tiết)
    }

    public void setData(List<RecipeForMealPlan> list, IMealItemListener listener) {
        this.mlist = list;
        this.iMealItemListener = listener;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MealRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_meal_item_meal_recipe, parent, false);
        return new MealRecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealRecipeViewHolder holder, int position) {
        RecipeForMealPlan item = mlist.get(position);
        if (item == null) return;

        holder.tvName.setText(item.getRecipe_name());

        String imgPath = "file:///android_asset/images/" + item.getRecipe_img() + ".webp";
        Glide.with(holder.itemView.getContext())
                .load(imgPath)
                .placeholder(R.drawable.ic_loading)
                .error(R.drawable.ic_default_recipe)
                .centerCrop()
                .into(holder.img);

        // Sự kiện Click nút Xóa (Thùng rác)
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iMealItemListener != null) {
                    iMealItemListener.onDeleteClick(item);
                }
            }
        });

        //  Sự kiện Click vào cả dòng (để xem chi tiết món)
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iMealItemListener != null) {
                    iMealItemListener.onItemClick(item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mlist != null) {
            return mlist.size();
        }
        return 0;
    }

    public class MealRecipeViewHolder extends RecyclerView.ViewHolder {
        ImageView img, btnDelete;
        TextView tvName;

        public MealRecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img_meal_item);
            tvName = itemView.findViewById(R.id.tv_meal_item_name);
            btnDelete = itemView.findViewById(R.id.btn_delete_meal_item);
        }
    }
}
