package com.unidapp.manager.api.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "usuario_rol_establecimiento")
public class UserRoleEstablishment {
	@Id
	private String _id;
	private String id_usuario;
	private String id_rol;
	private String id_establecimiento;
	private String num_inmueble;
	private String estado;
	private String creado_e;
	private String actualizado_e;

	public String get_id() {
		return this._id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getId_usuario() {
		return this.id_usuario;
	}

	public void setId_usuario(String id_usuario) {
		this.id_usuario = id_usuario;
	}

	public String getId_rol() {
		return this.id_rol;
	}

	public void setId_rol(String id_rol) {
		this.id_rol = id_rol;
	}

	public String getId_establecimiento() {
		return this.id_establecimiento;
	}

	public void setId_establecimiento(String id_establecimiento) {
		this.id_establecimiento = id_establecimiento;
	}

	public String getNum_inmueble() {
		return this.num_inmueble;
	}

	public void setNum_inmueble(String num_inmueble) {
		this.num_inmueble = num_inmueble;
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
			", id_usuario='" + getId_usuario() + "'" +
			", id_rol='" + getId_rol() + "'" +
			", id_establecimiento='" + getId_establecimiento() + "'" +
			", num_inmueble='" + getNum_inmueble() + "'" +
			", estado='" + getEstado() + "'" +
			", creado_e='" + getCreado_e() + "'" +
			", actualizado_e='" + getActualizado_e() + "'" +
			"}";
	}

}
