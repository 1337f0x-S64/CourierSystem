package com.example.courier.domain.repositories;

import com.example.courier.domain.delivery.model.Package;
import com.example.courier.domain.identityaccess.model.UserId;

import java.util.List;
import java.util.Optional;


// Repository interface: Delivery Management bounded context.
    // Abstracts persistence for the Package aggregate.
public interface PackageRepository {

    List<Package> findAll();
    List<Package> findWithoutRoute(String descriptionFilter);
    Optional<Package> findById(int packageId);
    boolean save(Package pkg);
    boolean update(Package pkg);
    boolean delete(int packageId);
}
