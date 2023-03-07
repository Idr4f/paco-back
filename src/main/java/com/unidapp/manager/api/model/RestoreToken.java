package com.unidapp.manager.api.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "restore_tokens")
public class RestoreToken {
	@Id
	private String _id;
	private String email;
	private String token;
	private String verified;
	private String created_at;
	private String updated_at;


	public String get_id() {
		return this._id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getVerified() {
		return this.verified;
	}

	public void setVerified(String verified) {
		this.verified = verified;
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
			", email='" + getEmail() + "'" +
			", token='" + getToken() + "'" +
			", verified='" + getVerified() + "'" +
			", created_at='" + getCreated_at() + "'" +
			", updated_at='" + getUpdated_at() + "'" +
			"}";
	}

}
