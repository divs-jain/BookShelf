import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.ArrayList;

public class Main extends Application {

    private final ArrayList<Book> library = new ArrayList<>();
    private ListView<String> bookList = new ListView<>();
    private Stage window;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        window.setTitle("Library Management System");

        // Show login scene at the start
        Scene loginScene = createLoginScene();
        window.setScene(loginScene);
        window.show();
    }

    private Scene createLoginScene() {

        Label usernameLabel = new Label("Username:");
        TextField usernameInput = new TextField();

        Label passwordLabel = new Label("Password:");
        PasswordField passwordInput = new PasswordField();

        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> {
            String username = usernameInput.getText();
            String password = passwordInput.getText();

            if (username.equals("pooja") && password.equals("oop")) {
                showAlert("Login Success", "Welcome, Pooja ma'am!");
                window.setScene(createMainMenuScene());
            } else if (username.equals("vibha") && password.equals("pdc")) {
                showAlert("Login Success", "Welcome, Vibha ma'am!");
                window.setScene(createMainMenuScene());
            } else if (username.equals("admin") && password.equals("123")) {
                showAlert("Login Success", "Welcome, Admin!");
                window.setScene(createMainMenuScene());
            } else {
                showAlert("Login Failed", "Invalid username or password.");
            }
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(usernameLabel, usernameInput, passwordLabel, passwordInput, loginButton);

        return new Scene(layout, 300, 200);

    }

    private Scene createMainMenuScene() {
        Label welcomeLabel = new Label("Welcome to the Library Management System");

        Label bookListLabel = new Label("List of Books:");
        updatebookList();

        Button addBookButton = new Button("Add Book");
        addBookButton.setOnAction(e -> window.setScene(createAddBookScene()));

        Button borrowBookButton = new Button("Borrow Book");
        borrowBookButton.setOnAction(e -> window.setScene(createBorrowBookScene()));

        Button returnBookButton = new Button("Return Book");
        returnBookButton.setOnAction(e -> window.setScene(createReturnBookScene()));

        VBox layout = new VBox(10);
        layout.getChildren().addAll(welcomeLabel, addBookButton, borrowBookButton, returnBookButton, bookListLabel, bookList);

        return new Scene(layout, 400, 300);
    }

    private Scene createAddBookScene() {
        Label titleLabel = new Label("Title:");
        TextField titleInput = new TextField();

        Label authorLabel = new Label("Author:");
        TextField authorInput = new TextField();

        Label yearLabel = new Label("Year:");
        TextField yearInput = new TextField();

        Button addButton = new Button("Add Book");
        addButton.setOnAction(e -> {
            String title = titleInput.getText();
            String author = authorInput.getText();
            String year = yearInput.getText();

            // Handle invalid or missing input with exception handling
            if (!title.isEmpty() && !author.isEmpty() && !year.isEmpty()) {
                try {
                    Integer.parseInt(year); // Validate if year is an integer
                    library.add(new Book(title, author, year, "Available"));
                    showAlert("Success", "Book added successfully.");
                    updatebookList();
                    window.setScene(createMainMenuScene());
                } catch (NumberFormatException ex) {
                    showAlert("Input Error", "Please enter a valid year.");
                }
            } else {
                showAlert("Input Error", "Please fill in all fields.");
            }
        });

        Button backButton = new Button("Back to Main Menu");
        backButton.setOnAction(e -> window.setScene(createMainMenuScene()));

        VBox layout = new VBox(10);
        layout.getChildren().addAll(titleLabel, titleInput, authorLabel, authorInput, yearLabel, yearInput, addButton, backButton);

        return new Scene(layout, 400, 300);
    }

    private Scene createBorrowBookScene() {
        Label selectBookLabel = new Label("Select a book to borrow:");
//        bookList = new ListView<>();
        updatebookList();

        Button borrowButton = new Button("Borrow Book");
        borrowButton.setOnAction(e -> {
            int selectedIndex = bookList.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                    Book selectedBook = library.get(selectedIndex);
                    if (selectedBook.getStatus().equals("Available")) {
                        selectedBook.setStatus("Borrowed");
                        showAlert("Success", "You borrowed the book.");
                        updatebookList();
                        window.setScene(createMainMenuScene());
                    } else {
                        showAlert("Error", "This book is already borrowed.");
                    }
            } else {
                showAlert("Selection Error", "Please select a book to borrow.");
            }
        });

        Button backButton = new Button("Back to Main Menu");
        backButton.setOnAction(e -> window.setScene(createMainMenuScene()));

        VBox layout = new VBox(10);
        layout.getChildren().addAll(selectBookLabel, bookList, borrowButton, backButton);

        return new Scene(layout, 400, 300);
    }

    private Scene createReturnBookScene() {
        Label selectBookLabel = new Label("Select a book to return:");
//        bookList = new ListView<>();
        updatebookList();

        Button returnButton = new Button("Return Book");
        returnButton.setOnAction(e -> {
            int selectedIndex = bookList.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                    Book selectedBook = library.get(selectedIndex);
                    if (selectedBook.getStatus().equals("Borrowed")) {
                        selectedBook.setStatus("Available");
                        showAlert("Success", "You returned the book.");
                        updatebookList();
                        window.setScene(createMainMenuScene());
                    } else {
                        showAlert("Error", "This book was not borrowed.");
                    }
            } else {
                showAlert("Selection Error", "Please select a book to return.");
            }
        });

        Button backButton = new Button("Back to Main Menu");
        backButton.setOnAction(e -> window.setScene(createMainMenuScene()));

        VBox layout = new VBox(10);
        layout.getChildren().addAll(selectBookLabel, bookList, returnButton, backButton);

        return new Scene(layout, 400, 300);
    }

    private void updatebookList() {
        bookList.getItems().clear();
        for(int i = 0; i < library.size(); i++) {
            Book book = library.get(i);
            bookList.getItems().add(book.toString());
        }
    }


    private static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class Book {
        private final String title;
        private final String author;
        private final String year;
        private String status;

        public Book(String title, String author, String year, String status) {
            this.title = title;
            this.author = author;
            this.year = year;
            this.status = status;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return title + " by " + author + " (" + year + ") - " + status;
        }
    }
}