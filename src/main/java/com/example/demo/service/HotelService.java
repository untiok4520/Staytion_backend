package com.example.demo.service;

import org.springframework.data.domain.Page;

import com.example.demo.dto.HotelDetailDTO;
import com.example.demo.dto.HotelSearchRequest;
import com.example.demo.dto.HotelSearchResult;

public interface HotelService {
    Page<HotelSearchRequest> searchHotels(HotelSearchResult dto, int page, int size);

    HotelDetailDTO getHotelDetail(Long hotelId);
}

// @Service
// public class HotelService {
// @Autowired
// private HotelRepository hotelRepository;

// public Page<Hotel> getHotels(int page, int size) {
// Pageable pageable = PageRequest.of(page - 1, size); // page 從 0 開始
// return hotelRepository.findAll(pageable);
// }

// public List<HotelSearchRequestDTO> searchHotels(HotelSearchResultDTO
// resultDTO) {
// List<Hotel> hotels = hotelRepository.findAll();

// return hotels.stream()
// .filter(hotel -> {
// // 城市
// if (resultDTO.getCity() != null && !resultDTO.getCity().isEmpty()) {
// String cityValue = hotel.getDistrict().getCity().getCname();
// if (!cityValue.equalsIgnoreCase(resultDTO.getCity())) {
// return false;
// }
// }
// // 區域
// if (resultDTO.getArea() != null && !resultDTO.getArea().isEmpty()) {
// String areaValue = hotel.getDistrict().getDname();
// if (!areaValue.equalsIgnoreCase(resultDTO.getArea())) {
// return false;
// }
// }
// // 價格
// if (resultDTO.getPrice() != null && !resultDTO.getPrice().isEmpty()) {
// String priceStr = resultDTO.getPrice();
// int min = 0, max = Integer.MAX_VALUE;
// if (priceStr.contains("-")) {
// String[] range = priceStr.split("-");
// min = Integer.parseInt(range[0]);
// max = Integer.parseInt(range[1]);
// } else if (priceStr.endsWith("up")) {
// min = Integer.parseInt(priceStr.replace("+", "").replace("up", ""));
// }
// Integer minRoomPrice = hotel.getRoomTypes().stream()
// .map(rt -> rt.getPrice().intValue())
// .min(Integer::compareTo)
// .orElse(0);
// if (minRoomPrice < min || minRoomPrice > max) {
// return false;
// }
// }
// // 設施
// if (resultDTO.getFacility() != null && !resultDTO.getFacility().isEmpty()) {
// List<String> userFacilities = resultDTO.getFacility();
// boolean hasAll = userFacilities.stream().allMatch(fac ->
// hotel.getRoomTypes().stream()
// .flatMap(rt -> rt.getAmenities().stream())
// .anyMatch(a -> fac.equalsIgnoreCase(a.getAname()))
// );
// if (!hasAll) {
// return false;
// }
// }
// // 評分
// if (resultDTO.getScore() != null) {
// double avg = hotel.getReviews().stream().mapToDouble(r ->
// r.getScore()).average().orElse(0.0);
// if (avg < resultDTO.getScore()) {
// return false;
// }
// }
// // 其他條件可再補
// return true;
// })
// .sorted((h1, h2) -> {
// String sort = resultDTO.getSort();
// if ("price_highest".equals(sort)) {
// int p1 = h1.getRoomTypes().stream().map(rt ->
// rt.getPrice().intValue()).min(Integer::compareTo).orElse(0);
// int p2 = h2.getRoomTypes().stream().map(rt ->
// rt.getPrice().intValue()).min(Integer::compareTo).orElse(0);
// return Integer.compare(p2, p1);
// } else if ("rating_highest".equals(sort)) {
// double r1 = h1.getReviews().stream().mapToDouble(r ->
// r.getScore()).average().orElse(0.0);
// double r2 = h2.getReviews().stream().mapToDouble(r ->
// r.getScore()).average().orElse(0.0);
// return Double.compare(r2, r1);
// }
// return 0;
// })
// .map(hotel -> {
// HotelSearchRequestDTO dto = new HotelSearchRequestDTO();
// dto.setId(hotel.getId());
// dto.setName(hotel.getHname());
// dto.setCity(hotel.getDistrict().getCity().getCname());
// dto.setDistrict(hotel.getDistrict().getDname());
// dto.setLat(hotel.getLatitude());
// dto.setLng(hotel.getLongitude());
// dto.setImgUrl(
// hotel.getImages().stream()
// .filter(img -> Boolean.TRUE.equals(img.getIsCover()))
// .map(img -> img.getImgUrl())
// .findFirst()
// .orElse("https://fakeimg.pl/200x200/?text=No+Image")
// );
// dto.setMapUrl("https://maps.google.com/?q=" + hotel.getLatitude() + "," +
// hotel.getLongitude());
// dto.setRoomType(
// hotel.getRoomTypes().stream()
// .map(rt -> rt.getRname())
// .findFirst()
// .orElse("")
// );
// dto.setPrice(
// hotel.getRoomTypes().stream()
// .map(rt -> rt.getPrice().intValue())
// .min(Integer::compareTo)
// .orElse(0)
// );
// dto.setRating(
// hotel.getReviews().stream()
// .mapToDouble(r -> r.getScore())
// .average().orElse(0.0)
// );
// dto.setNight(getNight(resultDTO.getCheckin(), resultDTO.getCheckout()));
// dto.setAdults(resultDTO.getAdult() != null ? resultDTO.getAdult() : 2);
// return dto;
// })
// .collect(Collectors.toList());
// }
// private Integer getNight(String checkin, String checkout) {
// try {
// if (checkin == null || checkout == null) return null;
// java.time.LocalDate in = java.time.LocalDate.parse(checkin);
// java.time.LocalDate out = java.time.LocalDate.parse(checkout);
// return (int) java.time.temporal.ChronoUnit.DAYS.between(in, out);

// } catch (Exception e) {
// return null;
// }
// }

// public Page<HotelSearchRequestDTO> searchHotels(
// HotelSearchResultDTO resultDTO, int page, int size) {
// List<HotelSearchRequestDTO> filtered = searchHotels(resultDTO);

// int fromIndex = Math.max((page - 1) * size, 0);
// int toIndex = Math.min(fromIndex + size, filtered.size());

// List<HotelSearchRequestDTO> pagedList = fromIndex >= filtered.size() ?
// java.util.Collections.emptyList() : filtered.subList(fromIndex, toIndex);

// Pageable pageable = PageRequest.of(page - 1, size);
// return new org.springframework.data.domain.PageImpl<>(pagedList, pageable,
// filtered.size());

// }
// }
