import com.google.inject.Guice;
import com.google.inject.Injector;
import store.BookStoreModule;
import store.DAO.BookDAO;
import store.Model.Book;
import store.Services.BookService;

class Main {

    public static void main(final String... args) {
        Injector injector = Guice.createInjector(new BookStoreModule());
        BookService service = injector.getInstance(BookService.class);
        BookDAO bookDAO = injector.getInstance(BookDAO.class);
        bookDAO.insert(Book.create("Игра в бисер", "Герман Гессе", 450));
    }
}