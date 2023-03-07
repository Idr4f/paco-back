package com.unidapp.manager.api.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "establecimientos")
public class Establishment {
	@Id
	private String _id;	
	private String cod_establec;
	private String nom_establec;
	private String nom_corto_establec;
	private String nom_administrador;
	private String telefono;
	private String celular;
	private String direccion;
	private String correo;
	private String tipo_establec;
	private String estado;
	private String creado_e;
	private String actualizado_e;
	private String ruta_imagen_establ;
	

	public String get_id() {
		return this._id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getCod_establec() {
		return this.cod_establec;
	}

	public void setCod_establec(String cod_establec) {
		this.cod_establec = cod_establec;
	}

	public String getNom_establec() {
		return this.nom_establec;
	}

	public void setNom_establec(String nom_establec) {
		this.nom_establec = nom_establec;
	}

	public String getNom_corto_establec() {
		return this.nom_corto_establec;
	}

	public void setNom_corto_establec(String nom_corto_establec) {
		this.nom_corto_establec = nom_corto_establec;
	}

	public String getNom_administrador() {
		return this.nom_administrador;
	}

	public void setNom_administrador(String nom_administrador) {
		this.nom_administrador = nom_administrador;
	}

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getCelular() {
		return this.celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getCorreo() {
		return this.correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getTipo_establec() {
		return this.tipo_establec;
	}

	public void setTipo_establec(String tipo_establec) {
		this.tipo_establec = tipo_establec;
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

	public String getruta_imagen_establ() {
		return this.ruta_imagen_establ;
	}

	public void setruta_imagen_establ(String ruta_imagen_establ) {
		this.ruta_imagen_establ = ruta_imagen_establ;
	}


	

	@Override
	public String toString() {
		return "{" +
			" _id='" + get_id() + "'" +
			", cod_establec='" + getCod_establec() + "'" +
			", nom_establec='" + getNom_establec() + "'" +
			", nom_corto_establec='" + getNom_corto_establec() + "'" +
			", nom_administrador='" + getNom_administrador() + "'" +
			", telefono='" + getTelefono() + "'" +
			", celular='" + getCelular() + "'" +
			", direccion='" + getDireccion() + "'" +
			", correo='" + getCorreo() + "'" +
			", tipo_establec='" + getTipo_establec() + "'" +
			", estado='" + getEstado() + "'" +
			", creado_e='" + getCreado_e() + "'" +
			", actualizado_e='" + getActualizado_e() + "'" +
			", ruta_imagen_establ='" + getruta_imagen_establ() + "'" +
			"}";
	}

}
