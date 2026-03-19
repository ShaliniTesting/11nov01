import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/**
 * AgeCalculator — Core business logic class for the Age Calculator application.
 *
 * <p>Provides methods for parsing a date of birth from a string in DD/MM/YYYY format,
 * validating that the date is not in the future, and computing the exact age as a
 * {@link java.time.Period} containing years, months, and days. All methods use the
 * modern {@link java.time} API for accurate date handling including leap-year support.</p>
 *
 * <p>This class is intentionally decoupled from all I/O operations (Scanner, System.in,
 * System.out) to ensure maximum testability and clean separation of concerns. It contains
 * only pure parsing, validation, and calculation logic with no side effects.</p>
 */
public class AgeCalculator {

    /**
     * Strict date formatter for parsing DD/MM/YYYY input.
     *
     * <p>Uses pattern "dd/MM/uuuu" with {@link ResolverStyle#STRICT} to ensure
     * invalid dates such as 31/02/2020 or 29/02/2001 are rejected during parsing.
     * The "uuuu" year pattern (proleptic year) is required when using STRICT resolver
     * style instead of "yyyy".</p>
     */
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    /**
     * Parses a date-of-birth string in DD/MM/YYYY format into a {@link LocalDate}.
     *
     * <p>Uses strict formatting to reject invalid calendar dates such as 31/02/2020
     * (February has no 31st day) and 29/02/2001 (2001 is not a leap year). Valid
     * leap-year dates such as 29/02/2000 are correctly accepted.</p>
     *
     * @param dob the date of birth as a string in DD/MM/YYYY format (e.g., "15/08/1998")
     * @return the parsed {@link LocalDate} representing the date of birth
     * @throws DateTimeParseException if the input is not in DD/MM/YYYY format or represents
     *                                an invalid calendar date
     */
    public static LocalDate parseDateOfBirth(String dob) {
        return LocalDate.parse(dob, FORMATTER);
    }

    /**
     * Validates that the given date of birth is not in the future.
     *
     * <p>Compares the provided date against today's date ({@link LocalDate#now()}).
     * A date of birth equal to today is considered valid (the person was born today).
     * A date strictly after today is rejected.</p>
     *
     * @param dob the date of birth to validate
     * @throws IllegalArgumentException if the date of birth is after today's date
     */
    public static void validateDateOfBirth(LocalDate dob) {
        if (dob.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date of birth cannot be in the future.");
        }
    }

    /**
     * Computes the exact age based on the given date of birth and today's date.
     *
     * <p>Returns a {@link Period} object that decomposes the age into years, months,
     * and days. The calculation uses {@link Period#between(LocalDate, LocalDate)} which
     * natively handles leap years and variable-length months without manual arithmetic.</p>
     *
     * @param dob the date of birth (must not be in the future; validate using
     *            {@link #validateDateOfBirth(LocalDate)} before calling this method)
     * @return a {@link Period} representing the age in years, months, and days
     */
    public static Period calculateAge(LocalDate dob) {
        return Period.between(dob, LocalDate.now());
    }
}
