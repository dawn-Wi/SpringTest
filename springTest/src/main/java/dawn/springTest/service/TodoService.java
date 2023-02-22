package dawn.springTest.service;

import dawn.springTest.domain.Todo;
import dawn.springTest.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class TodoService {
    private final TodoRepository todoRepository;
    public List<Todo> getTodoListByUser(String email){
        return todoRepository.findAllByOwnerEmail(email);
    }

    public Todo submitTodo(Todo toAdd){
        Todo todoToSave = new Todo(toAdd.getContent(),toAdd.getLimitDateTime(),toAdd.getTag(), toAdd.getOwnerEmail());
        return todoRepository.save(todoToSave);
    }
}
