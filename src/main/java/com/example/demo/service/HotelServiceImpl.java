package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import com.example.demo.projection.HotelProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.dto.AmenityDTO;
import com.example.demo.dto.HotelDetailDTO;
import com.example.demo.dto.HotelSearchRequest;
import com.example.demo.dto.HotelSearchResult;
import com.example.demo.dto.ImageDTO;
import com.example.demo.dto.RoomTypeDTO;
import com.example.demo.entity.Hotel;
import com.example.demo.repository.AmenityRepository;
import com.example.demo.repository.HotelRepository;
import com.example.demo.repository.ImageRepository;
import com.example.demo.repository.RoomTypeRepository;

@Service
public class HotelServiceImpl implements HotelService {

    @Autowired
    private AmenityRepository amenityRepository;
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private RoomTypeRepository roomTypeRepository;
    @Autowired
    private ImageRepository imageRepository;

    @Override
    public Page<HotelSearchRequest> searchHotels(HotelSearchResult dto, int page, int size) {
        List<Hotel> hotels = hotelRepository.findAll();

        List<HotelSearchRequest> filtered = hotels.stream()
                .filter(hotel -> {
                    if (dto.getAdult() != null || dto.getChild() != null) {
                        int totalPeople = (dto.getAdult() != null ? dto.getAdult() : 0)
                                + (dto.getChild() != null ? dto.getChild() : 0);
                        boolean hasRoom = hotel.getRoomTypes().stream()
                                .anyMatch(rt -> rt.getCapacity() >= totalPeople);
                        if (!hasRoom) return false;
                    }
                    // 城市
                    if (dto.getCity() != null && !dto.getCity().isEmpty()) {
                        String cityValue = hotel.getDistrict().getCity().getCname();
                        if (!cityValue.equalsIgnoreCase(dto.getCity())) {
                            return false;
                        }
                    }
                    // 區域
                    if (dto.getArea() != null && !dto.getArea().isEmpty()) {
                        String areaValue = hotel.getDistrict().getDname();
                        if (!areaValue.equalsIgnoreCase(dto.getArea())) {
                            return false;
                        }
                    }
                    // 價格
                    if (dto.getPrice() != null && !dto.getPrice().isEmpty()) {
                        String priceStr = dto.getPrice();
                        int min = 0, max = Integer.MAX_VALUE;
                        if (priceStr.contains("-")) {
                            String[] range = priceStr.split("-");
                            min = Integer.parseInt(range[0]);
                            max = Integer.parseInt(range[1]);
                        } else if (priceStr.endsWith("up")) {
                            min = Integer.parseInt(priceStr.replace("+", "").replace("up", ""));
                        }
                        Integer minRoomPrice = hotel.getRoomTypes().stream()
                                .map(rt -> rt.getPrice().intValue())
                                .min(Integer::compareTo)
                                .orElse(0);
                        if (minRoomPrice < min || minRoomPrice > max) {
                            return false;
                        }
                    }
                    // 設施
                    if (dto.getAmenity() != null && !dto.getAmenity().isEmpty()) {
                        List<Long> userAmenityIds = dto.getAmenity();
                        boolean hasAll = userAmenityIds.stream().allMatch(fac -> hotel.getRoomTypes().stream()
                                .flatMap(rt -> rt.getAmenities().stream())
                                .anyMatch(a -> fac.equals(a.getId())));
                        if (!hasAll) {
                            return false;
                        }
                    }
                    // 評分
                    if (dto.getScore() != null) {
                        double avg = hotel.getReviews().stream().mapToDouble(r -> r.getScore()).average().orElse(0.0);
                        if (avg < dto.getScore()) {
                            return false;
                        }
                    }
                    return true;
                })
                .sorted((h1, h2) -> {
                    String sort = dto.getSort();
                    if ("price_highest".equals(sort)) {
                        int p1 = h1.getRoomTypes().stream().map(rt -> rt.getPrice().intValue()).min(Integer::compareTo)
                                .orElse(0);
                        int p2 = h2.getRoomTypes().stream().map(rt -> rt.getPrice().intValue()).min(Integer::compareTo)
                                .orElse(0);
                        return Integer.compare(p2, p1);
                    } else if ("rating_highest".equals(sort)) {
                        double r1 = h1.getReviews().stream().mapToDouble(r -> r.getScore()).average().orElse(0.0);
                        double r2 = h2.getReviews().stream().mapToDouble(r -> r.getScore()).average().orElse(0.0);
                        return Double.compare(r2, r1);
                    }
                    return 0;
                })
                .map(hotel -> {
                    HotelSearchRequest out = new HotelSearchRequest();
                    out.setId(hotel.getId());
                    out.setName(hotel.getHname());
                    out.setCity(hotel.getDistrict().getCity().getCname());
                    out.setDistrict(hotel.getDistrict().getDname());
                    out.setLat(hotel.getLatitude());
                    out.setLng(hotel.getLongitude());
                    out.setImgUrl(
                            hotel.getImages().stream()
                                    .filter(img -> Boolean.TRUE.equals(img.getIsCover()))
                                    .map(img -> img.getImgUrl())
                                    .findFirst()
                                    .orElse("https://fakeimg.pl/200x200/?text=No+Image"));
                    out.setMapUrl("https://maps.google.com/?q=" + hotel.getLatitude() + "," + hotel.getLongitude());
                    out.setRoomType(
                            hotel.getRoomTypes().stream()
                                    .map(rt -> rt.getRname())
                                    .findFirst()
                                    .orElse(""));
                    out.setPrice(
                            hotel.getRoomTypes().stream()
                                    .map(rt -> rt.getPrice().intValue())
                                    .min(Integer::compareTo)
                                    .orElse(0));
                    out.setRating(
                            hotel.getReviews().stream()
                                    .mapToDouble(r -> r.getScore())
                                    .average().orElse(0.0));
                    out.setNight(getNight(dto.getCheckin(), dto.getCheckout()));
                    out.setAdults(dto.getAdult() != null ? dto.getAdult() : 2);
                    return out;
                })
                .collect(Collectors.toList());

        // 分頁
        int fromIndex = Math.max((page - 1) * size, 0);
        int toIndex = Math.min(fromIndex + size, filtered.size());
        List<HotelSearchRequest> pagedList = fromIndex >= filtered.size()
                ? java.util.Collections.emptyList()
                : filtered.subList(fromIndex, toIndex);

        Pageable pageable = PageRequest.of(page - 1, size);
        return new PageImpl<>(pagedList, pageable, filtered.size());
    }

    // 飯店詳情
    @Override
    public HotelDetailDTO getHotelDetail(Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow();
        HotelDetailDTO dto = new HotelDetailDTO();
        dto.setId(hotel.getId());
        dto.setHname(hotel.getHname());
        dto.setAddress(hotel.getAddress());
        dto.setDescription(hotel.getDescription());

        // 飯店評分
        Double score = hotel.getReviews() != null && !hotel.getReviews().isEmpty()
                ? hotel.getReviews().stream().mapToDouble(r -> r.getScore()).average().orElse(0.0)
                : null;
        dto.setScore(score);

        // 查飯店圖片
        List<ImageDTO> imageDTOs = imageRepository.findByHotelIdAndIsCover(hotelId, false)
                .stream().map(img -> {
                    ImageDTO imgDTO = new ImageDTO();
                    imgDTO.setId(img.getId());
                    imgDTO.setImgUrl(img.getImgUrl());
                    imgDTO.setIsCover(img.getIsCover());
                    return imgDTO;
                }).collect(Collectors.toList());
        dto.setImages(imageDTOs);

        // 查所有房型
        List<RoomTypeDTO> roomTypeDTOs = roomTypeRepository.findByHotelId(hotelId)
                .stream().map(roomType -> {
                    RoomTypeDTO rtdto = new RoomTypeDTO();
                    rtdto.setId(roomType.getId());
                    rtdto.setRname(roomType.getRname());
                    rtdto.setDescription(roomType.getDescription());
                    rtdto.setPrice(roomType.getPrice());
                    rtdto.setSize(roomType.getSize());
                    rtdto.setView(roomType.getView());
                    rtdto.setImgUrl(roomType.getImgUrl());
                    rtdto.setIsCanceled(roomType.getIsCanceled());
                    rtdto.setQuantity(roomType.getQuantity());
                    rtdto.setBedCount(roomType.getBedCount());
                    rtdto.setBedType(roomType.getBedType());
                    rtdto.setCapacity(roomType.getCapacity());

                    // 查房型圖片
                    List<ImageDTO> roomImages = imageRepository.findByHotelIdAndIsCover(hotelId, true)
                            .stream().map(img -> {
                                ImageDTO imgDTO = new ImageDTO();
                                imgDTO.setId(img.getId());
                                imgDTO.setImgUrl(img.getImgUrl());
                                imgDTO.setIsCover(img.getIsCover());
                                return imgDTO;
                            }).collect(Collectors.toList());
                    rtdto.setImages(roomImages);

                    // 查房型設施
                    List<AmenityDTO> rtAmenities = amenityRepository.findByRoomTypeId(roomType.getId())
                            .stream().map(a -> {
                                AmenityDTO adto = new AmenityDTO();
                                adto.setId(a.getId());
                                adto.setAname(a.getAname());
                                return adto;
                            }).collect(Collectors.toList());
                    rtdto.setAmenities(rtAmenities);

                    return rtdto;
                }).collect(Collectors.toList());
        dto.setRoomTypes(roomTypeDTOs);

        return dto;
    }

    @Override
    public List<HotelProjection> getTopHotels() {
        return hotelRepository.findTopHotels();
    }

    // 共用工具
    private Integer getNight(String checkin, String checkout) {
        try {
            if (checkin == null || checkout == null)
                return null;
            java.time.LocalDate in = java.time.LocalDate.parse(checkin);
            java.time.LocalDate out = java.time.LocalDate.parse(checkout);
            return (int) java.time.temporal.ChronoUnit.DAYS.between(in, out);
        } catch (Exception e) {
            return null;
        }
    }
}