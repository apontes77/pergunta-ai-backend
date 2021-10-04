package br.com.pucgo.perguntaai.controllers.v1;

import br.com.pucgo.perguntaai.models.DTO.UserDto;
import br.com.pucgo.perguntaai.models.User;
import br.com.pucgo.perguntaai.models.form.LoginForm;
import br.com.pucgo.perguntaai.models.form.UserForm;
import br.com.pucgo.perguntaai.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    public UserController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/registration")
    @Transactional
    @CacheEvict(value = "user", allEntries = true)
    public ResponseEntity<?> register(@RequestBody @Valid UserForm userForm,
                                      UriComponentsBuilder uriBuilder) {

        Optional<User> userFound = userService.findByEmail(userForm.getEmail());
        if (!userFound.isPresent()) {

            User user = User.builder()
                    .name(userFound.get().getName())
                    .password(passwordEncoder.encode(userFound.get().getPassword()))
                    .course(userFound.get().getCourse())
                    .email(userFound.get().getPassword())
                    .build();

            final User userInserted = userService.saveUser(user);

            URI uri = uriBuilder.path("/{id}").buildAndExpand(userInserted.getId()).toUri();
            return ResponseEntity.created(uri).body(new UserDto(userInserted));
        }
        return ResponseEntity.status(418).body("Já existe um usuário cadastrado com esse e-mail.");
    }

    @PutMapping("/redefine-password")
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


