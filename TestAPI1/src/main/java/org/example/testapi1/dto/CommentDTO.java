package org.example.testapi1.dto;

public class CommentDTO {
    private String content;
    private int id;

    public CommentDTO(String content, int id) {
        this.content = content;
        this.id = id;
    }

    public CommentDTO(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
