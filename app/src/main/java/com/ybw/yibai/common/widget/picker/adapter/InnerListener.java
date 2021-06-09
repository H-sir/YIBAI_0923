package com.ybw.yibai.common.widget.picker.adapter;

import com.ybw.yibai.common.widget.picker.model.City;

public interface InnerListener {
    void dismiss(int position, City data);
    void locate();
}
