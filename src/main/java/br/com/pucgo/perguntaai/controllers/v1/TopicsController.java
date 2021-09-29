package br.com.pucgo.perguntaai.controllers.v1;

import br.com.pucgo.perguntaai.models.DTO.TopicDto;
import br.com.pucgo.perguntaai.models.DTO.TopicDtoDetails;
import br.com.pucgo.perguntaai.models.DTO.TopicFormUpdate;
import br.com.pucgo.perguntaai.models.Topic;
import br.com.pucgo.perguntaai.models.form.TopicForm;
import br.com.pucgo.perguntaai.repositories.TopicRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
@RequestMapping("/api/v1/topics")
public class TopicsController {

    @Autowired
    private TopicRepository topicRepository;

    @ApiResponse(description = "retorna os tópicos do fórum")
    @GetMapping
    @Cacheable(value = "topicsList")
    @Operation(summary = "list topics", security = @SecurityRequirement(name = "bearerAuth"))
    public Page<TopicDto> list(@RequestParam(required = false) String status,
                               @Parameter(hidden = true) @PageableDefault(sort = "id", direction = Sort.Direction.ASC, page = 0, size = 10) Pageable pagination) {

        final Page<Topic> topicPage;
        if (status == null) {
            topicPage = topicRepository.findAll(pagination);
        } else {
            topicPage = topicRepository.findByStatus(status, pagination);
        }
        return TopicDto.convert(topicPage);
    }

    @PostMapping
    @Transactional
    @CacheEvict(value = "topicsList", allEntries = true)
    @Operation(summary = "insert topic", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<TopicDto> register(@RequestBody @Valid TopicForm topicForm,
                                             UriComponentsBuilder uriBuilder) {
        Topic topic = topicForm.convert();
        topicRepository.save(topic);

        URI uri = uriBuilder.path("/topics/{id}").buildAndExpand(topic.getId()).toUri();
        return ResponseEntity.created(uri).body(new TopicDto(topic));
    }

    @GetMapping("/{id}")
    @Operation(summary = "details topic", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<TopicDtoDetails> detail(@PathVariable("id") Long id) {
        Optional<Topic> topic = topicRepository.findById(id);
        return topic.map(value -> ResponseEntity.ok(new TopicDtoDetails(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Transactional
    @CacheEvict(value = "topicsList", allEntries = true)
    @Operation(summary = "update topic", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<TopicDto> update(@PathVariable Long id,
                                           @RequestBody @Valid TopicFormUpdate topicForm) {
        Optional<Topic> topicOptional = topicRepository.findById(id);
        if (topicOptional.isPresent()) {
            Topic topic = topicForm.update(id, topicRepository);
            return ResponseEntity.ok(new TopicDto(topic));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    @CacheEvict(value = "topicsList", allEntries = true)
    @Operation(summary = "delete topic", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Topic> topicOptional = topicRepository.findById(id);
        if (topicOptional.isPresent()) {
            topicRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
