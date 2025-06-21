package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.City;
import com.example.demo.entity.District;
import com.example.demo.repository.CityRepository;
import com.example.demo.repository.DistrictRepository;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class LocationController {
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private DistrictRepository districtRepository;

    @Operation(
            summary = "查詢所有城市",
            description = "取得所有城市清單,內容包含:label(城市名稱)、value(城市名稱)"
    )    @GetMapping("/cities")
    public List<Map<String, String>> getCities() {
        List<City> cities = cityRepository.findAll();

        return cities.stream().map(city -> {
            Map<String, String> m = new HashMap<>();
            m.put("label", city.getCname());
            m.put("value", city.getCname());
            return m;
        }).collect(Collectors.toList());
    }

    @Operation(
            summary = "查詢指定城市的所有區域",
            description = "根據城市名稱查詢該城市的所有區域,內容包含:label(區域名稱)、value(區域名稱)"
    )
    @GetMapping("/areas")
    public List<Map<String, String>> getAreaByCity(@RequestParam("city") String cityName) {
        List<District> districts = districtRepository.findByCity_Cname(cityName);

        return districts.stream().map(dist -> {
            Map<String, String> m = new HashMap<>();
            m.put("label", dist.getDname());
            m.put("value", dist.getDname());
            return m;
        }).collect(Collectors.toList());
    }

}
