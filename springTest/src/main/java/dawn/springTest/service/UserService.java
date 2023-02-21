package dawn.springTest.service;

import dawn.springTest.controller.UserForm;
import dawn.springTest.domain.User;
import dawn.springTest.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public User signup(UserForm userForm) {
        userRepository.findByEmail(userForm.getEmail()).ifPresent(foundUser -> {
            throw new EntityExistsException("이미 존재하는 회원입니다.");
        });
        User userToSave = new User(userForm.getEmail(), userForm.getPassword());
        return userRepository.save(userToSave);
    }

    public User login(UserForm userForm) {
        Optional<User> tryLoginUser = userRepository.findByEmailAndPassword(userForm.getEmail(), userForm.getPassword());
        System.out.println(tryLoginUser);
        return tryLoginUser.get();
    }


}
