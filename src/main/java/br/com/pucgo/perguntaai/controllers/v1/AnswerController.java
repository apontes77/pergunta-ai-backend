package br.com.pucgo.perguntaai.controllers.v1;

import br.com.pucgo.perguntaai.models.Answer;
import br.com.pucgo.perguntaai.models.DTO.AnswerDto;
import br.com.pucgo.perguntaai.models.DTO.TopicDto;
import br.com.pucgo.perguntaai.models.form.AnswerUpdateForm;
import br.com.pucgo.perguntaai.models.form.TopicFormUpdate;
import br.com.pucgo.perguntaai.models.Topic;
import br.com.pucgo.perguntaai.models.User;
import br.com.pucgo.perguntaai.models.enums.TopicStatus;
import br.com.pucgo.perguntaai.models.form.AnswerForm;
import br.com.pucgo.perguntaai.repositories.AnswerRepository;
import br.com.pucgo.perguntaai.repositories.TopicRepository;
import br.com.pucgo.perguntaai.repositories.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/answer")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerRepository answerRepository;
    private final TopicRepository topicRepository;
    private final UserRepository userRepository;

    @ApiResponse(description = "retorna as resposta do tópico selecionado")
    @GetMapping("/{id}")
    @Operation(summary = "list answer", security = @SecurityRequirement(name = "bearerAuth"))
    public Page<AnswerDto> list(@PathVariable("id") Long id,
                                @Parameter(hidden = true) @PageableDefault(sort = "id", direction = Sort.Direction.ASC, page = 0, size = 10) Pageable pagination) {

        Page<Answer> answerPage;
        Optional<Topic> topicOptional = topicRepository.findById(id);
        answerPage = answerRepository.findByTopic(topicOptional.get(), pagination);

        return AnswerDto.convert(answerPage);
    }

    @PostMapping
    @Transactional
    @Operation(summary = "insert answer", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> register(@RequestBody @Valid AnswerForm answerForm,
                                             UriComponentsBuilder uriBuilder) {
        Answer newAnswer = answerForm.convert();
        User author = userRepository.getById(newAnswer.getAuthor().getId());
        newAnswer.setAuthor(author);
        Topic topic = topicRepository.getById(newAnswer.getTopic().getId());
        newAnswer.setTopic(topic);

        Optional<Answer> answerOptional = answerRepository.findByTopicAndAuthor(topic,author);
        if(answerOptional.isPresent())
            return ResponseEntity.status(403).body("Este usuário já respondeu esse tópico.");
        if(topic.getStatus().equals(TopicStatus.SOLVED) || topic.getStatus().equals(TopicStatus.CLOSED))
            return ResponseEntity.status(403).body("Este tópico está resolvido ou fechado. Não é possivel enviar novas respostas para ele.");
        answerRepository.save(newAnswer);
        URI uri = uriBuilder.path("/answer/{id}").buildAndExpand(newAnswer.getId()).toUri();
        return ResponseEntity.created(uri).body(new AnswerDto(newAnswer));
    }

    @PutMapping("/{id}")
    @Transactional
    @Operation(summary = "update answer", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestBody @Valid AnswerUpdateForm answerForm) {
        Optional<Answer> oldAnswerOptional = answerRepository.findById(id);
        if (oldAnswerOptional.isPresent()) {
            if(!oldAnswerOptional.get().getAuthor().getId().equals(answerForm.getAuthorId())){
                return ResponseEntity.status(403).body("Somente o criador da resposta pode altualizar seu conteudo.");
            }
            if(oldAnswerOptional.get().getTopic().getStatus().equals(TopicStatus.CLOSED) || oldAnswerOptional.get().getTopic().getStatus().equals(TopicStatus.SOLVED)){
                return ResponseEntity.status(403).body("Status do tópico está como fechado ou resolvido. Não é possivel editar sua resposta.");
            }
            if(oldAnswerOptional.get().isSolution())
                return ResponseEntity.status(403).body("Essa resposta é a solução do tópico. Não é possivel alterar seu conteudo");
            Answer answer = answerForm.update(id, answerRepository);
            return ResponseEntity.ok(new AnswerDto(answer));
        }
        return ResponseEntity.notFound().build();
    }

    /*@DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "delete answer", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Answer> answerOptional = answerRepository.findById(id);
        if (answerOptional.isPresent()) {
            if(answerOptional.get().getTopic().getStatus().equals(TopicStatus.CLOSED) || answerOptional.get().getTopic().getStatus().equals(TopicStatus.SOLVED)){
                return ResponseEntity.status(403).body("Status do tópico está como fechado ou resolvido. Não é possivel excluir sua resposta.");
            }
            if(answerOptional.get().isSolution())
                return ResponseEntity.status(403).body("Essa resposta é a solução do tópico. Não é possivel excluir sua resposta.");
            answerRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }*/
}

