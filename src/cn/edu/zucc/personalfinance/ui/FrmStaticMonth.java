package cn.edu.zucc.personalfinance.ui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;

import cn.edu.zucc.personalfinance.control.PaymentManager;
import cn.edu.zucc.personalfinance.model.Payment;
import cn.edu.zucc.personalfinance.util.BaseException;
import cn.edu.zucc.personalfinance.util.DbException;

public class FrmStaticMonth extends JDialog {
	private JPanel toolBar = new JPanel();
	private JLabel lableMonth = new JLabel("月份");
	private JTextField edtMonth = new JTextField(20);
	
	private Object tblTitle[]={"编码","交易时间","交易类型","金额","类别","摘要"};
	private Object tblData[][];
	DefaultTableModel tablmod=new DefaultTableModel();
	private JTable dataTable=new JTable(tablmod);
	private final java.text.SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	private void reloadTable(){
		try {
			List<Payment> records=PaymentManager.StaticsPayment_Time(this.edtMonth.getText());
			tblData =new Object[records.size()][6];
			for(int i=0;i<records.size();i++){
				tblData[i][0]=records.get(i).getPayId();
				tblData[i][1]=sdf.format(records.get(i).getPayDate());
				tblData[i][2]=records.get(i).getPaymentTypeId();
				tblData[i][3]=records.get(i).getPayMoney();
				tblData[i][4]=records.get(i).getPayOrIncome();
				tblData[i][5]=records.get(i).getPS();
			}
			
			tablmod.setDataVector(tblData,tblTitle);
			this.dataTable.validate();
			this.dataTable.repaint();
		} catch (BaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void reloadPaymentInfo(){
		String s=this.edtMonth.getText().trim();
		List<Payment> pp=null;
		if(!"".equals(s)){
			try {
				pp=(new PaymentManager()).StaticsPayment_Time(s);
			} catch (BaseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.reloadTable();
	}
	public FrmStaticMonth(Frame f, String s, boolean b) {
		super(f, s, b);
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.lableMonth.setForeground(Color.red);
		toolBar.add(lableMonth);
		toolBar.add(edtMonth);
		
		this.getContentPane().add(toolBar, BorderLayout.NORTH);
		//提取现有数据
		this.reloadTable();
		this.getContentPane().add(new JScrollPane(this.dataTable), BorderLayout.CENTER);
		
		// 屏幕居中显示
		this.setSize(800, 600);
		double width = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		this.setLocation((int) (width - this.getWidth()) / 2,
				(int) (height - this.getHeight()) / 2);

		this.validate();
		
		this.edtMonth.getDocument().addDocumentListener(new javax.swing.event.DocumentListener(){
		    public void changedUpdate(DocumentEvent e) {//这是更改操作的处理
		    	FrmStaticMonth.this.reloadPaymentInfo();
		  	}
			@Override
			public void insertUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				FrmStaticMonth.this.reloadPaymentInfo();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				FrmStaticMonth.this.reloadPaymentInfo();
			}
		});
	}
}
