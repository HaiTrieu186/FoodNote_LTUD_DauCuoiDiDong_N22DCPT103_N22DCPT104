package com.example.foodnote_n22dcpt103_n22dcpt104.adapter.list_fragment_adapter;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodnote_n22dcpt103_n22dcpt104.R;
import com.example.foodnote_n22dcpt103_n22dcpt104.database.models.ShoppingListIngredient;

import java.util.List;

public class ShoppingItemAdapter extends RecyclerView.Adapter<ShoppingItemAdapter.ViewHolder> {

    private List<ShoppingListIngredient> list;
    private ICheckListener listener;

    public interface ICheckListener {
        void onCheck(ShoppingListIngredient item, boolean isChecked);
    }

    public void setData(List<ShoppingListIngredient> list, ICheckListener listener) {
        this.list = list;
        this.listener = listener;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_shopping_detail_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ShoppingListIngredient item = list.get(position);
        if (item == null) return;

        holder.tvName.setText(item.getIngredient_name());

        // --- XỬ LÝ SỐ LƯỢNG & ĐƠN VỊ ---
        String qtyStr = String.valueOf(item.getTotal_quantity());
        if (qtyStr.endsWith(".0")) qtyStr = qtyStr.replace(".0", "");

        String unit = item.getIngredient_unit();
        if (unit == null) unit = "";

        holder.tvQty.setText("Cần mua: " + qtyStr + " " + unit);

        // --- XỬ LÝ ẢNH ---
        String imgPath = "file:///android_asset/images/" + item.getIngredient_img() + ".webp";
        Glide.with(holder.itemView.getContext())
                .load(imgPath)
                .placeholder(R.drawable.ic_loading)
                .error(R.drawable.ic_default_ingredient)
                .centerCrop()
                .into(holder.imgItem);

        // --- CHECKBOX ---
        holder.cbBought.setOnCheckedChangeListener(null);
        holder.cbBought.setChecked(item.isIs_bought());

        if (item.isIs_bought()) {
            holder.tvName.setPaintFlags(holder.tvName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tvName.setAlpha(0.5f);
            holder.tvQty.setAlpha(0.5f);
            holder.imgItem.setAlpha(0.5f); // Làm mờ cả ảnh
        } else {
            holder.tvName.setPaintFlags(holder.tvName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.tvName.setAlpha(1.0f);
            holder.tvQty.setAlpha(1.0f);
            holder.imgItem.setAlpha(1.0f);
        }

        holder.cbBought.setOnCheckedChangeListener((buttonView, isChecked) -> {
            listener.onCheck(item, isChecked);
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox cbBought;
        TextView tvName, tvQty;
        ImageView imgItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cbBought = itemView.findViewById(R.id.cb_item_bought);
            tvName = itemView.findViewById(R.id.tv_item_name);
            tvQty = itemView.findViewById(R.id.tv_item_qty);
            imgItem = itemView.findViewById(R.id.img_item_shopping);
        }
    }
}