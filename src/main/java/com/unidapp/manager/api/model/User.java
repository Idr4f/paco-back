package com.unidapp.manager.api.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class User {
	@Id
	private String _id;
	private String email;
	private String nombre;
	private String password;
	private String estado;
	private String updated_at;
	private String created_at;
	private String attemps;
	private String foto;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getAttemps() {
		return this.attemps;
	}

	public void setAttemps(String attemps) {
		this.attemps = attemps;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	@Override
	public String toString() {
		return "{" + " _id='" + get_id() + "'" + ", email='" + getEmail() + "'" + ", nombre='" + getNombre() + "'"
				+ ", password='" + getPassword() + "'" + ", estado='" + getEstado() + "'" + ", updated_at='"
				+ getUpdated_at() + "'" + ", created_at='" + getCreated_at() + "'" + ", attemps='" + getAttemps() + "'"
				+ ", foto='" + getFoto() + "'" + "}";
	}

}
