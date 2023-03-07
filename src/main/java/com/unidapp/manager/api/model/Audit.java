package com.unidapp.manager.api.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "audit")
public class Audit {
	@Id
	private String _id;
	private String action;
	private String entity;
	private String user_id;
	private Object old_data;
	private Object new_data;
	private String created_at;

	public String get_id() {
		return this._id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getAction() {
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getEntity() {
		return this.entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public String getUser_id() {
		return this.user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public Object getOld_data() {
		return this.old_data;
	}

	public void setOld_data(Object old_data) {
		this.old_data = old_data;
	}

	public Object getNew_data() {
		return this.new_data;
	}

	public void setNew_data(Object new_data) {
		this.new_data = new_data;
	}

	public String getCreated_at() {
		return this.created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	@Override
	public String toString() {
		return "{" +
			" _id='" + get_id() + "'" +
			", action='" + getAction() + "'" +
			", entity='" + getEntity() + "'" +
			", user_id='" + getUser_id() + "'" +
			", old_data='" + getOld_data().toString() + "'" +
			", new_data='" + getNew_data().toString() + "'" +
			", created_at='" + getCreated_at() + "'" +
			"}";
	}
	
}
