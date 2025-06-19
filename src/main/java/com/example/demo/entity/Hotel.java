package com.example.demo.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "hotels")
public class Hotel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "hname")
	private String hname;
	
	@Column(name = "address")
	private String address;
	
	@Column(name = "tel")
	private String tel;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "latitude")
	private Double latitude;
	
	@Column(name = "longitude")
	private Double longitude;

	// Constructor
	public Hotel() {

	}

	public Hotel(String hname, String address, String tel, String description, Double latitude, Double longitude) {
		this.hname = hname;
		this.address = address;
		this.tel = tel;
		this.description = description;
		this.latitude = latitude;
		this.longitude = longitude;

	}

	// Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getHname() {
		return hname;
	}

	public void setHname(String hname) {
		this.hname = hname;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	// -------------------------------------
	@ManyToOne
	@JoinColumn(name = "district_id")
	private District district;

	public District getDistrict() {
		return district;
	}

	public void setDistrict(District district) {
		this.district = district;
	}

	// -------------------------------------
	@ManyToOne
	@JoinColumn(name = "owner_id")
	private User owner;

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;

	}

	// -------------------------------------
	@OneToMany(mappedBy = "hotel")
	private List<RoomType> roomTypes;

	public List<RoomType> getRoomTypes() {
		return roomTypes;
	}

	public void setRoomTypes(List<RoomType> roomTypes) {
		this.roomTypes = roomTypes;
	}

	// -------------------------------------
	// // 要再討論看看要不要加
	// private Double rating;
	//
	// public Double getRating() {
	// return rating;
	// }
	//
	// public void setRating(Double rating) {
	// this.rating = rating;
	// }

	// -------------------------------------
	// @Override
	// public String toString() {
	// return "Hotel{" + "id=" + id + ", hname='" + hname + '\'' + ", address='" +
	// address + '\'' + ", tel='" + tel
	// + '\'' + ", districtId=" + district.getId() + ", latitude=" + latitude + ",
	// longitude=" + longitude
	// + ", ownerId=" + owner.getId() + '}';
	// }
}
