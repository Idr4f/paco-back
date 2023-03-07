package com.unidapp.manager.api.model;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "newsletter")
public class Newsletter {
	@Id
	private String _id;
	private String is_important;
	private String title;
	private String description;
	private List<Object> images;
	private String state;
	private String start_date;
	private String end_date;
	private String author;
	private String link;
	private String type;
	private String btn_text;
	private String id_establishment;
	private String created_at;
	private String updated_at;

	public String get_id() {
		return this._id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getIs_important() {
		return this.is_important;
	}

	public void setIs_important(String is_important) {
		this.is_important = is_important;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Object> getImages() {
		return this.images;
	}

	public void setImages(List<Object> images) {
		this.images = images;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStart_date() {
		return this.start_date;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	public String getEnd_date() {
		return this.end_date;
	}

	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}

	public String getAuthor() {
		return this.author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getLink() {
		return this.link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBtn_text() {
		return this.btn_text;
	}

	public void setBtn_text(String btn_text) {
		this.btn_text = btn_text;
	}

	public String getId_establishment() {
		return this.id_establishment;
	}

	public void setId_establishment(String id_establishment) {
		this.id_establishment = id_establishment;
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
			", is_important='" + getIs_important() + "'" +
			", title='" + getTitle() + "'" +
			", description='" + getDescription() + "'" +
			", images='" + getImages() + "'" +
			", state='" + getState() + "'" +
			", start_date='" + getStart_date() + "'" +
			", end_date='" + getEnd_date() + "'" +
			", author='" + getAuthor() + "'" +
			", link='" + getLink() + "'" +
			", type='" + getType() + "'" +
			", btn_text='" + getBtn_text() + "'" +
			", id_establishment='" + getId_establishment() + "'" +
			", created_at='" + getCreated_at() + "'" +
			", updated_at='" + getUpdated_at() + "'" +
			"}";
	}

}
