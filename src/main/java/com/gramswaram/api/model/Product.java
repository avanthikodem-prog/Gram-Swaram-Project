package com.gramswaram.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String item_name_telugu;
    private String item_name_english;
    private String categoryTelugu;
    private String categoryEnglish;
    private Double price;
    private Integer availableQuantity;

    @ManyToOne
    @JoinColumn(name = "vendor_id")
    @JsonIgnoreProperties("products")
    private Vendor vendor;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getItem_name_telugu() { return item_name_telugu; }
    public void setItem_name_telugu(String item_name_telugu) { this.item_name_telugu = item_name_telugu; }

    public String getItem_name_english() { return item_name_english; }
    public void setItem_name_english(String item_name_english) { this.item_name_english = item_name_english; }

    public String getCategoryTelugu() { return categoryTelugu; }
    public void setCategoryTelugu(String categoryTelugu) { this.categoryTelugu = categoryTelugu; }

    public String getCategoryEnglish() { return categoryEnglish; }
    public void setCategoryEnglish(String categoryEnglish) { this.categoryEnglish = categoryEnglish; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getAvailableQuantity() { return availableQuantity; }
    public void setAvailableQuantity(Integer availableQuantity) { this.availableQuantity = availableQuantity; }

    public Vendor getVendor() { return vendor; }
    public void setVendor(Vendor vendor) { this.vendor = vendor; }

    @Transient
    @JsonProperty("displayName")
    public String getDisplayName() {
        return safe(item_name_english) + " - " + safe(item_name_telugu);
    }

    @Transient
    @JsonProperty("displayCategory")
    public String getDisplayCategory() {
        return safe(categoryEnglish) + " - " + safe(categoryTelugu);
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }
}