package com.sologram.common;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import static android.view.View.TEXT_ALIGNMENT_TEXT_START;

public class PopupMenu extends PopupWindow implements AdapterView.OnItemClickListener,
		PopupWindow.OnDismissListener {
	static private final String TAG = PopupMenu.class.getSimpleName();

	private Adapter adapter = new Adapter();
	private Listener listener;
	private ListView layout;

	public PopupMenu(Context context, String initText) {
		float[] r = new float[]{2, 2, 2, 2, 2, 2, 2, 2};
		ShapeDrawable bg = new ShapeDrawable();
		bg.setPadding(0, 0, 0, 0);
		bg.setShape(new RoundRectShape(r, null, null));
		bg.getPaint().setStyle(Paint.Style.FILL);
		bg.getPaint().setColor(0xFFBBBBBB);
		ShapeDrawable fg = new ShapeDrawable();
		fg.setPadding(0, 8, 0, 8);
		fg.setShape(new RoundRectShape(r, null, null));
		fg.getPaint().setStyle(Paint.Style.FILL);
		fg.getPaint().setColor(0xFFFFFFFF);
		Drawable[] layers = {bg, fg};
		LayerDrawable da = new LayerDrawable(layers);
		da.setLayerInset(1, 0, 0, 1, 1);

		layout = new ListView(context);
		layout.setBackground(da);
		layout.setAdapter(adapter);
		layout.setOnItemClickListener(this);
		adapter.addItem(null, initText);

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
		adapter.removeItem(null);
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
		dismiss();
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
