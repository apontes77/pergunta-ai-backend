package br.com.pucgo.perguntaai.services;

import br.com.pucgo.perguntaai.exceptions.NotFoundUserException;
import br.com.pucgo.perguntaai.models.User;
import br.com.pucgo.perguntaai.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }
    public User updatePasswordUser(User user, String newPassword) {
        user.setPassword(newPassword);
        return userRepository.save(user);
    }
}
