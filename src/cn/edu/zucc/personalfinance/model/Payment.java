package cn.edu.zucc.personalfinance.model;

import java.math.BigDecimal;
import java.util.Date;

public class Payment {
	private int PayId;
	private int PaymentTypeId;
	private String userId;
	private BigDecimal PayMoney;
	private Date PayDate;
	private Boolean PayOrIncome;
	private String PS;
	public int getPayId() {
		return PayId;
	}
	public void setPayId(int payId) {
		PayId = payId;
	}
	public int getPaymentTypeId() {
		return PaymentTypeId;
	}
	public void setPaymentTypeId(int paymentTypeId) {
		PaymentTypeId = paymentTypeId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public BigDecimal getPayMoney() {
		return PayMoney;
	}
	public void setPayMoney(BigDecimal payMoney) {
		PayMoney = payMoney;
	}
	public Date getPayDate() {
		return PayDate;
	}
	public void setPayDate(Date payDate) {
		PayDate = payDate;
	}
	public Boolean getPayOrIncome() {
		return PayOrIncome;
	}
	public void setPayOrIncome(Boolean payOrIncome) {
		PayOrIncome = payOrIncome;
	}
	public String getPS() {
		return PS;
	}
	public void setPS(String pS) {
		PS = pS;
	}
}
