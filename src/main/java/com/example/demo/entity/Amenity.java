package com.example.demo.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "amenities")
public class Amenity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name="id")
	private Long id;

	@Column(name = "aname", unique = true)
	private String aname;

	@ManyToMany(mappedBy = "amenities") // mappedBy 指向 RoomType 中定義的 "amenities" 屬性
	private Set<RoomType> roomTypes = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAname() {
        return aname;
    }

    public void setAname(String aname) {
        this.aname = aname;
    }

    public Set<RoomType> getRoomTypes() {
        return roomTypes;
    }

    public void setRoomTypes(Set<RoomType> roomTypes) {
        this.roomTypes = roomTypes;
    }

	
	// -------------------------------------
	// @Override
	// public String toString() {
	// return "Amenity{" + "id=" + id + ", aname='" + aname + '\'' + '}';
	// }

}