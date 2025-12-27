package com.example.foodnote_n22dcpt103_n22dcpt104.adapter.home_fragment_apdapter;

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
import com.example.foodnote_n22dcpt103_n22dcpt104.models.Cuisine;

import java.util.List;

public class CuisineAdapter extends RecyclerView.Adapter<CuisineAdapter.CuisineViewHolder>{
    private List<Cuisine> mlist;
    private ICuisineClickListener iCuisineClickListener;

    //  Tạo Interface cho sự kiện click
    public interface ICuisineClickListener {
        void onClickItem(Cuisine cuisine);
    }

    public void setData(List<Cuisine> list, ICuisineClickListener listener){
        this.mlist=list;
        this.iCuisineClickListener=listener;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CuisineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_home_recyclerview_cuisine,parent, false);
        return new CuisineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CuisineViewHolder holder, int position) {
        Cuisine cuisine= mlist.get(position);

        if (cuisine == null)
            return;

        holder.tv_cuisine_name.setText(cuisine.getNameDisplay());

        String imgPath = "file:///android_asset/images/" + cuisine.getImgName() + ".webp";
        Glide.with(holder.itemView.getContext())
                .load(imgPath)
                .centerCrop()
                .placeholder(R.drawable.ic_default_recipe)
                .into(holder.imv_cuisine);


        // Sự kiện Click khi click vào cuisine (Chuyển sang trang list)
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gọi interface để báo ra Fragment
                if ( iCuisineClickListener!= null) {
                    iCuisineClickListener.onClickItem(cuisine);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        if (mlist != null)
            return mlist.size();
        return 0;
    }

    public class CuisineViewHolder extends RecyclerView.ViewHolder{
        ImageView imv_cuisine;
        TextView tv_cuisine_name;

        public CuisineViewHolder(@NonNull View itemView) {
            super(itemView);
            imv_cuisine=itemView.findViewById(R.id.imv_cuisine_home_rcv_cuisine);
            tv_cuisine_name=itemView.findViewById(R.id.tv_cuisine_name_home_rcv_cuisine);
        }
    }
}
