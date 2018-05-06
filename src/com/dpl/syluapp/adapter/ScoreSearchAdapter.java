package com.dpl.syluapp.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dpl.syluapp.R;
import com.dpl.syluapp.model.ScoreSearchInfo;

public class ScoreSearchAdapter extends BaseAdapter {
	private ViewHolder holder;
	private List<ScoreSearchInfo> infos;
	private List<ScoreSearchInfo> faidinfos;
	private Context context;
	private LayoutInflater inflater;

	public ScoreSearchAdapter(List<ScoreSearchInfo> infos, Context context) {
		super();
		this.infos = infos;
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	public void setFaideInfo(List<ScoreSearchInfo> faidinfos) {
		this.faidinfos = faidinfos;
	}

	@Override
	public int getCount() {

		return infos.size();

	}

	@Override
	public Object getItem(int position) {
		return infos.size();
		// return infos.get(infos.size() - 1 - position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
			view = inflater.inflate(R.layout.scoresearch_item, null);
			holder = new ViewHolder();
			holder.name = (TextView) view.findViewById(R.id.tv_scoreadapter_name);
			holder.score = (TextView) view.findViewById(R.id.tv_scoreadapter_score);
			view.setTag(holder);
		}else {
			holder=(ViewHolder) view.getTag();
		}
		
		 filaterFaide(holder.name,holder.score,infos.get(infos.size() - 1 - position).getName());
		if (infos.get(infos.size() - 1 - position).getMakeupinfo() != null
				&& infos.get(infos.size() - 1 - position).getRebuildsocreFlag() != null) {
			if (infos.get(infos.size() - 1 - position).getMakeupinfo().length() == 2
					&& infos.get(infos.size() - 1 - position).getRebuildsocreFlag().length() != 2)

			{
				holder.name.setText(infos.get(infos.size() - 1 - position).getName() + " (补考)");
				holder.score.setText(infos.get(infos.size() - 1 - position).getMakeupinfo());

			}

			else if (infos.get(infos.size() - 1 - position).getRebuildsocreFlag().length() == 2) {
				holder.name.setText(infos.get(infos.size() - 1 - position).getName() + " (重修)");
				holder.score.setText(infos.get(infos.size() - 1 - position).getResult());
			}

			else {
				holder.name.setText(infos.get(infos.size() - 1 - position).getName());
				holder.score.setText(infos.get(infos.size() - 1 - position).getResult());
			}

		} else {
			holder.name.setText(infos.get(infos.size() - 1 - position).getName());
			holder.score.setText(infos.get(infos.size() - 1 - position).getResult());
		}

		return view;

	}

	@SuppressLint("ResourceAsColor")
	public void filaterFaide(TextView name, TextView score, String str) {

		boolean flag = false;
		if (!faidinfos.isEmpty() && faidinfos != null) {
			for (ScoreSearchInfo info : faidinfos) {
				if (str.equals(info.getName())) {
					flag = true;
				}
			}

			if (flag) {
				name.setTextColor(R.color.red);
				score.setTextColor(R.color.red);
			} else {
				name.setTextColor(Color.BLACK);
				score.setTextColor(Color.BLACK);
			}
		}

	}

	class ViewHolder {
		TextView name;
		TextView score;

	}
}
