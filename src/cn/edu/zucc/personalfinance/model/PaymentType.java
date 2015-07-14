package cn.edu.zucc.personalfinance.model;

public class PaymentType {
	private int PaymentTypeId;
	private String userId;
	private String PaymentTypeName;
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
	public String getPaymentTypeName() {
		return PaymentTypeName;
	}
	public void setPaymentTypeName(String paymentTypeName) {
		PaymentTypeName = paymentTypeName;
	}
}
