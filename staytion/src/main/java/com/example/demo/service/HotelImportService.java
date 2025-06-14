package com.example.demo.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Condition;

import org.hibernate.service.spi.Stoppable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.HotelJsonDto;
import com.example.demo.dto.HotelJsonDto.PostalAddress;
import com.example.demo.dto.HotelJsonDto.Telephone;
import com.example.demo.dto.HotelWrapperDto;
import com.example.demo.entity.Hotel;
import com.example.demo.repository.HotelRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class HotelImportService {
	@Autowired
	private HotelRepository hotelRepository;

	public void importFromJson() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			File file = new File(
					"/Users/ycchu/Documents/iSpan-JAVA/hotel-project/Staytion_backend/staytion/src/main/resources/data/HotelList.json");
			HotelWrapperDto wrapper = mapper.readValue(file, HotelWrapperDto.class);
			List<HotelJsonDto> dtoList = wrapper.getHotels();
			List<Hotel> hotels = new ArrayList<>();
			for (HotelJsonDto dto : dtoList) {
				if (dto.getHotelName() == null || dto.getHotelName().isBlank()) {
					System.out.println("無飯店名稱，略過");
					continue;
				}
				Hotel hotel = new Hotel();
				// hname
				hotel.setHname(dto.getHotelName());
				// address
				PostalAddress addr = dto.getPostalAddress();
				if (addr != null) {
					String fullAddress = String.format("%s%s%s", 
							addr.getCity() != null ? addr.getCity():"",
							addr.getTown() != null ? addr.getTown():"",
							addr.getStreetAddress() != null ? addr.getStreetAddress():"");
					hotel.setAddress(fullAddress);
				}
				// tel
				List<Telephone> telList = dto.getTelephones();
				if (telList != null && !telList.isEmpty()) {
					hotel.setTel(telList.get(0).getTel());
				}
				// description
				hotel.setDescription(dto.getDescription());
				// 經緯度
				hotel.setLatitude(dto.getPositionLat());
				hotel.setLongitude(dto.getPositionLon());
				// 暫不處理owner_id和district_id
				hotel.setOwnerId(null);
				hotel.setDistrictId(null);
				
				hotels.add(hotel);

			}
			hotelRepository.saveAll(hotels);
			System.out.println("匯入完成" + hotels.size());

		} catch (Exception e) {
			System.out.println(e);
		}

	}
}
