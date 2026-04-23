package com.example.courier.domain;

import com.example.courier.domain.delivery.model.Package;
import com.example.courier.domain.identityaccess.model.UserId;
import com.example.courier.domain.valueobjects.DeliveryStatus;
import com.example.courier.domain.valueobjects.TrackingNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Package aggregate root.
 *
 * Business Rules tested:
 *   - A Package cannot be created with invalid data (null owner, non-positive
 *     weight, blank description)
 *   - A new Package always starts in REGISTERED status
 *   - A new Package automatically receives a generated TrackingNumber
 *   - ship() advances status from REGISTERED → IN_TRANSIT
 *   - deliver() advances status from IN_TRANSIT → DELIVERED
 *   - deliver() without ship() is rejected (state machine enforced)
 *   - updateWeight() and updateDescription() enforce their own invariants
 *   - Package identity is based on ID, not on field values
 *
 * No database or JavaFX runtime required.
 */
@DisplayName("Package aggregate root")
class PackageTest {

    // A valid UserId used across multiple tests
    private UserId validOwner;

    @BeforeEach
    void setUp() {
        validOwner = new UserId(1);
    }

    // =========================================================================
    // Construction invariants
    // =========================================================================

    @Nested
    @DisplayName("Construction invariants")
    class ConstructionInvariants {

        @Test
        @DisplayName("Rejects null owner ID")
        void rejectsNullOwner() {
            // null ownerId means the package has no owner — not allowed
            assertThrows(IllegalArgumentException.class,
                    () -> new Package(null, null, 1.0, "Electronics"),
                    "A null owner ID must throw IllegalArgumentException");
        }

        @Test
        @DisplayName("Rejects zero weight")
        void rejectsZeroWeight() {
            assertThrows(IllegalArgumentException.class,
                    () -> new Package(null, validOwner, 0.0, "Electronics"),
                    "Zero weight must throw IllegalArgumentException");
        }

        @Test
        @DisplayName("Rejects negative weight")
        void rejectsNegativeWeight() {
            assertThrows(IllegalArgumentException.class,
                    () -> new Package(null, validOwner, -0.5, "Electronics"),
                    "Negative weight must throw IllegalArgumentException");
        }

        @Test
        @DisplayName("Rejects null description")
        void rejectsNullDescription() {
            assertThrows(IllegalArgumentException.class,
                    () -> new Package(null, validOwner, 1.0, null),
                    "A null description must throw IllegalArgumentException");
        }

        @Test
        @DisplayName("Rejects empty description")
        void rejectsEmptyDescription() {
            assertThrows(IllegalArgumentException.class,
                    () -> new Package(null, validOwner, 1.0, ""),
                    "An empty description must throw IllegalArgumentException");
        }

        @Test
        @DisplayName("Rejects blank description (whitespace only)")
        void rejectsBlankDescription() {
            assertThrows(IllegalArgumentException.class,
                    () -> new Package(null, validOwner, 1.0, "   "),
                    "A blank description must throw IllegalArgumentException");
        }

        @Test
        @DisplayName("Accepts a valid Package and stores all fields correctly")
        void acceptsValidPackage() {
            Package pkg = new Package(42, validOwner, 3.5, "Fragile glassware");

            assertEquals(42,            pkg.getId());
            assertEquals(validOwner,    pkg.getOwnerId());
            assertEquals(3.5,           pkg.getWeight());
            assertEquals("Fragile glassware", pkg.getDescription());
        }
    }

    // =========================================================================
    // Initial state
    // =========================================================================

    @Nested
    @DisplayName("Initial state of a new package")
    class InitialState {

        @Test
        @DisplayName("New package starts in REGISTERED status")
        void newPackageIsRegistered() {
            Package pkg = new Package(null, validOwner, 2.5, "Books");

            assertTrue(pkg.getStatus().isRegistered(),
                    "A newly created package must start in REGISTERED status");
        }

        @Test
        @DisplayName("New package receives a non-null TrackingNumber automatically")
        void newPackageHasTrackingNumber() {
            Package pkg = new Package(null, validOwner, 2.5, "Books");

            assertNotNull(pkg.getTrackingNumber(),
                    "A new package must have a TrackingNumber assigned");
            assertFalse(pkg.getTrackingNumber().getValue().isBlank(),
                    "The assigned TrackingNumber must not be blank");
        }

        @Test
        @DisplayName("Two different new packages receive different tracking numbers")
        void newPackagesHaveUniqueTrackingNumbers() {
            Package a = new Package(null, validOwner, 1.0, "Item A");
            Package b = new Package(null, validOwner, 1.0, "Item B");

            assertNotEquals(a.getTrackingNumber(), b.getTrackingNumber(),
                    "Two new packages must not share the same tracking number");
        }
    }

    // =========================================================================
    // Business behaviour: status transitions via ship() and deliver()
    // =========================================================================

    @Nested
    @DisplayName("Business behaviour")
    class BusinessBehaviour {

        @Test
        @DisplayName("ship() transitions status from REGISTERED to IN_TRANSIT")
        void shipTransitionsToInTransit() {
            Package pkg = new Package(1, validOwner, 2.5, "Books");
            pkg.ship();

            assertTrue(pkg.getStatus().isInTransit(),
                    "After ship() the status must be IN_TRANSIT");
        }

        @Test
        @DisplayName("deliver() after ship() transitions status to DELIVERED")
        void deliverAfterShipIsDelivered() {
            Package pkg = new Package(1, validOwner, 2.5, "Books");
            pkg.ship();
            pkg.deliver();

            assertTrue(pkg.getStatus().isDelivered(),
                    "After deliver() the status must be DELIVERED");
        }

        @Test
        @DisplayName("deliver() without ship() throws IllegalStateException")
        void deliverWithoutShipThrows() {
            Package pkg = new Package(1, validOwner, 2.5, "Books");

            // Business rule: you cannot deliver a package that was never shipped
            assertThrows(IllegalStateException.class,
                    pkg::deliver,
                    "Calling deliver() on a REGISTERED package must throw");
        }

        @Test
        @DisplayName("ship() after deliver() throws IllegalStateException")
        void shipAfterDeliverThrows() {
            Package pkg = new Package(1, validOwner, 2.5, "Books");
            pkg.ship();
            pkg.deliver();

            assertThrows(IllegalStateException.class,
                    pkg::ship,
                    "Calling ship() on a DELIVERED package must throw");
        }
    }

    // =========================================================================
    // Business behaviour: field updates
    // =========================================================================

    @Nested
    @DisplayName("Field update invariants")
    class FieldUpdateInvariants {

        @Test
        @DisplayName("updateWeight() with a positive value succeeds")
        void updateWeightPositiveSucceeds() {
            Package pkg = new Package(1, validOwner, 2.5, "Books");
            pkg.updateWeight(5.0);

            assertEquals(5.0, pkg.getWeight());
        }

        @Test
        @DisplayName("updateWeight() with zero throws IllegalArgumentException")
        void updateWeightZeroThrows() {
            Package pkg = new Package(1, validOwner, 2.5, "Books");

            assertThrows(IllegalArgumentException.class,
                    () -> pkg.updateWeight(0),
                    "updateWeight(0) must throw IllegalArgumentException");
        }

        @Test
        @DisplayName("updateWeight() with a negative value throws IllegalArgumentException")
        void updateWeightNegativeThrows() {
            Package pkg = new Package(1, validOwner, 2.5, "Books");

            assertThrows(IllegalArgumentException.class,
                    () -> pkg.updateWeight(-1),
                    "updateWeight(-1) must throw IllegalArgumentException");
        }

        @Test
        @DisplayName("updateDescription() with a valid string succeeds")
        void updateDescriptionValidSucceeds() {
            Package pkg = new Package(1, validOwner, 2.5, "Books");
            pkg.updateDescription("Updated description");

            assertEquals("Updated description", pkg.getDescription());
        }

        @Test
        @DisplayName("updateDescription() with blank throws IllegalArgumentException")
        void updateDescriptionBlankThrows() {
            Package pkg = new Package(1, validOwner, 2.5, "Books");

            assertThrows(IllegalArgumentException.class,
                    () -> pkg.updateDescription(""),
                    "updateDescription(\"\") must throw IllegalArgumentException");
        }

        @Test
        @DisplayName("updateDescription() with null throws IllegalArgumentException")
        void updateDescriptionNullThrows() {
            Package pkg = new Package(1, validOwner, 2.5, "Books");

            assertThrows(IllegalArgumentException.class,
                    () -> pkg.updateDescription(null),
                    "updateDescription(null) must throw IllegalArgumentException");
        }
    }

    // =========================================================================
    // Reconstitution constructor
    // The second constructor is used by repositories to reload existing packages
    // =========================================================================

    @Nested
    @DisplayName("Reconstitution constructor (for repository use)")
    class ReconstitutionConstructor {

        @Test
        @DisplayName("Reconstituted package preserves all provided fields")
        void preservesAllFields() {
            TrackingNumber tn     = TrackingNumber.of("RP-ABCD1234");
            DeliveryStatus status = DeliveryStatus.inTransit();
            Package pkg = new Package(7, validOwner, 1.5, "Ceramics", tn, status);

            assertEquals(7,           pkg.getId());
            assertEquals(validOwner,  pkg.getOwnerId());
            assertEquals(1.5,         pkg.getWeight());
            assertEquals("Ceramics",  pkg.getDescription());
            assertEquals(tn,          pkg.getTrackingNumber());
            assertTrue(pkg.getStatus().isInTransit());
        }

        @Test
        @DisplayName("Reconstitution with null tracking number throws")
        void nullTrackingNumberThrows() {
            assertThrows(IllegalArgumentException.class,
                    () -> new Package(1, validOwner, 1.0, "Item",
                            null, DeliveryStatus.registered()));
        }

        @Test
        @DisplayName("Reconstitution with null status throws")
        void nullStatusThrows() {
            assertThrows(IllegalArgumentException.class,
                    () -> new Package(1, validOwner, 1.0, "Item",
                            TrackingNumber.of("RP-12345678"), null));
        }
    }

    // =========================================================================
    // Aggregate identity
    // Package identity is determined by ID — not by field values
    // =========================================================================

    @Nested
    @DisplayName("Aggregate identity")
    class AggregateIdentity {

        @Test
        @DisplayName("Two packages with the same ID are equal regardless of other fields")
        void equalById() {
            Package a = new Package(5, new UserId(1), 1.0, "Item A");
            Package b = new Package(5, new UserId(2), 9.9, "Completely different");

            assertEquals(a, b,
                    "Packages are identified by their ID alone");
        }

        @Test
        @DisplayName("Two packages with different IDs are not equal")
        void notEqualDifferentIds() {
            Package a = new Package(1, validOwner, 1.0, "Same item");
            Package b = new Package(2, validOwner, 1.0, "Same item");

            assertNotEquals(a, b);
        }

        @Test
        @DisplayName("Equal packages have the same hash code")
        void equalHashCode() {
            Package a = new Package(3, new UserId(1), 1.0, "Item");
            Package b = new Package(3, new UserId(4), 5.0, "Different");

            assertEquals(a.hashCode(), b.hashCode(),
                    "Packages with the same ID must have equal hash codes");
        }

        @Test
        @DisplayName("A package is equal to itself (reflexive)")
        void reflexiveEquality() {
            Package pkg = new Package(1, validOwner, 1.0, "Item");
            assertEquals(pkg, pkg);
        }
    }
}
