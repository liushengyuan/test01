package com.enation.app.b2b2c.core.action.backend.count;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import ChartDirector.*;

import com.enation.app.shop.core.service.IAllianceCountManager;

public class AnglePie implements DemoModule {
	private IAllianceCountManager allianceCountManager;
	// 横纵坐标数据
	static double[] data = new double[1000];
	static String[] labels = new String[1000];
	
	// 标题栏
	public String toString() {
		return "统计饼状图";
	}

	// 每个窗口显示饼状图数量
	public int getNoOfCharts() {
		return 1;
	}

	// Main code for creating charts
	public void createChart(ChartViewer viewer, int index) {
		// Determine the starting angle(角) and direction based on input parameter
		int angle = 0;
		boolean clockwise = true;
		if (index != 0) {
			angle = 90;
			clockwise = false;
		}
		
		// 破解并去除黄色注册标志
		Chart.setLicenseCode("SXZVFNRN9MZ9L8LGA0E2B1BB");
		
		// 文字外框大小
		PieChart c = new PieChart(1000, 800);
		
		// 设置字体
		c.setLabelStyle( "宋体 ",9,0x20FF0000);

		// 参数含义：1、2、内部饼图的大小，3、像素
		c.setPieSize(500, 250, 120);

		// Add a title to the pie to show the start angle and direction
//		c.addTitle("地区访问数统计饼状图");
		// 标题，解决中文乱码
		TextBox title = c.addTitle("地区访问数统计饼状图");
		title.setFontStyle("System");
		title.setFontSize(20); 

		// Set the pie start angle and direction
		c.setStartAngle(angle, clockwise);

		// Draw the pie in 3D
		c.set3D();

		// Set the pie data and the pie labels
		c.setData(data, labels);

		// Explode the 1st sector (index = 0)
		c.setExplode(0);

		// Output the chart
		viewer.setImage(c.makeImage());

		// 光标位置显示的信息
		viewer.setImageMap(c.getHTMLImageMap("clickable", "", "title='{label}: ({percent}%)'"));
	}

	@SuppressWarnings("rawtypes")
	public void init(List list, List list2) {
		//System.out.println("进入init方法");
		List addrlist = list;
		List addrCountlist = list2;
		for(int i=0 ; i<addrlist.size() ; i++){
			double numCount = Double.parseDouble((String) addrCountlist.get(i).toString().subSequence(11, addrCountlist.get(i).toString().length()-1));
			String addr = (String) addrlist.get(i).toString().subSequence(11, addrlist.get(i).toString().length()-1);
			data[i] = numCount;
			labels[i] = addr;
		}
		//System.out.println("给data和labels赋值成功");
		DemoModule demo = new AnglePie();
		final JFrame frame = new JFrame(demo.toString());
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				frame.dispose();
			}
		});
		frame.getContentPane().setBackground(Color.white);
		frame.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER));
		frame.setSize(800, 600);// 窗口大小
		frame.setLocationRelativeTo(null);// 窗口居中显示

		// Create the charts and put them in the content pane
		for (int i = 0; i < demo.getNoOfCharts(); ++i) {
			ChartViewer viewer = new ChartViewer();
			demo.createChart(viewer, i);
			frame.getContentPane().add(viewer);
		}
		frame.setVisible(true);
		//System.out.println("窗口建立成功");
	}

	public IAllianceCountManager getAllianceCountManager() {
		return allianceCountManager;
	}

	public void setAllianceCountManager(IAllianceCountManager allianceCountManager) {
		this.allianceCountManager = allianceCountManager;
	}
	
}
