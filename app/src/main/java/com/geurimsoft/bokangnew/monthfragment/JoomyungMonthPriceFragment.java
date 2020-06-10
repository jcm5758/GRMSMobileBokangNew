package com.geurimsoft.bokangnew.monthfragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.geurimsoft.bokangnew.R;
import com.geurimsoft.bokangnew.view.StatsHeaderAndFooterView;
import com.geurimsoft.conf.AppConfig;
import com.geurimsoft.data.StAdapter;
import com.geurimsoft.data.StatsListData;
import com.geurimsoft.data.XmlConverter;
import com.geurimsoft.grms.data.GSMonthInOut;
import com.geurimsoft.socket.client.SocketClient;

public class JoomyungMonthPriceFragment extends Fragment
{

	private ListView yi_month_price_listview;
	private TextView yi_month_price_date;
	private String dateStr;
	private LinearLayout yi_month_price_header_container, yi_month_price_loading_indicator, yi_month_price_loading_fail;

	private MonthpriceTask monthpriceTask;

	public JoomyungMonthPriceFragment() {}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.yi_month_price, container,false);
		return v;
	}
	
	@Override
	public void onResume()
	{

		super.onResume();

		View view = this.getView();
	
		this.yi_month_price_listview = (ListView)view.findViewById(R.id.yi_month_price_listview);
		this.yi_month_price_date = (TextView)view.findViewById(R.id.yi_month_price_date);
				
		this.yi_month_price_header_container = (LinearLayout)view.findViewById(R.id.yi_month_price_header_container);
		
		this.yi_month_price_loading_indicator = (LinearLayout)view.findViewById(R.id.yi_month_price_loading_indicator);
		this.yi_month_price_loading_fail = (LinearLayout)view.findViewById(R.id.yi_month_price_loading_fail);
		
		this.yi_month_price_listview.setDividerHeight(0);
		
		makeMonthpriceData(AppConfig.DAY_STATS_YEAR, AppConfig.DAY_STATS_MONTH);

	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		monthpriceTask.cancel(true);
	}
	
	private void makeMonthpriceData(int _year, int _monthOfYear)
	{

		dateStr = _year + "년 " + _monthOfYear + "월  입출고 현황(단위:천원)";
		yi_month_price_date.setText(dateStr);

		String queryDate = _year + ",";

		if (_monthOfYear < 10)
			queryDate += "0" + _monthOfYear;
		else
			queryDate += _monthOfYear;
		
		monthpriceTask = new MonthpriceTask(queryDate);
		monthpriceTask.execute();
		
	}
	
	private void setDisplay(GSMonthInOut statsListData)
	{
		
		yi_month_price_header_container.removeAllViews();

		StatsHeaderAndFooterView statsHeaderAndFooterView = new StatsHeaderAndFooterView(getActivity(), statsListData);
		statsHeaderAndFooterView.makeHeaderView(yi_month_price_header_container);

		StAdapter adapter = new StAdapter(getActivity(), statsListData);

		View foot = View.inflate(getActivity(), R.layout.stats_foot, null);
		LinearLayout footer_layout = (LinearLayout)foot.findViewById(R.id.stats_footer_container);

		statsHeaderAndFooterView.makeFooterView(footer_layout);
		yi_month_price_listview.addFooterView(foot);

		yi_month_price_listview.setAdapter(adapter);

	}

	public class MonthpriceTask extends AsyncTask<String, String, GSMonthInOut>
	{

		private String queryDate;
		private String responseMessage;
		
		public MonthpriceTask(String _queryDate) {
			this.queryDate = _queryDate;
		}

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			yi_month_price_loading_indicator.setVisibility(View.VISIBLE);
		}
		
		@Override
		protected GSMonthInOut doInBackground(String... params)
		{

			String department = "3,";
			String message ="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><GEURIMSOFT><GCType>MONTH_MONEY</GCType><GCQuery>" + department + queryDate + "</GCQuery></GEURIMSOFT>\n";
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
				Log.e(AppConfig.TAG, "ERROR : " + this.getClass().getName() + " : doInBackground() : " + e.toString());
				return null;
			}

			if (responseMessage == null || responseMessage.equals("Fail"))
			{
				Log.d(AppConfig.TAG, "Returned xml is null.");
				return null;
			}

			GSMonthInOut data = XmlConverter.parseMonth(responseMessage);
			
			try
			{
				Thread.sleep(10);
			}
			catch (Exception e)
			{
				Log.e(AppConfig.TAG, "StatViewDetailDaily.makeDailyData() : " + e.toString());
			}

			return data;

		}

		@Override
		protected void onPostExecute(GSMonthInOut result)
		{

			super.onPostExecute(result);
			
			if(result == null)
			{
				yi_month_price_loading_fail.setVisibility(View.VISIBLE);
			}
			else
			{
				yi_month_price_loading_fail.setVisibility(View.GONE);
				setDisplay(result);
			}
			
			yi_month_price_loading_indicator.setVisibility(View.GONE);

		}

	}
}
