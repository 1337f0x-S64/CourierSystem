package com.example.courier.domain;

import com.example.courier.domain.valueobjects.TrackingNumber;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the TrackingNumber value object.
 *
 * Business Rules tested:
 *   - A tracking number cannot be null or blank
 *   - Two tracking numbers with the same value are considered equal (value object)
 *   - Generated tracking numbers have the expected format and are unique
 *
 * No database or JavaFX runtime required.
 */
@DisplayName("TrackingNumber value object")
class TrackingNumberTest {

    // =========================================================================
    // Construction invariants
    // =========================================================================

    @Nested
    @DisplayName("Construction invariants")
    class ConstructionInvariants {

        @Test
        @DisplayName("Rejects null value")
        void rejectsNull() {
            assertThrows(IllegalArgumentException.class,
                    () -> new TrackingNumber(null),
                    "A null tracking number must throw IllegalArgumentException");
        }

        @Test
        @DisplayName("Rejects empty string")
        void rejectsEmpty() {
            assertThrows(IllegalArgumentException.class,
                    () -> new TrackingNumber(""),
                    "An empty tracking number must throw IllegalArgumentException");
        }

        @Test
        @DisplayName("Rejects blank string (whitespace only)")
        void rejectsBlank() {
            assertThrows(IllegalArgumentException.class,
                    () -> new TrackingNumber("   "),
                    "A blank tracking number must throw IllegalArgumentException");
        }

        @Test
        @DisplayName("Accepts a valid alphanumeric value")
        void acceptsValidAlphanumeric() {
            TrackingNumber tn = new TrackingNumber("1234567890");
            assertEquals("1234567890", tn.getValue());
        }

        @Test
        @DisplayName("Accepts a value with dashes (RP- format)")
        void acceptsRpFormat() {
            TrackingNumber tn = new TrackingNumber("RP-ABCD1234");
            assertEquals("RP-ABCD1234", tn.getValue());
        }
    }

    // =========================================================================
    // Factory method: of()
    // =========================================================================

    @Nested
    @DisplayName("of() factory method")
    class OfFactory {

        @Test
        @DisplayName("of() with valid string returns a TrackingNumber with that value")
        void ofValidString() {
            TrackingNumber tn = TrackingNumber.of("ABC-12345");
            assertEquals("ABC-12345", tn.getValue());
        }

        @Test
        @DisplayName("of() delegates to constructor — rejects blank")
        void ofBlankDelegatesToConstructor() {
            assertThrows(IllegalArgumentException.class,
                    () -> TrackingNumber.of(""));
        }
    }

    // =========================================================================
    // Factory method: generate()
    // =========================================================================

    @Nested
    @DisplayName("generate() factory method")
    class GenerateFactory {

        @Test
        @DisplayName("generate() produces a non-null, non-blank value")
        void generateIsNonBlank() {
            TrackingNumber tn = TrackingNumber.generate();
            assertNotNull(tn);
            assertFalse(tn.getValue().isBlank());
        }

        @Test
        @DisplayName("generate() starts with the RP- prefix")
        void generateHasRpPrefix() {
            TrackingNumber tn = TrackingNumber.generate();
            assertTrue(tn.getValue().startsWith("RP-"),
                    "Generated tracking numbers must start with 'RP-', got: "
                            + tn.getValue());
        }

        @Test
        @DisplayName("generate() produces values of the expected length (RP- + 8 chars)")
        void generateHasExpectedLength() {
            TrackingNumber tn = TrackingNumber.generate();
            // "RP-" (3) + 8 uppercase hex characters = 11 characters total
            assertEquals(11, tn.getValue().length(),
                    "Expected length 11 (RP- + 8 chars), got: " + tn.getValue());
        }

        @RepeatedTest(20)
        @DisplayName("generate() produces unique values across repeated calls")
        void generateIsUnique() {
            // Run 20 times via @RepeatedTest — each call should be unique
            Set<String> seen = new HashSet<>();
            for (int i = 0; i < 100; i++) {
                seen.add(TrackingNumber.generate().getValue());
            }
            assertEquals(100, seen.size(),
                    "100 generated tracking numbers must all be unique");
        }
    }

    // =========================================================================
    // Value object equality
    // Two TrackingNumbers with the same string value must be equal
    // =========================================================================

    @Nested
    @DisplayName("Value object equality")
    class ValueObjectEquality {

        @Test
        @DisplayName("Two instances with the same value are equal")
        void equalByValue() {
            TrackingNumber a = TrackingNumber.of("ABC-12345");
            TrackingNumber b = TrackingNumber.of("ABC-12345");

            assertEquals(a, b,
                    "Tracking numbers with the same value must be equal");
        }

        @Test
        @DisplayName("Two instances with different values are not equal")
        void notEqualDifferentValues() {
            TrackingNumber a = TrackingNumber.of("ABC-11111");
            TrackingNumber b = TrackingNumber.of("ABC-22222");

            assertNotEquals(a, b);
        }

        @Test
        @DisplayName("Equal tracking numbers have equal hash codes")
        void equalHashCodes() {
            TrackingNumber a = TrackingNumber.of("SAME-VALUE");
            TrackingNumber b = TrackingNumber.of("SAME-VALUE");

            assertEquals(a.hashCode(), b.hashCode(),
                    "Equal objects must have equal hash codes");
        }

        @Test
        @DisplayName("toString() returns the raw value string")
        void toStringReturnsValue() {
            TrackingNumber tn = TrackingNumber.of("RP-ABCD1234");
            assertEquals("RP-ABCD1234", tn.toString());
        }
    }
}
