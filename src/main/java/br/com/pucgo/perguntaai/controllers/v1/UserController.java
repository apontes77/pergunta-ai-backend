package br.com.pucgo.perguntaai.controllers.v1;

import br.com.pucgo.perguntaai.models.DTO.UserDto;
import br.com.pucgo.perguntaai.models.User;
import br.com.pucgo.perguntaai.models.form.UserForm;
import br.com.pucgo.perguntaai.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    @Transactional
    @CacheEvict(value = "user", allEntries = true)
    public ResponseEntity<?> register(@RequestBody @Valid UserForm userForm,
                                            UriComponentsBuilder uriBuilder) {

        final User userFound = userService.findUserByEmail(userForm.getEmail());
        if (userFound!=null) {
            User user = userForm.convert();
            final User userInserted = userService.saveUser(user);

            URI uri = uriBuilder.path("/{id}").buildAndExpand(userInserted.getId()).toUri();
            return ResponseEntity.created(uri).body(new UserDto(userInserted));
        }
        return ResponseEntity.status(418).body("Já existe um usuário cadastrado com esse e-mail.");
    }
}
