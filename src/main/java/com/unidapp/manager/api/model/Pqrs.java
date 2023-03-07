package com.unidapp.manager.api.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "pqrs")
public class Pqrs {
	@Id
	private String _id;
	private String id_asset;
	private String asset_num;
	private String id_category;
	private String id_neighbor;
	private String neighbor_name;
	private String consecutive;
	private String topic;
	private String description;
	private String state;
	private String response;
	private Object attachments;
	private String close_date;
	private String created_at;
	private String updated_at;


	public String get_id() {
		return this._id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getId_asset() {
		return this.id_asset;
	}

	public void setId_asset(String id_asset) {
		this.id_asset = id_asset;
	}

	public String getAsset_num() {
		return this.asset_num;
	}

	public void setAsset_num(String asset_num) {
		this.asset_num = asset_num;
	}

	public String getId_category() {
		return this.id_category;
	}

	public void setId_category(String id_category) {
		this.id_category = id_category;
	}

	public String getId_neighbor() {
		return this.id_neighbor;
	}

	public void setId_neighbor(String id_neighbor) {
		this.id_neighbor = id_neighbor;
	}

	public String getNeighbor_name() {
		return this.neighbor_name;
	}

	public void setNeighbor_name(String neighbor_name) {
		this.neighbor_name = neighbor_name;
	}

	public String getConsecutive() {
		return this.consecutive;
	}

	public void setConsecutive(String consecutive) {
		this.consecutive = consecutive;
	}

	public String getTopic() {
		return this.topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getResponse() {
		return this.response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public Object getAttachments() {
		return this.attachments;
	}

	public void setAttachments(Object attachments) {
		this.attachments = attachments;
	}

	public String getClose_date() {
		return this.close_date;
	}

	public void setClose_date(String close_date) {
		this.close_date = close_date;
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
			", id_asset='" + getId_asset() + "'" +
			", asset_num='" + getAsset_num() + "'" +
			", id_category='" + getId_category() + "'" +
			", id_neighbor='" + getId_neighbor() + "'" +
			", neighbor_name='" + getNeighbor_name() + "'" +
			", consecutive='" + getConsecutive() + "'" +
			", topic='" + getTopic() + "'" +
			", description='" + getDescription() + "'" +
			", state='" + getState() + "'" +
			", response='" + getResponse() + "'" +
			", attachments='" + getAttachments() + "'" +
			", close_date='" + getClose_date() + "'" +
			", created_at='" + getCreated_at() + "'" +
			", updated_at='" + getUpdated_at() + "'" +
			"}";
	}

}
