package com.gramswaram.api.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gramswaram.api.model.Product;
import com.gramswaram.api.model.Vendor;
import com.gramswaram.api.repository.ProductRepository;
import com.gramswaram.api.repository.VendorRepository;

@Configuration
public class SeedDataConfig {

    @Bean
    CommandLineRunner seedInitialData(VendorRepository vendorRepository, ProductRepository productRepository) {
        return args -> {
            if (vendorRepository.count() > 0 || productRepository.count() > 0) {
                return;
            }

            Vendor villageShop1 = createVendor("Rythu Kirana - Kothapalli", "9000000001", "Kothapalli Village", 16.5595, 80.7921);
            Vendor villageShop2 = createVendor("Sri Lakshmi Stores - Ramapuram", "9000000002", "Ramapuram Village", 16.5412, 80.8110);
            Vendor villageShop3 = createVendor("Annapurna Shop - Velpuru", "9000000003", "Velpuru Village", 16.5763, 80.7692);
            Vendor villageShop4 = createVendor("Maa Ooru Mart - Chintaluru", "9000000004", "Chintaluru Village", 16.5898, 80.8344);
            Vendor mandalShop1 = createVendor("Mandal Ration Depot - Rajupalem", "9000000011", "Rajupalem Mandal Center", 16.6062, 80.7837);
            Vendor mandalShop2 = createVendor("Fresh Basket - Nandigama", "9000000012", "Nandigama Mandal Center", 16.7711, 80.2850);

            List<Vendor> savedVendors = vendorRepository.saveAll(List.of(
                villageShop1, villageShop2, villageShop3, villageShop4, mandalShop1, mandalShop2
            ));

            List<Product> products = List.of(
                createProduct("బియ్యం", "Rice (Sona Masuri)", "ధాన్యాలు", "Grains", 56.0, 120, savedVendors.get(0)),
                createProduct("పప్పు", "Toor Dal", "పప్పులు", "Pulses", 145.0, 80, savedVendors.get(0)),
                createProduct("ఆపిల్", "Apple", "పండ్లు", "Fruits", 180.0, 40, savedVendors.get(1)),
                createProduct("అరటి", "Banana", "పండ్లు", "Fruits", 50.0, 100, savedVendors.get(1)),
                createProduct("బియ్యం", "Rice (Boiled)", "ధాన్యాలు", "Grains", 52.0, 90, savedVendors.get(2)),
                createProduct("పెసర పప్పు", "Moong Dal", "పప్పులు", "Pulses", 132.0, 70, savedVendors.get(2)),
                createProduct("మామిడి", "Mango", "పండ్లు", "Fruits", 120.0, 60, savedVendors.get(3)),
                createProduct("ద్రాక్ష", "Grapes", "పండ్లు", "Fruits", 95.0, 55, savedVendors.get(3)),
                createProduct("ఐస్ క్రీం కప్", "Vanilla Ice Cream Cup", "ఐస్ క్రీమ్స్", "Ice Creams", 35.0, 150, savedVendors.get(4)),
                createProduct("ఐస్ క్రీం బార్", "Chocolate Ice Cream Bar", "ఐస్ క్రీమ్స్", "Ice Creams", 40.0, 140, savedVendors.get(4)),
                createProduct("బియ్యం", "Rice Premium", "ధాన్యాలు", "Grains", 62.0, 110, savedVendors.get(5)),
                createProduct("సెనగ పప్పు", "Chana Dal", "పప్పులు", "Pulses", 98.0, 75, savedVendors.get(5))
            );

            productRepository.saveAll(products);
        };
    }

    private Vendor createVendor(String name, String phone, String location, Double latitude, Double longitude) {
        Vendor vendor = new Vendor();
        vendor.setName(name);
        vendor.setPhone(phone);
        vendor.setLocation(location);
        vendor.setLatitude(latitude);
        vendor.setLongitude(longitude);
        return vendor;
    }

    private Product createProduct(String teluguName, String englishName, String categoryTelugu, String categoryEnglish,
                                  Double price, Integer quantity, Vendor vendor) {
        Product product = new Product();
        product.setItem_name_telugu(teluguName);
        product.setItem_name_english(englishName);
        product.setCategoryTelugu(categoryTelugu);
        product.setCategoryEnglish(categoryEnglish);
        product.setPrice(price);
        product.setAvailableQuantity(quantity);
        product.setVendor(vendor);
        return product;
    }
}
