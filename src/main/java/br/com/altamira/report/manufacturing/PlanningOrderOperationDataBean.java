package br.com.altamira.report.manufacturing;

import java.math.BigDecimal;
import java.util.Date;

public class PlanningOrderOperationDataBean {

	private BigDecimal quantity;
	private String operDescription;
	private String componentDescription;
	private String color;
	private Long bomNumber;
	private String customer;
	private Date startDate;
	private Date bomDelivery;
	private BigDecimal weight;
	
	public BigDecimal getQuantity() {
		return quantity;
	}
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
	public String getComponentDescription() {
		return componentDescription;
	}
	public void setComponentDescription(String componentDescription) {
		this.componentDescription = componentDescription;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public Long getBomNumber() {
		return bomNumber;
	}
	public void setBomNumber(Long bomNumber) {
		this.bomNumber = bomNumber;
	}
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public BigDecimal getWeight() {
		return weight;
	}
	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}
	public String getOperDescription() {
		return operDescription;
	}
	public void setOperDescription(String operDescription) {
		this.operDescription = operDescription;
	}
	public Date getBomDelivery() {
		return bomDelivery;
	}
	public void setBomDelivery(Date bomDelivery) {
		this.bomDelivery = bomDelivery;
	}
	
}
