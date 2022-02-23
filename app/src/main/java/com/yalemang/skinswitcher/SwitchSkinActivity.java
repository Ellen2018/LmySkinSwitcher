package com.yalemang.skinswitcher;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.yalemang.skinswitcher.fragment.FragmentThree;
import com.yalemang.skinswitcher.fragment.FragmentOne;
import com.yalemang.skinswitcher.fragment.FragmentTwo;
import com.yalemang.skinswitcher.utils.PermissionUtils;

import java.util.ArrayList;
import java.util.List;

public class SwitchSkinActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private String[] titles = {"界面一", "界面二","界面三"};
    private ViewPager2 viewPager2;
    private List<Fragment> fragmentList;
    private PermissionUtils permissionUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_skin);
        permissionUtils = new PermissionUtils(this);
        permissionUtils.startCheckFileReadWritePermission(0, new PermissionUtils.PermissionCallback() {
            @Override
            public void success() {

            }

            @Override
            public void failure() {

            }
        });
        initView();
    }

    private void initView() {
        tabLayout = findViewById(R.id.tab_layout);
        viewPager2 = findViewById(R.id.view_pager_2);

        fragmentList = new ArrayList<>();
        fragmentList.add(new FragmentOne());
        fragmentList.add(new FragmentTwo());
        fragmentList.add(new FragmentThree());

        viewPager2.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getItemCount() {
                return 3;
            }
        });
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(titles[position]);
            }
        });
        tabLayoutMediator.attach();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionUtils.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }
}
