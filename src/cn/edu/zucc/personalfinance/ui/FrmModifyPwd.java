package cn.edu.zucc.personalfinance.ui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import cn.edu.zucc.personalfinance.control.UserManager;
import cn.edu.zucc.personalfinance.util.BaseException;

public class FrmModifyPwd extends JDialog implements ActionListener {
	private JPanel toolBar = new JPanel();
	private JPanel workPane = new JPanel();
	private Button btnOk = new Button("OK");
	private Button btnCancel = new Button("Cancel");
	
	private JLabel labelPwdOld = new JLabel("原密码：");
	private JLabel labelPwd = new JLabel("新密码：");
	private JLabel labelPwd2 = new JLabel("新密码：");
	private JTextField edtPwdOld = new JTextField(20);
	private JPasswordField edtPwd = new JPasswordField(20);
	private JPasswordField edtPwd2 = new JPasswordField(20);
	public FrmModifyPwd(Frame f, String s, boolean b) {
		super(f, s, b);
		toolBar.setLayout(new FlowLayout(FlowLayout.RIGHT));
		toolBar.add(this.btnOk);
		toolBar.add(btnCancel);
		this.getContentPane().add(toolBar, BorderLayout.SOUTH);
		workPane.add(labelPwdOld);
		workPane.add(edtPwdOld);
		workPane.add(labelPwd);
		workPane.add(edtPwd);
		workPane.add(labelPwd2);
		workPane.add(edtPwd2);
		this.getContentPane().add(workPane, BorderLayout.CENTER);
		this.setSize(300, 220);
		this.btnCancel.addActionListener(this);
		this.btnOk.addActionListener(this);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==this.btnCancel)
			this.setVisible(false);
		else if(e.getSource()==this.btnOk){
			try {
				UserManager.changeUserPwd(UserManager.currentUser.getUserId(),edtPwdOld.getText(),new String(edtPwd.getPassword()));
				this.setVisible(false);
			} catch (BaseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
			
		
	}


}
