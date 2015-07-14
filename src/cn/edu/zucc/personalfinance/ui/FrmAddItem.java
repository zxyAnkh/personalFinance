package cn.edu.zucc.personalfinance.ui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cn.edu.zucc.personalfinance.control.UserManager;
import cn.edu.zucc.personalfinance.control.PaymentTypeManager;
import cn.edu.zucc.personalfinance.control.PaymentManager;
import cn.edu.zucc.personalfinance.model.Payment;
import cn.edu.zucc.personalfinance.util.BaseException;
import cn.edu.zucc.personalfinance.util.BusinessException;

public class FrmAddItem<PaymentType> extends JDialog implements ActionListener {

	private JPanel toolBar = new JPanel();
	private JPanel workPane = new JPanel();
	private Button btnOk = new Button("OK");
	private Button btnCancel = new Button("Cancel");
	private JLabel labelId = new JLabel("ID:");
	private JLabel labelBoolean = new JLabel("支出（0）或收入（1）:");
	private JLabel labelTitle = new JLabel("条目摘要：");
	private JLabel labelCash = new JLabel("金额：");
	private JLabel labelCashDate = new JLabel("发生日期：");
	private JLabel labelType = new JLabel("收支类别:");

	private JTextField edtId = new JTextField(20);
	private JTextField edtBoolean = new JTextField(1);
	private JTextField edtCash = new JTextField(20);
	private JTextField edtCashDate = new JTextField(20);
	private JTextField edtTitle = new JTextField(20);
	private JTextField edtType = new JTextField(20);

	public FrmAddItem(JFrame f, String s, boolean b) {
		super(f, s, b);
		toolBar.setLayout(new FlowLayout(FlowLayout.RIGHT));
		toolBar.add(btnOk);
		toolBar.add(btnCancel);
		this.getContentPane().add(toolBar, BorderLayout.SOUTH);
		workPane.add(labelId);
		workPane.add(edtId);
		workPane.add(labelTitle);
		workPane.add(edtTitle);
		workPane.add(labelCash);
		workPane.add(edtCash);
		workPane.add(labelType);
		workPane.add(edtType);
		workPane.add(labelCashDate);
		workPane.add(edtCashDate);
		workPane.add(labelBoolean);
		workPane.add(edtBoolean);
		this.getContentPane().add(workPane, BorderLayout.CENTER);
		this.setSize(260, 360);
		// 屏幕居中显示
		double width = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		this.setLocation((int) (width - this.getWidth()) / 2,
				(int) (height - this.getHeight()) / 2);

		this.validate();
		this.btnOk.addActionListener(this);
		this.btnCancel.addActionListener(this);

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.btnCancel) {
			this.setVisible(false);
			return;
		} else if (e.getSource() == this.btnOk) {
			Payment tp = new Payment();
			try {
				if (Integer.parseInt(edtBoolean.getText().trim()) == 1 )
					tp.setPayOrIncome(true);
				else if (Integer.parseInt(edtBoolean.getText().trim()) == 0)
					tp.setPayOrIncome(false);
				else if (Integer.parseInt(edtBoolean.getText().trim()) != 1 && Integer.parseInt(edtBoolean.getText().trim()) != 0)
					throw new BusinessException("输入错误");
				SimpleDateFormat cashDate = new SimpleDateFormat("yyyy-MM-dd");
				try {
					tp.setPayDate(cashDate.parse(edtCashDate.getText()));
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				tp.setPaymentTypeId(Integer.parseInt(edtType.getText()));
				tp.setPayId(Integer.parseInt(edtId.getText()));
				tp.setPayMoney(new BigDecimal(edtCash.getText()));
				tp.setPS(edtTitle.getText());
				tp.setUserId(UserManager.currentUser.getUserId());
				PaymentManager.CreatePayment(tp);
				this.setVisible(false);
			} catch (BaseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

	}

}
