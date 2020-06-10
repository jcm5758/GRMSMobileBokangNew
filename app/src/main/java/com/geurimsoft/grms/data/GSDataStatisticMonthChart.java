package com.geurimsoft.grms.data;

import java.util.ArrayList;

import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import com.geurimsoft.bokangnew.R;
import com.geurimsoft.data.StatsListData;

public class GSDataStatisticMonthChart {
	
	private Context mContext;
	private String mDate;
	private XYMultipleSeriesRenderer multiRenderer;
	private XYMultipleSeriesDataset dataset;
	
	private StatsListData statsListData;
	String data;

	public GSDataStatisticMonthChart(Context context, String _date, StatsListData _statsListData) {

		this.mContext = context;
		this.mDate = _date;
		this.statsListData = _statsListData;
		
		this.makeData();
		this.makeChart(this.statsListData);

	}

	public XYMultipleSeriesRenderer getRenderer() {
		return this.multiRenderer;
	}

	public XYMultipleSeriesDataset getDataSet() {
		return this.dataset;
	}

	private void makeData() {

	}

	private void makeChart(StatsListData _statsListData) {
	
		ArrayList<String[]> item_list = new ArrayList<String[]>();
		item_list = _statsListData.getItems_list();
		
		int doubleArraySize = (_statsListData.getHeader_count() - 1) * (_statsListData.getItem_count() - 1);
		double[] Max = new double[doubleArraySize];
		
		int max_index = 0;
		
		for(int i = 0; i < item_list.size(); i++) {
			
			String[] items = item_list.get(i);
			for(int j = 0; j < items.length; j++) {
				
				if(j == 0)
					continue;
				
				String temp = items[j].replace(",", "");
				
				Max[max_index] = Double.parseDouble(temp);
				max_index++;
			}
		}
		
		double Maxnode = Max[0];
		
		for(int i = 1; i < Max.length; i++){
			if(Max[i] > Maxnode)
				Maxnode = Max[i];
		}
		
		int itemCount = _statsListData.getItem_count() - 1;
		dataset = new XYMultipleSeriesDataset();
		
		multiRenderer = new XYMultipleSeriesRenderer();
		multiRenderer.setXLabels(0);
		multiRenderer.setChartTitle(mDate);
		multiRenderer.setXTitle("일");
		multiRenderer.setYTitle("루베");
		
		int[] rainbow = mContext.getResources().getIntArray(R.array.rainbow);


		for(int i = 0; i < _statsListData.getHeader_count()-1; i++) {
			XYSeries series = new XYSeries(_statsListData.getHeader_titles()[i+1]);
			
			for(int j = 0; j < itemCount; j++) {
				int itemIndex = i + j * (_statsListData.getHeader_count()-1); 
				
				series.add(j, Max[itemIndex]);
			}

			dataset.addSeries(series);
			
			// Creating XYSeriesRenderer to customize incomeSeries
	 		XYSeriesRenderer renderer = new XYSeriesRenderer();
	 		renderer.setColor(rainbow[i]);
	 		renderer.setPointStyle(PointStyle.CIRCLE);
	 		renderer.setLineWidth(2);
	 		renderer.setDisplayChartValues(true);
	 		renderer.setDisplayChartValuesDistance(10);
			
	 		multiRenderer.addSeriesRenderer(renderer);
			
		}
	
		

		/***
		 * Customizing graphs
		 */
		multiRenderer.setChartTitleTextSize(40);
		multiRenderer.setAxisTitleTextSize(24);
		multiRenderer.setLabelsTextSize(15);
		multiRenderer.setZoomButtonsVisible(false);
		multiRenderer.setPanEnabled(false, false);
		multiRenderer.setClickEnabled(false);
		multiRenderer.setZoomEnabled(false, false);
		multiRenderer.setShowGridY(true);
		multiRenderer.setShowGridX(true);
		multiRenderer.setFitLegend(true);
		multiRenderer.setShowGrid(true);
		multiRenderer.setZoomEnabled(false);
		multiRenderer.setExternalZoomEnabled(false);
		multiRenderer.setAntialiasing(true);
		multiRenderer.setInScroll(true);
		multiRenderer.setLegendHeight(30);
		multiRenderer.setXLabelsAlign(Align.CENTER);
		multiRenderer.setYLabelsAlign(Align.LEFT);
		multiRenderer.setTextTypeface("sans_serif", Typeface.BOLD);
		multiRenderer.setLabelsColor(Color.BLACK);
		multiRenderer.setAxesColor(Color.BLACK);
		multiRenderer.setYLabels(10);
		multiRenderer.setYAxisMax(Maxnode+150);
		multiRenderer.setXAxisMin(-0.5);
		multiRenderer.setXAxisMax(itemCount);
		multiRenderer.setBarSpacing(0.5);
		multiRenderer.setBackgroundColor(Color.TRANSPARENT);
		multiRenderer.setMarginsColor(this.mContext.getResources().getColor(R.color.transparent_background));
		multiRenderer.setApplyBackgroundColor(true);
		multiRenderer.setMargins(new int[] { 30, 30, 30, 30 });

		for (int i = 0; i < itemCount; i++) {
			multiRenderer.addXTextLabel(i, (i + 1) + "");
		}

	}

}
