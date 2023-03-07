package com.unidapp.manager.api.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "rol_opciones")
public class RolesOptions {
	@Id
	private String _id;
	private String id_rol;
	private String id_opcion;
	private String creacion;
	private String lectura;
	private String modificacion;
	private String eliminacion;
	private String estado;
	private String creado_e;
	private String actualizado_e;


	public String get_id() {
		return this._id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getId_rol() {
		return this.id_rol;
	}

	public void setId_rol(String id_rol) {
		this.id_rol = id_rol;
	}

	public String getId_opcion() {
		return this.id_opcion;
	}

	public void setId_opcion(String id_opcion) {
		this.id_opcion = id_opcion;
	}

	public String getCreacion() {
		return this.creacion;
	}

	public void setCreacion(String creacion) {
		this.creacion = creacion;
	}

	public String getLectura() {
		return this.lectura;
	}

	public void setLectura(String lectura) {
		this.lectura = lectura;
	}

	public String getModificacion() {
		return this.modificacion;
	}

	public void setModificacion(String modificacion) {
		this.modificacion = modificacion;
	}

	public String getEliminacion() {
		return this.eliminacion;
	}

	public void setEliminacion(String eliminacion) {
		this.eliminacion = eliminacion;
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
			", id_rol='" + getId_rol() + "'" +
			", id_opcion='" + getId_opcion() + "'" +
			", creacion='" + getCreacion() + "'" +
			", lectura='" + getLectura() + "'" +
			", modificacion='" + getModificacion() + "'" +
			", eliminacion='" + getEliminacion() + "'" +
			", estado='" + getEstado() + "'" +
			", creado_e='" + getCreado_e() + "'" +
			", actualizado_e='" + getActualizado_e() + "'" +
			"}";
	}
}
