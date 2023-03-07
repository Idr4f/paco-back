package com.unidapp.manager.api.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "vecinos")
public class Neighbor {
	@Id
	private String _id;
	private String id_usuario;
	private String id_establishment;
	private String tipo_doc;
	private String identificacion;
	private String nombres;
	private String apellidos;
	private String fecha_nacimiento;
	private String sexo;
	private String numero_celular;
	private String foto;
	private String estado;
	private String updated_at;
	private String created_at;



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

	public String getId_establishment() {
		return this.id_establishment;
	}

	public void setId_establishment(String id_establishment) {
		this.id_establishment = id_establishment;
	}

	public String getTipo_doc() {
		return this.tipo_doc;
	}

	public void setTipo_doc(String tipo_doc) {
		this.tipo_doc = tipo_doc;
	}

	public String getIdentificacion() {
		return this.identificacion;
	}

	public void setIdentificacion(String identificacion) {
		this.identificacion = identificacion;
	}

	public String getNombres() {
		return this.nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getApellidos() {
		return this.apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getFecha_nacimiento() {
		return this.fecha_nacimiento;
	}

	public void setFecha_nacimiento(String fecha_nacimiento) {
		this.fecha_nacimiento = fecha_nacimiento;
	}

	public String getSexo() {
		return this.sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String getNumero_celular() {
		return this.numero_celular;
	}

	public void setNumero_celular(String numero_celular) {
		this.numero_celular = numero_celular;
	}

	public String getFoto() {
		return this.foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getUpdated_at() {
		return this.updated_at;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
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
			", id_usuario='" + getId_usuario() + "'" +
			", id_establishment='" + getId_establishment() + "'" +
			", tipo_doc='" + getTipo_doc() + "'" +
			", identificacion='" + getIdentificacion() + "'" +
			", nombres='" + getNombres() + "'" +
			", apellidos='" + getApellidos() + "'" +
			", fecha_nacimiento='" + getFecha_nacimiento() + "'" +
			", sexo='" + getSexo() + "'" +
			", numero_celular='" + getNumero_celular() + "'" +
			", foto='" + getFoto() + "'" +
			", estado='" + getEstado() + "'" +
			", updated_at='" + getUpdated_at() + "'" +
			", created_at='" + getCreated_at() + "'" +
			"}";
	}
	

}
