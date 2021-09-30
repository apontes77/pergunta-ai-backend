package br.com.pucgo.perguntaai.services;

import br.com.pucgo.perguntaai.exceptions.NotFoundUserException;
import br.com.pucgo.perguntaai.models.User;
import br.com.pucgo.perguntaai.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundUserException("Usuário não encontrado"));

    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
