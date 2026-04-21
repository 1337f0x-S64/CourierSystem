package com.example.courier.domain.repositories;

import com.example.courier.modelsPersonalize.Routes;
import javafx.collections.ObservableList;
import java.sql.Timestamp;

/* Repository interface: Delivery Management bounded context.
   Abstracts persistence for the Route aggregate root.
   Enforces Business Rule: a route cannot exist without an associated package.
 */
public interface RouteRepository {

    //Returns all routes with their associated package and courier information.
    ObservableList<Routes> findAll();

    //Returns a single route by its ID, or null if not found
    Routes findById(int routeId);


     //Business Rule: packageId must reference an existing package.

    boolean save(int packageId, String startLocation, String endLocation,
                 String trackingNumber, String optimalPath, String status,
                 int courierId, Timestamp deliveryDate, Timestamp possibleReceptionDate);

    // Updates an existing route
    boolean update(int id, int packageId, String startLocation, String endLocation,
                   String trackingNumber, String optimalPath, String status,
                   int courierId, Timestamp possibleReceptionDate, Timestamp deliveryDate);

    // Removes a route by ID
    boolean delete(int routeId);

    //Marks a route as delivered by recording the reception date and setting status to "d".
    //Business Rule: only couriers can complete a delivery.

    boolean complete(int routeId);
}
