package com.sologram.common;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sologram.bletest.R;

import static android.view.View.TEXT_ALIGNMENT_TEXT_START;

public class PopupMenu extends PopupWindow implements AdapterView.OnItemClickListener,
		PopupWindow.OnDismissListener {
	static private final String TAG = PopupMenu.class.getSimpleName();

	private Adapter adapter = new Adapter();
	private Listener listener;
	private ListView layout;

	public PopupMenu(Context context) {
		layout = new ListView(context);
		layout.setBackground(context.getResources().getDrawable(R.drawable.values));
		layout.setAdapter(adapter);
		layout.setOnItemClickListener(this);

		setContentView(layout);
		setWidth(240);
		setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
		setBackgroundDrawable(new ColorDrawable());
		setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
		setTouchable(true);
		setOnDismissListener(this);
		setOutsideTouchable(true);
		setFocusable(true);
	}

	public void addItem(Object key, String text) {
		adapter.addItem(key, text);
	}

	@Override
	public void onDismiss() {
		listener.onDismiss();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		adapter.getItem(position);
		listener.onItemClick(adapter.getKey(position), adapter.getText(position));
		listener.onDismiss();
		dismiss();
	}

	public void removeItem(Object key) {
		adapter.removeItem(key);
	}

	public void setListener(Listener listener) {
		this.listener = listener;
	}

	public void show(View v) {
		update();
		showAsDropDown(v, 50, 0);
	}

	class Adapter extends BaseAdapter {
		private MapList list = new MapList();

		void addItem(Object key, String text) {
			if (list.add(key, text)) {
				Log.w(TAG, "addItem: " + key + ", " + text);
				notifyDataSetChanged();
			}
		}

		@Override
		public int getCount() {
			return list.size();
		}

		public Object getKey(int position) {
			return getItem(position);
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		String getText(int position) {
			return (String) list.getValue(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView re = new TextView(layout.getContext());
			re.setGravity(1);
			re.setPadding(20, 8, 20, 8);
			re.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
			re.setTextSize(16);
			re.setText((CharSequence) list.getValue(position));
			return re;
		}

		void removeItem(Object key) {
			if (list.remove(key))
				notifyDataSetChanged();
		}
	}

	public interface Listener {
		void onItemClick(Object key, String text);

		void onDismiss();
	}
}
