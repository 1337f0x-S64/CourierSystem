package com.example.courier.db;

import com.example.courier.modelsPersonalize.Packages;
import com.example.courier.utils.messages.Message;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class PackagesDatabase {


    public static ObservableList<Packages> loadData() {
        String sqlQuery = """
                SELECT p.id,
                       p.user_id,
                       u.name  AS name_user,
                       p.weight,
                       p.description,
                       p.tracking_number,
                       p.status
                FROM   public.packages  p
                INNER JOIN public.users u ON u.id = p.user_id
                """;
        ObservableList<Packages> packages = FXCollections.observableArrayList();
        try (Connection connection = DatabaseConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs   = stmt.executeQuery(sqlQuery)) {

            while (rs.next()) {
                Packages pkg = new Packages(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("name_user"),
                        rs.getDouble("weight"),
                        rs.getString("description")
                );
                pkg.setTrackingNumber(rs.getString("tracking_number"));
                pkg.setStatus(rs.getString("status"));
                packages.add(pkg);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return packages;
    }

    public static ObservableList<Packages> loadDataNoRoute(String searchPackage) {
        String sqlQuery = """
                SELECT p.id,
                       p.user_id,
                       u.name  AS name_user,
                       p.weight,
                       p.description,
                       p.tracking_number,
                       p.status
                FROM   public.packages  p
                INNER JOIN public.users u ON u.id = p.user_id
                LEFT  JOIN public.routes r ON r.package_id = p.id
                WHERE  r.package_id IS NULL
                AND    p.description ILIKE '%' || ? || '%'
                """;
        ObservableList<Packages> packages = FXCollections.observableArrayList();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {

            stmt.setString(1, searchPackage == null ? "" : searchPackage);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Packages pkg = new Packages(
                            rs.getInt("id"),
                            rs.getInt("user_id"),
                            rs.getString("name_user"),
                            rs.getDouble("weight"),
                            rs.getString("description")
                    );
                    pkg.setTrackingNumber(rs.getString("tracking_number"));
                    pkg.setStatus(rs.getString("status"));
                    packages.add(pkg);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return packages;
    }

    /**
     * Inserts a new package row.
     * Called by PackageRepositoryImpl.save() after domain validation.
     *
     * CHANGE: now accepts trackingNumber and statusCode so the domain
     * aggregate's generated tracking number and initial status are persisted.
     * The old signature only stored user_id, weight, and description.
     */
    public static Boolean createPackage(int userId,
                                        double weight,
                                        String description,
                                        String trackingNumber,
                                        String statusCode) {
        String sqlQuery = """
                INSERT INTO public.packages
                    (user_id, weight, description, tracking_number, status)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sqlQuery)) {

            ps.setInt(1, userId);
            ps.setDouble(2, weight);
            ps.setString(3, description);
            ps.setString(4, trackingNumber);
            ps.setString(5, statusCode);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Boolean updatePackage(int id,
                                        int userId,
                                        double weight,
                                        String description,
                                        String trackingNumber,
                                        String statusCode) {
        String sqlQuery = """
                UPDATE public.packages
                SET    user_id         = ?,
                       weight          = ?,
                       description     = ?,
                       tracking_number = ?,
                       status          = ?
                WHERE  id = ?
                """;
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sqlQuery)) {

            ps.setInt(1, userId);
            ps.setDouble(2, weight);
            ps.setString(3, description);
            ps.setString(4, trackingNumber);
            ps.setString(5, statusCode);
            ps.setInt(6, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Boolean deletePackage(int packageId) {
        String sqlQuery = "DELETE FROM public.packages WHERE id = ?;";
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sqlQuery)) {

            ps.setInt(1, packageId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            if (e.getMessage() != null
                    && e.getMessage().contains("violates foreign key constraint")) {
                Message.showError("Error",
                        "Package has linked records and cannot be deleted.");
            } else {
                Message.showError("Error", "An error occurred during deletion.");
            }
            return false;
        }
    }

    public static Packages loadData(int packageId) {
        String sqlQuery = """
                SELECT p.id,
                       p.user_id,
                       u.name  AS name_user,
                       p.weight,
                       p.description,
                       p.tracking_number,
                       p.status
                FROM   public.packages  p
                INNER JOIN public.users u ON u.id = p.user_id
                WHERE  p.id = ?
                """;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {

            stmt.setInt(1, packageId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Packages pkg = new Packages(
                            rs.getInt("id"),
                            rs.getInt("user_id"),
                            rs.getString("name_user"),
                            rs.getDouble("weight"),
                            rs.getString("description")
                    );
                    pkg.setTrackingNumber(rs.getString("tracking_number"));
                    pkg.setStatus(rs.getString("status"));
                    return pkg;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}