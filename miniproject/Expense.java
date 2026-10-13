package miniproject;


import java.math.BigDecimal;
import java.time.LocalDate;

public class Expense {

    private int id;
    private int userId;
    private BigDecimal amount;
    private LocalDate date;
    private int categoryId;
    private String description;

    // Constructor initializes every field except id
    public Expense(int userId, BigDecimal amount,
                   LocalDate date, int categoryId,
                   String description) {

        this.userId = userId;
        this.amount = amount;
        this.date = date;
        this.categoryId = categoryId;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
