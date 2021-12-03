package br.com.pucgo.perguntaai.services;

import br.com.pucgo.perguntaai.models.Answer;
import br.com.pucgo.perguntaai.repositories.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;

    public void insertAnswerToAnTopic(final Answer answer) {
        answerRepository.save(answer);
    }

    public List<Answer> getAnswersAssociatedToAnTopic() {
        return answerRepository.findAll();
    }
}
