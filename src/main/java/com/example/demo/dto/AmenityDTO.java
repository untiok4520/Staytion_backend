package com.example.demo.dto;

public class AmenityDTO {
    private Long id;
    private String aname;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAname() {
        return aname;
    }

    public void setAname(String aname) {
        this.aname = aname;
    }

    public AmenityDTO() {}
    public AmenityDTO(Long id, String aname) {
        this.id = id;
        this.aname = aname;
    }
}
