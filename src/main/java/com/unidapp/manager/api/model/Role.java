package com.unidapp.manager.api.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "roles")
public class Role {
	@Id
	private String _id;
	private String nom_rol;
	private String desc_rol;
	private String estado;
	private String creado_e;
	private String actualizado_e;


	public String get_id() {
		return this._id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getNom_rol() {
		return this.nom_rol;
	}

	public void setNom_rol(String nom_rol) {
		this.nom_rol = nom_rol;
	}

	public String getDesc_rol() {
		return this.desc_rol;
	}

	public void setDesc_rol(String desc_rol) {
		this.desc_rol = desc_rol;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getCreado_e() {
		return this.creado_e;
	}

	public void setCreado_e(String creado_e) {
		this.creado_e = creado_e;
	}

	public String getActualizado_e() {
		return this.actualizado_e;
	}

	public void setActualizado_e(String actualizado_e) {
		this.actualizado_e = actualizado_e;
	}


	@Override
	public String toString() {
		return "{" +
			" _id='" + get_id() + "'" +
			", nom_rol='" + getNom_rol() + "'" +
			", desc_rol='" + getDesc_rol() + "'" +
			", estado='" + getEstado() + "'" +
			", creado_e='" + getCreado_e() + "'" +
			", actualizado_e='" + getActualizado_e() + "'" +
			"}";
	}


}
