package com.example.foodnote_n22dcpt103_n22dcpt104.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodnote_n22dcpt103_n22dcpt104.R;
import com.example.foodnote_n22dcpt103_n22dcpt104.database.AppDatabase;
import com.example.foodnote_n22dcpt103_n22dcpt104.database.entities.Recipe;

import java.util.List;

public class ListRecipeAdapter extends RecyclerView.Adapter<ListRecipeAdapter.ListRecipeViewHolder> {

    private List<Recipe> mList;
    private IClickItemListener iClickItemListenerr;

    // Interface để Activity hứng sự kiện click vào item
    public interface IClickItemListener {
        void onClickItem(Recipe recipe);
    }

    public void setData(List<Recipe> list, IClickItemListener listener) {
        this.mList = list;
        this.iClickItemListenerr = listener;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ListRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.acitivity_list_recipe_item, parent, false);
        return new ListRecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListRecipeViewHolder holder, int position) {
        Recipe recipe = mList.get(position);
        if (recipe == null) return;

        holder.tvName.setText(recipe.getName());
        holder.tvTime.setText(recipe.getReadyInMinutes() + " phút");
        holder.tvCuisine.setText(recipe.getCuisine());

        String duongDanAsset = "file:///android_asset/images/" + recipe.getImg() + ".webp";
        Glide.with(holder.itemView.getContext())
                .load(duongDanAsset)
                .centerCrop()
                .placeholder(R.drawable.ic_loading)
                .error(R.drawable.ic_default_recipe)
                .into(holder.img);


        boolean isFav = (recipe.isFavorite() == 1);
        holder.btnFav.setActivated(isFav);


        holder.btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newStatus = (recipe.isFavorite() == 1) ? 0 : 1;
                v.setActivated(newStatus == 1);
                recipe.setFavorite(newStatus);

                if (newStatus == 1) {
                    Toast.makeText(v.getContext(), "Đã thêm vào yêu thích ❤️", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(v.getContext(), "Đã bỏ khỏi yêu thích 💔", Toast.LENGTH_SHORT).show();
                }

                // Cập nhật DB
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        AppDatabase db = AppDatabase.getInstance(v.getContext());
                        db.recipeDAO().updateFavorite(recipe.getId(), newStatus );
                    }
                }).start();
            }
        });


        //  Sự kiện click vào cả dòng (Item) để xem chi tiết
        holder.itemView.setOnClickListener(v -> {
            if (iClickItemListenerr != null) {
                iClickItemListenerr.onClickItem(recipe);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mList != null)
            return mList.size();
        return 0;
    }

    public class ListRecipeViewHolder extends RecyclerView.ViewHolder{

        ImageView img, btnFav;
        TextView tvName, tvTime, tvCuisine;
        public ListRecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img_recipe_list);
            tvName = itemView.findViewById(R.id.tv_name_recipe_list);
            tvTime = itemView.findViewById(R.id.tv_time_recipe_list);
            tvCuisine = itemView.findViewById(R.id.tv_cuisine_recipe_list);
            btnFav = itemView.findViewById(R.id.btn_fav_recipe_list);
        }
    }
}
