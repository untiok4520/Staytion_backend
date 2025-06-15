package com.example.demo.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name = "room_amenities")
@IdClass(RoomAmenity.RoomAmenityId.class)
public class RoomAmenity {

	@Id
	@Column(name = "room_type_id")
	private Integer roomTypeId; // 外鍵

	@Id
	@Column(name = "amenity_id")
	private Integer amenityId; // 外鍵

	public static class RoomAmenityId implements Serializable {

		private Long roomTypeId;
		private Long amenityId;

		public RoomAmenityId() {

		}

		public RoomAmenityId(Long roomTypeId, Long amenityId) {
			this.roomTypeId = roomTypeId;
			this.amenityId = amenityId;
		}

		public Long getRoomTypeId() {
			return roomTypeId;
		}

		public void setRoomTypeId(Long roomTypeId) {
			this.roomTypeId = roomTypeId;
		}

		public Long getAmenityId() {
			return amenityId;
		}

		public void setAmenityId(Long amenityId) {
			this.amenityId = amenityId;
		}

		@Override
		public int hashCode() {
			return Objects.hash(roomTypeId, amenityId);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!(obj instanceof RoomAmenityId))
				return false;
			RoomAmenityId other = (RoomAmenityId) obj;
			return Objects.equals(roomTypeId, other.roomTypeId) && Objects.equals(amenityId, other.amenityId);
		}

	}

}
