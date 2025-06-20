package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.HotelImportService;

@RestController
@RequestMapping("/api/import")
public class HotelImportController {
	@Autowired
	private HotelImportService hotelImportService;
	
	@PostMapping("/hotels")
	public ResponseEntity<String> importHotels(){
		try {
			hotelImportService.importFromJson();
			return ResponseEntity.ok("飯店資料匯入完成");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("匯入失敗：" + e.getMessage());
		}
	}
}
