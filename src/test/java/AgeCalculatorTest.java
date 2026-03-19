import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 test class for {@link AgeCalculator}.
 *
 * <p>Validates all three public static methods of the AgeCalculator business logic class:
 * <ul>
 *   <li>{@code parseDateOfBirth(String dob)} — DD/MM/YYYY date parsing with strict validation</li>
 *   <li>{@code validateDateOfBirth(LocalDate dob)} — future date rejection</li>
 *   <li>{@code calculateAge(LocalDate dob)} — exact age computation in years, months, and days</li>
 * </ul>
 *
 * <p>All date-dependent expected values are computed dynamically using {@link LocalDate#now()}
 * to ensure tests remain valid regardless of the calendar date on which they are executed.
 * No hardcoded "today" dates are used anywhere in this test class.</p>
 *
 * <p>This class resides in the default package (no {@code package} statement),
 * mirroring the main source structure.</p>
 */
public class AgeCalculatorTest {

    // ========================================================================
    // Tests for parseDateOfBirth(String dob) — DD/MM/YYYY Date Parsing
    // ========================================================================

    /**
     * Verifies that a standard valid date string in DD/MM/YYYY format is correctly
     * parsed into a LocalDate object.
     */
    @Test
    @DisplayName("parseDateOfBirth parses a valid DD/MM/YYYY date correctly")
    void testParseDateOfBirthWithValidDate() {
        // Arrange: define a standard date string
        String dob = "15/08/1998";

        // Act: parse the date
        LocalDate result = AgeCalculator.parseDateOfBirth(dob);

        // Assert: verify the parsed LocalDate matches the expected value
        assertEquals(LocalDate.of(1998, 8, 15), result,
                "Parsed date should be 1998-08-15 for input '15/08/1998'");
    }

    /**
     * Verifies that February 29 in a leap year (2000) is accepted as a valid date.
     * Year 2000 is a leap year (divisible by 400).
     */
    @Test
    @DisplayName("parseDateOfBirth accepts leap year date 29/02/2000")
    void testParseDateOfBirthWithLeapYearDate() {
        // Arrange: February 29 in a leap year
        String dob = "29/02/2000";

        // Act: parse the date
        LocalDate result = AgeCalculator.parseDateOfBirth(dob);

        // Assert: verify the parsed LocalDate is February 29, 2000
        assertEquals(LocalDate.of(2000, 2, 29), result,
                "Leap year date 29/02/2000 should be accepted and parsed correctly");
    }

    /**
     * Verifies that an impossible calendar date (February 31) is rejected
     * with a DateTimeParseException due to strict date resolution.
     */
    @Test
    @DisplayName("parseDateOfBirth rejects invalid date 31/02/2020")
    void testParseDateOfBirthWithInvalidDate() {
        // Arrange: February 31 does not exist in any year
        String dob = "31/02/2020";

        // Act & Assert: parsing should throw DateTimeParseException
        assertThrows(DateTimeParseException.class,
                () -> AgeCalculator.parseDateOfBirth(dob),
                "Invalid date 31/02/2020 should throw DateTimeParseException");
    }

    /**
     * Verifies that a non-date string input is rejected with a DateTimeParseException.
     */
    @Test
    @DisplayName("parseDateOfBirth rejects non-date string input")
    void testParseDateOfBirthWithNonDateString() {
        // Arrange: clearly non-date input
        String dob = "abc";

        // Act & Assert: parsing should throw DateTimeParseException
        assertThrows(DateTimeParseException.class,
                () -> AgeCalculator.parseDateOfBirth(dob),
                "Non-date string 'abc' should throw DateTimeParseException");
    }

    /**
     * Verifies that a date in ISO format (YYYY-MM-DD) is rejected because it
     * does not match the expected DD/MM/YYYY format.
     */
    @Test
    @DisplayName("parseDateOfBirth rejects ISO format date 1998-08-15")
    void testParseDateOfBirthWithIsoFormat() {
        // Arrange: valid date but wrong format (ISO instead of DD/MM/YYYY)
        String dob = "1998-08-15";

        // Act & Assert: parsing should throw DateTimeParseException
        assertThrows(DateTimeParseException.class,
                () -> AgeCalculator.parseDateOfBirth(dob),
                "ISO format date '1998-08-15' should throw DateTimeParseException");
    }

    /**
     * Verifies that February 29 in a non-leap year (2001) is rejected.
     * Year 2001 is not a leap year.
     */
    @Test
    @DisplayName("parseDateOfBirth rejects 29/02/2001 (non-leap year)")
    void testParseDateOfBirthWithNonLeapYearFeb29() {
        // Arrange: 2001 is not a leap year
        String dob = "29/02/2001";

        // Act & Assert: parsing should throw DateTimeParseException
        assertThrows(DateTimeParseException.class,
                () -> AgeCalculator.parseDateOfBirth(dob),
                "Non-leap year date 29/02/2001 should throw DateTimeParseException");
    }

    // ========================================================================
    // Tests for validateDateOfBirth(LocalDate dob) — Future Date Rejection
    // ========================================================================

    /**
     * Verifies that a date of birth in the future is rejected with an
     * IllegalArgumentException.
     */
    @Test
    @DisplayName("validateDateOfBirth rejects future date")
    void testValidateDateOfBirthWithFutureDate() {
        // Arrange: create a date one year in the future
        LocalDate futureDate = LocalDate.now().plusYears(1);

        // Act & Assert: validation should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class,
                () -> AgeCalculator.validateDateOfBirth(futureDate),
                "A future date should throw IllegalArgumentException");
    }

    /**
     * Verifies that today's date is accepted as a valid date of birth.
     * A person born today has age 0 years, 0 months, and 0 days.
     */
    @Test
    @DisplayName("validateDateOfBirth accepts today's date")
    void testValidateDateOfBirthWithTodayDate() {
        // Arrange: today's date
        LocalDate today = LocalDate.now();

        // Act & Assert: validation should not throw any exception
        assertDoesNotThrow(() -> AgeCalculator.validateDateOfBirth(today),
                "Today's date should be accepted as a valid date of birth");
    }

    /**
     * Verifies that a past date is accepted as a valid date of birth without exceptions.
     */
    @Test
    @DisplayName("validateDateOfBirth accepts past date")
    void testValidateDateOfBirthWithPastDate() {
        // Arrange: a date clearly in the past
        LocalDate pastDate = LocalDate.of(1990, 1, 1);

        // Act & Assert: validation should not throw any exception
        assertDoesNotThrow(() -> AgeCalculator.validateDateOfBirth(pastDate),
                "A past date should be accepted as a valid date of birth");
    }

    // ========================================================================
    // Tests for calculateAge(LocalDate dob) — Age Computation
    // ========================================================================

    /**
     * Verifies that calculateAge returns a Period with correct years, months, and days
     * for a standard date of birth. Uses dynamic today's date for expectation.
     */
    @Test
    @DisplayName("calculateAge computes correct age for a standard DOB")
    void testCalculateAgeWithStandardDob() {
        // Arrange: define a past date of birth
        LocalDate dob = LocalDate.of(1998, 8, 15);

        // Act: compute the age
        Period age = AgeCalculator.calculateAge(dob);

        // Assert: compare with expected Period computed dynamically
        Period expected = Period.between(dob, LocalDate.now());
        assertEquals(expected.getYears(), age.getYears(),
                "Years component should match dynamically computed expectation");
        assertEquals(expected.getMonths(), age.getMonths(),
                "Months component should match dynamically computed expectation");
        assertEquals(expected.getDays(), age.getDays(),
                "Days component should match dynamically computed expectation");
    }

    /**
     * Verifies that a person born today has an age of 0 years, 0 months, and 0 days.
     */
    @Test
    @DisplayName("calculateAge returns zero period for today's date as DOB")
    void testCalculateAgeWithTodayDob() {
        // Arrange: use today as the date of birth
        LocalDate today = LocalDate.now();

        // Act: compute the age
        Period age = AgeCalculator.calculateAge(today);

        // Assert: all components should be zero
        assertEquals(0, age.getYears(), "Years should be 0 for a person born today");
        assertEquals(0, age.getMonths(), "Months should be 0 for a person born today");
        assertEquals(0, age.getDays(), "Days should be 0 for a person born today");
    }

    /**
     * Verifies that calculateAge correctly computes the age for a leap-year date of birth
     * (February 29, 2000). The Period should be computed accurately regardless of whether
     * the current year is a leap year.
     */
    @Test
    @DisplayName("calculateAge computes correct age for leap year DOB 29/02/2000")
    void testCalculateAgeWithLeapYearDob() {
        // Arrange: February 29, 2000 (leap year)
        LocalDate dob = LocalDate.of(2000, 2, 29);

        // Act: compute the age
        Period age = AgeCalculator.calculateAge(dob);

        // Assert: compare with expected Period computed dynamically
        Period expected = Period.between(dob, LocalDate.now());
        assertEquals(expected.getYears(), age.getYears(),
                "Years component should match for leap year DOB");
        assertEquals(expected.getMonths(), age.getMonths(),
                "Months component should match for leap year DOB");
        assertEquals(expected.getDays(), age.getDays(),
                "Days component should match for leap year DOB");
    }

    /**
     * Verifies that calculateAge works correctly for a very old date of birth,
     * ensuring no overflow or arithmetic errors for extreme age values.
     */
    @Test
    @DisplayName("calculateAge handles very old DOB correctly")
    void testCalculateAgeWithVeryOldDob() {
        // Arrange: a very old date of birth
        LocalDate dob = LocalDate.of(1900, 1, 1);

        // Act: compute the age
        Period age = AgeCalculator.calculateAge(dob);

        // Assert: compare with expected Period computed dynamically
        Period expected = Period.between(dob, LocalDate.now());
        assertEquals(expected.getYears(), age.getYears(),
                "Years component should match for a very old DOB");
        assertEquals(expected.getMonths(), age.getMonths(),
                "Months component should match for a very old DOB");
        assertEquals(expected.getDays(), age.getDays(),
                "Days component should match for a very old DOB");
    }
}
