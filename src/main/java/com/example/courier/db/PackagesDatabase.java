package com.example.courier.db;

import com.example.courier.domain.delivery.model.Package;
import com.example.courier.domain.delivery.repository.PackageRepository;
import com.example.courier.domain.identityaccess.model.UserId;
import com.example.courier.modelsPersonalize.Packages;
import com.example.courier.utils.messages.Message;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// Infrastructure implementation of the PackageRepository.
public class PackagesDatabase implements PackageRepository {

    @Override
    public void save(Package pkg) {
        if (pkg.getId() == null) {
            createPackageFromDomain(pkg);
        } else {
            updatePackageFromDomain(pkg);
        }
    }

    private void createPackageFromDomain(Package pkg) {
        String sqlQuery = "INSERT INTO public.packages(user_id, weight, description) VALUES (?, ?, ?);";
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = cn.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, pkg.getOwnerId().getValue());
            preparedStatement.setDouble(2, pkg.getWeight());
            preparedStatement.setString(3, pkg.getDescription());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updatePackageFromDomain(Package pkg) {
        String sqlQuery = "UPDATE public.packages SET user_id = ?, weight = ?, description = ? WHERE id = ?;";
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = cn.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, pkg.getOwnerId().getValue());
            preparedStatement.setDouble(2, pkg.getWeight());
            preparedStatement.setString(3, pkg.getDescription());
            preparedStatement.setInt(4, pkg.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Package> findById(int id) {
        String sqlQuery = "SELECT id, user_id, weight, description FROM public.packages WHERE id = ?;";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Package(
                            rs.getInt("id"),
                            new UserId(rs.getInt("user_id")),
                            rs.getDouble("weight"),
                            rs.getString("description")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Package> findAll() {
        List<Package> packagesList = new ArrayList<>();
        String sqlQuery = "SELECT id, user_id, weight, description FROM public.packages;";
        try (Connection connection = DatabaseConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sqlQuery)) {
            while (rs.next()) {
                packagesList.add(new Package(
                        rs.getInt("id"),
                        new UserId(rs.getInt("user_id")),
                        rs.getDouble("weight"),
                        rs.getString("description")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return packagesList;
    }

    @Override
    public boolean deleteById(int id) {
        return deletePackage(id);
    }


    public static ObservableList<Packages> loadData() {
        String sqlQuery = "select p.id, p.user_id, u.name as name_user, p.weight, p.description from public.packages as p inner join public.users u on u.id = p.user_id;";
        ObservableList<Packages> packages = FXCollections.observableArrayList();
        try (Connection connection = DatabaseConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sqlQuery)) {
            while (rs.next()) {
                packages.add(new Packages(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("name_user"),
                        rs.getDouble("weight"),
                        rs.getString("description")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return packages;
    }

    public static ObservableList<Packages> loadDataNoRoute(String searchPackage) {
        String sqlQuery = "select p.id, p.user_id, u.name as name_user, p.weight, p.description from public.packages as p inner join public.users u on u.id = p.user_id left join public.routes r on r.package_id = p.id where r.package_id is null and p.description ilike '%' || ? ||  '%';";
        ObservableList<Packages> packages = FXCollections.observableArrayList();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {
            stmt.setString(1, searchPackage);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                packages.add(new Packages(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("name_user"),
                        rs.getDouble("weight"),
                        rs.getString("description")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return packages;
    }

    public static Boolean createPackage(int userId, double weight, String pkgDescription, String value, String description) {
        String sqlQuery = "INSERT INTO public.packages(user_id, weight, description) VALUES (?, ?, ?);";
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = cn.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setDouble(2, weight);
            preparedStatement.setString(3, description);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Boolean updatePackage(int id, int userId, double weight, String pkgDescription, String value, String description) {
        String sqlQuery = "UPDATE public.packages SET user_id = ?, weight = ?, description = ? WHERE id = ?;";
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = cn.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setDouble(2, weight);
            preparedStatement.setString(3, description);
            preparedStatement.setInt(4, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Boolean deletePackage(int packageId) {
        String sqlQuery = "delete from public.packages where id = ?;";
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = cn.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, packageId);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            if (e.getMessage().contains("violates foreign key constraint")) {
                Message.showError("Error", "Package has linked records and cannot be deleted.");
            } else {
                Message.showError("Error", "An error occurred during deletion.");
            }
            return false;
        }
    }

    public static Packages loadData(int packageId) {
        String sqlQuery = "select p.id, p.user_id, u.name as name_user, p.weight, p.description from public.packages as p inner join public.users u on u.id = p.user_id where p.id = ?;";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sqlQuery)) {
            stmt.setInt(1, packageId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Packages(
                            rs.getInt("id"),
                            rs.getInt("user_id"),
                            rs.getString("name_user"),
                            rs.getDouble("weight"),
                            rs.getString("description")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
