package cn.edu.zucc.personalfinance.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import cn.edu.zucc.personalfinance.control.UserManager;
import cn.edu.zucc.personalfinance.control.PaymentManager;
import cn.edu.zucc.personalfinance.control.PaymentTypeManager;
import cn.edu.zucc.personalfinance.model.Payment;
import cn.edu.zucc.personalfinance.model.PaymentType;
import cn.edu.zucc.personalfinance.util.BaseException;

public class FrmMain extends JFrame implements ActionListener {
	private JMenuBar menubar = new JMenuBar();;
	private JMenu menu_type = new JMenu("收支类别管理");
	private JMenu menu_item = new JMenu("收支管理");
	private JMenu menu_static = new JMenu("查询统计");
	private JMenu menu_more = new JMenu("更多");

	private JButton btnRefresh = new JButton("刷新");
	
	private JMenuItem menuItem_AddType = new JMenuItem("新建类别");
	private JMenuItem menuItem_DeleteType = new JMenuItem("删除类别");
	private JMenuItem menuItem_AddItem = new JMenuItem("添加条目");
	private JMenuItem menuItem_DeleteItem = new JMenuItem("删除条目");
	private JMenuItem menuItem_modifyItem = new JMenuItem("修改条目");

	private JMenuItem menuItem_modifyPwd = new JMenuItem("密码修改");
	private JMenuItem menuIten_resetPwd = new JMenuItem("密码重置");

	private JMenuItem menuItem_staticMonth = new JMenuItem("按月统计");
	private JMenuItem menuItem_search = new JMenuItem("关键词查找");

	private FrmLogin dlgLogin = null;
	private JPanel statusBar = new JPanel();

	private Object tblTypeTitle[] = { "编号", "名称", "計數" ,"金额"};
	private Object tblTypeData[][];
	DefaultTableModel tabTypeModel = new DefaultTableModel();
	private JTable dataTableType = new JTable(tabTypeModel);

	private Object tblItemTitle[] = { "编号", "摘要", "金额", "时间", "类型" };
	private Object tblItemData[][];
	DefaultTableModel tabItemModel = new DefaultTableModel();
	private JTable dataTableItem = new JTable(tabItemModel);

	List<Payment> p = null;
	List<PaymentType> tp = null;

	private void reloadTypeTable(String userId) {// 这是测试数据，需要用实际数替换

		try {
			tp = PaymentTypeManager.loadType(userId);
			tblTypeData = new Object[tp.size()][4];
			// System.out.println(tp.size());
			for (int i = 0; i < tp.size(); i++) {
				
				tblTypeData[i][0] = tp.get(i).getPaymentTypeId();
				// System.out.println(tp.get(i).getPaymentTypeId()+"");
				tblTypeData[i][1] = tp.get(i).getPaymentTypeName();
				tblTypeData[i][2] = PaymentManager.StaticsPayment_Type(tp
						.get(i).getPaymentTypeId());
				tblTypeData[i][3] = PaymentTypeManager.Statics_Money(tp.get(i).getPaymentTypeId());
				// System.out.println(tblItemData[i][2]);
			}

		} catch (BaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tabTypeModel.setDataVector(tblTypeData, tblTypeTitle);
		this.dataTableType.validate();
		this.dataTableType.repaint();
	}
	
	private void reloadTypeItemTabel(int Typeid) {// 这是测试数据，需要用实际数替换
		try {
			p = PaymentManager.load(Typeid);
			tblItemData = new Object[p.size()][5];
			for (int i = 0; i < p.size(); i++) {
				tblItemData[i][0] = p.get(i).getPayId();
				tblItemData[i][1] = p.get(i).getPS();
				tblItemData[i][2] = p.get(i).getPayMoney();
				tblItemData[i][3] = p.get(i).getPayDate();
				if (p.get(i).getPayOrIncome() == true)
					tblItemData[i][4] = "收入";
				else
					tblItemData[i][4] = "支出";
			}
			tabItemModel.setDataVector(tblItemData, tblItemTitle);
			this.dataTableItem.validate();
			this.dataTableItem.repaint();
		} catch (BaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public FrmMain() throws BaseException {

		this.setExtendedState(Frame.MAXIMIZED_BOTH);
		this.setTitle("个人收支管理系统");
		dlgLogin = new FrmLogin(this, "登陆", true);
		dlgLogin.setVisible(true);
		
		// 菜单
		this.menu_type.add(this.menuItem_AddType);
		this.menuItem_AddType.addActionListener(this);
		this.menu_type.add(this.menuItem_DeleteType);
		this.menuItem_DeleteType.addActionListener(this);
		this.menu_item.add(this.menuItem_AddItem);
		this.menuItem_AddItem.addActionListener(this);
		this.menu_item.add(this.menuItem_DeleteItem);
		this.menuItem_DeleteItem.addActionListener(this);
		this.menu_item.add(this.menuItem_modifyItem);
		this.menuItem_modifyItem.addActionListener(this);
		this.menu_static.add(this.menuItem_staticMonth);
		this.menuItem_staticMonth.addActionListener(this);
		this.menuItem_search.addActionListener(this);
		this.menu_static.add(this.menuItem_search);
		this.menu_more.add(this.menuIten_resetPwd);
		this.menuIten_resetPwd.addActionListener(this);
		this.menu_more.add(this.menuItem_modifyPwd);
		this.menuItem_modifyPwd.addActionListener(this);

		menubar.add(menu_type);
		menubar.add(menu_item);
		menubar.add(menu_static);
		menubar.add(menu_more);
		menubar.add(btnRefresh);
		this.setJMenuBar(menubar);

		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");  
		Date date=new Date(); 
		String str=sdf.format(new java.sql.Date(date.getTime())); 
		
		try {
			if(PaymentManager.Statics_Time(str).compareTo(new BigDecimal(0))==-1){
				JOptionPane.showMessageDialog(null, "本月支出"+PaymentManager.Statics_Time(str)+"元！", "消费提示", JOptionPane.ERROR_MESSAGE);
			}
			else if(PaymentManager.Statics_Time(str).compareTo(new BigDecimal(0))==0){
				JOptionPane.showMessageDialog(null, "本月无收支！", "消费提示", JOptionPane.ERROR_MESSAGE);
			}
			else if(PaymentManager.Statics_Time(str).compareTo(new BigDecimal(0))==1){
				JOptionPane.showMessageDialog(null, "本月收入"+PaymentManager.Statics_Time(str)+"元！", "消费提示", JOptionPane.ERROR_MESSAGE);
			}
			
		} catch (HeadlessException | BaseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		this.getContentPane().add(new JScrollPane(this.dataTableType),
				BorderLayout.WEST);
		this.dataTableType.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				int i = FrmMain.this.dataTableType.getSelectedRow();
				if (i < 0) {
					return;
				}
				int typeid = Integer.parseInt(FrmMain.this.tblTypeData[i][0].toString());
				FrmMain.this.reloadTypeItemTabel(typeid);
			}

		});
		this.getContentPane().add(new JScrollPane(this.dataTableItem),
				BorderLayout.CENTER);

		this.reloadTypeTable(UserManager.currentUser.getUserId());
		// 状态栏
		statusBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel label = new JLabel("您好!" + UserManager.currentUser.getUserName()+"，账户余额为"+UserManager.loadUser(UserManager.currentUser.getUserId()).getUserRemainingSum()+"元！");
		statusBar.add(label);
		this.getContentPane().add(statusBar, BorderLayout.SOUTH);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == this.menuItem_AddType) {
			FrmAddType dlg = new FrmAddType(this, "添加计划", true);
			dlg.setVisible(true);
			FrmMain.this.reloadTypeTable(UserManager.currentUser.getUserId());
		} else if (e.getSource() == this.menuItem_DeleteType) {
			
			FrmDeleteType dlg = new FrmDeleteType(this, "添加计划", true);
			dlg.setVisible(true);
//			
//			int i = FrmMain.this.dataTableType.getSelectedRow();
//			if (i < 0) {
//				JOptionPane.showMessageDialog(null, "请选择类别", "提示",
//						JOptionPane.ERROR_MESSAGE);
//				return;
//			}
//			int typeid = Integer.parseInt(FrmMain.this.tblTypeData[i][0]
//					.toString().trim());
//			if (JOptionPane.showConfirmDialog(this, "确定删除类别吗？", "确认",
//					JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
//				try {
//					PaymentTypeManager.DeleteType(typeid);
//				} catch (BaseException e1) {
//					// TODO Auto-generated catch block
//					JOptionPane.showMessageDialog(null, e1.getMessage(), "错误",
//							JOptionPane.ERROR_MESSAGE);
//					e1.printStackTrace();
//				}
//			}
			FrmMain.this.reloadTypeTable(UserManager.currentUser.getUserId());
		} else if (e.getSource() == this.menuItem_AddItem) {
			FrmAddItem dlg = new FrmAddItem(this, "添加账目", true);
			dlg.setVisible(true);
			this.reloadTypeTable(UserManager.currentUser.getUserId());
		} else if (e.getSource() == this.menuItem_DeleteItem) {
			FrmDeleteItem dlg = new FrmDeleteItem(this,"删除账目",true);
			dlg.setVisible(true);
			this.reloadTypeTable(UserManager.currentUser.getUserId());
		} else if (e.getSource() == this.menuItem_modifyItem) {
			FrmModifyItem dlg = new FrmModifyItem(this,"修改账目",true);
			dlg.setVisible(true);
			this.reloadTypeTable(UserManager.currentUser.getUserId());
		}

		else if (e.getSource() == this.menuItem_staticMonth) {
			FrmStaticMonth dlg = new FrmStaticMonth(this,"按月统计",true);
			dlg.setVisible(true);
			
		} else if (e.getSource() == this.menuItem_modifyPwd) {
			FrmModifyPwd dlg = new FrmModifyPwd(this, "密码修改", true);
			dlg.setVisible(true);
		}
		else if(e.getSource() == this.menuItem_search){
			FrmSearch dlg = new FrmSearch(this,"",true);
			dlg.setVisible(true);
		}
		else if(e.getSource() == this.menuIten_resetPwd){
			try {
				UserManager.resetUserPwd(UserManager.currentUser.getUserId());
			} catch (BaseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else if(e.getSource()==this.btnRefresh){
			btnRefresh.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					FrmMain.this.statusBar.repaint();
				}
				
			});
		}
	}
}
