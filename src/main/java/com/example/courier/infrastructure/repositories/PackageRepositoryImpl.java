package com.example.courier.infrastructure.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.courier.db.PackagesDatabase;
import com.example.courier.domain.delivery.model.Package;
import com.example.courier.domain.identityaccess.model.UserId;
import com.example.courier.domain.repositories.PackageRepository;
import com.example.courier.domain.valueobjects.DeliveryStatus;
import com.example.courier.domain.valueobjects.TrackingNumber;
import com.example.courier.modelsPersonalize.Packages;

public class PackageRepositoryImpl implements PackageRepository {


    @Override
    public List<Package> findAll() {
        return PackagesDatabase.loadData().stream()
                .map(this::toDomain)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<Package> findWithoutRoute(String descriptionFilter) {
        return PackagesDatabase.loadDataNoRoute(descriptionFilter).stream()
                .map(this::toDomain)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Package> findById(int packageId) {
        Packages p = PackagesDatabase.loadData(packageId);
        return Optional.ofNullable(p).map(this::toDomain);
    }

    @Override
    public List<Packages> findAllWithOwnerName() {
        return new ArrayList<>(PackagesDatabase.loadData());
    }


    @Override
    public boolean save(Package pkg) {
        return PackagesDatabase.createPackage(
                pkg.getOwnerId().getValue(),
                pkg.getWeight(),
                pkg.getDescription(),
                pkg.getTrackingNumber().getValue(),
                pkg.getStatus().toCode()
        );
    }

    @Override
    public boolean update(Package pkg) {
        return PackagesDatabase.updatePackage(
                pkg.getId(),
                pkg.getOwnerId().getValue(),
                pkg.getWeight(),
                pkg.getDescription(),
                pkg.getTrackingNumber().getValue(),
                pkg.getStatus().toCode()
        );
    }

    @Override
    public boolean delete(int packageId) {
        return PackagesDatabase.deletePackage(packageId);
    }


    private Package toDomain(Packages p) {
        if (p == null) return null;

        String trackingValue = (p.getTrackingNumber() != null
                && !p.getTrackingNumber().isBlank())
                ? p.getTrackingNumber()
                : "RP-00000000"; 

        String statusCode = (p.getStatus() != null
                && !p.getStatus().isBlank())
                ? p.getStatus()
                : "w"; 

        return new Package(
                p.getId(),
                new UserId(p.getUserId()),
                p.getWeight(),
                p.getDescription(),
                TrackingNumber.of(trackingValue),
                DeliveryStatus.fromCode(statusCode)
        );
    }
}