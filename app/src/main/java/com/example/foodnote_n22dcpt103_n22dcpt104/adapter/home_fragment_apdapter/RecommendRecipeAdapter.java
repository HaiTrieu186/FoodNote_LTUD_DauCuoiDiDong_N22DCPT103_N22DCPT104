package com.example.foodnote_n22dcpt103_n22dcpt104.adapter.home_fragment_apdapter;

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

public class RecommendRecipeAdapter extends RecyclerView.Adapter<RecommendRecipeAdapter.RecommendRecipeViewHolder> {

    private List<Recipe> mlist;

    public void setData(List<Recipe> list){
        this.mlist=list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecommendRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_home_recyclerview_recommend_recipe,parent, false);
        return new RecommendRecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendRecipeViewHolder holder, int position) {
        Recipe recipe = mlist.get(position);
        if (recipe == null)
            return;

        holder.tv_name.setText(recipe.getName());
        holder.tv_readyinminutes.setText(String.valueOf(recipe.getReadyInMinutes()));
        holder.tv_cuisine.setText(recipe.getCuisine());

        String category;
        switch (recipe.getCategory()){
            case 1:
                category="Món chính";
                break;
            case 2:
                category="Canh - Rau";
                break;
            case 3:
                category="Ăn vặt / giải khát";
                break;
            case 4:
                category="Món chay / tương";
                break;
            default:
                category="Món chính";
        }

        holder.tv_category.setText(category);


        String duongDanAsset = "file:///android_asset/images/" + recipe.getImg() + ".webp";
        Glide.with(holder.itemView.getContext())
                .load(duongDanAsset)
                .centerCrop()
                .placeholder(R.drawable.ic_loading)
                .error(R.drawable.ic_default_recipe)
                .into(holder.imv_img);

        // Tính năng yêu thích
        boolean isFav = (recipe.isFavorite() == 1);
        holder.imv_isfavorite.setActivated(isFav);

        holder.imv_isfavorite.setOnClickListener(new View.OnClickListener() {
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

    }



    @Override
    public int getItemCount() {
        if (mlist != null)
            return mlist.size();
        return 0;
    }

    public class RecommendRecipeViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name, tv_cuisine, tv_category, tv_readyinminutes;
        private ImageView imv_img, imv_isfavorite;

        public RecommendRecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name= itemView.findViewById(R.id.tv_name_home_rcv_recommend);
            tv_cuisine= itemView.findViewById(R.id.tv_cuisine_home_rcv_recommend);
            tv_category= itemView.findViewById(R.id.tv_category_home_rcv_recommend);
            tv_readyinminutes= itemView.findViewById(R.id.tv_readyinminutes_home_rcv_recommned);

            imv_img= itemView.findViewById(R.id.imv_img_home_rcv_recommend);
            imv_isfavorite= itemView.findViewById(R.id.imv_isfavorite_home_rcv_recommend);
        }
    }
}
