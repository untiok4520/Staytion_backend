package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.request.HotelRequestDto;
import com.example.demo.dto.response.HotelResponseDto;
import com.example.demo.service.HotelService;

@RestController
@RequestMapping("/api/admin/hotels")
@CrossOrigin
public class HotelController {
	@Autowired
	private HotelService hotelService;

	@GetMapping("/{id}")
	public HotelResponseDto getById(@PathVariable Long id) {
		return hotelService.getHotelById(id);
	}
	
//	查詢指定使用者的飯店
    @GetMapping("/owner/{ownerId}")
    public List<HotelResponseDto> getByHotel(@PathVariable Long ownerId) {
        return hotelService.getHotelsByOwner(ownerId);
    }

	@PostMapping
	public HotelResponseDto create(@RequestBody HotelRequestDto dto) {
		return hotelService.saveHotel(dto);
	}

	@PutMapping("/{id}")
	public HotelResponseDto update(@PathVariable Long id, @RequestBody HotelRequestDto dto) {
		return hotelService.updateHotel(id, dto);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		hotelService.deleteHotel(id);
	}
}
