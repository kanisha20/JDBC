package miniproject;


import java.sql.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Scanner;

public class ExpenseController {

    static final String URL =
        "jdbc:mysql://localhost:3306/expense_tracker";
    static final String USER = "root";
    static final String PASSWORD = "";

    static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void main(String[] args) {

        try (Scanner sc = new Scanner(System.in)) {
            System.out.print("Enter logged-in user ID: ");
            int loggedInUserId = sc.nextInt();
            sc.nextLine();

            System.out.println("\nWelcome to Expense Tracker!");
            showAITips();

            while (true) {
                System.out.println("\n1. Add Expense");
                System.out.println("2. Monthly Report");
                System.out.println("3. Exit");
                System.out.print("Choose option: ");

                int choice = sc.nextInt();
                sc.nextLine();

                try {
                    switch (choice) {
                        case 1:
                            addExpense(sc, loggedInUserId);
                            break;

                        case 2:
                            monthlyReport(sc, loggedInUserId);
                            break;

                        case 3:
                            System.out.println("Goodbye!");
                            return;

                        default:
                            System.out.println("Invalid choice.");
                    }
                } catch (SQLException e) {
                    System.out.println(
                        "Database operation failed. "
                        + "Please check your database and try again.");
                }
            }

        } catch (Exception e) {
            System.out.println(
                "Unable to start the application. "
                + "Please check your input and database settings.");
        }
    }

    static void addExpense(Scanner sc, int userId)
            throws SQLException {

        System.out.print("Enter amount: ");
        BigDecimal amount = new BigDecimal(sc.nextLine());

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Amount must be greater than zero.");
            return;
        }

        System.out.print("Enter date (YYYY-MM-DD): ");
        LocalDate date = LocalDate.parse(sc.nextLine());

        String categorySql =
            "SELECT id, name FROM SpendingCategory ORDER BY id";

        System.out.println("\nAvailable categories:");

        try (Connection con = getConnection();
             PreparedStatement ps =
                 con.prepareStatement(categorySql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                System.out.println(
                    rs.getInt("id") + ". " +
                    rs.getString("name"));
            }
        }

        System.out.print("Enter category ID: ");
        int categoryId = Integer.parseInt(sc.nextLine());

        System.out.print("Enter description: ");
        String description = sc.nextLine();

        Expense expense = new Expense(
            userId, amount, date, categoryId, description);

        String sql =
            "INSERT INTO Expenses " +
            "(userId, amount, date, categoryId, description) " +
            "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, expense.getUserId());
            ps.setBigDecimal(2, expense.getAmount());
            ps.setDate(3, Date.valueOf(expense.getDate()));
            ps.setInt(4, expense.getCategoryId());
            ps.setString(5, expense.getDescription());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Expense added successfully!");
            }
        }
    }

    static void monthlyReport(Scanner sc, int userId)
            throws SQLException {

        System.out.print("Enter month (YYYY-MM): ");
        YearMonth month = YearMonth.parse(sc.nextLine());

        LocalDate startDate = month.atDay(1);
        LocalDate endDate = month.plusMonths(1).atDay(1);

        String sql =
            "SELECT c.name AS category, " +
            "SUM(e.amount) AS total " +
            "FROM Expenses e " +
            "JOIN SpendingCategory c ON e.categoryId = c.id " +
            "WHERE e.userId = ? " +
            "AND e.date >= ? AND e.date < ? " +
            "GROUP BY c.id, c.name " +
            "ORDER BY total DESC";

        BigDecimal grandTotal = BigDecimal.ZERO;

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setDate(2, Date.valueOf(startDate));
            ps.setDate(3, Date.valueOf(endDate));

            try (ResultSet rs = ps.executeQuery()) {

                System.out.println("\nMonthly Expense Report: " + month);
                System.out.println("-------------------------------------------");
                System.out.printf("%-22s %12s%n",
                                  "Category", "Total Spent");
                System.out.println("-------------------------------------------");

                boolean found = false;

                while (rs.next()) {
                    found = true;

                    String category = rs.getString("category");
                    BigDecimal total = rs.getBigDecimal("total");

                    System.out.printf("%-22s %12.2f%n",
                                      category, total);

                    grandTotal = grandTotal.add(total);
                }

                if (!found) {
                    System.out.println(
                        "No expenses found for this month.");
                } else {
                    System.out.println("-------------------------------------------");
                    System.out.printf("%-22s %12.2f%n",
                                      "GRAND TOTAL", grandTotal);
                }
            }
        }
    }

    static void showAITips() {

        System.out.println("\n========== AI TIPS ==========");
        System.out.println("1. Track every expense daily to find "
                         + "where your money goes.");
        System.out.println("2. Set a monthly budget for food, travel, "
                         + "shopping and entertainment.");
        System.out.println("3. Save a fixed amount of your allowance "
                         + "before spending on non-essential items.");
        System.out.println("=============================");
    }
}
