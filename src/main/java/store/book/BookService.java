package store.book;

import store.book.Book;

import java.util.List;


public interface BookService {

    /**
     * Зарегистрировать новую книгу и создать задание для менеджеров - написать описание книги для сайта
     * @param book
     * @return taskId
     */
    int registerNewBook(Book book);
}