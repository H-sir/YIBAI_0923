package com.ybw.yibai.common.widget.picker.adapter;

import com.ybw.yibai.common.widget.picker.model.City;

public interface OnPickListener {
    void onPick(int position, City data);
    void onLocate();
    void onCancel();
}
