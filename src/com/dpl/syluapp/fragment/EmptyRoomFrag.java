package com.dpl.syluapp.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dpl.syluapp.R;
import com.dpl.syluapp.adapter.EmptyRoomAdapter;
import com.dpl.syluapp.config.NetConfig;
import com.dpl.syluapp.db.EmptyRoomDb;
import com.dpl.syluapp.model.EmptyRoomInfo;
import com.dpl.syluapp.net.EmptyRoomNet1;
import com.dpl.syluapp.net.EmptyRoomNet1.EmptyRoomCallback;
import com.dpl.syluapp.widget.CustomToast;
import com.dpl.syluapp.widget.EmptyRoomListView;

/**
 * 空教室查询
 * 
 * @author JUST玖
 * 
 *         2015-4-25
 */
public class EmptyRoomFrag extends Fragment {
	private TextView tv_emptyroom_promt;

	private LinearLayout llyt_emptyroom_detail;
	private EmptyRoomDb roomDb;
	private List<HashMap<String, String>> listString;
	public static final String TITLE = "title";
	public static final String TABLE = "table";
	private String DefaultTitle = "title";
	private String DefaulTable = "table";

	private EmptyRoomAdapter adapter1;
	private EmptyRoomAdapter adapter2;
	private EmptyRoomAdapter adapter3;
	private EmptyRoomAdapter adapter4;
	private TextView freagment_room_view1;
	private TextView freagment_room_view2;
	private TextView freagment_room_view3;
	private TextView freagment_room_view4;

	private EmptyRoomListView freagment_room_listview1;
	private EmptyRoomListView freagment_room_listview2;
	private EmptyRoomListView freagment_room_listview3;
	private EmptyRoomListView freagment_room_listview4;
	private EmptyRoomNet1 net1 = new EmptyRoomNet1();
	private List<EmptyRoomInfo> listY = new ArrayList<EmptyRoomInfo>();
	private List<EmptyRoomInfo> listJ = new ArrayList<EmptyRoomInfo>();
	private List<EmptyRoomInfo> listX = new ArrayList<EmptyRoomInfo>();
	private List<EmptyRoomInfo> listS = new ArrayList<EmptyRoomInfo>();
	private List<String> listitemy = new ArrayList<String>();
	private List<String> listitemj = new ArrayList<String>();
	private List<String> listitemx = new ArrayList<String>();
	private List<String> listitems = new ArrayList<String>();
	private EmptyRoomInfo yInfo;
	private EmptyRoomInfo jInfo;
	private EmptyRoomInfo xInfo;
	private EmptyRoomInfo sInfo;
	private int yFlag = 0;
	private int jFlag = 0;
	private int xFlag = 0;
	private int sFlag = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			DefaultTitle = getArguments().getString(TITLE);
			DefaulTable = getArguments().getString(TABLE);

		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		listString = null;
		roomDb = new EmptyRoomDb(getActivity(), DefaulTable);
		listString = roomDb.query(null);
		View view = inflater.inflate(R.layout.fragment_emptyroom, null);

		initView(view);
		if (listString.size() == 0) {
			connectNet();
			Log.e("TAB", "tableNAme--->" + DefaulTable + "------>ListSize==0");
		}

		else {
			Log.e("TAB", "--->" + DefaulTable + "---" + listString.toString());
			bindData();
		}

		return view;
	}

	private void connectNet() {

		net1.downRoom(null, DefaultTitle, DefaulTable, new EmptyRoomCallback() {

			@Override
			public void onSuccess(List<EmptyRoomInfo> roomInfos) {
				
				tv_emptyroom_promt.setVisibility(View.GONE);
				llyt_emptyroom_detail.setVisibility(View.VISIBLE);

				roomDb = new EmptyRoomDb(getActivity(), DefaulTable);
				listString = roomDb.query(null);
				Log.e("TAB",
						"---Connect>" + DefaulTable + "---"
								+ listString.toString());

				bindData();

			}

			@Override
			public void onFailed(int netCode) {
				
				switch (netCode) {
				case NetConfig.SERVER_NO_RESPONES:
					tv_emptyroom_promt.setText("服务器无响应");
					
					break;
				case NetConfig.NO_DATA:
					tv_emptyroom_promt.setText("暂无数据");
					
					break;
				case NetConfig.FAILED:
					tv_emptyroom_promt.setText("连接异常");
					
					break;
				case NetConfig.CONNECT_TIME_OUT:
					tv_emptyroom_promt.setText("连接超时");
					
					break;
				case NetConfig.REQUEST_TIME_OUT:
					tv_emptyroom_promt.setText("请求超时，请检查网络");
					
					break;
					

			
				}
				

			}
		});
	}

	private void initData() {
		adapter1 = new EmptyRoomAdapter(getActivity());
		adapter1.setList(listY);
		freagment_room_listview1.setAdapter(adapter1);

		adapter2 = new EmptyRoomAdapter(getActivity());
		adapter2.setList(listX);
		freagment_room_listview2.setAdapter(adapter2);

		adapter3 = new EmptyRoomAdapter(getActivity());
		adapter3.setList(listJ);
		freagment_room_listview3.setAdapter(adapter3);

		adapter4 = new EmptyRoomAdapter(getActivity());
		adapter4.setList(listS);
		freagment_room_listview4.setAdapter(adapter4);

	}

	private void initView(View view) {
		tv_emptyroom_promt = (TextView) view
				.findViewById(R.id.tv_emptyroom_promt);
		llyt_emptyroom_detail = (LinearLayout) view
				.findViewById(R.id.llyt_emptyroom_detail);
		freagment_room_view1 = (TextView) view
				.findViewById(R.id.freagment_room_view1);
		freagment_room_view2 = (TextView) view
				.findViewById(R.id.freagment_room_view2);
		freagment_room_view3 = (TextView) view
				.findViewById(R.id.freagment_room_view3);
		freagment_room_view4 = (TextView) view
				.findViewById(R.id.freagment_room_view4);
		freagment_room_listview1 = (EmptyRoomListView) view
				.findViewById(R.id.freagment_room_listview1);
		freagment_room_listview2 = (EmptyRoomListView) view
				.findViewById(R.id.freagment_room_listview2);
		freagment_room_listview3 = (EmptyRoomListView) view
				.findViewById(R.id.freagment_room_listview3);
		freagment_room_listview4 = (EmptyRoomListView) view
				.findViewById(R.id.freagment_room_listview4);
	}

	public void addToListInfo(List<String> list, List<EmptyRoomInfo> infos) {
		infos.clear();
		int flag = 0;
		EmptyRoomInfo info = null;
		for (int i = 0; i < list.size(); i++) {
			if (flag == 0)
				info = new EmptyRoomInfo();
			switch (flag++) {
			case 0:

				info.setRoom1(list.get(i).toString());
				if (i + 1 == list.size())
					infos.add(info);

				break;
			case 1:

				info.setRoom2(list.get(i).toString());
				if (i + 1 == list.size())
					infos.add(info);
				break;
			case 2:

				info.setRoom3(list.get(i).toString());

				if (i + 1 == list.size())
					infos.add(info);
			case 3:

				info.setRoom4(list.get(i).toString());
				infos.add(info);

				flag = 0;

				break;

				

			}

			// for (EmptyRoomInfo in : infos) {
			// Log.i("TAB", infos.toString());
			// }

		}
	}

	@Override
	public void onResume() {

		super.onResume();
	}

	private void bindData() {
		tv_emptyroom_promt.setVisibility(View.GONE);
		llyt_emptyroom_detail.setVisibility(View.VISIBLE);
		listitemy.clear();
		listitemj.clear();
		listitems.clear();
		listitemx.clear();
		// // listString = roomDb.query(null);
		Map<String, String> map;
		if (listString != null) {
			for (int i = 0; i < listString.size(); i++) {

				map = listString.get(i);
				if (map != null) {
					if (map.get("room1") != null
							&& !"".equals(map.get("room1"))) {
						switch (map.get("room1").charAt(0)) {
						case 'Y':
							listitemy.add(map.get("room1"));
							break;
						case 'J':
							listitemj.add(map.get("room1"));
							break;
						case 'S':
							listitems.add(map.get("room1"));
							break;
						case 'X':
							listitemx.add(map.get("room1"));
							break;

						}

					}
					if (map.get("room2") != null
							&& !"".equals(map.get("room2"))) {
						switch (map.get("room2").charAt(0)) {
						case 'Y':
							listitemy.add(map.get("room2"));
							break;
						case 'J':
							listitemj.add(map.get("room2"));
							break;
						case 'S':
							listitems.add(map.get("room2"));
							break;
						case 'X':
							listitemx.add(map.get("room2"));
							break;

						}
					}
					if (map.get("room3") != null
							&& !"".equals(map.get("room3"))) {
						switch (map.get("room3").charAt(0)) {
						case 'Y':
							listitemy.add(map.get("room3"));
							break;
						case 'J':
							listitemj.add(map.get("room3"));
							break;
						case 'S':
							listitems.add(map.get("room3"));
							break;
						case 'X':
							listitemx.add(map.get("room3"));
							break;

						}
					}
						if (map.get("room4") != null
								&& !"".equals(map.get("room4"))) {
							switch (map.get("room4").charAt(0)) {
							case 'Y':
								listitemy.add(map.get("room4"));
								break;
							case 'J':
								listitemj.add(map.get("room4"));
								break;
							case 'S':
								listitems.add(map.get("room4"));
								break;
							case 'X':
								listitemx.add(map.get("room4"));
								break;

							}
						}

				}

			}
			addToListInfo(listitemj, listJ);

			addToListInfo(listitemx, listX);

			addToListInfo(listitemy, listY);

			addToListInfo(listitems, listS);

			//
			initData();
		}
	}

}
