package com.unidapp.manager.api.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "notifications")
public class Notification {
	@Id
	private String _id;
	private String id_establishment;
	private String id_user;
	private String id_category;
	private String id_asset;
	private String message;
	private String status;
	private List<String> atachments;
	private String response;
	private String response_date;
	private String created_at;
	private String updated_at;

	public String get_id() {
		return this._id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getId_establishment() {
		return this.id_establishment;
	}

	public void setId_establishment(String id_establishment) {
		this.id_establishment = id_establishment;
	}

	public String getId_user() {
		return this.id_user;
	}

	public void setId_user(String id_user) {
		this.id_user = id_user;
	}

	public String getId_category() {
		return this.id_category;
	}

	public void setId_category(String id_category) {
		this.id_category = id_category;
	}

	public String getId_asset() {
		return this.id_asset;
	}

	public void setId_asset(String id_asset) {
		this.id_asset = id_asset;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<String> getAtachments() {
		return this.atachments;
	}

	public void setAtachments(List<String> atachments) {
		this.atachments = atachments;
	}

	public String getResponse() {
		return this.response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getResponse_date() {
		return this.response_date;
	}

	public void setResponse_date(String response_date) {
		this.response_date = response_date;
	}

	public String getCreated_at() {
		return this.created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getUpdated_at() {
		return this.updated_at;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}

	@Override
	public String toString() {
		return "{" +
			" _id='" + get_id() + "'" +
			", id_establishment='" + getId_establishment() + "'" +
			", id_user='" + getId_user() + "'" +
			", id_category='" + getId_category() + "'" +
			", id_asset='" + getId_asset() + "'" +
			", message='" + getMessage() + "'" +
			", status='" + getStatus() + "'" +
			", atachments='" + getAtachments() + "'" +
			", response='" + getResponse() + "'" +
			", response_date='" + getResponse_date() + "'" +
			", created_at='" + getCreated_at() + "'" +
			", updated_at='" + getUpdated_at() + "'" +
			"}";
	}

}
