package com.sysu.yibaosysu.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.sysu.yibaosysu.R;

/**
 * 单车信息的实体类
 * 
 * @author kuxinwei
 */
public class BikeInfo {

	public static final String BIKE_ID = "bikeId";
	public static final String BIKE_NAME = "bikeName";
	public static final String BIKE_CONTENT = "content";
	public static final String CREATE_TIME = "time";
	public static final String AUTHOR_NAME = "authorName";

	public static final int DEFAULT_ICON = R.drawable.default_bike_cover;
	public static final int[] DEFAULT_ICON_ARRAY = new int[] {
			R.drawable.default_bike_cover, R.drawable.default_bike_cover_1,
			R.drawable.default_bike_cover_2, R.drawable.default_bike_cover_3 };
	public static final String BIKE_IMAGE = "image";

	private Integer bikeId;
	private String bikePicPath;
	private String createTime;
	private String content;
	private String bikename;
	private String authorName;
	private int image;

	@Override
	public boolean equals(Object o) {
		if (o instanceof BikeInfo) {
			return this.bikeId.intValue() == ((BikeInfo) o).bikeId.intValue();
		}
		return false;
	}
	public BikeInfo() {
		image = BikeInfo.getBikeCoverIcon();
	}

	public static List<BikeInfo> parseList(List<Map<String, Object>> mData) {
		List<BikeInfo> bikeList = new ArrayList<BikeInfo>();
		for (Map<String, Object> data : mData) {
			BikeInfo temp = new BikeInfo();
			temp.bikeId = (Integer) data.get(BIKE_ID);
			temp.bikename = (String) data.get(BIKE_NAME);
			temp.content = (String) data.get(BIKE_CONTENT);
			temp.createTime = (String) data.get(CREATE_TIME);
			temp.authorName = (String) data.get(AUTHOR_NAME);
			bikeList.add(temp);
		}
		return bikeList;
	}

	public Integer getBikeId() {
		return bikeId;
	}

	public String getBikePicPath() {
		return bikePicPath;
	}

	public String getCreateTime() {
		return createTime;
	}

	public String getContent() {
		return content;
	}

	public String getBikeName() {
		return bikename;
	}

	public String getAuthorName() {
		return authorName;
	}

	public static int getBikeCoverIcon() {
		Random random = new Random();
		int i = Math.abs(random.nextInt()) % DEFAULT_ICON_ARRAY.length;
		return DEFAULT_ICON_ARRAY[i];
	}

	public int getImage() {
		return image;
	}

	public void setImage(int image) {
		this.image = image;
	}
}
