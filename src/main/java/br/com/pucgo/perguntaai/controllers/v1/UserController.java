package br.com.pucgo.perguntaai.controllers.v1;

import br.com.pucgo.perguntaai.config.security.TokenService;
import br.com.pucgo.perguntaai.models.DTO.TokenDto;
import br.com.pucgo.perguntaai.models.DTO.UserDto;
import br.com.pucgo.perguntaai.models.User;
import br.com.pucgo.perguntaai.models.form.LoginForm;
import br.com.pucgo.perguntaai.models.form.UserForm;
import br.com.pucgo.perguntaai.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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
    private UserRepository userRepository;

    @PostMapping
    @Transactional
    @CacheEvict(value = "user", allEntries = true)
    public ResponseEntity<UserDto> register(@RequestBody @Valid UserForm userForm,
                                            UriComponentsBuilder uriBuilder) {
        User user = userForm.convert();
        userRepository.save(user);

        URI uri = uriBuilder.path("/register/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(uri).body(new UserDto(user));
    }
}

