package br.com.pucgo.perguntaai.services;

import br.com.pucgo.perguntaai.exceptions.NotFoundUserException;

import br.com.pucgo.perguntaai.models.Topic;
import br.com.pucgo.perguntaai.repositories.TopicRepository;

import br.com.pucgo.perguntaai.models.DTO.UserDto;
import br.com.pucgo.perguntaai.models.User;
import br.com.pucgo.perguntaai.models.form.UserForm;
import br.com.pucgo.perguntaai.models.form.UserRedefineForm;

import br.com.pucgo.perguntaai.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TopicRepository topicRepository;

    private final PasswordEncoder passwordEncoder;

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@pucgo\\.edu\\.br$", Pattern.CASE_INSENSITIVE);

    public static final Pattern VALID_PASSWORD_REGEX =
            Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$", Pattern.CASE_INSENSITIVE);

    public static final Pattern VALID_NAME_REGEX =
            Pattern.compile("^\\S*[A-Z]{5,255}$", Pattern.CASE_INSENSITIVE);

    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    public boolean findUserByEmail(String email) {
        final Optional<User> userOptional = userRepository.findByEmail(email);
        return userOptional.isPresent();
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

    public void deleteUser(User user) {
        final Page<Topic> topicPage;

        topicPage = topicRepository.findByAuthor(user, null);

        topicPage.forEach(topic -> {
            topic.setAuthor(null);
            topicRepository.save(topic);
        });

        userRepository.delete(user);
    }

    public User saveUser(User user) {
        Matcher matcherMail = VALID_EMAIL_ADDRESS_REGEX.matcher(user.getEmail());
        Matcher matcherPassword = VALID_PASSWORD_REGEX.matcher(user.getPassword());
        Matcher matcherName = VALID_NAME_REGEX.matcher(user.getName());

        if(!matcherMail.find()) {
            LOGGER.info("Invalid Email: {}", user.getEmail());
            return null;
        }

        if(!matcherPassword.find()) {
            LOGGER.info("Invalid Password: {}", user.getPassword());
            return null;
        }

        if(!matcherName.find()) {
            LOGGER.info("Invalid Name: {}", user.getName());
            return null;
        }

        LOGGER.info("saving user: id={}, name={}", user.getId(), user.getName());
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
