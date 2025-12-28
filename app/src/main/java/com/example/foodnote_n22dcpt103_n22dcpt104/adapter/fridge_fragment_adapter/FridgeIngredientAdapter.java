package com.example.foodnote_n22dcpt103_n22dcpt104.adapter.fridge_fragment_adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodnote_n22dcpt103_n22dcpt104.R;
import com.example.foodnote_n22dcpt103_n22dcpt104.database.AppDatabase;
import com.example.foodnote_n22dcpt103_n22dcpt104.database.entities.Fridge;
import com.example.foodnote_n22dcpt103_n22dcpt104.database.entities.Ingredient;

import java.util.List;

public class FridgeIngredientAdapter extends RecyclerView.Adapter<FridgeIngredientAdapter.FridgeIngredientViewHolder>{

    private List<Ingredient> mlist;
    private IClickIngredientListener iClickIngredientListener;

    public interface IClickIngredientListener {
        void onClickItem();
    }

    public void setData(List<Ingredient> list, IClickIngredientListener listener) {
        this.mlist = list;
        this.iClickIngredientListener = listener;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FridgeIngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_fridge_item_ingredientt, parent, false);
        return new FridgeIngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FridgeIngredientViewHolder holder, int position) {
        final Ingredient ingredient = mlist.get(position);
        if (ingredient == null) return;


        holder.tv_name.setText(ingredient.getName());
        String imgPath = "file:///android_asset/images/" + ingredient.getImg() + ".webp";
        Glide.with(holder.itemView.getContext())
                .load(imgPath)
                .placeholder(R.drawable.ic_loading)
                .error(R.drawable.ic_default_ingredient)
                .into(holder.imv_img);


        //  Kiểm tra Có trong tủ lạnh chưa? --> cập nhật UI
        new Thread(new Runnable() {
            @Override
            public void run() {
                //  Check DB
                AppDatabase db = AppDatabase.getInstance(holder.itemView.getContext());
                final boolean isInFridge = db.ingredientDao().checkIfInFridge(ingredient.getId());

                //Cập nhật UI
                holder.itemView.post(new Runnable() {
                    @Override
                    public void run() {
                        holder.layout_item.setSelected(isInFridge);
                    }
                });
            }
        }).start();

        // Sự kiện khi click vào nguyên liệu (item)
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Đổi màu UI
                boolean currentState = holder.layout_item.isSelected();
                final boolean newState = !currentState; // Trạng thái mới
                holder.layout_item.setSelected(newState);

                // Lưu vào Database (Chạy ngầm)
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        AppDatabase db = AppDatabase.getInstance(holder.itemView.getContext());

                        if (newState) {
                            try {
                                db.ingredientDao().addToFridge(ingredient.getId());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            db.ingredientDao().removeFromFridge(ingredient.getId());
                        }

                        // Báo ra ngoài Fragment để reload danh sách gợi ý món ăn
                        // Dùng view.post để đảm bảo hàm onClickItem() được gọi ở luồng chính
                        holder.itemView.post(new Runnable() {
                            @Override
                            public void run() {
                                if (iClickIngredientListener != null) {
                                    iClickIngredientListener.onClickItem();
                                }
                            }
                        });
                    }
                }).start();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mlist != null) return mlist.size();
        return 0;
    }

    public class FridgeIngredientViewHolder extends RecyclerView.ViewHolder {
        ImageView imv_img;
        TextView tv_name;
        LinearLayout layout_item;

        public FridgeIngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            imv_img = itemView.findViewById(R.id.img_ingredient);
            tv_name = itemView.findViewById(R.id.tv_ingredient_name);
            layout_item = itemView.findViewById(R.id.layout_item_fridge);
        }
    }
}
