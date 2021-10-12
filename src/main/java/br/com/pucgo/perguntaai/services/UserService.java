package br.com.pucgo.perguntaai.services;

import br.com.pucgo.perguntaai.exceptions.NotFoundUserException;
import br.com.pucgo.perguntaai.models.DTO.UserDto;
import br.com.pucgo.perguntaai.models.User;
import br.com.pucgo.perguntaai.models.form.UserForm;
import br.com.pucgo.perguntaai.models.form.UserRedefineForm;
import br.com.pucgo.perguntaai.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    public boolean findUserByEmail(String email) {
        final Optional<User> userOptional = userRepository.findByEmail(email);
        if(!userOptional.isPresent()){
            return true;
        }
        return false;
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public User update(User obj) {
        User newObj = findById(obj.getId());
        newObj = updateUserData(newObj, obj);
        return userRepository.save(newObj);
    }

    public User updatePassword(User obj) {
        User newObj = findById(obj.getId());
        newObj = updateUserPassword(newObj, obj.getPassword());
        return userRepository.save(newObj);
    }

    public User findById(Long id) {
        Optional<User> obj = userRepository.findById(id);
        return obj.orElseThrow(() -> new NotFoundUserException(
                "Objeto não encontrado! Id: " + id + ", Tipo: " + User.class.getName()));
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    private User updateUserData(User existentUser, User userToBeUpdated) {
        existentUser.setName(userToBeUpdated.getName());
        existentUser.setRoleUser(userToBeUpdated.getRoleUser());
        existentUser.setCourse(userToBeUpdated.getCourse());
        existentUser.setPassword(passwordEncoder.encode(userToBeUpdated.getPassword()));
        existentUser.setAvatarOptions(userToBeUpdated.getAvatarOptions());
        existentUser.setBirthDate(userToBeUpdated.getBirthDate());
        return existentUser;
    }

    private User updateUserPassword(User existentUser, String passwordToBeUpdated) {
        existentUser.setPassword(passwordEncoder.encode(passwordToBeUpdated));
        return existentUser;
    }

    public User fromUserRedefineForm(@Valid UserRedefineForm userRedefineForm) {
        return new User(
                userRedefineForm.getName(),
                userRedefineForm.getPassword(),
                userRedefineForm.getCourse(),
                userRedefineForm.getRoleUser(),
                userRedefineForm.getAvatarOptions(),
                userRedefineForm.getBirthDate()
        );
    }
}
