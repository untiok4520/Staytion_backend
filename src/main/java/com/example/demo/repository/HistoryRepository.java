package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.History;
import com.example.demo.projection.HistoryProjection;

public interface HistoryRepository extends JpaRepository<History, Long> {
	@Query(value = """
		SELECT c.cname, c.img_url, h.check_in_date, h.check_out_date, (h.adults+h.kids) AS total
		FROM histories h 
		JOIN cities c ON h.city_id = c.id
		WHERE user_id=:userId;
		""", nativeQuery = true)

	List<HistoryProjection> findHistoriesByUserId(Long userId);
}
