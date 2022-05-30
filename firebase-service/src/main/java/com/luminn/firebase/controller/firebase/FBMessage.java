package com.luminn.firebase.controller.firebase;

import org.springframework.util.StringUtils;

public class FBMessage {

	private String id;
	private String message;
	private String scheduleDate;
	private String driverName;
	private String carName, status, statusHistory;
	private String createdAt;

	public FBMessage() {
		this.id = Long.toString(System.currentTimeMillis());
		this.createdAt = DemoUtility.currentDateFormate1();
		this.status = DemoConstants.STATUS_MSG_CREATED;
	}

	public FBMessage(String message) {
		this.id = Long.toString(System.currentTimeMillis());
		this.message = message;
		this.createdAt = DemoUtility.currentDateFormate1();
		this.status = DemoConstants.STATUS_MSG_CREATED;
	}

	public String getId() {
		return id;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getDriverName() {
		return driverName;
	}

	public String getCarName() {
		return carName;
	}

	public void setCarName(String carName) {
		this.carName = carName;
	}

	public void setStatus(String status) {
		this.status = status;
		if (StringUtils.isEmpty(this.statusHistory)) {
			this.statusHistory = status;
		} else {
			this.statusHistory += " " + status;
		}
	}

	public String getStatus() {
		return status;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getScheduleDate() {
		return scheduleDate;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setScheduleDate(String scheduleDate) {
		this.scheduleDate = scheduleDate;
	}

	public void setStatusHistory(String statusHistory) {
		this.statusHistory = statusHistory;
	}

	public String getStatusHistory() {
		return statusHistory;
	}

	@Override
	public String toString() {
		return "FBMessage [id=" + id + ", message=" + message + ", driverName=" + driverName + ", carName=" + carName
				+ ", status=" + status + "]";
	}

}
