package com.luminn.firebase.dto;


public class TaxiCompanyDTO extends TaxiPriceDTO{

	public String company;
	public TaxiCompanyDTO(TaxisDTO dto){
		super(dto);
	}

	public TaxiCompanyDTO(){

	}


	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}
}
