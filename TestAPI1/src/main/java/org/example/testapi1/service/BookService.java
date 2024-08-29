package org.example.testapi1.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.testapi1.dto.BookDTO;
import org.example.testapi1.dto.CommentDTO;
import org.example.testapi1.entity.Book;
import org.example.testapi1.entity.Comment;
import org.example.testapi1.repository.BookRepository;
import org.example.testapi1.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final CommentRepository commentRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    public BookService(BookRepository bookRepository, CommentRepository commentRepository) {
        this.bookRepository = bookRepository;
        this.commentRepository = commentRepository;
    }

    //save book
    public Book saveBook(Book book) {
        //book.getComments().forEach(comment -> comment.setBook(book));
        return bookRepository.save(book);
    }

    //List Book
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book createBook(BookDTO bookDTO){
        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());

        List<Comment> comments = bookDTO.getComments().stream()
                .map(commentDTO ->{
                    Comment comment = new Comment();
                    comment.setContent(commentDTO.getContent());
                    comment.setBook(book);
                    return comment;
                }).collect(Collectors.toList());
        book.setComments(comments);
        return bookRepository.save(book);
    }

    //get comment By id book
    public List<Comment> findCommentsByBookId(Integer bookId){
        CriteriaBuilder db = entityManager.getCriteriaBuilder();
        CriteriaQuery<Comment> query = db.createQuery(Comment.class);
        Root<Comment> commentRoot = query.from(Comment.class);

        query.select(commentRoot).where(db.equal(commentRoot.get("book"), bookId));

        TypedQuery<Comment> comments = entityManager.createQuery(query);
        return comments.getResultList();
    }
//    public List<CommentDTO> getCommentsByBookId(Integer bookId){
//        Book book = bookRepository.findCommentsByBookId(bookId)
//                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id " + bookId));
//    }
    public List<Comment> getCommentsByBookId(Long bookId){
        return bookRepository.findCommentsByBookId(bookId);
    }

    public List<CommentDTO> getCommentsByBookId1(Long bookId){
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        return book.getComments().stream()
                .map(comment -> new CommentDTO(comment.getContent(), comment.getId()))
                .collect(Collectors.toList());
    }

    public List<CommentDTO> getCommentFilter(Long bookId){
        List<Comment> comments = commentRepository.findByBookId(bookId);
        return comments.stream().map(comment -> new CommentDTO(comment.getContent())).collect(Collectors.toList());
    }

    public List<String> getCommentFilter1(Long bookId){
        List<Comment> comments = commentRepository.findByBookId(bookId);
        return comments.stream().map(Comment::getContent).collect(Collectors.toList());
    }

}
