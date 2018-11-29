package com.ximai.savingsmore.save.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ximai.savingsmore.R;

/**
 * Created by luck on 2017/12/15 0015.
 */

public class SearchChooseGoodsFragment extends Fragment {
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_choosegoods, null);

        initView();

        initData();

        return view;
    }


    /**
     * init - view
     */
    private void initView() {
    }

    /**
     * init - data
     */
    private void initData() {
    }
}
