package com.unidapp.manager.api.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "pqrs_categories")
public class PqrsCategory {
	@Id
	private String _id;
	private String id_establishment;
	private String name;
	private String description;
	private String atention_days;
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

	public String getAtention_days() {
		return this.atention_days;
	}

	public void setAtention_days(String atention_days) {
		this.atention_days = atention_days;
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
			", atention_days='" + getAtention_days() + "'" +
			", created_at='" + getCreated_at() + "'" +
			", updated_at='" + getUpdated_at() + "'" +
			"}";
	}

}
