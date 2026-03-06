package org.example.models;

public class Book {

    private int id;
    private String authorFirstName;
    private String authorLastName;
    private String title;
    private String isbn;
    private String libraryId;
    private int quantity;

    public Book(int id, String authorFirstName, String authorLastName, String title, String isbn, String libraryId, int quantity) {

        this.id = id;
        this.authorFirstName = authorFirstName;
        this.authorLastName = authorLastName;
        this.title = title;
        this.isbn = isbn;
        this.libraryId = libraryId;
        this.quantity = quantity;
    }

    public Book(String authorFirstName, String authorLastName, String title, String isbn, String libraryId, int quantity) {

        this(-1, authorFirstName, authorLastName, title, isbn, libraryId, quantity);
    }

    public int getId() {
        return id;
    }

    public String getAuthorFirstName() {
        return authorFirstName;
    }

    public void setAuthorFirstName(String authorFirstName) {
        this.authorFirstName = authorFirstName;
    }

    public String getAuthorLastName() {
        return authorLastName;
    }

    public void setAuthorLastName(String authorLastName) {
        this.authorLastName = authorLastName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(String libraryId) {
        this.libraryId = libraryId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
