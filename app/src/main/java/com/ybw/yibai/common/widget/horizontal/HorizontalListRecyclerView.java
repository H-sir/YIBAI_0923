package com.ybw.yibai.common.widget.horizontal;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

/**
 * <pre>
 *     author : HKR
 *     time   : 2020/08/31
 *     desc   :
 * </pre>
 */
public class HorizontalListRecyclerView extends ListRecyclerView
{
    public HorizontalListRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public HorizontalListRecyclerView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public HorizontalListRecyclerView(Context context)
    {
        super(context);
        init();
    }

    private void init()
    {
        setOrientation(LinearLayoutManager.HORIZONTAL);
    }
}
