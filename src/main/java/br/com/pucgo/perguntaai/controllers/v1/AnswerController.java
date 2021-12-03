package br.com.pucgo.perguntaai.controllers.v1;

import br.com.pucgo.perguntaai.models.Answer;
import br.com.pucgo.perguntaai.models.DTO.AnswerDto;
import br.com.pucgo.perguntaai.services.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/answers")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;

    @PostMapping
    public void insertAnswerToTopic(@RequestBody Answer answer) {
        answerService.insertAnswerToAnTopic(answer);
    }

    @GetMapping
    public List<Answer> getAnswersAssociatedToAnTopic() {
        return answerService.getAnswersAssociatedToAnTopic();
    }

}
