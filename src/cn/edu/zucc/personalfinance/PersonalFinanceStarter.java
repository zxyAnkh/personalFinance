package cn.edu.zucc.personalfinance;

import cn.edu.zucc.personalfinance.ui.FrmMain;
import cn.edu.zucc.personalfinance.util.BaseException;

public class PersonalFinanceStarter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			new FrmMain();
		} catch (BaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
