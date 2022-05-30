package com.luminn.firebase.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;

@Component
public class CityDTO {
	private Long id;
	private String code;
	private String name;
	private String description;
	private String zipCode;
	private Long countryId;
	private String region;
	private String lang;

	
	public CityDTO(){
		
	}

	@JsonCreator
	public CityDTO(@JsonProperty("id") Long id, @JsonProperty("code") String code, @JsonProperty("name") String name){
		this.id = id;
		this.code = code;
		this.name = name;
	}

	@JsonCreator
	public CityDTO(@JsonProperty("id") Long id, @JsonProperty("code") String code, @JsonProperty("name") String name, @JsonProperty("zipCode") String zipCode, @JsonProperty("region") String region){
		this.id = id;
		this.code = code;
		this.name = name;
		this.zipCode = zipCode;
		this.region = region;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}



	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}
}
