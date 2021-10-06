package br.com.pucgo.perguntaai.controllers.v1;

import br.com.pucgo.perguntaai.models.DTO.UserDto;
import br.com.pucgo.perguntaai.models.User;
import br.com.pucgo.perguntaai.models.form.UserForm;
import br.com.pucgo.perguntaai.repositories.UserRepository;
import br.com.pucgo.perguntaai.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping
    @Transactional
    @CacheEvict(value = "user", allEntries = true)
    public ResponseEntity<?> register(@RequestBody @Valid UserForm userForm,
                                      UriComponentsBuilder uriBuilder) {

        if(!userForm.getEmail().contains("@pucgo.edu.br"))
        {
            return ResponseEntity.status(400).body("E-mail inválido! O email deve ser do domininio: @pucgo.edu.br");
        }
        Optional<User> userFound = userRepository.findByEmail(userForm.getEmail());
        if (!userFound.isPresent()) {
            User user = userForm.convert();
            final User userInserted = userService.saveUser(user);

            URI uri = uriBuilder.path("/{id}").buildAndExpand(userInserted.getId()).toUri();
            return ResponseEntity.created(uri).body(new UserDto(userInserted));
        }
        return ResponseEntity.status(418).body("Já existe um usuário cadastrado com esse e-mail.");
    }

    @DeleteMapping
    @Transactional
    @CacheEvict(value = "user", allEntries = true)
    public ResponseEntity<?> delete(@RequestBody User user) {
        userService.deleteUser(user);

        return ResponseEntity.ok().build();
    }
}


