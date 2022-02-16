package com.yalemang.skinswitcher.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.yalemang.skinswitcher.R;
import com.yalemang.skinswitcher.SkinManagerActivity;

public class FragmentOne extends Fragment {

    private Button btSwitchSkin;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one,container,false);
        btSwitchSkin = view.findViewById(R.id.bt_switch_skin);
        btSwitchSkin.setOnClickListener(v -> {
            //跳到到切换皮肤界面
            Intent intent = new Intent(getContext(), SkinManagerActivity.class);
            getActivity().startActivity(intent);
        });
        return view;
    }
}
