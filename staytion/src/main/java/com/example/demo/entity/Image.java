package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "images")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "hotel_id")
    private Integer hotelId;

    @Column(name = "img_url")
    private String imgUrl;

    @Column(name = "is_cover")
    private Boolean isCover;
    
//    @ManyToOne
//    @JoinColumn(name = "") 
    
//    constructor
    public Image() {

    }

    // Getter / Setter
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getHotelId() { return hotelId; }
    public void setHotelId(Integer hotelId) { this.hotelId = hotelId; }

    public String getImgUrl() { return imgUrl; }
    public void setImgUrl(String imgUrl) { this.imgUrl = imgUrl; }

    public Boolean getIsCover() { return isCover; }
    public void setIsCover(Boolean isCover) { this.isCover = isCover; }
}
