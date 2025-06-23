package com.example.demo.dto;

public class FavHotelDto {
    private Long id;
    private String name;
    private String city;
    private String imgUrl;

    public FavHotelDto(Long id, String name, String city, String imgUrl) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.imgUrl = imgUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
