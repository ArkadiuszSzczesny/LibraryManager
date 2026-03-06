package org.example.models;

import java.time.LocalDate;

public class Loan {

    private int id;
    private int userId;
    private int bookId;
    private LocalDate loanDate;
    private LocalDate returnDate;

    public Loan(int id, int userId, int bookId, LocalDate loanDate, LocalDate returnDate) {

        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getBookId() {
        return bookId;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public boolean isReturned() {
        return returnDate != null;
    }
}
