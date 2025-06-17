package com.example.demo.dto;

import java.util.List;

public class HotelSearchResultDTO {
    private String city;
    private String area;
    private String checkin;
    private String checkout;
    private Integer adult;
    private Integer child;
    private Integer room;
    private String price;
    private List<String> facility;
    private Integer score;
    private String sort;
    
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getArea() {
        return area;
    }
    public void setArea(String area) {
        this.area = area;
    }
    public String getCheckin() {
        return checkin;
    }
    public void setCheckin(String checkin) {
        this.checkin = checkin;
    }
    public String getCheckout() {
        return checkout;
    }
    public void setCheckout(String checkout) {
        this.checkout = checkout;
    }
    public Integer getAdult() {
        return adult;
    }
    public void setAdult(Integer adult) {
        this.adult = adult;
    }
    public Integer getChild() {
        return child;
    }
    public void setChild(Integer child) {
        this.child = child;
    }
    public Integer getRoom() {
        return room;
    }
    public void setRoom(Integer room) {
        this.room = room;
    }
    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public List<String> getFacility() {
        return facility;
    }
    public void setFacility(List<String> facility) {
        this.facility = facility;
    }
    public Integer getScore() {
        return score;
    }
    public void setScore(Integer score) {
        this.score = score;
    }
    public String getSort() {
        return sort;
    }
    public void setSort(String sort) {
        this.sort = sort;
    }

    
}
