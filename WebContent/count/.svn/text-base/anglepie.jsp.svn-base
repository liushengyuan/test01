<%@page import="ChartDirector.*"%>
<%@page import="javax.servlet.http.*"%>
<%@ page import="java.util.List"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%!// A simple structure to represent a chart image with an image map
	private static class ImageWithHotSpot {
		String imageURL;
		String imageMap;
	}

	// Function to create the demo charts
	ImageWithHotSpot createChart(HttpServletRequest request, int index) {
		Chart.setLicenseCode("SXZVFNRN9MZ9L8LGA0E2B1BB");
		
		List addrlist = (List) request.getAttribute("addrlist");
		List addrCountlist = (List) request.getAttribute("addrCountlist");
		
		double[] data = new double[1000];
		String[] labels = new String[1000];
		
		for(int i=0 ; i<addrlist.size() ; i++){
			double numCount = Double.parseDouble((String) addrCountlist.get(i).toString().subSequence(11, addrCountlist.get(i).toString().length()-1));
			String addr = (String) addrlist.get(i).toString().subSequence(11, addrlist.get(i).toString().length()-1);
			data[i] = numCount;
			labels[i] = addr;
		}
		
		int angle = 0;
		boolean clockwise = true;
		if (index != 0) {
			angle = 90;
			clockwise = false;
		}

		// Create a PieChart object of size 280 x 240 pixels
		PieChart c = new PieChart(480, 440);

		c.setLabelStyle( "宋体",9,0x20FF0000);
		
		// Set the center of the pie at (140, 130) and the radius to 80 pixels
		c.setPieSize(240, 230, 180);

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
		ImageWithHotSpot ret = new ImageWithHotSpot();
		ret.imageURL = c.makeSession(request, "chart" + index);

		// Include tool tip for the chart
		ret.imageMap = c.getHTMLImageMap("", "", "title='{label}: US${value}K ({percent}%)'");

		return ret;
	}%>
<%
	ImageWithHotSpot chart0 = createChart(request, 0);
%>
<html>
<body style="margin: 5px 0px 0px 5px">
	<img src='<%=response.encodeURL("/count/getchart.jsp?" + chart0.imageURL)%>' usemap="#map0" border="0">
	<map name="map0"><%=chart0.imageMap%></map>
</body>
</html>

