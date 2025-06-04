package com.example.Exercise5.service;

import com.example.Exercise5.model.Book;
import com.example.Exercise5.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    @Autowired
    BookRepository repo;


    public List<Book> getAllBooks() {
        return repo.findAll();
    }

    public Book getBookById(int id) {
        return repo.findById(id).orElse(new Book());

    }

    public Book addBook(Book book) {
        return repo.save(book);

    }

    public Book updateBook(Book book) {
        return repo.save(book);

    }

    public void deleteBook(int id) {
        repo.deleteById(id);
    }
}
