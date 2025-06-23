package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.request.HotelRequestDto;
import com.example.demo.dto.response.HotelResponseDto;
import com.example.demo.entity.District;
import com.example.demo.entity.Hotel;
import com.example.demo.entity.User;
import com.example.demo.repository.DistrictRepository;
import com.example.demo.repository.HotelRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.AddressParserService;
import com.example.demo.util.GeocodingService;

@Service
public class ArvinHotelService {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DistrictRepository districtRepository;
    
    @Autowired
    private AddressParserService addressParserService;
    
    @Autowired
    private GeocodingService geocodingService;

//    查詢全部飯店(但應該不需要?)
//    public List<HotelResponseDto> getAllHotels() {
//        return hotelRepository.findAll()
//                .stream()
//                .map(this::toDto)
//                .collect(Collectors.toList());
//    }
    
    // 查詢指定飯店的房型
    public List<HotelResponseDto> getHotelsByOwner(Long ownerId) {
        return hotelRepository.findByOwnerId(ownerId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public HotelResponseDto getHotelById(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hotel not found"));
        return toDto(hotel);
    }

    public HotelResponseDto saveHotel(HotelRequestDto dto) {
        Hotel hotel = new Hotel();
        return saveOrUpdate(hotel, dto);
    }

    public HotelResponseDto updateHotel(Long id, HotelRequestDto dto) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hotel not found"));
        return saveOrUpdate(hotel, dto);
    }

    public void deleteHotel(Long id) {
        hotelRepository.deleteById(id);
    }

    private HotelResponseDto saveOrUpdate(Hotel hotel, HotelRequestDto dto) {
        hotel.setHname(dto.getHotelname());
        hotel.setAddress(dto.getAddress());
        hotel.setTel(dto.getTel());
        hotel.setDescription(dto.getDescription());
        
        // 自動解析地址 → District
        District district = addressParserService.parseDistrictFromAddress(dto.getAddress());
        hotel.setDistrict(district);
       
        // 自動轉換地址 → 經緯度
        double[] latLng = geocodingService.getLatLng(dto.getAddress());
        hotel.setLatitude(latLng[0]);
        hotel.setLongitude(latLng[1]);

        if (dto.getOwnerId() != null) {
            User owner = userRepository.findById(dto.getOwnerId())
                    .orElseThrow(() -> new RuntimeException("Owner not found"));
            hotel.setOwner(owner);
        }

        

        return toDto(hotelRepository.save(hotel));
    }

    private HotelResponseDto toDto(Hotel hotel) {
        HotelResponseDto dto = new HotelResponseDto();
        dto.setId(hotel.getId());
        dto.setHotelname(hotel.getHname());
        dto.setAddress(hotel.getAddress());
        dto.setTel(hotel.getTel());
        dto.setDescription(hotel.getDescription());
        dto.setLatitude(hotel.getLatitude());
        dto.setLongitude(hotel.getLongitude());

        if (hotel.getOwner() != null) {
            dto.setOwnerId(hotel.getOwner().getId());
            dto.setOwnerName(hotel.getOwner().getFirstName() + " " + hotel.getOwner().getLastName());
        }

        if (hotel.getDistrict() != null) {
            dto.setDistrictId(hotel.getDistrict().getId());
            dto.setDistrictName(hotel.getDistrict().getDname());
        }

        return dto;
    }
}
