package com.example.courier.infrastructure.repositories;

import com.example.courier.db.DeliveryViewsDatabase;
import com.example.courier.domain.repositories.RouteRepository;
import com.example.courier.modelsPersonalize.Routes;
import javafx.collections.ObservableList;
import java.sql.Timestamp;

//Infrastructure implementation of RouteRepository.
//it delgates to the existing DeliveryViewsDatabase.

public class RouteRepositoryImpl implements RouteRepository {

    @Override
    public ObservableList<Routes> findAll() {
        return DeliveryViewsDatabase.loadData();
    }

    @Override
    public Routes findById(int routeId) {
        return DeliveryViewsDatabase.loadRoute(routeId);
    }

    @Override
    public boolean save(int packageId, String startLocation, String endLocation,
                        String trackingNumber, String optimalPath, String status,
                        int courierId, Timestamp deliveryDate, Timestamp possibleReceptionDate) {
        return DeliveryViewsDatabase.createRoute(packageId, startLocation, endLocation,
                trackingNumber, optimalPath, status, courierId, deliveryDate, possibleReceptionDate);
    }

    @Override
    public boolean update(int id, int packageId, String startLocation, String endLocation,
                          String trackingNumber, String optimalPath, String status,
                          int courierId, Timestamp possibleReceptionDate, Timestamp deliveryDate) {
        return DeliveryViewsDatabase.updateRoute(id, packageId, startLocation, endLocation,
                trackingNumber, optimalPath, status, courierId, possibleReceptionDate, deliveryDate);
    }

    @Override
    public boolean delete(int routeId) {
        return DeliveryViewsDatabase.deleteRoute(routeId);
    }

    @Override
    public boolean complete(int routeId) {
        return DeliveryViewsDatabase.completeRoute(routeId);
    }
}
