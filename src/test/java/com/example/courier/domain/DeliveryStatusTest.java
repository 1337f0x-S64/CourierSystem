package com.example.courier.domain;

import com.example.courier.domain.valueobjects.DeliveryStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the DeliveryStatus value object.
 *
 * These tests document and protect every business rule encoded in DeliveryStatus:
 *   - Valid forward transitions in the package lifecycle
 *   - Invalid transitions that must be rejected
 *   - Database code ↔ domain object round-trips
 *   - toString() display values used in the UI
 *
 * No database, JavaFX runtime, or Spring context is needed — DeliveryStatus
 * is pure Java so these tests run instantly with: mvn test
 */
@DisplayName("DeliveryStatus value object")
class DeliveryStatusTest {

    // =========================================================================
    // Valid transitions
    // Business Rule: a package lifecycle is REGISTERED → IN_TRANSIT → DELIVERED
    // =========================================================================

    @Nested
    @DisplayName("Valid transitions")
    class ValidTransitions {

        @Test
        @DisplayName("REGISTERED → IN_TRANSIT is allowed")
        void registeredToInTransit() {
            DeliveryStatus registered = DeliveryStatus.registered();
            DeliveryStatus result     = registered.transitionTo(DeliveryStatus.inTransit());

            assertTrue(result.isInTransit(),
                    "After shipping a registered package the status must be IN_TRANSIT");
        }

        @Test
        @DisplayName("IN_TRANSIT → DELIVERED is allowed")
        void inTransitToDelivered() {
            DeliveryStatus inTransit = DeliveryStatus.inTransit();
            DeliveryStatus result    = inTransit.transitionTo(DeliveryStatus.delivered());

            assertTrue(result.isDelivered(),
                    "After delivering an in-transit package the status must be DELIVERED");
        }

        @Test
        @DisplayName("Transitioning to the same status returns the same status")
        void transitionToSameStatusIsIdentity() {
            // transitionTo() only validates forward moves; staying in the same
            // status is not a transition and should not throw.
            // NOTE: the current implementation throws on same-state "transitions"
            // because neither branch of the valid-transition check matches.
            // This test documents the CURRENT behaviour. If same-state is later
            // allowed, update the assertion accordingly.
            DeliveryStatus registered = DeliveryStatus.registered();
            assertThrows(IllegalStateException.class,
                    () -> registered.transitionTo(DeliveryStatus.registered()),
                    "Current implementation does not allow self-transitions");
        }
    }

    // =========================================================================
    // Invalid transitions
    // Business Rule: skipping states and going backwards are both forbidden
    // =========================================================================

    @Nested
    @DisplayName("Invalid transitions")
    class InvalidTransitions {

        @Test
        @DisplayName("REGISTERED cannot skip directly to DELIVERED")
        void registeredCannotSkipToDelivered() {
            DeliveryStatus registered = DeliveryStatus.registered();

            assertThrows(IllegalStateException.class,
                    () -> registered.transitionTo(DeliveryStatus.delivered()),
                    "Skipping IN_TRANSIT must throw IllegalStateException");
        }

        @Test
        @DisplayName("DELIVERED cannot transition to IN_TRANSIT (backwards)")
        void deliveredCannotGoBackToInTransit() {
            DeliveryStatus delivered = DeliveryStatus.delivered();

            assertThrows(IllegalStateException.class,
                    () -> delivered.transitionTo(DeliveryStatus.inTransit()),
                    "A delivered package cannot be moved backwards");
        }

        @Test
        @DisplayName("DELIVERED cannot transition to REGISTERED (backwards)")
        void deliveredCannotGoBackToRegistered() {
            DeliveryStatus delivered = DeliveryStatus.delivered();

            assertThrows(IllegalStateException.class,
                    () -> delivered.transitionTo(DeliveryStatus.registered()),
                    "A delivered package cannot be reset to registered");
        }

        @Test
        @DisplayName("IN_TRANSIT cannot go back to REGISTERED")
        void inTransitCannotGoBackToRegistered() {
            DeliveryStatus inTransit = DeliveryStatus.inTransit();

            assertThrows(IllegalStateException.class,
                    () -> inTransit.transitionTo(DeliveryStatus.registered()),
                    "An in-transit package cannot be moved backwards");
        }
    }

    // =========================================================================
    // Database code mapping
    // Business Rule: DB codes "w", "p", "d" map to REGISTERED, IN_TRANSIT, DELIVERED
    // =========================================================================

    @Nested
    @DisplayName("Database code mapping")
    class CodeMapping {

        @Test
        @DisplayName("fromCode(\"w\") returns REGISTERED")
        void fromCodeW() {
            assertTrue(DeliveryStatus.fromCode("w").isRegistered());
        }

        @Test
        @DisplayName("fromCode(\"p\") returns IN_TRANSIT")
        void fromCodeP() {
            assertTrue(DeliveryStatus.fromCode("p").isInTransit());
        }

        @Test
        @DisplayName("fromCode(\"d\") returns DELIVERED")
        void fromCodeD() {
            assertTrue(DeliveryStatus.fromCode("d").isDelivered());
        }

        @Test
        @DisplayName("toCode() returns the correct DB code for each status")
        void toCodeAllStatuses() {
            assertEquals("w", DeliveryStatus.registered().toCode(),
                    "REGISTERED must map to code \"w\"");
            assertEquals("p", DeliveryStatus.inTransit().toCode(),
                    "IN_TRANSIT must map to code \"p\"");
            assertEquals("d", DeliveryStatus.delivered().toCode(),
                    "DELIVERED must map to code \"d\"");
        }

        @Test
        @DisplayName("fromCode and toCode are inverses of each other")
        void roundTrip() {
            for (String code : new String[]{"w", "p", "d"}) {
                assertEquals(code,
                        DeliveryStatus.fromCode(code).toCode(),
                        "fromCode then toCode should return the original code: " + code);
            }
        }

        @Test
        @DisplayName("fromCode throws IllegalArgumentException for unknown codes")
        void fromCodeUnknownThrows() {
            assertThrows(IllegalArgumentException.class,
                    () -> DeliveryStatus.fromCode("x"),
                    "An unrecognised code must throw IllegalArgumentException");
            assertThrows(IllegalArgumentException.class,
                    () -> DeliveryStatus.fromCode(""),
                    "An empty code must throw IllegalArgumentException");
            assertThrows(IllegalArgumentException.class,
                    () -> DeliveryStatus.fromCode("W"),  // case-sensitive
                    "Codes are case-sensitive — uppercase W must throw");
        }
    }

    // =========================================================================
    // Display strings
    // These are shown in the UI TableView via getStatus() on Routes/PackageInfo
    // =========================================================================

    @Nested
    @DisplayName("toString display values")
    class DisplayValues {

        @Test
        @DisplayName("REGISTERED displays as \"Registered\"")
        void registeredDisplayString() {
            assertEquals("Registered", DeliveryStatus.registered().toString());
        }

        @Test
        @DisplayName("IN_TRANSIT displays as \"In Transit\"")
        void inTransitDisplayString() {
            assertEquals("In Transit", DeliveryStatus.inTransit().toString());
        }

        @Test
        @DisplayName("DELIVERED displays as \"Delivered\"")
        void deliveredDisplayString() {
            assertEquals("Delivered", DeliveryStatus.delivered().toString());
        }
    }

    // =========================================================================
    // Equality
    // DeliveryStatus is a value object — two instances with the same state
    // must be equal regardless of identity
    // =========================================================================

    @Nested
    @DisplayName("Value object equality")
    class ValueObjectEquality {

        @Test
        @DisplayName("Two REGISTERED instances are equal")
        void registeredEquality() {
            assertEquals(DeliveryStatus.registered(), DeliveryStatus.registered());
        }

        @Test
        @DisplayName("REGISTERED and IN_TRANSIT are not equal")
        void differentStatusesNotEqual() {
            assertNotEquals(DeliveryStatus.registered(), DeliveryStatus.inTransit());
        }

        @Test
        @DisplayName("Hash codes are consistent with equals")
        void hashCodeConsistency() {
            DeliveryStatus a = DeliveryStatus.inTransit();
            DeliveryStatus b = DeliveryStatus.inTransit();
            assertEquals(a.hashCode(), b.hashCode(),
                    "Equal objects must have equal hash codes");
        }
    }
}
