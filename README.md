# Courier Management System (DDD Implementation)

This project is a desktop-based Courier Management System developed using **Domain-Driven Design (DDD)** principles, JavaFX for the user interface, and PostgreSQL for storage.

## 1. Domain-Driven Design Principles Applied

The system is divided into layers and contexts to ensure that business logic is isolated from infrastructure and user interface concerns.

### Strategic DDD
*   **Bounded Contexts:** The system is split into two primary contexts:
    *   **Delivery Management:** Owns the lifecycle of packages and routes.
    *   **Identity & Access Management:** Manages users, roles, and authentication.
*   **Ubiquitous Language:** Method names like `ship()`, `deliver()`, and `authenticate()` are used across the code to match the terminology used by business stakeholders.
*   **Shared Kernel:** The `UserId` Value Object acts as a shared kernel, allowing the Delivery context to reference package owners without being tightly coupled to the full User entity.

### Tactical DDD
*   **Aggregate Roots:** The `Package` class acts as the Aggregate Root. All changes to a package's status or details must go through this class, ensuring that business invariants (like status transition rules) are never violated.
*   **Value Objects:** 
    *   `TrackingNumber`: Immutable and self-validating. 
    *   `DeliveryStatus`: Encapsulates the state machine for the package lifecycle (Registered, In Transit, Delivered).
    *   `UserRole`: Contains logic for role-based permissions.
*   **Repositories:** Interfaces like `PackageRepository` define how to access data in domain terms. The implementation (`PackageRepositoryImpl`) handles the technical details of mapping database rows to domain objects.
*   **Domain Services:** 
    *   `AuthenticationService`: Coordinates between users and security utilities.
    *   `RouteOptimizationService`: Handles stateless logic for path calculation.

## 2. Project Architecture

The directory structure follows a layered architecture:

```text
com.example.courier
├── domain/                 # Core Business Logic 
│   ├── delivery/           # Delivery Bounded Context
│   ├── identityaccess/     # Identity Bounded Context
│   ├── repositories/       # Domain Interfaces
│   ├── services/           # Domain Services
│   └── valueobjects/       # Immutable Value Objects
├── infrastructure/         # The technical Details 
│   ├── repositories/       # Persistence Implementations
│   └── db/                 # Database connection and SQL helpers
└── controllers/            # Presentation Layer (JavaFX)
```

## 3. Key Business Rules Enforced
1.  **Status Integrity:** A package cannot skip a state.
2.  **Role-Based UI:** The `MainMenuController` uses the `UserRole` domain object to dynamically show or hide features based on whether the user is an Administrator, Courier, or Client.
3.  **Data Validation:** Objects like `Package` and `UserId` prevent invalid data (like negative weights or Ids). 

## 4. Setup and Installation

### Prerequisites
*   Java 
*   PostgreSQL
*   Maven

### Database Setup
1. Create a database named `courier_db`.
2. Execute the migration scripts located in the project's documentation to create the `packages`, `users`, `roles`, and `routes` tables.

### Running the Application
Execute the following command in the terminal:
```bash
mvn clean javafx:run
```

## 5. User Roles
**Administrator:** Full access to user management, packages, and routes.
**Courier:** View assigned routes and update package status.
**Client:** Update their information and view track existing shipments via tracking number. 
