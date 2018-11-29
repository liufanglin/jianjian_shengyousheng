package com.ximai.savingsmore.save.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ximai.savingsmore.R;

/**
 * Created by caojian on 16/11/22.
 */
//商家侧栏
public class MerchentFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.merchent_fragment,null);
        return view;
    }
}
