package store.Services;

import store.Model.Book;

import java.util.List;


public interface BookService {

    /**
     * Зарегистрировать новую книгу и создать задание для менеджеров - написать описание книги для сайта
     * @param book
     * @return taskId
     */
    int registerNewBook(Book book);
}