package store.book;

/**
 * Represents Book entity by its Id, Title, Author and Pages Count.
 */
public class Book {

    private Integer id;
    private String title;
    private String author;
    private int pagesCount;

    public static Book create(String title, String author, int pagesCount) {
        return new Book(null, title, author, pagesCount);
    }

    public static Book existing(int id, String title, String author, int pagesCount) {
        return new Book(id, title, author, pagesCount);
    }

    private Book(Integer id, String title, String author, int pagesCount) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.pagesCount = pagesCount;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPagesCount(int pagesCount) {
        this.pagesCount = pagesCount;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getPagesCount() {
        return pagesCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        if (pagesCount != book.pagesCount) return false;
        if (id != null ? !id.equals(book.id) : book.id != null) return false;
        if (title != null ? !title.equals(book.title) : book.title != null) return false;
        return author != null ? author.equals(book.author) : book.author == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + pagesCount;
        return result;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", pagesCount=" + pagesCount +
                '}';
    }
}
