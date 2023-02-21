package dawn.springTest.controller;

import dawn.springTest.domain.Todo;
import dawn.springTest.domain.User;
import dawn.springTest.service.UserService;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class UserController {
    private final UserService userService;

    @PostMapping("users/sign-up")
    public ResponseEntity<User> signup(@RequestBody UserForm userForm){
        try {
            return ResponseEntity.ok(userService.signup(userForm));
        }catch(EntityExistsException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("users/login")
    public ResponseEntity<User> login(@RequestBody UserForm userForm){
        try {
            return ResponseEntity.ok(userService.login(userForm));
        }catch (EntityExistsException e){
            return ResponseEntity.badRequest().build();
        }
    }


}
