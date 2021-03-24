package com.ybw.yibai.module.change;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.ybw.yibai.R;

import java.util.List;

/**
 * <pre>
 *     author : HKR
 *     time   : 2021/03/24
 *     desc   :
 * </pre>
 */
public class PoiAdapter extends BaseAdapter {
    private Context context;
    private List<PoiInfo> pois;
    private LinearLayout linearLayout;

    PoiAdapter(Context context, List<PoiInfo> pois) {
        this.context = context;
        this.pois = pois;
    }

    @Override
    public int getCount() {
        return pois.size();
    }

    @Override
    public Object getItem(int position) {
        return pois.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.locationpois_item, null);
            linearLayout = (LinearLayout) convertView.findViewById(R.id.locationpois_linearlayout);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == 0) {
            holder.iv_gps.setImageDrawable(context.getResources().getDrawable(R.mipmap.gps_orange));
            holder.locationpoi_name.setTextColor(Color.parseColor("#FF9D06"));
            holder.locationpoi_address.setTextColor(Color.parseColor("#FF9D06"));
        } else {
            holder.iv_gps.setImageDrawable(context.getResources().getDrawable(R.mipmap.gps_grey));
            holder.locationpoi_name.setTextColor(Color.parseColor("#4A4A4A"));
            holder.locationpoi_address.setTextColor(Color.parseColor("#7b7b7b"));
        }
        PoiInfo poiInfo = pois.get(position);
        holder.locationpoi_name.setText(poiInfo.name);
        holder.locationpoi_address.setText(poiInfo.address);
        return convertView;
    }

    class ViewHolder {
        ImageView iv_gps;
        TextView locationpoi_name;
        TextView locationpoi_address;


        ViewHolder(View view) {
            locationpoi_name = (TextView) view.findViewById(R.id.locationpois_name);
            locationpoi_address = (TextView) view.findViewById(R.id.locationpois_address);
            iv_gps = (ImageView) view.findViewById(R.id.iv_gps);
        }
    }
}