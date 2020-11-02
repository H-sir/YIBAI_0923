package com.ybw.yibai.common.widget.loading;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dyhdyh.widget.loading.factory.LoadingFactory;
import com.ybw.yibai.R;

public class CustomLoadingFactory implements LoadingFactory {
    @Override
    public View onCreateView(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_process_dialog_anim, parent, false);
        return view;
    }
}