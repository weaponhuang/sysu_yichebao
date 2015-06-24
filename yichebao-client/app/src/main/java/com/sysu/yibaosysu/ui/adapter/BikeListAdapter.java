package com.sysu.yibaosysu.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sysu.yibaosysu.R;
import com.sysu.yibaosysu.model.BikeInfo;
import com.sysu.yibaosysu.utils.StringUtils;

import java.util.List;

public class BikeListAdapter extends BaseAdapter {

	Context mContext;
	List<BikeInfo> mData;

	TextView mBikeIdTv;
	ImageView mBikeCover;
	TextView mBikeNameTv;
	TextView mBikeContentTv;
	TextView mAuthorTv;
	TextView mCreateTimeTv;

	public BikeListAdapter(Context context, List<BikeInfo> data) {
		this.mContext = context;
		this.mData = data;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.listitem_bikeinfo, null);
		}

		mBikeIdTv = (TextView) convertView.findViewById(R.id.list_bikeid);
		mBikeNameTv = (TextView) convertView.findViewById(R.id.list_bike_name);
		mAuthorTv = (TextView) convertView.findViewById(R.id.list_author_user);
		mBikeContentTv = (TextView) convertView
				.findViewById(R.id.list_bike_content);
		mCreateTimeTv = (TextView) convertView
				.findViewById(R.id.list_create_date);
		mBikeCover = (ImageView) convertView.findViewById(R.id.list_bikeimg);

		mBikeIdTv.setText(mData.get(position).getBikeId().toString());
		mBikeNameTv.setText(StringUtils.getWrappedBikeName(mData.get(position)
				.getBikeName()));
		mBikeContentTv.setText(StringUtils.getSimpleIntro(mData.get(position)
				.getContent()));
		mCreateTimeTv.setText(mData.get(position).getCreateTime());
		mAuthorTv.setText(mData.get(position).getAuthorName());
		mBikeCover.setImageResource(mData.get(position).getImage());

		return convertView;
	}

}
