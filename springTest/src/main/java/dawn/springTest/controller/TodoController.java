package dawn.springTest.controller;

import dawn.springTest.domain.Todo;
import dawn.springTest.service.TodoService;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class TodoController {
    private final TodoService todoService;

    @GetMapping("todo")
    public ResponseEntity<List<Todo>> getTodoListByUser(@RequestParam("email")String email){
        try {
            return ResponseEntity.ok(todoService.getTodoListByUser(email));
        }catch (EntityExistsException e){
            return ResponseEntity.badRequest().build();
        }
    }
    @PostMapping("todo")
    public ResponseEntity<Todo> submitTodo(@RequestBody Todo toAdd){
        try {
            return ResponseEntity.ok(todoService.submitTodo(toAdd));
        }catch (EntityExistsException e){
            return ResponseEntity.badRequest().build();
        }
    }
    @PostMapping("todo/finish")
    public ResponseEntity<Todo> finishTodo(@RequestBody Todo finishTodo){
        try {
            return ResponseEntity.ok(todoService.finishTodo(finishTodo));
        }catch (EntityExistsException e){
            return ResponseEntity.badRequest().build();
        }
    }
    @PostMapping("todo/delete")
    public ResponseEntity<Todo> deleteTodo(@RequestBody Todo deleteTodo){
        try {
            return ResponseEntity.ok(todoService.deleteTodo(deleteTodo));
        }catch (EntityExistsException e){
            return ResponseEntity.badRequest().build();
        }
    }
    @PostMapping("todo/edit")
    public ResponseEntity<Todo> editTodo(@RequestBody Todo editTodo){
        try {
            return ResponseEntity.ok(todoService.editTodo(editTodo));
        }catch (EntityExistsException e){
            return ResponseEntity.badRequest().build();
        }
    }
}
