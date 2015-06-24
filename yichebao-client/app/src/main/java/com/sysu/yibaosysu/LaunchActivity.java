package com.sysu.yibaosysu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.sysu.yibaosysu.ui.fragment.LaunchFragment;
import com.sysu.yibaosysu.utils.PreferencesUtils;

public class LaunchActivity extends Activity {

	@Override
	protected void onStart() {
		super.onStart();

		if (PreferencesUtils.getUserId(getApplicationContext()) != -1) {
			startActivity(new Intent(LaunchActivity.this, MainActivity.class));
			this.finish();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_launch);
		getFragmentManager().beginTransaction()
				.replace(R.id.launch_container, new LaunchFragment()).commit();
	}
}
