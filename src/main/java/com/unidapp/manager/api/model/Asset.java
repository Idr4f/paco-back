package com.unidapp.manager.api.model;

import java.util.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "inmuebles")
public class Asset {
	@Id
	private String _id;
	private String id_establecimiento;
	private String num_inmueble;
	private String numero_fijo;
	private String coeficiente;
	private List<Object> cuartosUtiles;
	private List<Object> vehiculos;
	private List<Object> mascotas;
	private List<Object> celdas;
	private String created_at;
	private String updated_at;

	public String get_id() {
		return this._id;
	}

	public void set_id(String _id) {
		this._id = _id;
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

	public String getNumero_fijo() {
		return this.numero_fijo;
	}

	public void setNumero_fijo(String numero_fijo) {
		this.numero_fijo = numero_fijo;
	}

	public String getCoeficiente() {
		return this.coeficiente;
	}

	public void setCoeficiente(String coeficiente) {
		this.coeficiente = coeficiente;
	}

	public List<Object> getCuartosUtiles() {
		return this.cuartosUtiles;
	}

	public void setCuartosUtiles(List<Object> cuartosUtiles) {
		this.cuartosUtiles = cuartosUtiles;
	}

	public List<Object> getVehiculos() {
		return this.vehiculos;
	}

	public void setVehiculos(List<Object> vehiculos) {
		this.vehiculos = vehiculos;
	}

	public List<Object> getMascotas() {
		return this.mascotas;
	}

	public void setMascotas(List<Object> mascotas) {
		this.mascotas = mascotas;
	}

	public List<Object> getCeldas() {
		return this.celdas;
	}

	public void setCeldas(List<Object> celdas) {
		this.celdas = celdas;
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
			", id_establecimiento='" + getId_establecimiento() + "'" +
			", num_inmueble='" + getNum_inmueble() + "'" +
			", numero_fijo='" + getNumero_fijo() + "'" +
			", coeficiente='" + getCoeficiente() + "'" +
			", cuartosUtiles='" + getCuartosUtiles() + "'" +
			", vehiculos='" + getVehiculos() + "'" +
			", mascotas='" + getMascotas() + "'" +
			", celdas='" + getCeldas() + "'" +
			", created_at='" + getCreated_at() + "'" +
			", updated_at='" + getUpdated_at() + "'" +
			"}";
	}

}
