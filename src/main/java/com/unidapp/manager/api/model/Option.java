package com.unidapp.manager.api.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "opciones")
public class Option {
	@Id
	private String _id;
	private String nom_opcion;
	private String desc_opcion;
	private String app_establec;
	private String app_miembro;
	private String estado;
	private String creado_e;
	private String actualizado_e;


	public String get_id() {
		return this._id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getNom_opcion() {
		return this.nom_opcion;
	}

	public void setNom_opcion(String nom_opcion) {
		this.nom_opcion = nom_opcion;
	}

	public String getDesc_opcion() {
		return this.desc_opcion;
	}

	public void setDesc_opcion(String desc_opcion) {
		this.desc_opcion = desc_opcion;
	}

	public String getApp_establec() {
		return this.app_establec;
	}

	public void setApp_establec(String app_establec) {
		this.app_establec = app_establec;
	}
	

	public String getApp_miembro() {
		return this.app_miembro;
	}

	public void setApp_miembro(String app_miembro) {
		this.app_miembro = app_miembro;
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
			", nom_opcion='" + getNom_opcion() + "'" +
			", desc_opcion='" + getDesc_opcion() + "'" +
			", app_establec='" + getApp_establec() + "'" +
			", estado='" + getEstado() + "'" +
			", creado_e='" + getCreado_e() + "'" +
			", actualizado_e='" + getActualizado_e() + "'" +
			"}";
	}

}
