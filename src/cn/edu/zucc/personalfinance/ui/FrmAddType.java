package cn.edu.zucc.personalfinance.ui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cn.edu.zucc.personalfinance.control.PaymentTypeManager;
import cn.edu.zucc.personalfinance.model.PaymentType;
import cn.edu.zucc.personalfinance.util.BaseException;


public class FrmAddType extends JDialog implements ActionListener {
	
	private JPanel toolBar = new JPanel();
	private JPanel workPane = new JPanel();
	private Button btnOk = new Button("OK");
	private Button btnCancel = new Button("Cancel");
	private JLabel labelName = new JLabel("名称：");
	private JLabel labelId = new JLabel("ID");
	private JTextField edtId = new JTextField(20);
	private JTextField edtName = new JTextField(20);
	public FrmAddType(JFrame f, String s, boolean b) {
		super(f, s, b);
		toolBar.setLayout(new FlowLayout(FlowLayout.RIGHT));
		toolBar.add(btnOk);
		toolBar.add(btnCancel);
		this.getContentPane().add(toolBar, BorderLayout.SOUTH);
		workPane.add(labelId);
		workPane.add(edtId);
		workPane.add(labelName);
		workPane.add(edtName);
		this.getContentPane().add(workPane, BorderLayout.CENTER);
		this.setSize(260, 180);
		// 屏幕居中显示
		double width = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		this.setLocation((int) (width - this.getWidth()) / 2,
				(int) (height - this.getHeight()) / 2);

		this.validate();
		this.btnOk.addActionListener(this);
		this.btnCancel.addActionListener(this);
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==this.btnCancel) {
			this.setVisible(false);
			return;
		}
		else if(e.getSource()==this.btnOk){
			PaymentType tp = new PaymentType();
			tp.setPaymentTypeId(Integer.parseInt(edtId.getText()));
			tp.setPaymentTypeName(edtName.getText());
			try {
				PaymentTypeManager.CreateType(tp);
			} catch (BaseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			this.setVisible(false);
		}
		
	}
	
	
}
