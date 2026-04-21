package com.example.courier.infrastructure.repositories;

import com.example.courier.db.PackagesDatabase;
import com.example.courier.domain.delivery.model.Package;
import com.example.courier.domain.identityaccess.model.UserId;
import com.example.courier.domain.repositories.PackageRepository;
import com.example.courier.domain.valueobjects.DeliveryStatus;
import com.example.courier.domain.valueobjects.TrackingNumber;
import com.example.courier.modelsPersonalize.Packages;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public boolean save(Package pkg) {
        // Domain ensures tracking number and status exist upon creation
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

    //it bridges the Persistence Model (Packages) to the Domain Aggregate (Package)
    private Package toDomain(Packages p) {
        if (p == null) return null;


        String tracking = (p.getTrackingNumber() != null && !p.getTrackingNumber().isBlank()) 
                          ? p.getTrackingNumber() : "RP-00000000";
        String statusCode = (p.getStatus() != null && !p.getStatus().isBlank()) 
                            ? p.getStatus() : "w"; // 'w' = REGISTERED

        return new Package(
                p.getId(),
                new UserId(p.getUserId()),
                p.getWeight(),
                p.getDescription(),
                TrackingNumber.of(tracking), 
                DeliveryStatus.fromCode(statusCode)
        );
    }
}
