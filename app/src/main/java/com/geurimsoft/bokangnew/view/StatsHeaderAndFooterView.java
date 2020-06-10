package com.geurimsoft.bokangnew.view;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geurimsoft.bokangnew.R;
import com.geurimsoft.conf.AppConfig;
import com.geurimsoft.data.StatsListData;
import com.geurimsoft.grms.data.GSMonthInOut;
import com.geurimsoft.grms.data.GSMonthInOutDetail;

import java.util.ArrayList;

public class StatsHeaderAndFooterView
{

	private Context mContext;
	private GSMonthInOut monthData;

	private LinearLayout header_layout, footer_layout;

	private int header_count;
	private int footer_count;
	private String[] header_titles;
	private ArrayList<GSMonthInOutDetail> footer_items;

	public StatsHeaderAndFooterView(Context _context, GSMonthInOut monthData)
	{

		this.mContext = _context;
		this.monthData = monthData;
		
		this.header_count = this.monthData.headerCount;
		this.footer_count = this.monthData.headerCount;
		
		this.header_titles = this.monthData.header;
		this.footer_items = this.monthData.list;

	}
	
	public void makeHeaderView(LinearLayout _header_layout)
	{
		
		this.header_layout = _header_layout;
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		
		LinearLayout layout = new LinearLayout(mContext);
		layout.setLayoutParams(params);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		
		// Header Layout
		for(int header_index = 0; header_index < this.header_count; header_index++)
		{
			TextView stock_title_textview = makeMenuTextView(mContext, this.header_titles[header_index], "#ffffff", Gravity.CENTER);
			layout.addView(stock_title_textview);
		}
		
		header_layout.addView(layout);

	}
	
	public void makeFooterView(LinearLayout _footer_layout)
	{
		
		this.footer_layout = _footer_layout;
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		
		LinearLayout layout = new LinearLayout(mContext);
		layout.setLayoutParams(params);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		
		TextView textview = null;

		for(GSMonthInOutDetail detail : footer_items)
		{

			double[] values = detail.values;

			textview = makeMenuTextView(mContext, detail.day, "#000000",  Gravity.CENTER);
			layout.addView(textview);

			for(int footer_index = 0; footer_index < values.length; footer_index++)
			{
				textview = makeMenuTextView(mContext, AppConfig.changeToCommanString(values[footer_index]), "#000000",  Gravity.CENTER_VERTICAL | Gravity.RIGHT);
				layout.addView(textview);
			}

		}

		footer_layout.addView(layout);

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
	
}
