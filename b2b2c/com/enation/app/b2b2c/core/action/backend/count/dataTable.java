package com.enation.app.b2b2c.core.action.backend.count;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import ChartDirector.CDMLTable;
import ChartDirector.Chart;
import ChartDirector.ChartViewer;
import ChartDirector.LegendBox;
import ChartDirector.LineLayer;
import ChartDirector.PlotArea;
import ChartDirector.TextBox;
import ChartDirector.XYChart;

import com.enation.framework.util.DateUtil;

public class dataTable implements DemoModule {
	static double[] data0 = new double[15];
	static String[] labels = new String[15];
	// 标题栏
	public String toString() {
		return "访问数折线图";
	}

	// 每个窗口显示折线图数量
	public int getNoOfCharts() {
		return 1;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String[] getLabels() throws ParseException{
		List list = new ArrayList();
		// 当前时间
		Long today = DateUtil.getDateline();
		for(long l = today-86400l*15; l < today; l += 86400l){
			list.add(DateUtil.toString(l, "MM.dd"));
		}
		String[] arr = (String[])list.toArray(new String[list.size()]);
		return arr;
	}
	
	// Main code for creating charts
	public void createChart(ChartViewer viewer, int index) {
		// 破解并去除黄色注册标志
		Chart.setLicenseCode("SXZVFNRN9MZ9L8LGA0E2B1BB");
		// 折线图大小
		XYChart c = new XYChart(770, 400);
		// 最上面标题名字，字体，字号
		TextBox title = c.addTitle("访问数-折线图", "黑体", 18);
		PlotArea plotArea = c.setPlotArea(200, 55, c.getWidth() - 100, c.getHeight() - 120, c.linearGradientColor(0, 55, 0,
				55 + c.getHeight() - 120, 0xaaccff, 0xf9fcff), -1, -1, c.dashLineColor(0xaa000000, Chart.DotLine), -1);
		LegendBox legendBox = c.addLegend(c.getWidth() / 2, title.getHeight() , false, "宋体", 10);
		legendBox.setAlignment(Chart.TopCenter);
		legendBox.setBackground(Chart.Transparent, Chart.Transparent);
		c.yAxis().setTitle("访问数", "宋体", 10);
		c.yAxis().setTickDensity(30);
		LineLayer layer = c.addLineLayer2();
		layer.setLineWidth(1);
		layer.addDataSet(data0, 0xff0000, "日期").setDataSymbol(Chart.CircleSymbol, 9);
		c.xAxis().setLabels(labels);
		CDMLTable table = c.xAxis().makeLabelTable();
		table.getStyle().setMargin2(0, 0, 1, 1);
		table.getRowStyle(0).setFontStyle("宋体");
		table.appendRow().setBackground(0xeeeeee, Chart.LineColor);
		for (int i = 0; i < data0.length; ++i) {
			table.setText(i, 1, String.valueOf(data0[i]));
		}
		table.insertCol(0).setMargin2(5, 5, 3, 3);
		table.getCell(0, 0).setBackground(Chart.Transparent, Chart.Transparent);
		table.setText(0, 1, layer.getLegendIcon(0));
		c.layoutLegend();
		c.packPlotArea(2, legendBox.getTopY() + legendBox.getHeight(), c.getWidth() - 3, c.getHeight() - 3);
		legendBox.setPos(plotArea.getLeftX() + (plotArea.getWidth() - legendBox.getWidth()) / 2, legendBox.getTopY());
		// title 位置
		title.setPos(plotArea.getLeftX() + (plotArea.getWidth() - title.getWidth()) / 2, title.getTopY());
		viewer.setImage(c.makeImage());
		// 光标位置显示的信息
		viewer.setImageMap(c.getHTMLImageMap("clickable", "", "title='{xLabel}{dataSetName} : {value}个'"));
	}

	@SuppressWarnings("rawtypes")
	public void init(List list) throws ParseException {
		//System.out.println("进入init方法");
		labels = this.getLabels();
		for(int i=0; i<list.size(); i++){
			double numCount = Double.parseDouble((String) list.get(i).toString());
			data0[i] = numCount;
		}
		//System.out.println("给data和labels赋值成功");
		DemoModule demo = new dataTable();
		final JFrame frame = new JFrame(demo.toString());
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				frame.dispose();
			}
		});
		frame.getContentPane().setBackground(Color.white);
		ChartViewer viewer = new ChartViewer();
		demo.createChart(viewer, 0);
		frame.getContentPane().add(viewer);
		frame.setSize(800, 600);// 窗口大小
		frame.setLocationRelativeTo(null);// 窗口居中显示
		frame.setVisible(true);
		//System.out.println("窗口建立成功");
	}
}
