package com.enation.app.tradeease.core.service.student.impl;
import java.applet.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class ExchanageRate extends Applet implements ActionListener
{
private JFrame frame=new JFrame("汇率转换");
private JTextField text1=new JTextField();
private JTextField text2=new JTextField();
private String s[]={"美元","人民币","日元 ",
   "欧元","英镑","瑞郎","澳元","加元","港币","坡元","纽元","瑞典克朗","韩元","泰铢","台币"};
private float rate[]={1,(float)6.8276,(float)89.1,(float)0.6841,(float)0.615,
   (float)1.0346 ,(float)1.0956 ,(float) 1.0601 ,(float)7.7503,(float)1.3913,(float)1.3794,(float)7.1208,(float)0.116,(float)33.12,(float)32.2};
private JComboBox cb=null;
private JComboBox cb1=null;
private double sum;//兑换后的金额

JLabel num=new JLabel("兑换金额");
JLabel money=new JLabel ("由             ");
JLabel bmoney=new JLabel("兑换至   ");
JLabel bnum=new JLabel("兑换后的金额为");
Panel p1;
Panel p2;
Panel p3;
Panel p4;
Panel p5;
JButton button;

public void init() 
{ 
cb=new JComboBox(s);
cb1=new JComboBox(s);
text1 = new JTextField(15);
text2= new JTextField(10) ;
p1=new Panel();
p2=new Panel();
p3=new Panel();
p4=new Panel();
p5=new Panel();
p1.add(num,BorderLayout.WEST);
p1.add(text1,BorderLayout.EAST);
p2.add(money,BorderLayout.WEST);
p2.add(cb,BorderLayout.EAST);
p3.add(bmoney,BorderLayout.WEST);
p3.add(cb1,BorderLayout.EAST);
add(p1,BorderLayout.NORTH);
add(p2,BorderLayout.CENTER);
add(p3,BorderLayout.SOUTH);
button=new JButton("兑换");
p4.add(button,BorderLayout.WEST);
add(p4);
p5.add(bnum,BorderLayout.WEST);
p5.add(text2,BorderLayout.EAST);
add(p5);
cb.addActionListener(this);
cb1.addActionListener(this);
button.addActionListener(this);


}


public void actionPerformed(ActionEvent e) {

JButton button1=(JButton) e.getSource();
java.text.DecimalFormat df=new java.text.DecimalFormat("#.000000");
String text=text1.getText();
String sum1;
int cbx = cb.getSelectedIndex();//记录下标
int cb1x=cb1.getSelectedIndex();
if(button1==button)
{
   sum=Float.parseFloat(text);
  
   if(sum>0)
   {
   
    sum1=df.format((double)rate[cb1x]/(double)rate[cbx]*sum);
    text2.setText(sum1);
   }
   else
    text2.setText("你输入的金额错误！");
}

}
}












 











 











 
