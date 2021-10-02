package br.com.pucgo.perguntaai.controllers.v1;

import br.com.pucgo.perguntaai.exceptions.NotFoundUserException;
import br.com.pucgo.perguntaai.models.DTO.TokenDto;
import br.com.pucgo.perguntaai.models.DTO.TopicDto;
import br.com.pucgo.perguntaai.models.DTO.TopicFormUpdate;
import br.com.pucgo.perguntaai.models.DTO.UserDto;
import br.com.pucgo.perguntaai.models.Topic;
import br.com.pucgo.perguntaai.models.User;
import br.com.pucgo.perguntaai.models.form.LoginForm;
import br.com.pucgo.perguntaai.models.form.UserForm;
import br.com.pucgo.perguntaai.repositories.UserRepository;
import br.com.pucgo.perguntaai.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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
        Optional<User> userFound = userService.findByEmail(userForm.getEmail());
        if (!userFound.isPresent()) {
            User user = userForm.convert();
            final User userInserted = userService.saveUser(user);

            URI uri = uriBuilder.path("/{id}").buildAndExpand(userInserted.getId()).toUri();
            return ResponseEntity.created(uri).body(new UserDto(userInserted));
        }
        return ResponseEntity.status(418).body("Já existe um usuário cadastrado com esse e-mail.");
    }

    @PutMapping("/redefinePassword")
    @Transactional
    public ResponseEntity<?> redefinePassword(@RequestBody @Valid LoginForm loginForm,
                                                    UriComponentsBuilder uriBuilder) {

        Optional<User> userFound = userService.findByEmail(loginForm.getEmail());

        if (userFound.isPresent()) {
            User userRedefine = userService.updatePasswordUser(userFound.get(), loginForm.getPassword());

            URI uri = uriBuilder.path("/{id}").buildAndExpand(userRedefine.getId()).toUri();
            return ResponseEntity.created(uri).body(new UserDto(userRedefine));
        }
          return ResponseEntity.notFound().build();
    }
}


