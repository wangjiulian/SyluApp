package com.dpl.syluapp.adapter;

import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dpl.syluapp.R;
import com.dpl.syluapp.adapter.LibrarySelectAdapter.renew;
import com.dpl.syluapp.application.MoApplication;
import com.dpl.syluapp.model.LibrarySelectInfo;
import com.dpl.syluapp.net.LibraryRenew;
import com.dpl.syluapp.net.LibraryRenew.LibraryCallBack;
import com.dpl.syluapp.utils.DateFormat;
import com.dpl.syluapp.widget.CustomToast;

public class LibraryItemAdapter extends BaseAdapter {
	private Context mContext;
	private ProgressDialog progress;
	private ViewHolder mViewHolder;
	private LayoutInflater mInflater;
	private List<LibrarySelectInfo> mInfos;

	public LibraryItemAdapter(Context context, List<LibrarySelectInfo> infos) {
		mInflater = LayoutInflater.from(context);
		this.mInfos = infos;
		mContext = context;

	}

	@Override
	public int getCount() {
		return mInfos.size();
	}

	@Override
	public Object getItem(int position) {
		return mInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
			view = mInflater.inflate(R.layout.library_detail_item, null);
			mViewHolder = new ViewHolder();
			mViewHolder.bName = (TextView) view
					.findViewById(R.id.tv_libraryitem_bName);
			mViewHolder.author = (TextView) view
					.findViewById(R.id.tv_libraryitem_author);
			mViewHolder.dayleft = (TextView) view
					.findViewById(R.id.tv_libraryitem_dayleft);
			mViewHolder.renew = (Button) view
					.findViewById(R.id.btn_libraryitem_renew);
			mViewHolder.exceed = (TextView) view
					.findViewById(R.id.btn_libraryitem_exceed);
			
			view.setTag(mViewHolder);

		} else
			mViewHolder = (ViewHolder) view.getTag();

		mViewHolder.bName.setText(mInfos.get(position).getbName());
		mViewHolder.author.setText(mInfos.get(position).getbAuthor());
		DateFormat con = new DateFormat();
		String returnDay = mInfos.get(position).getbReturnDay();
		final String Bbar_code = mInfos.get(position).getBbar_code();
		final String code = mInfos.get(position).getCode();
		String bRenew = mInfos.get(position).getbRenew();
		int flag = con.conTrast(returnDay);
         
		mViewHolder.dayleft.setText(String.valueOf(flag));
		if (bRenew.equals("0") & flag >= 0) {
			mViewHolder.renew.setText("续借");
			mViewHolder.renew.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							mContext);
					builder.setTitle("图书续借提示");
					builder.setMessage("您确定要续借吗？");
					builder.setPositiveButton("确定", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							String[] code_params = { Bbar_code, code };
							LibraryRenew libraryRenew = new LibraryRenew();
							libraryRenew.renew(Bbar_code, code,
									new LibraryCallBack() {

										@Override
										public void onSuccess(
												List<LibrarySelectInfo> infos) {
											mInfos.clear();
											mInfos = infos;
											notifyDataSetChanged();
											progress.dismiss();
											// Toast
											// .makeText(mContext, "续借ok",
											// 1).show();
											CustomToast.showShortText(mContext,
													"续借成功");
										}

										@Override
										public void onFailed(int netCode) {
											CustomToast.showShortText(mContext,
													"续借失败");
											progress.dismiss();
										}
									});
							// new renew().execute(code_params);
							progress = new ProgressDialog(mContext);
							progress.setTitle("联网提醒");
							progress.setMessage("正在续借，请稍后.....");
							progress.show();

						}
					});

					builder.setNegativeButton("取消", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					});
					builder.show();

				}
			});
		}

		else if (bRenew.equals("1") && flag >= 0) {

			mViewHolder.renew.setText("不可续借");
			mViewHolder.renew.setClickable(false);
		} else if (flag < 0) {
			mViewHolder.renew.setText("超期!不得续借!");
			mViewHolder.exceed.setText("超期天数");
			mViewHolder.dayleft.setText(String.valueOf(-flag));
			mViewHolder.renew.setClickable(false);
		}
		return view;
	}

	class ViewHolder {
		TextView bName;
		TextView exceed;
		TextView author;
		TextView dayleft;
		Button renew;
	}

}
