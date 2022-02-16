package com.yalemang.skinswitcher.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yalemang.skinswitcher.R;
import com.yalemang.skinswitcherlibraray.LmySkin;
import com.yalemang.skinswitcherlibraray.LmySkinManager;

import java.util.List;

public class SkinManagerAdapter extends RecyclerView.Adapter<SkinManagerAdapter.SkinManagerViewHolder> {

    private List<LmySkin> lmySkinList;
    private ItemClick itemClick;

    public SkinManagerAdapter(List<LmySkin> lmySkinList) {
        this.lmySkinList = lmySkinList;
    }

    public void setItemClick(ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    @NonNull
    @Override
    public SkinManagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_skin_manager, parent, false);
        return new SkinManagerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SkinManagerViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String skinName = lmySkinList.get(position).getName();
        holder.tvSkinName.setText(skinName);
        holder.itemView.setOnClickListener(v -> {
            if (itemClick != null) {
                itemClick.click(position);
            }
        });
        //设置选中
        if(LmySkinManager.getInstance().isSelectSkin(lmySkinList.get(position))){
            //选中
            holder.ivSelect.setVisibility(View.VISIBLE);
        }else {
            //未选中
            holder.ivSelect.setVisibility(View.GONE);
        }
        int selectColor = -1;
        if(position == 0){
            holder.tvSkinName.setTextColor(selectColor = Color.parseColor("#FFA500"));
        }else if(position == 1){
            holder.tvSkinName.setTextColor(selectColor = Color.BLUE);
        }else if(position == 2){
            holder.tvSkinName.setTextColor(selectColor = Color.BLACK);
        }else if(position == 3){
            holder.tvSkinName.setTextColor(selectColor = Color.GREEN);
        }else if(position == 4){
            holder.tvSkinName.setTextColor(selectColor = Color.RED);
        }
        holder.ivSelect.setBackgroundColor(selectColor);
    }

    @Override
    public int getItemCount() {
        return lmySkinList.size();
    }

    static class SkinManagerViewHolder extends RecyclerView.ViewHolder {

        TextView tvSkinName;
        ImageView ivSelect;

        public SkinManagerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSkinName = itemView.findViewById(R.id.tv_skin_name);
            ivSelect = itemView.findViewById(R.id.iv_select);
        }
    }

    public interface ItemClick {
        void click(int position);
    }
}
