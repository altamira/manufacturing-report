package br.com.altamira.report.manufacturing;

import java.math.BigDecimal;

public class OrderItemProductDataBean {
	private String code;
	private String color;
	private String description;
	private String note = "";
	private BigDecimal quantity;
	private BigDecimal weight;
	private long itemCode;
	private String itemDescription;

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public BigDecimal getQuantity() {
		return quantity;
	}
	public void setQuantity(BigDecimal bigDecimal) {
		this.quantity = bigDecimal;
	}
	public BigDecimal getWeight() {
		return weight;
	}
	public void setWeight(BigDecimal bigDecimal) {
		this.weight = bigDecimal;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public long getItemCode() {
		return itemCode;
	}
	public void setItemCode(long itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemDescription() {
		return itemDescription;
	}
	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}
	
}
