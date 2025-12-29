package com.example.foodnote_n22dcpt103_n22dcpt104.adapter.list_fragment_adapter;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.foodnote_n22dcpt103_n22dcpt104.R;
import com.example.foodnote_n22dcpt103_n22dcpt104.database.entities.Shopping_list;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ViewHolder> {

    private List<Shopping_list> list;
    private IClickListListener listener;

    public interface IClickListListener {
        void onClickItem(Shopping_list item);
    }

    public void setData(List<Shopping_list> list, IClickListListener listener) {
        this.list = list;
        this.listener = listener;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_list_item_shopping_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Shopping_list item = list.get(position);
        if (item == null) return;

        holder.tvName.setText(item.getName());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        holder.tvDate.setText(sdf.format(item.getCreatedAt()));

        if (item.isStatus()) {
            holder.tvStatus.setText("Hoàn thành");
            holder.tvStatus.setTextColor(Color.parseColor("#4CAF50")); // Xanh lá
            holder.tvStatus.setBackgroundColor(Color.parseColor("#E8F5E9"));
        } else {
            holder.tvStatus.setText("Đang mua");
            holder.tvStatus.setTextColor(Color.parseColor("#E23E3E")); // Đỏ
            holder.tvStatus.setBackgroundColor(Color.parseColor("#FFF0F0"));
        }

        holder.itemView.setOnClickListener(v -> listener.onClickItem(item));
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDate, tvStatus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_list_name);
            tvDate = itemView.findViewById(R.id.tv_created_date);
            tvStatus = itemView.findViewById(R.id.tv_list_status);
        }
    }
}
