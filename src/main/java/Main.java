import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Main — Entry point for the Age Calculator console application.
 *
 * <p>This class is responsible exclusively for user interaction (I/O): reading input from the
 * console, displaying formatted output, handling exceptions gracefully, and managing the
 * repeat-calculation loop. All business logic — date-of-birth parsing, validation, and age
 * computation — is delegated to {@link AgeCalculator}.</p>
 *
 * <p>The application prompts the user to enter their Date of Birth (DD/MM/YYYY), computes the
 * exact age in years, months, and days, and displays the result. Users can perform multiple
 * calculations in a single session and type "exit" or "quit" to terminate the program.</p>
 *
 * <p><strong>Design Decision — Separation of Concerns:</strong> This class intentionally
 * contains zero calculation logic. It does not import date arithmetic classes or perform
 * any age computation. This keeps the I/O layer cleanly separated from the business logic
 * layer, improving testability and maintainability.</p>
 */
public class Main {

    /**
     * Application entry point. Runs an interactive console loop that prompts the user
     * for their Date of Birth, delegates calculation to {@link AgeCalculator}, and displays
     * the computed age.
     *
     * <p>The loop continues until the user types "exit" or "quit" (case-insensitive).
     * All exceptions are handled gracefully — the program never crashes with a stack trace.</p>
     *
     * @param args command-line arguments (not used by this application)
     */
    public static void main(String[] args) {
        // Use nextLine() instead of nextInt() so we can read both date-of-birth input and
        // text commands like "exit" or "quit" from a single input stream without
        // leaving dangling newline characters in the buffer.
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Age Calculator!");
        System.out.println("Enter your Date of Birth to calculate your exact age.");
        System.out.println("Type \"exit\" or \"quit\" to stop.");
        System.out.println();

        // Repeat-calculation loop: allows the user to perform multiple calculations
        // in a single session without restarting the program. The loop runs
        // indefinitely until the user enters the exit sentinel ("exit" or "quit").
        while (true) {
            try {
                System.out.print("Enter your Date of Birth (DD/MM/YYYY): ");

                // Guard against EOF (end-of-stream): if stdin has been closed or
                // exhausted (e.g., piped input without a trailing "exit" command),
                // hasNextLine() returns false. We break out of the loop gracefully
                // rather than allowing nextLine() to throw NoSuchElementException,
                // which would be caught by the generic handler and cause an infinite loop.
                if (!scanner.hasNextLine()) {
                    System.out.println();
                    System.out.println("Input stream closed. Exiting.");
                    break;
                }

                // Read the entire line as a String to support both date-of-birth entries
                // and text-based exit commands in a unified input flow.
                String input = scanner.nextLine().trim();

                // Exit sentinel check: allow the user to gracefully terminate the
                // program by typing "exit" or "quit" (case-insensitive comparison
                // ensures "EXIT", "Quit", etc. are also accepted).
                if (input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("quit")) {
                    System.out.println("Thank you for using the Age Calculator. Goodbye!");
                    break;
                }

                // Delegate DOB parsing, validation, and age computation to AgeCalculator.
                // Main.java never performs date arithmetic or accesses date formatting APIs directly.
                // - parseDateOfBirth() parses the DD/MM/YYYY string into a LocalDate
                //   (throws DateTimeParseException for invalid formats or impossible dates)
                // - validateDateOfBirth() ensures the DOB is not in the future
                //   (throws IllegalArgumentException for future dates)
                // - calculateAge() computes the exact age as a Period (years, months, days)
                try {
                    LocalDate dob = AgeCalculator.parseDateOfBirth(input);
                    AgeCalculator.validateDateOfBirth(dob);
                    Period age = AgeCalculator.calculateAge(dob);

                    // Output the result in the exact format specified by the requirements.
                    // CRITICAL: spacing, commas, the word "and", and the trailing period must match exactly.
                    System.out.println("Your age is " + age.getYears() + " years, "
                            + age.getMonths() + " months, and " + age.getDays() + " days.");
                } catch (DateTimeParseException e) {
                    // Thrown when the input is not in DD/MM/YYYY format or represents an
                    // invalid calendar date (e.g., 31/02/2020, 29/02/2001, "abc").
                    System.out.println("Invalid date. Please enter a valid date in DD/MM/YYYY format.");
                } catch (IllegalArgumentException e) {
                    // Thrown when the parsed date is in the future (after today).
                    System.out.println("Date of birth cannot be in the future. Please enter a valid past date.");
                }

                System.out.println();

            } catch (Exception e) {
                // Safety net: catch any unexpected exceptions that were not anticipated
                // by the specific handlers above. This ensures the program never crashes
                // with an unhandled stack trace, regardless of what goes wrong.
                System.out.println("An unexpected error occurred: " + e.getMessage());
                System.out.println("Please try again.");
                System.out.println();
            }
        }

        // Release the Scanner resource after the loop exits. Closing the Scanner
        // also closes the underlying System.in stream.
        scanner.close();
    }
}
