package com.geurimsoft.bokangnew.view;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.geurimsoft.bokangnew.R;
import com.geurimsoft.conf.AppConfig;
import com.geurimsoft.data.StAdapter;
import com.geurimsoft.data.StatsData;
import com.geurimsoft.data.StatsListData;
import com.geurimsoft.data.XmlConverter;
import com.geurimsoft.grms.data.GSDailyInOut;
import com.geurimsoft.socket.client.SocketClient;

public class EnterpriseStatsView
{

	private Activity mActivity;
	private GSDailyInOut statsData;
	private int site;
	private int statsType;
	private String date;

	private Context mContext;

	private LinearLayout stock_layout, release_layout, petosa_layout;

	private int stock_header_count;
	private int release_header_count;
	private int petosa_header_count;
	
	private String[] stock_header_titles;
	private String[] release_header_titles;
	private String[] petosa_header_titles;
	
	private int stock_count;
	private int release_count;
	private int petosa_count;
	
	private ArrayList<String[]> stock_list;
	private ArrayList<String[]> release_list;
	private ArrayList<String[]> petosa_list;
	
	private String stock_total;
	private String release_total;
	private String petosa_total;
	

	
	public EnterpriseStatsView(Activity _activity, GSDailyInOut _statsData, int _site, int _statsType, String _date)
	{

		this.mContext = _activity;
		this.mActivity = _activity;
		this.statsData = _statsData;
		this.site = _site;
		this.statsType = _statsType;
		this.date = _date;

//		this.stock_header_count = statsData.getStock_header_count();
//		this.release_header_count = statsData.getRelease_header_count();
//		this.petosa_header_count = statsData.getPetosa_header_count();
//
//		this.stock_header_titles = statsData.getStock_header();
//		this.release_header_titles = statsData.getRelease_header();
//		this.petosa_header_titles = statsData.getPetosa_header();
//
//		this.stock_count = statsData.getStock_count();
//		this.release_count = statsData.getRelease_count();
//		this.petosa_count = statsData.getPetosa_count();
//
//		this.stock_list = statsData.getStock_list();
//		this.release_list = statsData.getRelease_list();
//		this.petosa_list = statsData.getPetosa_list();
//
//		this.stock_total = statsData.getStock_total();
//		this.release_total = statsData.getRelease_total();
//		this.petosa_total = statsData.getPetosa_total();

	}
	
	public void makeStockStatsView(LinearLayout _stock_layout)
	{

		if(stock_list == null || stock_list.size() <= 0)
			return;

		stock_layout = _stock_layout;
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		
		LinearLayout header_layout = new LinearLayout(mContext);
		header_layout.setLayoutParams(params);
		header_layout.setOrientation(LinearLayout.HORIZONTAL);
		
		// Stock Header Layout
		for(int stock_header_index = 0; stock_header_index < this.stock_header_count; stock_header_index++)
		{
			TextView stock_title_textview = makeMenuTextView(mContext, stock_header_titles[stock_header_index], "#ffffff", Gravity.CENTER);
			header_layout.addView(stock_title_textview);
		}
		
		stock_layout.addView(header_layout);

		TextView stock_item_textview;
		
		for(int stock_index = 0; stock_index < this.stock_list.size(); stock_index++)
		{
			
			String[] stock_items = stock_list.get(stock_index);
			
			LinearLayout stock_row_layout = new LinearLayout(mContext);
			
			stock_row_layout.setLayoutParams(params);
			stock_row_layout.setOrientation(LinearLayout.HORIZONTAL);
			
			for(int i = 0; i < stock_items.length; i++)
			{
				
				int gravity = 0;

				if(i == 0)
				{
					
					gravity = Gravity.CENTER;
					
					if(stock_index == stock_count - 1)
						stock_item_textview = makeMenuTextView(mContext, stock_items[i], "#000000", gravity);
					else
					{

						stock_item_textview = makeRowTextView(mContext, stock_items[i], gravity);
						stock_item_textview.setTag(stock_items[i]);
						
						stock_item_textview.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								String name = (String) v.getTag();
								
								new EnterpriseDetailTask(name, String.valueOf(site), String.valueOf(statsType), String.valueOf(AppConfig.MODE_STOCK), date).execute(); 
							}
						});
						
					}

					stock_row_layout.addView(stock_item_textview);

				}
				else
				{
					
					gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
					
					if(stock_index == stock_count - 1)
						stock_item_textview = makeMenuTextView(mContext, stock_items[i], "#000000", gravity);
					else
						stock_item_textview = makeRowTextView(mContext, stock_items[i], gravity);
					
					stock_row_layout.addView(stock_item_textview);

				}
				
			}

			stock_layout.addView(stock_row_layout);

		}
		
	}
	
	public void makeReleaseStatsView(LinearLayout _release_layout)
	{
		
		release_layout = _release_layout;
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		
		LinearLayout header_layout = new LinearLayout(mContext);
		header_layout.setLayoutParams(params);
		header_layout.setOrientation(LinearLayout.HORIZONTAL);
		
		//Release Header Layout
		for(int release_header_index = 0; release_header_index < this.release_header_count; release_header_index++) {
			
			TextView release_title_textview = makeMenuTextView(mContext, release_header_titles[release_header_index], "#ffffff", Gravity.CENTER);
			
			header_layout.addView(release_title_textview);
		}
		
		release_layout.addView(header_layout);
		
		if(release_list == null || release_list.size() <= 0)
			return;
		
		TextView release_item_textview;
		
		for(int release_index = 0; release_index < this.release_list.size(); release_index++) {
			
			String[] release_items = release_list.get(release_index);
			
			LinearLayout release_row_layout = new LinearLayout(mContext);
			
			release_row_layout.setLayoutParams(params);
			release_row_layout.setOrientation(LinearLayout.HORIZONTAL);
			
			for(int i = 0; i < release_items.length; i++) {
				
				int gravity = 0;
				if(i == 0) {
					
					gravity = Gravity.CENTER;
					
					if(release_index == release_count - 1) {
						release_item_textview = makeMenuTextView(mContext, release_items[i], "#000000", gravity);
						
					} else {
						release_item_textview = makeRowTextView(mContext, release_items[i], gravity);
						release_item_textview.setTag(release_items[i]);
						
						release_item_textview.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								String name = (String) v.getTag();
								
								new EnterpriseDetailTask(name, String.valueOf(site), String.valueOf(statsType), String.valueOf(AppConfig.MODE_RELEASE), date).execute(); 
							}
						});
						
					}
					
					release_row_layout.addView(release_item_textview);
				} else {
					
					gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
					
					if(release_index == release_count - 1)
						release_item_textview = makeMenuTextView(mContext, release_items[i], "#000000", gravity);
					else
						release_item_textview = makeRowTextView(mContext, release_items[i], gravity);
					
					release_row_layout.addView(release_item_textview);
				}
				
			}
			release_layout.addView(release_row_layout);
		}
		
	}

	public void makePetosaStatsView(LinearLayout _petosa_layout) {
		
		petosa_layout = _petosa_layout;
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		
		LinearLayout header_layout = new LinearLayout(mContext);
		header_layout.setLayoutParams(params);
		header_layout.setOrientation(LinearLayout.HORIZONTAL);
		
		//petosa Header Layout
		for(int petosa_header_index = 0; petosa_header_index < this.petosa_header_count; petosa_header_index++) {
			
			TextView petosa_title_textview = makeMenuTextView(mContext, petosa_header_titles[petosa_header_index], "#ffffff", Gravity.CENTER);
			
			header_layout.addView(petosa_title_textview);
		}
		
		petosa_layout.addView(header_layout);
		
		if(petosa_list == null || petosa_list.size() <= 0)
			return;
		
		TextView petosa_item_textview;
		
		for(int petosa_index = 0; petosa_index < this.petosa_list.size(); petosa_index++) {
			
			String[] petosa_items = petosa_list.get(petosa_index);
			
			LinearLayout petosa_row_layout = new LinearLayout(mContext);
			
			petosa_row_layout.setLayoutParams(params);
			petosa_row_layout.setOrientation(LinearLayout.HORIZONTAL);
			
			for(int i = 0; i < petosa_items.length; i++) {
				
				int gravity = 0;
				
				if(i == 0) {
					
					gravity = Gravity.CENTER;
					if(petosa_index == petosa_count - 1)
						petosa_item_textview = makeMenuTextView(mContext, petosa_items[i], "#000000", gravity);
					else {
						petosa_item_textview = makeRowTextView(mContext, petosa_items[i], gravity);
						petosa_item_textview.setTag(petosa_items[i]);
						
						petosa_item_textview.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								String name = (String) v.getTag();
								
								new EnterpriseDetailTask(name, String.valueOf(site), String.valueOf(statsType), String.valueOf(AppConfig.MODE_PETOSA), date).execute(); 
							}
						});
					}
						
					petosa_row_layout.addView(petosa_item_textview);
				} else {
					
					gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
					if(petosa_index == petosa_count - 1)
						petosa_item_textview = makeMenuTextView(mContext, petosa_items[i], "#000000", gravity);
					else
						petosa_item_textview = makeRowTextView(mContext, petosa_items[i], gravity);
					
					petosa_row_layout.addView(petosa_item_textview);
				}
				
			}
			petosa_layout.addView(petosa_row_layout);
		}
		
	}
	
	private TextView makeMenuTextView(Context context, String str, String color, int gravity)
	{
		
		LinearLayout.LayoutParams layout_params = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT);
		layout_params.weight = 1.0f;
		
		TextView tv = new TextView(context);
		tv.setLayoutParams(layout_params);
		tv.setGravity(gravity);
		tv.setBackgroundResource(R.drawable.menu_border);
		tv.setPadding(10, 20, 10, 20);
		tv.setTextColor(Color.parseColor(color));
		tv.setTextSize(13);
		tv.setText(str);
		
		return tv;

	}
	
	private TextView makeRowTextView(Context context, String str, int gravity)
	{
		
		LinearLayout.LayoutParams layout_params = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT);
		layout_params.weight = 1.0f;
		
		TextView tv = new TextView(context);
		tv.setLayoutParams(layout_params);
		tv.setGravity(gravity);
		tv.setBackgroundResource(R.drawable.row_border);
		tv.setPadding(10, 20, 10, 20);
		tv.setTextColor(Color.parseColor("#000000"));
		tv.setTextSize(13);
		tv.setText(str);
		
		return tv;

	}
	
	public class EnterpriseDetailTask extends AsyncTask<String, String, StatsListData>
	{

		private String queryDate;
		private String responseMessage;
		
		private String name;
		private String site;
		private String statsType;
		private String mode;
		private CustomProgressDialog progressDialog;

		public EnterpriseDetailTask(String _name, String _site, String _statsType, String _mode, String _queryDate)
		{
			this.queryDate = _queryDate;
			this.name = _name;
			this.site = _site;
			this.statsType = _statsType;
			this.mode = _mode;
		}

		@Override
		protected void onPreExecute()
		{

			super.onPreExecute();
			
			progressDialog = new CustomProgressDialog(mContext);
			progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
			progressDialog.show();

		}

		@Override
		protected StatsListData doInBackground(String... params)
		{

			String query = site + "," + mode + "," + statsType + "," + name + "," + queryDate;
			String message = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><GEURIMSOFT><GCType>statistic_month_single_company</GCType><GCQuery>"+query+"</GCQuery></GEURIMSOFT>\n";
			responseMessage = null;

			try
			{

				SocketClient sc = new SocketClient(AppConfig.SERVER_IP, AppConfig.SERVER_PORT, message, AppConfig.SOCKET_KEY);

				sc.start();
				sc.join();

				responseMessage = sc.getReturnString();
				
			}
			catch (Exception e)
			{
				Log.e(AppConfig.TAG, "StatViewDetailDaily.makeDailyData() : " + e.toString());
				return null;
			}

			StatsListData data = null;
			
			if (responseMessage == null || responseMessage.equals("Fail"))
			{
				Log.d(AppConfig.TAG, "Returned xml is null.");
				return data;
			}
			
			data = XmlConverter.parseStatsListInfo(responseMessage);
			
			try
			{
				Thread.sleep(10);
				
			}
			catch (Exception e)
			{
				// TODO: handle exception
			}

			return data;

		}

		@Override
		protected void onPostExecute(StatsListData result)
		{

			super.onPostExecute(result);
			
			if(result == null)
				showErrorDialog();
			else
				showEnterprisePopup(result, queryDate, name, String.valueOf(statsType), String.valueOf(mode));
			
			progressDialog.dismiss();

		}

	}
	
	private PopupWindow popupWindow;
    private int mWidthPixels, mHeightPixels; 
	
	private void showEnterprisePopup(StatsListData _data, String _queryDate, String _name, String _statsType, String _mode)
	{

//		WindowManager w = mActivity.getWindowManager();
//		Display d = w.getDefaultDisplay();
//		DisplayMetrics metrics = new DisplayMetrics();
//		d.getMetrics(metrics);
//
//		mWidthPixels = metrics.widthPixels;
//		mHeightPixels = metrics.heightPixels;
//
//		if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
//		{
//
//			try
//			{
//				mWidthPixels = (Integer) Display.class.getMethod("getRawWidth").invoke(d);
//				mHeightPixels = (Integer) Display.class.getMethod("getRawHeight").invoke(d);
//			}
//			catch (Exception ignored) {}
//
//		}
//
//		// 상태바와 메뉴바의 크기를 포함
//		if (Build.VERSION.SDK_INT >= 17)
//		{
//
//			try
//			{
//				Point realSize = new Point();
//				Display.class.getMethod("getRealSize", Point.class).invoke(d, realSize);
//				mWidthPixels = realSize.x;
//				mHeightPixels = realSize.y;
//			}
//			catch (Exception ignored) {}
//
//		}
//
//		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//		View layout = inflater.inflate(R.layout.enterprise_popup, null);
//
//		popupWindow = new PopupWindow(layout, mWidthPixels-20, LayoutParams.MATCH_PARENT, true);
//		popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
//
//		TextView popup_date = (TextView)layout.findViewById(R.id.popup_date);
//		LinearLayout popup_header_container = (LinearLayout)layout.findViewById(R.id.popup_header_container);
//
//		ListView popup_listview = (ListView)layout.findViewById(R.id.popup_listview);
//		popup_listview.setDividerHeight(0);
//
//		Button popup_close_btn = (Button)layout.findViewById(R.id.popup_close_btn);
//
//		popup_header_container.removeAllViews();
//
//		String[] dates = date.split(",");
//
//		String statsTypeStr = "";
//
//		if(_statsType.equals("0"))
//		{
//			statsTypeStr = "(단위:루베)";
//		}
//		else
//		{
//			statsTypeStr = "(단위:천원)";
//		}
//
//		String modeStr = "";
//
//		if(_mode.equals("0"))
//		{
//			modeStr = "입고 현황";
//		}
//		else if(_mode.equals("1"))
//		{
//			modeStr = "출고 현황";
//		}
//		else
//		{
//			modeStr = "폐토사 현황";
//		}
//
//		String dateStr = dates[0]+"년 "+ dates[1]+"월 "+_name+"\n"+modeStr+statsTypeStr;
//		popup_date.setText(dateStr);
//
//		StatsHeaderAndFooterView statsHeaderAndFooterView = new StatsHeaderAndFooterView(mActivity, _data);
//		statsHeaderAndFooterView.makeHeaderView(popup_header_container);
//
//		StAdapter adapter = new StAdapter(mActivity, _data);
//
//		View foot = View.inflate(mActivity, R.layout.stats_foot, null);
//		LinearLayout footer_layout = (LinearLayout)foot.findViewById(R.id.stats_footer_container);
//
//		statsHeaderAndFooterView.makeFooterView(footer_layout);
//		popup_listview.addFooterView(foot);
//		popup_listview.setAdapter(adapter);
//
//		popup_close_btn.setOnClickListener(new OnClickListener()
//		{
//
//			@Override
//			public void onClick(View v)
//			{
//				popupWindow.dismiss();
//			}
//
//		});

	}
	
	private void showErrorDialog()
	{

		AlertDialog.Builder errorDialog = new AlertDialog.Builder(mContext);
		errorDialog.setMessage(mContext.getString(R.string.loding_fail_msg));
		errorDialog.setPositiveButton(mContext.getString(R.string.dialog_confirm_button), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		errorDialog.show();

	}

}
