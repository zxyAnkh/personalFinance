package cn.edu.zucc.personalfinance.control;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cn.edu.zucc.personalfinance.model.BeanUser;
import cn.edu.zucc.personalfinance.model.Payment;
import cn.edu.zucc.personalfinance.model.PaymentType;
import cn.edu.zucc.personalfinance.util.BaseException;
import cn.edu.zucc.personalfinance.util.BusinessException;
import cn.edu.zucc.personalfinance.util.DBUtil;
import cn.edu.zucc.personalfinance.util.DbException;

public class PaymentTypeManager {
	private static String userId = UserManager.currentUser.getUserId();

	public static void CreateType(PaymentType tp) throws BaseException {
		if (tp.getPaymentTypeId() <= 0 || "".equals(tp.getPaymentTypeId())) {
			throw new BusinessException("账目类别必须是正整数");
		}
		if (tp.getPaymentTypeName() == null
				|| "".equals(tp.getPaymentTypeName())
				|| tp.getPaymentTypeName().length() > 50) {
			throw new BusinessException("账号名称必须是1-50个字");
		}
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "select PaymentTypeId from PaymentType where PaymentTypeId=? and userid='"
					+ userId + "'";
			java.sql.PreparedStatement pst = conn.prepareStatement(sql);
			pst.setInt(1, tp.getPaymentTypeId());
			java.sql.ResultSet rs = pst.executeQuery();
			if (rs.next())
				throw new BusinessException("类型已经存在");
			rs.close();
			pst.close();
			sql = "insert into PaymentType(paymenttypeid,paymenttypename,userid) values(?,?,?)";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, tp.getPaymentTypeId());
			pst.setString(2, tp.getPaymentTypeName());
			pst.setString(3, userId);
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

	public static void DeleteType(int id) throws BaseException {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "select paymenttypeid from paymenttype where userid='"
					+ userId + "'" + " and paymenttypeid=?";
			java.sql.PreparedStatement pst = conn.prepareStatement(sql);
			pst.setInt(1, id);
			java.sql.ResultSet rs = pst.executeQuery();
			if (!rs.next())
				throw new BusinessException("登陆账号不 存在");
			if (rs.getInt(1) != id)
				throw new BusinessException("该类别已经被删除");
			rs.close();
			pst.close();
			sql = "select payid from payment where paymenttypeid=?";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, id);
			rs = pst.executeQuery();
			if (rs.next())
				throw new BusinessException("该类别存在账目，无法删除");
			rs.close();
			pst.close();
			sql = "delete from paymenttype where paymenttypeid=? and userid='"
					+ userId + "'";
			pst = conn.prepareStatement(sql);
			pst.setInt(1, id);
			pst.execute();
			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();

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

	public static List<PaymentType> loadType(String userid)
			throws BaseException {

		List<PaymentType> tp = new ArrayList<PaymentType>();
		Connection conn = null;
		userid = userId;
		try {
			conn = DBUtil.getConnection();
			String sql = "select paymenttypeid,userid,paymenttypename from paymentType where userid='"
					+ userid + "' order by paymenttypeid";
			java.sql.Statement st = conn.createStatement();
			java.sql.ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				PaymentType tpp = new PaymentType();
				tpp.setPaymentTypeId(rs.getInt(1));
				tpp.setUserId(rs.getString(2));
				tpp.setPaymentTypeName(rs.getString(3));
				tp.add(tpp);
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
		return tp;

	}

	public static BigDecimal Statics_Money(int id) throws BaseException {
		Connection conn = null;
		if (PaymentManager.StaticsPayment_Type(id) == 0)
			return new BigDecimal(0);
		else {
			try {
				conn = DBUtil.getConnection();
				String sql = "select paymoney from payment where userid='"
						+ userId + "' and paymenttypeid=" + id + "";
				java.sql.Statement st = conn.createStatement();
				java.sql.ResultSet rs = st.executeQuery(sql);
				if (!rs.next())
					throw new BusinessException("");
				BigDecimal sum = rs.getBigDecimal(1);
				while (rs.next()) {
					sum = sum.add(rs.getBigDecimal(1));
				}
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
	}
}
