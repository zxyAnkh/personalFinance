package cn.edu.zucc.personalfinance.model;

import java.math.BigDecimal;
import java.util.Date;

public class BeanUser {
	private String userId;
	private String userPsw;
	private Date userCreDate;
	private Date userStopDate;
	private BigDecimal userRemainingSum;
	private String userName;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserPsw() {
		return userPsw;
	}
	public void setUserPsw(String userPsw) {
		this.userPsw = userPsw;
	}
	public Date getUserCreDate() {
		return userCreDate;
	}
	public void setUserCreDate(Date userCreDate) {
		this.userCreDate = userCreDate;
	}
	public Date getUserStopDate() {
		return userStopDate;
	}
	public void setUserStopDate(Date userStopDate) {
		this.userStopDate = userStopDate;
	}
	public BigDecimal getUserRemainingSum() {
		return userRemainingSum;
	}
	public void setUserRemainingSum(BigDecimal userRemainingSum) {
		this.userRemainingSum = userRemainingSum;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
