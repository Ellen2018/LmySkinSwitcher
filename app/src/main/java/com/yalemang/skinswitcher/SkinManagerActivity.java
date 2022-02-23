package com.yalemang.skinswitcher;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yalemang.skinswitcher.adapter.SkinManagerAdapter;
import com.yalemang.skinswitcher.utils.FileUtils;
import com.yalemang.skinswitcher.utils.SharePreferenceHelper;
import com.yalemang.skinswitcherlibraray.LmySkin;
import com.yalemang.skinswitcherlibraray.LmySkinManager;
import com.yalemang.skinswitcherlibraray.skinnterface.LmySkinSwitchListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SkinManagerActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView tvSkinColor;
    private List<LmySkin> skinData;
    private SkinManagerAdapter skinManagerAdapter;
    private LmySkinSwitchListener lmySkinSwitchListener = new LmySkinSwitchListener() {
        @Override
        public void switchSkin(LmySkin newSkin) {
            //保存当前的皮肤数据，下次应用启动需使用该皮肤
            SharePreferenceHelper.getInstance().saveSwitchSkin(newSkin);
            skinManagerAdapter.notifyDataSetChanged();
            refreshBottom();
            Toast.makeText(SkinManagerActivity.this,"皮肤:<"+newSkin.getName()+">切换完成",Toast.LENGTH_SHORT).show();
        }
    };

    private void refreshBottom(){
        int color = LmySkinManager.getInstance().getResources().getColor(R.color.main_color);
        tvSkinColor.setBackgroundColor(color);
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
            LmySkin lmySkin = skinData.get(position);
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
        skinData = new ArrayList<>();
        skinData.add(LmySkinManager.getInstance().getDefaultSkin());
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
            skinData.add(lmySkin);
        }
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
