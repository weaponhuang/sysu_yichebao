package com.sysu.yibaosysu.utils;

import android.widget.EditText;

import java.util.List;

public class StringUtils {

	public static boolean isEmpty(final EditText et) {
		String content = et.getText().toString();
		return content.isEmpty();
	}

	public static String parseLabelList(List<String> labelList) {

		StringBuilder sb = new StringBuilder();
		if (labelList != null) {
			sb.append("| ");
			for (String label : labelList) {
				sb.append(label).append(" | ");
			}
		}

		return sb.toString();
	}

	public static String getSimpleIntro(String content) {
		if (content.length() > 30) {
			content = content.substring(0, 30);
			content += "...";
		}
		return "简介：" + content;
	}

	public static String[] parseToStringArray(String labels) {
		return labels.split(" ");
	}

	public static String getWrappedBikeName(String bikeName) {
		if (bikeName.length() > 10) {
			bikeName = bikeName.substring(0, 10);
			bikeName = "品牌：" + bikeName + "...";
		} else {
			bikeName = "品牌：" + bikeName;
		}
		return bikeName;
	}

}
