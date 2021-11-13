package br.com.pucgo.perguntaai.services;

import br.com.pucgo.perguntaai.config.validation.UserRecordFieldValidationHandler;
import br.com.pucgo.perguntaai.exceptions.NotFoundUserException;
import br.com.pucgo.perguntaai.exceptions.UserRegistrationException;
import br.com.pucgo.perguntaai.models.Topic;
import br.com.pucgo.perguntaai.models.User;
import br.com.pucgo.perguntaai.models.form.UserRedefineForm;
import br.com.pucgo.perguntaai.repositories.TopicRepository;
import br.com.pucgo.perguntaai.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final TopicRepository topicRepository;

    private final UserRecordFieldValidationHandler validation;

    private final PasswordEncoder passwordEncoder;



    public boolean findUserByEmail(String email) {
        final Optional<User> userOptional = userRepository.findByEmail(email);
        return userOptional.isPresent();
    }

    public User getUserByEmail(String email) {
        Optional<User> obj = userRepository.findByEmail(email);
        return obj.orElseThrow(() -> new NotFoundUserException(
                "Usuário não encontrado! Email: " + email + ", Tipo: " + User.class.getName()));
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

    public void deleteUserById(Long id){
        try{
            userRepository.deleteById(id);
        }
        catch (Exception e) {
            throw new NotFoundUserException("user não excluído pois o mesmo não existe no BD!");
        }
    }

    public void deleteUser(User user) {
        final Page<Topic> topicPage;

        topicPage = topicRepository.findByAuthor(user, null);

        topicPage.forEach(topic -> {
            topic.setAuthor(null);
            topicRepository.save(topic);
        });

        userRepository.delete(user);
    }

    public Optional<User> userRegisterValidation(User user) {
        if(validation.validationOfFieldsInSavingUser(user).isPresent()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            LOGGER.info("validadas as credenciais do usuário com nome={}", user.getName());
            saveUser(user);
            return Optional.of(user);
        }

        else {
            throw new UserRegistrationException("Existem campos inválidos no cadastro deste usuário!");
        }
    }

    private void saveUser(User user) {
             userRepository.save(user);
    }

    private User updateUserData(User existentUser, User userToBeUpdated) {
        existentUser.setName(userToBeUpdated.getName());
        existentUser.setCourse(userToBeUpdated.getCourse());
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
                userRedefineForm.getCourse(),
                userRedefineForm.getAvatarOptions(),
                userRedefineForm.getBirthDate()
        );
    }
}
