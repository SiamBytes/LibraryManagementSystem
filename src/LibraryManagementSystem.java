import java.io.*;
import java.util.*;

class Book implements Serializable {
    private String title;
    private String author;
    private int id;
    private boolean borrowed;

    public Book(String title, String author, int id) {
        this.title = title;
        this.author = author;
        this.id = id;
        this.borrowed = false;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isBorrowed() {
        return borrowed;
    }

    public void borrow() {
        borrowed = true;
    }

    public void returnBook() {
        borrowed = false;
    }

    @Override
    public String toString() {
        String status = borrowed ? "\u001B[31mBorrowed\u001B[0m" : "\u001B[32mAvailable\u001B[0m";
        return String.format("ID: %d | Title: %s | Author: %s | Status: %s", id, title, author, status);
    }
}

public class LibraryManagementSystem {
    private static final String DATA_FILE = "books.dat";
    private static List<Book> books = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        loadBooks();
        while (true) {
            System.out.println("\n\u001B[36m--- Library Management System ---\u001B[0m");
            System.out.println("1. View all books");
            System.out.println("2. Add a new book");
            System.out.println("3. Borrow a book");
            System.out.println("4. Return a book");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> viewBooks();
                case "2" -> addBook();
                case "3" -> borrowBook();
                case "4" -> returnBook();
                case "5" -> {
                    saveBooks();
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("\u001B[31mInvalid choice.\u001B[0m");
            }
        }
    }

    private static void viewBooks() {
        if (books.isEmpty()) {
            System.out.println("\u001B[33mNo books available.\u001B[0m");
            return;
        }
        System.out.println("\u001B[35m--- Book List ---\u001B[0m");
        for (Book book : books) {
            System.out.println(book);
        }
    }

    private static void addBook() {
        System.out.print("Enter book title: ");
        String title = scanner.nextLine();
        System.out.print("Enter author: ");
        String author = scanner.nextLine();
        int id = books.size() + 1;
        books.add(new Book(title, author, id));
        System.out.println("\u001B[32mBook added successfully!\u001B[0m");
    }

    private static void borrowBook() {
        System.out.print("Enter book ID to borrow: ");
        int id = Integer.parseInt(scanner.nextLine());
        for (Book book : books) {
            if (book.getId() == id) {
                if (!book.isBorrowed()) {
                    book.borrow();
                    System.out.println("\u001B[32mYou borrowed: " + book.getTitle() + "\u001B[0m");
                } else {
                    System.out.println("\u001B[31mThat book is already borrowed.\u001B[0m");
                }
                return;
            }
        }
        System.out.println("\u001B[31mBook not found.\u001B[0m");
    }

    private static void returnBook() {
        System.out.print("Enter book ID to return: ");
        int id = Integer.parseInt(scanner.nextLine());
        for (Book book : books) {
            if (book.getId() == id) {
                if (book.isBorrowed()) {
                    book.returnBook();
                    System.out.println("\u001B[32mYou returned: " + book.getTitle() + "\u001B[0m");
                } else {
                    System.out.println("\u001B[33mThis book was not borrowed.\u001B[0m");
                }
                return;
            }
        }
        System.out.println("\u001B[31mBook not found.\u001B[0m");
    }

    @SuppressWarnings("unchecked")
    private static void loadBooks() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            books = (List<Book>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            books = new ArrayList<>(); // Start fresh if file not found
        }
    }

    private static void saveBooks() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(books);
        } catch (IOException e) {
            System.out.println("\u001B[31mError saving data.\u001B[0m");
        }
    }
}
