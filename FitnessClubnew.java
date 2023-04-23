import java.util.Objects;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class FitnessClubnew {
    private static final Map<String, List<Booking>> BOOKINGS = new HashMap<>();
    private static final Map<String, List<LocalDate>> TIMETABLE = createTimetable();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static double YOGA_PRICE = 10;
    public static double ZUMBA_PRICE = 20;
    public static double AQUASICE_PRICE = 30;
    public static double BODYSCULPT_PRICE = 40;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Welcome to the Fitness Club!");
            System.out.println("1. View Timetable");
            System.out.println("2. Make Booking");
            System.out.println("3. Cancel Booking");
            System.out.println("4. View Bookings");
            System.out.println("5. Attend Lesson");
            System.out.println("6. Average Rating Report");
            System.out.println("7. Generate Champion Report");
            System.out.println("8. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    viewTimetable();
                    break;
                case 2:
                    makeBooking(scanner);
                    break;
                case 3:
                    cancelBooking(scanner);
                    break;
                case 4:
                    viewBookings();
                    break;
                case 5:
                    attendLesson(scanner);
                    break;
                case 6:
                    generateMonthlyReport(scanner);
                    break;
                case 7:
                    generateChampionReport();
                    break;
                case 8:
                    System.out.println("Thank you for using the Fitness Club booking system. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static Map<String, List<LocalDate>> createTimetable() {
        Map<String, List<LocalDate>> timetable = new HashMap<>();
        timetable.put("Zumba", createLessonSchedule(LocalDate.of(2023, 5, 1)));
        timetable.put("Yoga", createLessonSchedule(LocalDate.of(2023, 5, 8)));
        timetable.put("Bodysculpt", createLessonSchedule(LocalDate.of(2023, 5, 15)));
        timetable.put("Aquasice", createLessonSchedule(LocalDate.of(2023, 5, 22)));
        return timetable;
    }

    private static List<LocalDate> createLessonSchedule(LocalDate startDate) {
        List<LocalDate> schedule = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            schedule.add(startDate.plusDays(i));
            schedule.add(startDate.plusDays(i).plusWeeks(1));
        }
        return schedule;
    }

    private static void generateMonthlyReport(Scanner scanner) {
        System.out.print("Enter a month number (e.g. 03 for March): ");
        String monthString = scanner.nextLine();
        int month;
        try {
            month = Integer.parseInt(monthString);
        } catch (Exception e) {
            System.out.println("Invalid month number. Please try again.");
            return;
        }
        LocalDate startDate = LocalDate.of(2023, month, 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);
        System.out.println("Monthly report for " + startDate.getMonth().name() + ":");
        for (String lesson : TIMETABLE.keySet()) {
            System.out.println(lesson + ":");
            for (LocalDate date : TIMETABLE.get(lesson)) {
                if (date.isAfter(endDate)) {
                    break;
                }
                if (date.getMonth() == startDate.getMonth()) {
                    int customers = getNumCustomers(lesson, date);
                    double rating = getAvgRating(lesson, date);
                    System.out.printf("%s: %d customers, %.2f average rating%n", date.format(DATE_FORMATTER), customers,
                            rating);
                }
            }
            System.out.println(); // add a blank line between lessons
        }
    }

    private static int getNumCustomers(String lesson, LocalDate date) {
        int count = 0;
        for (List<Booking> bookings : BOOKINGS.values()) {
            for (Booking booking : bookings) {
                if (booking.getLesson().equals(lesson) && booking.getDate().equals(date)) {
                    count++;
                }
            }
        }
        return count;
    }

    private static double getAvgRating(String lesson, LocalDate date) {
        double sum = 0;
        int count = 0;
        for (List<Booking> bookings : BOOKINGS.values()) {
            for (Booking booking : bookings) {
                if (booking.getLesson().equals(lesson) && booking.getDate().equals(date)) {
                    sum += booking.getRating();
                    count++;
                }
            }
        }
        if (count == 0) {
            return 0;
        }
        return sum / count;
    }

    private static void viewTimetable() {
        System.out.println("Timetable for the next 8 weekends:");
        for (String lesson : TIMETABLE.keySet()) {
            System.out.println(lesson + ":");
            for (LocalDate date : TIMETABLE.get(lesson)) {
                System.out.println("- " + date.format(DATE_FORMATTER));
            }
            System.out.println(); // add a blank line between lessons
        }
        System.out.print(
                " Yoga lesson price is :- 10 dollars \n Zumba lesson price is 20 dollars \n Aquasice lesson price is 50 dollars \n Bodysculpt lesson price is 15 dollars \n");
    }

    private static void makeBooking(Scanner scanner) {
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        System.out.println("Available lessons:");
        for (String lesson : TIMETABLE.keySet()) {
            System.out.println("- " + lesson);
        }
        System.out.print("Enter the lesson you want to book: ");
        String lesson = scanner.nextLine();
        if (!TIMETABLE.containsKey(lesson)) {
            System.out.println("Invalid lesson. Please try again.");
            return;
        }

        System.out.print("Enter the date you want to book (dd/MM/yyyy): ");
        String dateString = scanner.nextLine();
        LocalDate date;
        try {
            date = LocalDate.parse(dateString, DATE_FORMATTER);
        } catch (Exception e) {
            System.out.println("Invalid date format. Please try again.");
            return;
        }

        if (!TIMETABLE.get(lesson).contains(date)) {
            System.out.println("This lesson is not available on the selected date. Please try again.");
            return;
        }

        Booking booking = new Booking(name, lesson, date);

        if (!BOOKINGS.containsKey(name)) {
            BOOKINGS.put(name, new ArrayList<>());
        }
        List<Booking> userBookings = BOOKINGS.get(name);
        if (userBookings.contains(booking)) {
            System.out.println("You have already booked this lesson on this date.");
            return;
        }
        userBookings.add(booking);
        System.out.println("Booking successful.");

        if (lesson.equals("Yoga")) {
            YOGA_PRICE = YOGA_PRICE + 10;

        } else if (lesson.equals("Zumba")) {
            ZUMBA_PRICE = ZUMBA_PRICE + 20;
        } else if (lesson.equals("Aquasice")) {
            AQUASICE_PRICE = AQUASICE_PRICE + 50;
        } else if (lesson.equals("Bodysculpt")) {
            BODYSCULPT_PRICE = BODYSCULPT_PRICE + 15;
        }

    }

    private static void generateChampionReport() {

        if (YOGA_PRICE < ZUMBA_PRICE) {
            if (AQUASICE_PRICE < ZUMBA_PRICE) {
                if (BODYSCULPT_PRICE < ZUMBA_PRICE) {
                    System.out.println("The highest income generating lesson is ZUMBA.");
                }
            }

        }

        if (ZUMBA_PRICE < YOGA_PRICE) {
            if (AQUASICE_PRICE < YOGA_PRICE) {
                if (BODYSCULPT_PRICE < YOGA_PRICE) {
                    System.out.println("The highest income generating lesson is YOGA.");
                }
            }
        }

        if (ZUMBA_PRICE < AQUASICE_PRICE) {
            if (YOGA_PRICE < AQUASICE_PRICE) {
                if (BODYSCULPT_PRICE < AQUASICE_PRICE) {
                    System.out.println("The highest income generating lesson is AQUASICE.");
                }
            }
        }
        if (ZUMBA_PRICE < BODYSCULPT_PRICE) {
            if (AQUASICE_PRICE < BODYSCULPT_PRICE) {
                if (YOGA_PRICE < BODYSCULPT_PRICE) {
                    System.out.println("The highest income generating lesson is BODYSCULPT.");
                }
            }
        }

        if (BOOKINGS.isEmpty()) {
            System.out.println("NO BOOKINGS MADE YET for all lessons have 0 earnings");
        }
    }

    private static void cancelBooking(Scanner scanner) {
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        if (!BOOKINGS.containsKey(name)) {
            System.out.println("You don't have any bookings.");
            return;
        }

        List<Booking> userBookings = BOOKINGS.get(name);
        if (userBookings.isEmpty()) {
            System.out.println("You don't have any bookings.");
            return;
        }

        System.out.println("Your bookings:");
        for (int i = 0; i < userBookings.size(); i++) {
            Booking booking = userBookings.get(i);
            System.out.printf("%d. %s on %s%n", i + 1, booking.getLesson(), booking.getDate().format(DATE_FORMATTER));
        }

        System.out.print("Enter the number of the booking you want to cancel: ");
        int bookingNumber = scanner.nextInt();
        scanner.nextLine(); // consume newline

        if (bookingNumber < 1 || bookingNumber > userBookings.size()) {
            System.out.println("Invalid booking number. Please try again.");
            return;
        }

        Booking bookingToRemove = userBookings.get(bookingNumber - 1);
        userBookings.remove(bookingToRemove);

        System.out.println("Booking cancelled.");
    }

    private static void viewBookings() {
        System.out.println("Bookings:");
        for (String name : BOOKINGS.keySet()) {
            System.out.println(name + ":");
            List<Booking> userBookings = BOOKINGS.get(name);
            for (Booking booking : userBookings) {
                System.out.printf("- %s: %s on %s%n", name, booking.getLesson(),
                        booking.getDate().format(DATE_FORMATTER));
            }
            System.out.println(); // add a blank line between users
        }
    }

    private static class Booking {
        private final String name;
        private final String lesson;
        private final LocalDate date;

        public Booking(String name, String lesson, LocalDate date) {
            this.name = name;
            this.lesson = lesson;
            this.date = date;
        }

        int rating;

        public void setRating(int rating) {
            this.rating = rating;
        }

        public int getRating() {
            if (this.rating == 0) {
                System.out.println("This booking has not yet been rated.");
                return 0;
            } else {
                return this.rating;
            }
        }

        public String getName() {
            return name;
        }

        public String getLesson() {
            return lesson;
        }

        public LocalDate getDate() {
            return date;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (!(o instanceof Booking))
                return false;
            Booking booking = (Booking) o;
            return name.equals(booking.name) && lesson.equals(booking.lesson) && date.equals(booking.date);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, lesson, date);
        }
    }

    private static void attendLesson(Scanner scanner) {
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        if (!BOOKINGS.containsKey(name)) {
            System.out.println("You don't have any bookings.");
            return;
        }
        List<Booking> userBookings = BOOKINGS.get(name);
        if (userBookings.isEmpty()) {
            System.out.println("You don't have any bookings.");
            return;
        }
        System.out.println("Your bookings:");
        for (int i = 0; i < userBookings.size(); i++) {
            Booking booking = userBookings.get(i);
            System.out.printf("%d. %s on %s%n", i + 1, booking.getLesson(), booking.getDate().format(DATE_FORMATTER));
        }
        System.out.print("Did you attend the lesson? (y/n) ");
        String answer = scanner.nextLine();
        if (Objects.equals(answer, "y")) {
            System.out.print("Rate the class from 1 to 5: ");
            int rating = scanner.nextInt();
            scanner.nextLine(); // consume newline
            for (Booking booking : userBookings) {
                booking.setRating(rating);
            }
        } else if (Objects.equals(answer, "n")) {
            System.out.println("The class should be joined soon.");
        } else {
            System.out.println("Invalid choice.");
        }
    }

}