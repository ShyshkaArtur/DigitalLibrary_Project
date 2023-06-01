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
public class PersonDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Show all People on page
    public List<Person> index() {
        return jdbcTemplate.query("SELECT * FROM \"Person\"", new BeanPropertyRowMapper<>(Person.class));
    }

    // Show information about Person on page
    public Person show(int id) {
        return jdbcTemplate.query("SELECT * FROM \"Person\" WHERE id=?", new Object[]{id},
                new BeanPropertyRowMapper<>(Person.class)).stream().findAny().orElse(null);
    }

    // Add Person in DB
    public void save(Person person) {
        jdbcTemplate.update("INSERT INTO \"Person\"(user_name, date_of_birth) VALUES (?, ?)",
                person.getUserName(), person.getDateOfBirth());
    }

    // Update Person in DB
    public void update(int id, Person updatedPerson) {
        jdbcTemplate.update("UPDATE \"Person\" SET user_name=?, date_of_birth=? WHERE id=?",
                updatedPerson.getUserName(), updatedPerson.getDateOfBirth(), id);
    }

    // Delete Person from DB
    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM \"Person\" WHERE  id=?", id);

    }

    // Name uniqueness validation
    public Optional<Person> getPersonByFullName(String userName) {
        return jdbcTemplate.query("SELECT * FROM \"Person\" WHERE user_name=?", new Object[]{userName},
                new BeanPropertyRowMapper<>(Person.class)).stream().findAny();
    }

    // Get a book using a person's id
    public List<Book> getBooksByPersonId(int id) {
        return jdbcTemplate.query("SELECT * FROM \"Books\" WHERE person_id = ?", new Object[]{id},
                new BeanPropertyRowMapper<>(Book.class));
    }
}
