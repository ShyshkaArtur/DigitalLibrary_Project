package org.example.dao;

import org.example.models.Book;
import org.example.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BookDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Show all Book on page
    public List<Book> index() {
        return jdbcTemplate.query("SELECT * FROM \"Books\"", new BeanPropertyRowMapper<>(Book.class));
    }

    // Show information about Book on page
    public Book show(int id) {
        return jdbcTemplate.query("SELECT * FROM \"Books\" WHERE id=?", new Object[]{id},
                new BeanPropertyRowMapper<>(Book.class)).stream().findAny().orElse(null);
    }

    // Add Book in DB
    public void save(Book book) {
        jdbcTemplate.update("INSERT INTO \"Books\"(book_name, author, publication_year) VALUES (?, ?, ?)",
                book.getBookName(), book.getAuthor(), book.getPublicationYear());
    }

    // Update Book in DB
    public void update(int id, Book updatedBook) {
        jdbcTemplate.update("UPDATE \"Books\" SET book_name=?, author=?, publication_year=? WHERE id=?",
                updatedBook.getBookName(), updatedBook.getAuthor(), updatedBook.getPublicationYear(), id);
    }

    // Delete Book from DB
    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM \"Books\" WHERE  id=?", id);
    }

    // Join tables Books and Person
    public Optional<Person> getBookOwner(int id) {
        // Выбираем все колонки таблицы Person из объединенной таблицы
        return jdbcTemplate.query("SELECT \"Person\".* FROM \"Books\" JOIN \"Person\" ON \"Books\".person_id = \"Person\".id WHERE \"Books\".id = ?",
                        new Object[]{id}, new BeanPropertyRowMapper<>(Person.class)).stream().findAny();
    }

    // Release Book (Set null)
    public void release(int id) {
        jdbcTemplate.update("UPDATE \"Books\" SET person_id=NULL WHERE id=?", id);
    }

    //Assigns a Book to a Person (Set id)
    public void assign(int id, Person person) {
        jdbcTemplate.update("UPDATE \"Books\" SET person_id=? WHERE id=?", person.getId(), id);
    }
}
