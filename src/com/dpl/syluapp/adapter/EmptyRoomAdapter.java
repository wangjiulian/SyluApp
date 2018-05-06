package com.dpl.syluapp.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dpl.syluapp.R;
import com.dpl.syluapp.application.MoApplication;
import com.dpl.syluapp.model.EmptyRoomInfo;
import com.dpl.syluapp.utils.StringUtil;

public class EmptyRoomAdapter extends BaseAdapter {
	private LayoutInflater mlInflater;
	private List<EmptyRoomInfo> roomInfo = new ArrayList<EmptyRoomInfo>();
	private Context context;

	public EmptyRoomAdapter(Context context) {
		this.context = context;

	}

	public void setList(List<EmptyRoomInfo> roomInfoo) {
		roomInfo.clear();
		roomInfo = roomInfoo;
		mlInflater = LayoutInflater.from(MoApplication.getInstance().context);
		Log.e("TAA","--->"+	roomInfoo.size());

	}

	@Override
	public int getCount() {
		if (roomInfo != null)
			return roomInfo.size();
		else
			return 0;
	}

	@Override
	public Object getItem(int position) {
		if (roomInfo != null)
			return roomInfo.get(position);
		else
			return position;
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {

		if (roomInfo != null) {
			view = mlInflater.inflate(R.layout.emptyroom_adapter, null);

			TextView tv_room1 = (TextView) view
					.findViewById(R.id.tv_emptyroom_r1);
			TextView tv_room_r2 = (TextView) view
					.findViewById(R.id.tv_emptyroom_r2);
			TextView romm3 = (TextView) view.findViewById(R.id.tv_emptyroom_r3);
			TextView romm4 = (TextView) view.findViewById(R.id.tv_emptyroom_r4);

				tv_room1.setText(roomInfo.get(position).getRoom1());
				tv_room_r2.setText(roomInfo.get(position).getRoom2());
				romm3.setText(roomInfo.get(position).getRoom3());
				romm4.setText(roomInfo.get(position).getRoom4());
			    
			return view;
		} else
			return null;
	}

}
