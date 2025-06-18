package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.request.RoomTypeRequestDto;
import com.example.demo.dto.response.RoomTypeResponseDto;
import com.example.demo.service.RoomTypeService;

@RestController
@RequestMapping("/api/admin/roomTypes")
@CrossOrigin
public class RoomTypeController {

    @Autowired
    private RoomTypeService roomTypeService;

    @GetMapping
    public List<RoomTypeResponseDto> getAll() {
        return roomTypeService.getAllRoomTypes();
    }

    // 查詢指定飯店的房型
    @GetMapping("/hotel/{hotelId}")
    public List<RoomTypeResponseDto> getByHotel(@PathVariable Long hotelId) {
        return roomTypeService.getRoomTypesByHotel(hotelId);
    }

    // 分頁與搜尋（關鍵字）
    @GetMapping("/search")
    public Page<RoomTypeResponseDto> searchRoomTypes(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return roomTypeService.search(keyword, page, size);
    }

    // 新增
    @PostMapping
    public RoomTypeResponseDto createOrUpdate(@RequestBody RoomTypeRequestDto dto) {
        return roomTypeService.saveRoomType(dto);
    }

    // 更新
    @PutMapping("/{id}")
    public RoomTypeResponseDto update(@PathVariable Long id, @RequestBody RoomTypeRequestDto dto) {
        return roomTypeService.updateRoomType(id, dto);
    }

    // 刪除
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        roomTypeService.deleteRoomType(id);
    }

    // 查詢特定使用者房型總計
    /*
     * @GetMapping("/summary/owner/{ownerId}")
     * public List<RoomTypeSummaryDto> getSummaryByOwner(
     * 
     * @PathVariable Long ownerId,
     * 
     * @RequestParam String type
     * ) {
     * return roomTypeService.getOwnerRoomTypeSummary(ownerId, type);
     * }
     * 
     * @GetMapping("/summary/owner/{ownerId}/combined")
     * public RoomTypeSummaryDto getCombinedSummary(
     * 
     * @PathVariable Long ownerId,
     * 
     * @RequestParam String type
     * ) {
     * return roomTypeService.getCombinedRoomTypeSummary(ownerId, type);
     * }
     */
}
     