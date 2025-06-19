package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.History;
import com.example.demo.projection.HistoryProjection;

public interface HistoryRepository extends JpaRepository<History, Long> {
	@Query(value = """
		SELECT c.cname, c.img_url AS imgUrl, h.check_in_date AS checkInDate, h.check_out_date AS checkOutDate, (h.adults+h.kids) AS total
		FROM histories h 
		JOIN cities c ON h.city_id = c.id
		WHERE user_id=:userId
		ORDER BY h.search_time DESC
		""", nativeQuery = true)

	List<HistoryProjection> findHistoriesByUserId(Long userId);
}
