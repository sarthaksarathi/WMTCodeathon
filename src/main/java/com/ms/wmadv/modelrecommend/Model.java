package com.ms.wmadv.modelrecommend;

import java.io.Serializable;
import java.util.List;

public class Model implements Serializable {
	
	private static final long serialVersionUID = 1L; 

	private Integer rownum;
	
	private String account_name;
	
	private String inv_horizon;
	
	private Integer inv_obj_least;
	
	private Integer inv_obj_most;
	
	private Integer inv_obj_imp;
	
	private Integer inv_obj_some_imp;
	
	private Integer inv_amount;
	
	private String liquidy_need;
	
	private Integer model_id;
	
	private String model_name;
	
	private String primary_fin_need;
	
	private Integer risk_profile;
	
	private Integer risk_tolerance;
	
	private Double volatility;
	
	private List<ModelHolding> modelHoldings;

	public Integer getRownum() {
		return rownum;
	}

	public void setRownum(Integer rownum) {
		this.rownum = rownum;
	}

	public String getAccount_name() {
		return account_name;
	}

	public void setAccount_name(String account_name) {
		this.account_name = account_name;
	}

	public String getInv_horizon() {
		return inv_horizon;
	}

	public void setInv_horizon(String inv_horizon) {
		this.inv_horizon = inv_horizon;
	}

	public Integer getInv_obj_least() {
		return inv_obj_least;
	}

	public void setInv_obj_least(Integer inv_obj_least) {
		this.inv_obj_least = inv_obj_least;
	}

	public Integer getInv_obj_most() {
		return inv_obj_most;
	}

	public void setInv_obj_most(Integer inv_obj_most) {
		this.inv_obj_most = inv_obj_most;
	}

	public Integer getInv_obj_imp() {
		return inv_obj_imp;
	}

	public void setInv_obj_imp(Integer inv_obj_imp) {
		this.inv_obj_imp = inv_obj_imp;
	}

	public Integer getInv_obj_some_imp() {
		return inv_obj_some_imp;
	}

	public void setInv_obj_some_imp(Integer inv_obj_some_imp) {
		this.inv_obj_some_imp = inv_obj_some_imp;
	}

	public Integer getInv_amount() {
		return inv_amount;
	}

	public void setInv_amount(Integer inv_amount) {
		this.inv_amount = inv_amount;
	}

	public String getLiquidy_need() {
		return liquidy_need;
	}

	public void setLiquidy_need(String liquidy_need) {
		this.liquidy_need = liquidy_need;
	}

	public Integer getModel_id() {
		return model_id;
	}

	public void setModel_id(Integer model_id) {
		this.model_id = model_id;
	}

	public String getModel_name() {
		return model_name;
	}

	public void setModel_name(String model_name) {
		this.model_name = model_name;
	}

	public String getPrimary_fin_need() {
		return primary_fin_need;
	}

	public void setPrimary_fin_need(String primary_fin_need) {
		this.primary_fin_need = primary_fin_need;
	}

	public Integer getRisk_profile() {
		return risk_profile;
	}

	public void setRisk_profile(Integer risk_profile) {
		this.risk_profile = risk_profile;
	}

	public Integer getRisk_tolerance() {
		return risk_tolerance;
	}

	public void setRisk_tolerance(Integer risk_tolerance) {
		this.risk_tolerance = risk_tolerance;
	}

	public Double getVolatility() {
		return volatility;
	}

	public void setVolatility(Double volatility) {
		this.volatility = volatility;
	}

	public List<ModelHolding> getModelHoldings() {
		return modelHoldings;
	}

	public void setModelHoldings(List<ModelHolding> modelHoldings) {
		this.modelHoldings = modelHoldings;
	}
		
}
