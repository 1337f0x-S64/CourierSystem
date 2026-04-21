package com.example.courier.domain.services;

import com.example.courier.domain.valueobjects.TrackingNumber;

/*Domain Service: Delivery Management bounded context.
 It handles route optimization logic that does not belong to a single entity.
 The usiness Rule: optimal paths are determined to minimize delivery time and cost.
 */
public class RouteOptimizationService {

    //Calculates the optimal delivery path between two locations. The result is stored as the route's optimalPath field.

    public String calculateOptimalPath(String startLocation, String endLocation) {
        if (startLocation == null || startLocation.isBlank()
                || endLocation == null || endLocation.isBlank()) {
            throw new IllegalArgumentException("Start and end locations are required for route optimization.");
        }
        // Domain logic: direct route as the baseline optimal path.
        return startLocation + " " + endLocation;
    }

    // it generates a unique tracking number for a new package route.
    //Business Rule: each package must have exactly one tracking number.
    public TrackingNumber generateTrackingNumber() {
        return TrackingNumber.generate();
    }
}
