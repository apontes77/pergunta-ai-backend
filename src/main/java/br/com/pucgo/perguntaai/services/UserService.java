package br.com.pucgo.perguntaai.services;

import br.com.pucgo.perguntaai.exceptions.NotFoundUserException;
import br.com.pucgo.perguntaai.models.Topic;
import br.com.pucgo.perguntaai.models.User;
import br.com.pucgo.perguntaai.repositories.TopicRepository;
import br.com.pucgo.perguntaai.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TopicRepository topicRepository;

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundUserException("Usuário não encontrado"));

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
        return userRepository.save(user);
    }
}
