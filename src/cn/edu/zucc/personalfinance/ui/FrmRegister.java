package cn.edu.zucc.personalfinance.ui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import cn.edu.zucc.personalfinance.control.UserManager;
import cn.edu.zucc.personalfinance.model.BeanUser;
import cn.edu.zucc.personalfinance.util.BaseException;

public class FrmRegister extends JDialog implements ActionListener {
	private JPanel toolBar = new JPanel();
	private JPanel workPane = new JPanel();
	private Button btnOk = new Button("Register");
	private Button btnCancel = new Button("Cancel");
	
	private JLabel labelUser = new JLabel("用户Id：");
	private JLabel labelPwd = new JLabel("密码：");
	private JLabel labelSum = new JLabel("资金：");
	private JLabel labelName = new JLabel("用户名");
	private JTextField edtUserId = new JTextField(20);
	private JPasswordField edtPwd = new JPasswordField(20);
	private JTextField edtSum = new JTextField(20);
	private JTextField edtName = new JTextField(20);
	public FrmRegister(Dialog f, String s, boolean b) {
		super(f, s, b);
		toolBar.setLayout(new FlowLayout(FlowLayout.RIGHT));
		toolBar.add(this.btnOk);
		toolBar.add(btnCancel);
		this.getContentPane().add(toolBar, BorderLayout.SOUTH);
		workPane.add(labelUser);
		workPane.add(edtUserId);
		workPane.add(labelName);
		workPane.add(edtName);
		workPane.add(labelPwd);
		workPane.add(edtPwd);
		workPane.add(labelSum);
		workPane.add(edtSum);
		this.getContentPane().add(workPane, BorderLayout.CENTER);
		this.setSize(300, 180);
		this.btnCancel.addActionListener(this);
		this.btnOk.addActionListener(this);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==this.btnCancel)
			this.setVisible(false);
		else if(e.getSource()==this.btnOk){
			//补充注册相关代码
			BeanUser user=new BeanUser();
			user.setUserId(edtUserId.getText());
			user.setUserName(edtName.getText());
			user.setUserPsw(String.valueOf(edtPwd.getPassword()));
			user.setUserRemainingSum(new BigDecimal(edtSum.getText()));
			user.setUserCreDate(new java.sql.Timestamp(System.currentTimeMillis()));
			try {
				UserManager.createUser(user);
			} catch (BaseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			this.setVisible(false);
		}
			
		
	}


}
