// Class Book to store book information
class Book {
    private String title;
    private String author;
    private double price;
    private int stock;

    // Constructor
    Book(String title, String author, double price, int stock) {
        this.setTitle(title);
        this.setAuthor(author);
        this.setPrice(price);
        this.setStock(stock);
    }
    public static final double DISCOUNT_RATE = 0.10;
    // Display book details
    public void displayInfo() {
        System.out.println("Title: " + getTitle());
        System.out.println("Author: " + getAuthor());
        System.out.println("Price: $" + getPrice());
        System.out.println("Discounted Price: $" + calculateDiscount());
        System.out.println("Stock: " + getStock());
    }

    private double calculateDiscount() {
        return getPrice() - (getPrice() * DISCOUNT_RATE);
    }

    // Adjust the book stock
    public void adjustStock(int adjustment) {
        setStock(getStock() + adjustment);
        System.out.println("Stock adjusted.");
        System.out.println("Current stock: " + getStock());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}

// Class Library to store library location and a book
class Library {
    private Book book;
    private String location;

    Library(Book book, String location) {
        this.setBook(book);
        this.setLocation(location);
    }

    // Display library and book information
    public void showLibraryInfo() {
        System.out.println("Library Location: " + location);
        book.displayInfo();
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}


class Mainss{

    public static void main(String[] args) {
        Book book1 = new Book("Harry Potter", "J.K. Rowling", 10.2, 2);
        Library lib = new Library(book1, "Perpustakaan Kota");

        // Display initial information
        lib.showLibraryInfo();

        // Add more stock
        book1.adjustStock(5);

        // Display updated information
        lib.showLibraryInfo();
    }
}