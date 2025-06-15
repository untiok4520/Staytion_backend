package com.example.demo.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.stream.Collectors;

import org.hibernate.service.spi.Stoppable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.HotelJsonDto;
import com.example.demo.dto.HotelJsonDto.PostalAddress;
import com.example.demo.dto.HotelJsonDto.Telephone;
import com.example.demo.dto.HotelWrapperDto;
import com.example.demo.entity.District;
import com.example.demo.entity.Hotel;
import com.example.demo.repository.DistrictRepository;
import com.example.demo.repository.HotelRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class HotelImportService {
	@Autowired
	private HotelRepository hotelRepository;

	@Autowired
	private DistrictRepository districtRepository;
	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public void importFromJson() {
		try {
			// 清空原資料並重設 AUTO_INCREMENT
			hotelRepository.deleteAll();
			entityManager.createNativeQuery("ALTER TABLE hotels AUTO_INCREMENT = 1").executeUpdate();

			//載入json
			ObjectMapper mapper = new ObjectMapper();
			File file = new File(
					"src/main/resources/static/data/HotelList.json");
			HotelWrapperDto wrapper = mapper.readValue(file, HotelWrapperDto.class);
			List<HotelJsonDto> dtoList = wrapper.getHotels();
			
			//建立map：城市+行政區 => districtId
			List<District> districts = districtRepository.findAll();
			Map<String, Long> cityTownToDistrictIdMap = districts.stream().filter(d -> d.getCity() != null && d.getDname() != null)
					.collect(Collectors.toMap(
							d->d.getCity().getCname() + d.getDname(), 
							District::getId, 
							(existing, replacement)-> existing));
			//轉換為entity
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
							addr.getCity() != null ? addr.getCity() : "",
							addr.getTown() != null ? addr.getTown() : "",
							addr.getStreetAddress() != null ? addr.getStreetAddress() : "");
					hotel.setAddress(fullAddress);

					// 處理「臺」→「台」
					String city = addr.getCity() != null ? addr.getCity().replace("臺", "台") : "";
					String town = addr.getTown() != null ? addr.getTown().replace("臺", "台") : "";
					String key = city + town;
					Long districtId = cityTownToDistrictIdMap.getOrDefault(key, null);
					if(districtId == null && !key.isBlank()) {
						System.out.println("找不到對應 districtId：「" + key + "」，略過 district 設定");
					}
					hotel.setDistrictId(districtId);

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

				hotels.add(hotel);

			}
			hotelRepository.saveAll(hotels);
			System.out.println("匯入完成" + hotels.size());

		} catch (Exception e) {
			System.out.println(e);
		}

	}
}
