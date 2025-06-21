package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.District;
import com.example.demo.entity.Hotel;

public interface DistrictRepository extends JpaRepository<District, Long>{
	List<District> findByCityId(Long cityId);
}
