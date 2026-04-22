package com.gramswaram.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gramswaram.api.model.Vendor;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {
}
