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
        Todo todoToSave = new Todo(toAdd.getContent(),toAdd.getLimitDateTime(),toAdd.getTag(), toAdd.getOwnerEmail(),toAdd.getFinish());
        return todoRepository.save(todoToSave);
    }

    public Todo finishTodo(Todo finishTodo){
        if (finishTodo.getId()!=null){
            finishTodo.setFinish("true");
        }
        return todoRepository.save(finishTodo);
    }

    public Todo deleteTodo(Todo deleteTodo){
        todoRepository.delete(deleteTodo);
        return deleteTodo;
    }

    public Todo editTodo(Todo editTodo){
        if (editTodo.getId()!=null){
            editTodo.setContent(editTodo.getContent());
            editTodo.setFinish(editTodo.getFinish());
            editTodo.setTag(editTodo.getTag());
            editTodo.setLimitDateTime(editTodo.getLimitDateTime());
        }
        return todoRepository.save(editTodo);
    }
}
