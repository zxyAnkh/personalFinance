package cn.edu.zucc.personalfinance.control;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cn.edu.zucc.personalfinance.model.BeanUser;
import cn.edu.zucc.personalfinance.model.PaymentType;
import cn.edu.zucc.personalfinance.model.Payment;
import cn.edu.zucc.personalfinance.util.BaseException;
import cn.edu.zucc.personalfinance.util.BusinessException;
import cn.edu.zucc.personalfinance.util.DBUtil;
import cn.edu.zucc.personalfinance.util.DbException;

public class PaymentManager {
	private static String userId = UserManager.currentUser.getUserId();

	public static void CreatePayment(Payment tp) throws BaseException {
		if (tp.getPaymentTypeId() <= 0 || "".equals(tp.getPaymentTypeId())) {
			throw new BusinessException("账目类别必须是正整数");
		}
		if (tp.getPayId() <= 0 || "".equals(tp.getPayId())) {
			throw new BusinessException("账目ID必须是正整数");
		}
		if (tp.getPayOrIncome() == null || "".equals(tp.getPayOrIncome()))
			throw new BusinessException("必须说明支出或是收入");
		if (tp.getPayMoney() == null || "".equals(tp.getPayMoney()))
			throw new BusinessException("必须输入金额");
		if (tp.getPS() == null || "".equals(tp.getPS())
				|| tp.getPS().length() > 50)
			throw new BusinessException("账目说明必须是1-50字");
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "select PayId from Payment where PayId = ? and userid='"
					+ userId + "'";
			java.sql.PreparedStatement pst = conn.prepareStatement(sql);
			pst.setInt(1, tp.getPayId());
			java.sql.ResultSet rs = pst.executeQuery();
			if (rs.next())
				throw new BusinessException("账目已经存在");
			rs.close();
			pst.close();
			sql = "insert into Payment(paymenttypeid,payid,userid,paymoney,ps,paydate,payorincome) values(?,?,?,?,?,?,?)";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, tp.getPaymentTypeId());
			pst.setInt(2, tp.getPayId());
			pst.setString(3, userId);
			pst.setBigDecimal(4, tp.getPayMoney());
			pst.setString(5, tp.getPS());
			pst.setDate(6, new java.sql.Date(tp.getPayDate().getTime()));
			pst.setBoolean(7, tp.getPayOrIncome());
			// 更新账户资金
			UserManager.changeSum(tp.getPayMoney(), tp.getPayOrIncome(),
					tp.getUserId());
			pst.execute();
			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DbException(e);
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	public static void ChangePayment(Payment tp) throws BaseException {
		if (tp.getPS() == null || "".equals(tp.getPS())
				|| tp.getPS().length() > 50)
			throw new BusinessException("账目说明必须是1-50字");
		if (tp.getPayOrIncome() == null || "".equals(tp.getPayOrIncome()))
			throw new BusinessException("必须说明支出或是收入");
		if (tp.getPayMoney() == null || "".equals(tp.getPayMoney()))
			throw new BusinessException("必须输入金额");
		if (tp.getPaymentTypeId() <= 0 || "".equals(tp.getPaymentTypeId()))
			throw new BusinessException("支付类别必须是正整数");
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "select payid from payment where payid = ? and userid ='"
					+ userId + "'";
			java.sql.PreparedStatement pst = conn.prepareStatement(sql);
			pst.setInt(1, tp.getPayId());
			java.sql.ResultSet rs = pst.executeQuery();
			if (!rs.next())
				throw new BusinessException("账目不存在");
			rs.close();
			pst.close();
			sql = "update payment set ps = ?,paymoney = ?,paydate = ?,payorincome = ?,paymenttypeid = ? where payid = ? and userid='"
					+ userId + "'";
			pst = conn.prepareStatement(sql);
			pst.setString(1, tp.getPS());
			pst.setBigDecimal(2, tp.getPayMoney());
			pst.setDate(3, new java.sql.Date(tp.getPayDate().getTime()));
			pst.setBoolean(4, tp.getPayOrIncome());
			pst.setInt(5, tp.getPaymentTypeId());
			pst.setInt(6, tp.getPayId());
			UserManager.changeSum(tp.getPayMoney(), tp.getPayOrIncome(),
					tp.getUserId());
			pst.execute();
			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DbException(e);
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	public static void DeletePayment(int payId) throws BaseException {

		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "select payid from payment where payid = ? and userid='"
					+ userId + "'";
			java.sql.PreparedStatement pst = conn.prepareStatement(sql);
			pst.setInt(1, payId);
			java.sql.ResultSet rs = pst.executeQuery();
			if (!rs.next())
				throw new BusinessException("账目不存在");
			rs.close();
			pst.close();
			sql = "select paymoney,payorincome from payment where payid =? and userid='"
					+ userId + "'";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, payId);
			rs = pst.executeQuery();
			if(!rs.next()) throw new BusinessException("账户无余额信息");
			BigDecimal money = rs.getBigDecimal(1);
			Boolean bool = rs.getBoolean(2);
			UserManager.changeSum(money, !bool, userId);
			rs.close();
			pst.close();
			sql = "delete from Payment where payid = ? and userid='" + userId
					+ "'";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, payId);
			pst.execute();
			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DbException(e);
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	public static List<Payment> Search(String keyword) throws BaseException {
		Connection conn = null;
		List<Payment> p = new ArrayList<Payment>();
		try {
			conn = DBUtil.getConnection();
			String sql = "select * from payment where userid='"+userId+"' ";
			if(keyword!=null && !"".equals(keyword))
				sql+=" and (payid like ? or ps like ?)";
			sql+=" order by payid";
			java.sql.PreparedStatement pst=conn.prepareStatement(sql);
			if(keyword!=null && !"".equals(keyword)){
				pst.setString(1, "%"+keyword+"%");
				pst.setString(2, "%"+keyword+"%");	
			}
			java.sql.ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Payment pp = new Payment();
				pp.setPayId(rs.getInt(1));
				pp.setPaymentTypeId(rs.getInt(2));
				pp.setPayMoney(rs.getBigDecimal(4));
				pp.setPayDate(rs.getDate(5));
				pp.setPayOrIncome(rs.getBoolean(6));
				pp.setPS(rs.getString(7));
				p.add(pp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DbException(e);
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return p;
	}

	public static List<Payment> load(int typeid) throws BaseException {
		List<Payment> p = new ArrayList<Payment>();
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "select payid,paymoney,paydate,ps,payorincome from payment "
					+ "where paymenttypeid = ? and userId='" + userId + "'";
			java.sql.PreparedStatement pst = conn.prepareStatement(sql);
			pst.setInt(1, typeid);
			java.sql.ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Payment pp = new Payment();
				pp.setPayId(rs.getInt(1));
				pp.setPayMoney(rs.getBigDecimal(2));
				pp.setPayDate(rs.getDate(3));
				pp.setPS(rs.getString(4));
				pp.setPayOrIncome(rs.getBoolean(5));
				p.add(pp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DbException(e);
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return p;

	}

	public static int StaticsPayment_Type(int paymentTypeId)
			throws BaseException {

		// TODO Auto-generated method stub
		Connection conn = null;
		int sum;
		try {
			conn = DBUtil.getConnection();
			String sql = "select count(p.payid) from paymentType tp,payment p"
					+ " where p.userid='"
					+ userId
					+ "'"
					+ " and tp.paymenttypeid=p.paymenttypeid and p.paymenttypeid=?";
			java.sql.PreparedStatement pst = conn.prepareStatement(sql);
			pst.setInt(1, paymentTypeId);
			java.sql.ResultSet rs = pst.executeQuery();
			if (!rs.next())
				throw new BusinessException("无该类型的账目");
			sum = rs.getInt(1);
			rs.close();
			pst.close();
			return sum;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DbException(e);
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	public static List<Payment> StaticsPayment_Time(String Date)
			throws BaseException {

		Connection conn = null;
		List<Payment> p = new ArrayList<Payment>();
		try {
			conn = DBUtil.getConnection();

			String sql = "select * from payment where paydate like '%" + Date
					+ "%' and userid='"+userId+"' order by paydate";
			java.sql.Statement st = conn.createStatement();
			java.sql.ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				Payment pp = new Payment();
				pp.setPayId(rs.getInt(1));
				pp.setPaymentTypeId(rs.getInt(2));
				pp.setPayMoney(rs.getBigDecimal(4));
				pp.setPayDate(rs.getDate(5));
				pp.setPayOrIncome(rs.getBoolean(6));
				pp.setPS(rs.getString(7));
				p.add(pp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DbException(e);
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return p;
	}
	public static BigDecimal Statics_Time(String Date)
			throws BaseException {

		Connection conn = null;
		
		try {
			conn = DBUtil.getConnection();
			
			String sql = "select paymoney,payorincome from payment where paydate like '%" + Date
					+ "%' and userid='"+userId+"' order by paydate";
			java.sql.Statement st = conn.createStatement();
			java.sql.ResultSet rs = st.executeQuery(sql);
			if(!rs.next()) throw new BusinessException("");
			if(rs.getBoolean(2)==true){
			BigDecimal sum=rs.getBigDecimal(1);
			BigDecimal x = rs.getBigDecimal(1);
			while (rs.next()) {
				if(rs.getBoolean(2)==true)
				    sum=sum.add(rs.getBigDecimal(1));
				else
					 sum=sum.subtract(rs.getBigDecimal(1));
			}
			return sum.subtract(x).abs();
			}
			else{
				BigDecimal sum=rs.getBigDecimal(1);
				BigDecimal x = rs.getBigDecimal(1);
				while (rs.next()) {
					if(rs.getBoolean(2)==true)
					    sum=sum.add(rs.getBigDecimal(1));
					else
						 sum=sum.subtract(rs.getBigDecimal(1));
				}
				return sum.add(x).abs();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DbException(e);
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
	}
}
