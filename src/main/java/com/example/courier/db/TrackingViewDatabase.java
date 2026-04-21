package com.example.courier.db;
import com.example.courier.modelsPersonalize.PackageInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class TrackingViewDatabase {
    public static List<PackageInfo> getPackageByTrackingNumber(String trackingNumber) {
        List<PackageInfo> packageList = new ArrayList<>();

        String query = "SELECT r.tracking_number, p.description, r.start_location, r.end_location, r.status, r.delivery_date FROM routes r JOIN packages p ON r.package_id = p.id WHERE r.tracking_number = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, trackingNumber);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String id = rs.getString("tracking_number");
                String desc = rs.getString("description");
                String start = rs.getString("start_location");
                String end = rs.getString("end_location");
                String status = rs.getString("status");
                String date = (rs.getTimestamp("delivery_date") != null)
                        ? rs.getTimestamp("delivery_date").toString() : "N/A";

                packageList.add(new PackageInfo(id, desc, start, end, status, date));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return packageList;
    }
}