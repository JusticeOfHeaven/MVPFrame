package com.my.mvpframe.customview.qq_message;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.my.mvpframe.R;

public class QQMessage1Activity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qq_message);
		CoverManager.getInstance().init(this);

		ListView mList = (ListView) findViewById(R.id.list);
		mList.setAdapter(new DemoAdapter());

		CoverManager.getInstance().setMaxDragDistance(250);
		CoverManager.getInstance().setEffectDuration(150);

	}

	class DemoAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return 99;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(QQMessage1Activity.this).inflate(
						R.layout.view_list_item, null);
			}
			WaterDrop drop = (WaterDrop) convertView.findViewById(R.id.drop);
			drop.setText(String.valueOf(position));

			drop.setEffectResource(R.drawable.explosion_heart);
			drop.setOnDragCompeteListener(new CoverManager.OnDragCompeteListener() {

				@Override
				public void onDragComplete() {
					Toast.makeText(QQMessage1Activity.this, "remove:" + position,
							Toast.LENGTH_SHORT).show();
				}
			});

			return convertView;
		}
	}
}
