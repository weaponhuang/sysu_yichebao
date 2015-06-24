package com.sysu.yibaosysu.model;

public class DrawerItem {

	private int icon;
	private CharSequence title;

	public DrawerItem(int icon, CharSequence title) {
		this.icon = icon;
		this.title = title;
	}

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public CharSequence getTitle() {
		return title;
	}

	public void setTitle(CharSequence title) {
		this.title = title;
	}
}
