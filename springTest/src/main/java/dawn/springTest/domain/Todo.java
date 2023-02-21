package dawn.springTest.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Entity
@Data
@Table(name = "TODOLIST")
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private String limitDateTime;
    private String tag;
    private String ownerEmail;

    public Todo(String content, String limitDateTime, String tag, String ownerEmail){
        this.content = content;
        this.limitDateTime = limitDateTime;
        this.tag = tag;
        this.ownerEmail = ownerEmail;
    }
}
