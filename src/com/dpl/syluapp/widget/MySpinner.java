package com.dpl.syluapp.widget;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.dpl.syluapp.R;

public class MySpinner extends Spinner {
	public static int nowposi;
	public Context context;
	private Dialog dialog = null;
	private ArrayList<String> list;// ArrayList<String> list存储所要显示的数据
	private String Msg;

	public MySpinner(Context context) {
		super(context);
	}

	public MySpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = getContext();
		if (isInEditMode()) {
			return;
		}
		// 为MyDateSpinner设置adapter，主要用于显示spinner的text值
		MySpinner.this.setAdapter(new BaseAdapter() {

			@Override
			public int getCount() {
				return 1;
			}

			@Override
			public Object getItem(int arg0) {
				return null;
			}

			@Override
			public long getItemId(int arg0) {
				return 0;
			}

			@Override
			public View getView(int arg0, View arg1, ViewGroup arg2) {
				TextView text = new TextView(MySpinner.this.getContext());
				text.setText(list.get(arg0));
				/*text.setTextColor(Color.BLACK);
				text.setTextSize(18);*/
				return text;
			}
		});

	}

	@Override
	public boolean performClick() {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.spinner_listview, null);
		ListView listview = (ListView) view
				.findViewById(R.id.formcustomspinner);
		ListviewAdapter adapters = new ListviewAdapter(context, getList());
		listview.setAdapter(adapters);
		listview.setOnItemClickListener(onItemClick);
		dialog = new Dialog(context, R.style.dialog);// 创建Dialog并设置样式主题
		LayoutParams params = new LayoutParams(400, LayoutParams.WRAP_CONTENT);
		dialog.setCanceledOnTouchOutside(true);// 设置点击Dialog外部任意区域关闭Dialog
		dialog.show();
		dialog.addContentView(view, params);
		return true;
	}

	OnItemClickListener onItemClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			smesterListening.setSmester(position);
			//setSmesterListening(smesterListening);
			nowposi = position;
			setSelection(position);
			setMsg(list.get(position));
			if (dialog != null) {
				dialog.dismiss();
				dialog = null;
			}
		}
	};

	public ArrayList<String> getList() {
		return list;
	}

	public void setList(ArrayList<String> list) {
		this.list = list;
	}

	public String getMsg() {
		return Msg;
	}

	public void setMsg(String Msg) {
		this.Msg = Msg;
	}

	class ListviewAdapter extends BaseAdapter {
		private Context context;
		private ArrayList<String> Times;

		public ListviewAdapter(Context context, ArrayList<String> Times) {
			this.context = context;
			this.Times = Times;
		}

		@Override
		public int getCount() {
			return Times.size();
		}

		@Override
		public Object getItem(int arg0) {
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			ViewHolder viewHolder = null;
			if (arg1 == null && list.size() != 0) {
				viewHolder = new ViewHolder();
				LayoutInflater inflater = LayoutInflater.from(context);
				arg1 = inflater.inflate(R.layout.spinner_item, null);
				viewHolder.SysTimes = (TextView) arg1
						.findViewById(R.id.itemText);
				arg1.setTag(viewHolder);
			} else
				viewHolder = (ViewHolder) arg1.getTag();
			viewHolder.SysTimes.setText(Times.get(arg0));
			return arg1;
		}

		class ViewHolder {
			public TextView SysTimes;
		}

	}

	public interface SmesterListening {
		void setSmester(int position);

	}

	SmesterListening smesterListening = null;

	public void setSmesterListening(SmesterListening e) {
		smesterListening = e;
	}
}
