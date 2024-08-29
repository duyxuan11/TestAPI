package org.example.testapi1.repository;

import org.example.testapi1.entity.Book;
import org.example.testapi1.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("Select b FROM Book b LEFT JOIN FETCH b.comments")
    List<Book> findAll();

    @Query("SELECT c FROM Comment c WHERE c.book.id = :bookId")
    List<Comment> findCommentsByBookId(@Param("bookId") Long bookId);


    //List<Comment> findAllById(Long bookId);
}
