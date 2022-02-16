package com.yalemang.skinswitcher;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yalemang.skinswitcher.adapter.SkinManagerAdapter;
import com.yalemang.skinswitcher.utils.FileUtils;
import com.yalemang.skinswitcherlibraray.LmySkin;
import com.yalemang.skinswitcherlibraray.LmySkinManager;
import com.yalemang.skinswitcherlibraray.skinnterface.LmySkinSwitchListener;

import java.io.File;
import java.util.List;

public class SkinManagerActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView tvSkinColor;
    private List<LmySkin> skinData;
    private SkinManagerAdapter skinManagerAdapter;
    private LmySkinSwitchListener lmySkinSwitchListener = new LmySkinSwitchListener() {
        @Override
        public void switchSkin(LmySkin newSkin) {
            skinManagerAdapter.notifyDataSetChanged();
            refreshBottom();
        }
    };

    private void refreshBottom(){
        LmySkin lmySkin = LmySkinManager.getInstance().getCurrentSkin();
        if(lmySkin.getName().equals("skin_blue.apk")){
            tvSkinColor.setBackgroundColor(Color.BLUE);
        }else if(lmySkin.getName().equals("skin_red.apk")){
            tvSkinColor.setBackgroundColor(Color.RED);
        }else if(lmySkin.getName().equals("skin_black.apk")){
            tvSkinColor.setBackgroundColor(Color.BLACK);
        }else if(lmySkin.getName().equals("skin_green.apk")){
            tvSkinColor.setBackgroundColor(Color.GREEN);
        }else {
            tvSkinColor.setBackgroundColor(Color.parseColor("#FFA500"));
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin_manager);
        initView();
        initData();
        LmySkinManager.getInstance().addLmySkinSwitchListener(lmySkinSwitchListener);
        refreshBottom();
    }

    private void initData() {
        //载入皮肤数据
        loadSkinData();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        skinManagerAdapter = new SkinManagerAdapter(skinData);
        skinManagerAdapter.setItemClick(position -> {
            LmySkin lmySkin = LmySkinManager.getInstance().getSkinData().get(position);
            if(!new File(lmySkin.getPath()).exists()){
                //不存在，先从服务器进行下载，这里模拟只从assets目录复制到本地目录
                FileUtils.copyFileFromAssets(SkinManagerActivity.this, lmySkin.getName(),
                        SkinManagerActivity.this.getCacheDir().getAbsolutePath(), lmySkin.getName());
            }
            //切换皮肤
            LmySkinManager.getInstance().switchSkin(lmySkin);
        });
        recyclerView.setAdapter(skinManagerAdapter);
    }

    private void loadSkinData() {
        for (int i = 0; i < 4; i++) {
            String fileName = "";
            if (i == 0) {
                fileName = "skin_blue.apk";
            } else if (i == 1) {
                fileName = "skin_black.apk";
            } else if (i == 2) {
                fileName = "skin_green.apk";
            } else {
                fileName = "skin_red.apk";
            }
            File file = new File(this.getCacheDir(), fileName);
            LmySkin lmySkin = new LmySkin(file.getAbsolutePath());
            LmySkinManager.getInstance().addLmySkin(lmySkin);
        }
        skinData = LmySkinManager.getInstance().getSkinData();
    }

    private void initView() {
        recyclerView = findViewById(R.id.recycler_view);
        tvSkinColor = findViewById(R.id.tv_skin_color);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LmySkinManager.getInstance().removeLmySkinSwitchListener(lmySkinSwitchListener);
    }
}
