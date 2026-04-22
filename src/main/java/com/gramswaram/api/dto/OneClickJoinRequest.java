package com.gramswaram.api.dto;

public class OneClickJoinRequest {
    private String phone;
    private String shopName;
    private String location;
    private Double latitude;
    private Double longitude;
    private String shopPhotoUrl;

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getShopName() { return shopName; }
    public void setShopName(String shopName) { this.shopName = shopName; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public String getShopPhotoUrl() { return shopPhotoUrl; }
    public void setShopPhotoUrl(String shopPhotoUrl) { this.shopPhotoUrl = shopPhotoUrl; }
}
