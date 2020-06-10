package com.geurimsoft.bokangnew.monthfragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geurimsoft.bokangnew.R;
import com.geurimsoft.bokangnew.view.EnterpriseStatsView;
import com.geurimsoft.conf.AppConfig;
import com.geurimsoft.data.StatsData;
import com.geurimsoft.data.XmlConverter;
import com.geurimsoft.grms.data.GSDailyInOut;
import com.geurimsoft.socket.client.SocketClient;

public class JoomyungMonthEnterpriseAmountFragment extends Fragment
{

	private LinearLayout yi_month_enterprise_amount_income_empty_layout, yi_month_enterprise_amount_release_empty_layout, yi_month_enterprise_amount_petosa_empty_layout;
	private LinearLayout yi_month_enterprise_amount_loading_indicator, yi_month_enterprise_amount_loading_fail;

	private TextView yi_month_enterprise_amount_date, yi_month_enterprise_amount_income_title, yi_month_enterprise_amount_release_title, yi_month_enterprise_amount_petosa_title;

	private MonthEnterpriseAmountTask  monthEnterpriseAmountTask;

	public JoomyungMonthEnterpriseAmountFragment() {}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.yi_month_enterprise_amount, container, false);
		return v;
	}
	
	@Override
	public void onResume()
	{

		super.onResume();
		
		View view = this.getView();
		
		this.yi_month_enterprise_amount_income_empty_layout = (LinearLayout)view.findViewById(R.id.yi_month_enterprise_amount_income_empty_layout);
		this.yi_month_enterprise_amount_release_empty_layout = (LinearLayout)view.findViewById(R.id.yi_month_enterprise_amount_release_empty_layout);
		this.yi_month_enterprise_amount_petosa_empty_layout = (LinearLayout)view.findViewById(R.id.yi_month_enterprise_amount_petosa_empty_layout);
		
		this.yi_month_enterprise_amount_loading_indicator = (LinearLayout)view.findViewById(R.id.yi_month_enterprise_amount_loading_indicator); 
		this.yi_month_enterprise_amount_loading_fail = (LinearLayout)view.findViewById(R.id.yi_month_enterprise_amount_loading_fail);
		
		this.yi_month_enterprise_amount_date = (TextView)view.findViewById(R.id.yi_month_enterprise_amount_date); 
		this.yi_month_enterprise_amount_income_title = (TextView)view.findViewById(R.id.yi_month_enterprise_amount_income_title); 
		this.yi_month_enterprise_amount_release_title = (TextView)view.findViewById(R.id.yi_month_enterprise_amount_release_title); 
		this.yi_month_enterprise_amount_petosa_title = (TextView)view.findViewById(R.id.yi_month_enterprise_amount_petosa_title);

		makeMonthEnterpriseAmountData(AppConfig.DAY_STATS_YEAR, AppConfig.DAY_STATS_MONTH);

	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		monthEnterpriseAmountTask.cancel(true);
	}

	private void makeMonthEnterpriseAmountData(int _year, int _monthOfYear)
	{

		String dateStr = _year + "년 " + _monthOfYear + "월  입출고 현황";
		this.yi_month_enterprise_amount_date.setText(dateStr);

		String queryDate = _year + ",";

		if (_monthOfYear < 10)
			queryDate += "0" + _monthOfYear;
		else
			queryDate += _monthOfYear;
		
		monthEnterpriseAmountTask = new MonthEnterpriseAmountTask(queryDate, dateStr);
		monthEnterpriseAmountTask.execute();
		
	}
	
	private void setDisplay(GSDailyInOut statsData, String _date)
	{
		
//		String unit = getString(R.string.unit_lube);
//
//		yi_month_enterprise_amount_income_empty_layout.removeAllViews();
//		yi_month_enterprise_amount_release_empty_layout.removeAllViews();
//		yi_month_enterprise_amount_petosa_empty_layout.removeAllViews();
//
//		EnterpriseStatsView statsView = new EnterpriseStatsView(getActivity(), statsData, AppConfig.SITE_YONGIN, AppConfig.STATE_AMOUNT, _date);
//		statsView.makeStockStatsView(yi_month_enterprise_amount_income_empty_layout);
//		yi_month_enterprise_amount_income_title.setText("입고(" + statsData.getStock_total() + unit + ")");
//
//		statsView.makeReleaseStatsView(yi_month_enterprise_amount_release_empty_layout);
//		yi_month_enterprise_amount_release_title.setText("출고(" + statsData.getRelease_total() + unit + ")");
//
//		statsView.makePetosaStatsView(yi_month_enterprise_amount_petosa_empty_layout);
//		yi_month_enterprise_amount_petosa_title.setText("토사(" + statsData.getPetosa_total() + unit + ")");

	}

	public class MonthEnterpriseAmountTask extends AsyncTask<String, String, GSDailyInOut>
	{

		private String queryDate;
		private String responseMessage;
		private String dateStr;
		
		public MonthEnterpriseAmountTask(String _queryDate, String _dateStr)
		{
			this.queryDate = _queryDate;
			this.dateStr = _dateStr;
		}

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			yi_month_enterprise_amount_loading_indicator.setVisibility(View.VISIBLE);
		}
		
		@Override
		protected GSDailyInOut doInBackground(String... params)
		{

			String department ="3,";
			String message = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><GEURIMSOFT><GCType>MONTH_CUSTOMER_UNIT</GCType><GCQuery>" + department + queryDate + "</GCQuery></GEURIMSOFT>\n";
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
				Log.e(AppConfig.TAG, "ERROR : " + this.getClass().getName() + " : makeDailyData() : " + e.toString());
				return null;
			}

			if (responseMessage == null || responseMessage.equals("Fail"))
			{
				Log.d(AppConfig.TAG, "Returned xml is null.");
				return null;
			}

			GSDailyInOut data = XmlConverter.parseDaily(responseMessage);
			
			try
			{
				Thread.sleep(10);
			}
			catch (Exception e)
			{
				Log.e(AppConfig.TAG, "ERROR : " + this.getClass().getName() + " : makeDailyData() : " + e.toString());
				return null;
			}

			return data;

		}

		@Override
		protected void onPostExecute(GSDailyInOut result)
		{

			super.onPostExecute(result);
			
			if(result == null)
			{
				yi_month_enterprise_amount_loading_fail.setVisibility(View.VISIBLE);
			}
			else
			{
				yi_month_enterprise_amount_loading_fail.setVisibility(View.GONE);
				setDisplay(result, queryDate);
			}
			
			yi_month_enterprise_amount_loading_indicator.setVisibility(View.GONE);

		}

	}

}
