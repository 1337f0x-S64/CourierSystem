package com.example.courier.domain.delivery.repository;

import com.example.courier.domain.delivery.model.Package;
import java.util.List;
import java.util.Optional;


// Interface representing a Repository for managing Package object
// DDD Principle: Repositories encapsulate storage and retrieval of aggregates.
public interface PackageRepository {
    void save(Package pkg); // For both creating and updating a package
    Optional<Package> findById(int id);
    List<Package> findAll();
    boolean deleteById(int id);
}
