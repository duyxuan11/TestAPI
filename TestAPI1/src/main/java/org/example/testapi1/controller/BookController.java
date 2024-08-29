package org.example.testapi1.controller;

import org.example.testapi1.dto.BookDTO;
import org.example.testapi1.dto.CommentDTO;
import org.example.testapi1.entity.Book;
import org.example.testapi1.entity.Comment;
import org.example.testapi1.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class BookController {
    private final BookService bookService;
    private static final String API_KEY = "API_KEY";
    private static final String URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent";

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    //create book
//    @PostMapping("/create-book")
//    public ResponseEntity<Book> saveBook(@RequestBody Book book) {
//        Book newBok = bookService.saveBook(book);
//        return ResponseEntity.ok(newBok);
//    }
    //create book
    @PostMapping("/create-book")
    public ResponseEntity<Book> createBook(@RequestBody BookDTO bookDTO) {
        Book book = bookService.createBook(bookDTO);
        return ResponseEntity.ok(book);
    }

    //list all books
    @GetMapping("/list")
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    //@GetMapping("/listComment/{id}")
//    public ResponseEntity<Comment> getCommentById(@PathVariable Integer id) {
//        Optional<Comment> comment = bookService.findCommentsByBookId(id);
//        return comment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
//    }

    @GetMapping("/listComment/{id}")
    public ResponseEntity<Comment> getBookComment(@PathVariable Long id) {
        List<Comment> comment = bookService.getCommentsByBookId(id);
        return comment.stream().map(ResponseEntity::ok).findFirst().orElse(ResponseEntity.notFound().build());
    }

    //get comment by bookID
    @GetMapping("/listComment/{bookId}/comments")
    public ResponseEntity<List<CommentDTO>> getBookComments(@PathVariable Long bookId) {
        List<CommentDTO> comment = bookService.getCommentsByBookId1(bookId);
        return ResponseEntity.ok(comment);
    }

    @GetMapping("/listComment/{bookId}/comments/filter")
    public ResponseEntity<List<CommentDTO>> getBookCommentsFiltered(@PathVariable Long bookId) {
        List<CommentDTO> comment = bookService.getCommentFilter(bookId);
        return ResponseEntity.ok(comment);
    }

    @GetMapping("/listComment/{bookId}/comments/filter1")
    public ResponseEntity<List<Map<String,String>>> getBookComments1(@PathVariable Long bookId) {
        List<String> contents = bookService.getCommentFilter1(bookId);

        List<Map<String,String>> response = contents.stream()
                .map(content -> Map.of("content",content))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/listComment/{bookId}/comments/filter2")
    public ResponseEntity<String> generateSumary(@PathVariable Long bookId){
        List<String> contents = bookService.getCommentFilter1(bookId);

        List<Map<String, Object>> parts = new ArrayList<>();
        parts.add(Map.of("text","Summarize the following reviews:"));

//        for (int i=0;i<contents.size();i++){
//            //String reviewText = "Review "+(i+1)+": "+contents.get(i);
//            String reviewText = contents.get(i);
//            parts.add(Map.of("text",reviewText));
//        }
        for(String content : contents){
            parts.add(Map.of("text",content));
        }

        Map<String, Object> requestBody =Map.of(
                "contents", List.of(Map.of("parts",parts))
        );

        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL)
                .queryParam("key",API_KEY);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.POST,
                entity,
                String.class
        );
        return response;
    }


}
