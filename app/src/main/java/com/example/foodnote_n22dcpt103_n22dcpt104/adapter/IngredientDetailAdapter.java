package com.example.foodnote_n22dcpt103_n22dcpt104.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodnote_n22dcpt103_n22dcpt104.R;
import com.example.foodnote_n22dcpt103_n22dcpt104.database.models.IngredientsByRecipe;

import java.util.List;

public class IngredientDetailAdapter extends RecyclerView.Adapter<IngredientDetailAdapter.IngredientDetailViewHolder> {

    private List<IngredientsByRecipe> mlist;

    public void setData(List<IngredientsByRecipe> list) {
        this.mlist = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public IngredientDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_recipe_detail_item_ingredient, parent, false);
        return new IngredientDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientDetailViewHolder holder, int position) {
        IngredientsByRecipe item = mlist.get(position);
        if (item == null) return;

        holder.tvName.setText(item.getIngredient_name());
        holder.tvQuantity.setText(item.getQuantity_needed() + " " + item.getIngredient_unit());

        String note = item.getIngredient_note();
        if (note != null && !note.trim().isEmpty() && !note.equals("null")) {
            holder.tvNote.setText("(" + note + ")");
            holder.tvNote.setVisibility(View.VISIBLE);
        } else {
            holder.tvNote.setVisibility(View.GONE);
        }

        String imgPath = "file:///android_asset/images/" + item.getIngredient_img() + ".webp";
        Glide.with(holder.itemView.getContext())
                .load(imgPath)
                .centerCrop()
                .placeholder(R.drawable.ic_loading)
                .error(R.drawable.ic_default_ingredient)
                .into(holder.imgIngredient);
    }

    @Override
    public int getItemCount() {
        if (mlist != null)
            return mlist.size();
        return 0;
    }

    public class IngredientDetailViewHolder extends RecyclerView.ViewHolder{
        ImageView imgIngredient;
        TextView tvName, tvQuantity, tvNote;
        public IngredientDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            imgIngredient = itemView.findViewById(R.id.img_ingredient_item);
            tvName = itemView.findViewById(R.id.tv_ing_name_detail);
            tvQuantity = itemView.findViewById(R.id.tv_ing_quantity_detail);
            tvNote = itemView.findViewById(R.id.tv_ing_note_detail);
        }
    }
}
