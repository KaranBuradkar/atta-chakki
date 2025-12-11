package com.atachakki.components.address;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Address a WHERE a.id = ?1")
    boolean deleteIfExistById(Long addressId);

    Optional<Address> findByLandmarkAndCityAndDistrictAndStateAndCountryAndPostalCode(String landmark, String city, String district, String state, String country, String postalCode);
}