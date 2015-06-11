package cn.edu.zucc.personalfinance.control;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

import org.omg.PortableServer.CurrentOperations;

import cn.edu.zucc.personalfinance.model.BeanUser;
import cn.edu.zucc.personalfinance.util.BaseException;
import cn.edu.zucc.personalfinance.util.BusinessException;
import cn.edu.zucc.personalfinance.util.DBUtil;
import cn.edu.zucc.personalfinance.util.DbException;

public class UserManager {
	public static BeanUser currentUser = null;

	public static void createUser(BeanUser user) throws BaseException {
		if (user.getUserId() == null || "".equals(user.getUserId())
				|| user.getUserId().length() > 20) {
			throw new BusinessException("登陆账号必须是1-20个字");
		}
		if (user.getUserName() == null || "".equals(user.getUserName())
				|| user.getUserName().length() > 50) {
			throw new BusinessException("账号名称必须是1-20个字");
		}
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "select * from BeanUser where userid='"+user.getUserId()+"'";
			java.sql.PreparedStatement pst = conn.prepareStatement(sql);
			java.sql.ResultSet rs = pst.executeQuery();
			if (rs.next())
				throw new BusinessException("登陆账号已经存在");
			rs.close();
			pst.close();
			sql = "insert into BeanUser(userid,username,userpsw,usercreDate,userRemainingSum) values(?,?,?,?,?)";
			pst = conn.prepareStatement(sql);
			pst.setString(1, user.getUserId());
			pst.setString(2, user.getUserName());
			pst.setString(3, user.getUserPsw());
			pst.setTimestamp(4,
					new java.sql.Timestamp(System.currentTimeMillis()));
			pst.setBigDecimal(5, user.getUserRemainingSum());
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

	public static void changeUserPwd(String userid,String oldPwd,String newPwd)throws BaseException{
		if(oldPwd==null) throw new BusinessException("原始密码不能为空");
		if(newPwd==null || "".equals(newPwd) || newPwd.length()>16) throw new BusinessException("必须为1-20个字符");
		Connection conn=null;
		try {
			conn=DBUtil.getConnection();
			String sql="select userpsw from BeanUser where userid=?";
			java.sql.PreparedStatement pst=conn.prepareStatement(sql);
			pst.setString(1,userid);
			java.sql.ResultSet rs=pst.executeQuery();
			if(!rs.next()) throw new BusinessException("登陆账号不 存在");
			if(!oldPwd.equals(rs.getString(1))) throw new BusinessException("原始密码错误");
			rs.close();
			pst.close();
			sql="update BeanUser set userpsw=? where userid=?";
			pst=conn.prepareStatement(sql);
			pst.setString(1, newPwd);
			pst.setString(2, userid);
			pst.execute();
			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DbException(e);
		}
		finally{
			if(conn!=null)
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	public static void resetUserPwd(String userid) throws BaseException {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "select * from BeanUser where userid='"+userid+"'";
			java.sql.PreparedStatement pst = conn.prepareStatement(sql);
			java.sql.ResultSet rs = pst.executeQuery();
			if (!rs.next())
				throw new BusinessException("登陆账号不 存在");
			if (rs.getDate(4) != null)
				throw new BusinessException("账号已停用");
			rs.close();
			pst.close();
			sql = "update BeanUser set userpsw=? where userid='"+userid+"'";
			pst = conn.prepareStatement(sql);
			pst.setString(1, userid);
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

	public static void deleteUser(String userid) throws BaseException {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "select userStopDate from BeanUser where userid='"+userid+"'";
			java.sql.PreparedStatement pst = conn.prepareStatement(sql);
			java.sql.ResultSet rs = pst.executeQuery();
			if (!rs.next())
				throw new BusinessException("登陆账号不 存在");
			rs.close();
			pst.close();
			sql = "update BeanUser set userStopDate=? where userid="+userid;
			pst = conn.prepareStatement(sql);
			pst.setTimestamp(1,
					new java.sql.Timestamp(System.currentTimeMillis()));
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

	public static BeanUser loadUser(String userid) throws BaseException {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "select userid,username,userpsw,usercreDate,userstopDate,userRemainingSum from BeanUser where userid='"+userid+"'";
			java.sql.PreparedStatement pst = conn.prepareStatement(sql);
			java.sql.ResultSet rs = pst.executeQuery();
			if (!rs.next())
				throw new BusinessException("登陆账号不 存在");
			BeanUser u = new BeanUser();
			u.setUserId(rs.getString(1));
			u.setUserName(rs.getString(2));
			u.setUserPsw(rs.getString(3));
			u.setUserCreDate(rs.getDate(4));
			u.setUserStopDate(rs.getDate(5));
			u.setUserRemainingSum(rs.getBigDecimal(6));
			if (u.getUserStopDate() != null)
				throw new BusinessException("该账号已经停用");
			rs.close();
			pst.close();
			return u;
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

	@SuppressWarnings("resource")
	public static void changeSum(BigDecimal money, Boolean bool, String userId)
			throws BaseException {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "select userRemainingSum from beanuser where userid='"+userId+"'";
			java.sql.PreparedStatement pst = conn.prepareStatement(sql);
			java.sql.ResultSet rs = pst.executeQuery();
			if(!rs.next()) throw new BusinessException("账户无余额");
			BigDecimal m = rs.getBigDecimal(1);
			rs.close();
			if (bool == true) {
				sql = "update beanuser set userremainingsum=? where userid='"+userId+"'";
				pst=conn.prepareStatement(sql);
				pst.setBigDecimal(1, m.add(money));
				pst.execute();
				pst.close();
			} else if (bool == false) {
				if (m.compareTo(money) == -1)
					throw new BusinessException("余额不足");
				if (m.compareTo(money) != -1) {
					sql = "update beanuser set userremainingsum=? where userid='"+userId+"'";
					pst = conn.prepareStatement(sql);
					pst.setBigDecimal(1, m.subtract(money));
					pst.execute();
				}
				pst.close();
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
	public static void main(String[] args) {
		
		//test
		try {
			changeSum(new BigDecimal(10),false,"A001");
		} catch (BaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			currentUser = loadUser("A001");
		} catch (BaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(currentUser.getUserRemainingSum());
	}
}
