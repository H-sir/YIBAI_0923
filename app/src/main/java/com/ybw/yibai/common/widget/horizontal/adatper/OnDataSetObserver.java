package com.ybw.yibai.common.widget.horizontal.adatper;

import android.database.DataSetObserver;

import java.util.Collection;


public abstract class OnDataSetObserver extends DataSetObserver
{
	public void onDataAdded(int position, AbsAdapterItem item)
	{
	}

	public void onDataAdded(int position, Collection<? extends AbsAdapterItem> itemCollection)
	{
	}

	public void onDataRemoved(int position)
	{
	}

	public void onDataCleared()
	{
	}
}
