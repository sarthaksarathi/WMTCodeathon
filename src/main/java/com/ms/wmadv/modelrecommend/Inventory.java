package com.ms.wmadv.modelrecommend;

public class Inventory {

	private String itemName;
	private int quantity;
	
	public Inventory(String itemName, int quantity) {
		super();
		this.itemName = itemName;
		this.quantity = quantity;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	
}
