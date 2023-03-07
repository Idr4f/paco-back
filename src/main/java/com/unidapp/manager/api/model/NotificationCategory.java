package com.unidapp.manager.api.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "notification_category")
public class NotificationCategory {
	@Id
	private String _id;
	private String id_establishment;
	private String name;
	private String description;
	private String criticity;
	private String require_answer;
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

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCriticity() {
		return this.criticity;
	}

	public void setCriticity(String criticity) {
		this.criticity = criticity;
	}

	public String getRequire_answer() {
		return this.require_answer;
	}

	public void setRequire_answer(String require_answer) {
		this.require_answer = require_answer;
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
			", name='" + getName() + "'" +
			", description='" + getDescription() + "'" +
			", criticity='" + getCriticity() + "'" +
			", require_answer='" + getRequire_answer() + "'" +
			", created_at='" + getCreated_at() + "'" +
			", updated_at='" + getUpdated_at() + "'" +
			"}";
	}

}
