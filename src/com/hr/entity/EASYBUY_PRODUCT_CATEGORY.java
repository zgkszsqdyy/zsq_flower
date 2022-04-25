package com.hr.entity;

//商品分类
public class EASYBUY_PRODUCT_CATEGORY {
	private int EPC_ID;
	private String EPC_NAME;
	private int EPC_PARENT_ID;
	public EASYBUY_PRODUCT_CATEGORY(int ePCID, String ePCNAME, int ePCPARENTID) {
		//super();
		this.EPC_ID = ePCID;
		this.EPC_NAME = ePCNAME;
		this.EPC_PARENT_ID = ePCPARENTID;
	}
	public int getEPC_ID() {
		return EPC_ID;
	}
	public void setEPC_ID(int ePCID) {
		EPC_ID = ePCID;
	}
	public String getEPC_NAME() {
		return EPC_NAME;
	}
	public void setEPC_NAME(String ePCNAME) {
		EPC_NAME = ePCNAME;
	}
	public int getEPC_PARENT_ID() {
		return EPC_PARENT_ID;
	}
	public void setEPC_PARENT_ID(int ePCPARENTID) {
		EPC_PARENT_ID = ePCPARENTID;
	}
}
