package com.example.courier.domain.repositories;

import java.util.List;
import java.util.Optional;

import com.example.courier.domain.delivery.model.Package;
import com.example.courier.modelsPersonalize.Packages;

public interface PackageRepository {


    List<Package> findAll();


    List<Package> findWithoutRoute(String descriptionFilter);

    Optional<Package> findById(int packageId);

    boolean save(Package pkg);

    boolean update(Package pkg);

    boolean delete(int packageId);

    List<Packages> findAllWithOwnerName();
}