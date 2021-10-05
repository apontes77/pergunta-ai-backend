package br.com.pucgo.perguntaai.controllers.v1;

import br.com.pucgo.perguntaai.models.DTO.UserDto;
import br.com.pucgo.perguntaai.models.User;
import br.com.pucgo.perguntaai.models.form.UserForm;
import br.com.pucgo.perguntaai.services.UserService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    public ResponseEntity<User> find(@PathVariable Long id) {
        User obj = userService.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping("/registration")
    @Transactional
    @CacheEvict(value = "user", allEntries = true)
    public ResponseEntity<?> register(@RequestBody @Valid UserForm userForm) {

        if(!userForm.getEmail().contains("@pucgo.edu.br")){
            return ResponseEntity.status(400).body("E-mail inválido! O email deve ser do domínio: @pucgo.edu.br");
        }

        if(userService.findUserByEmail(userForm.getEmail())) {
            User user = userForm.convert();
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            final User userInserted = userService.saveUser(user);

            URI uri = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(userInserted.getId())
                    .toUri();
            return ResponseEntity.created(uri).body(new UserDto(userInserted));
        }

        return ResponseEntity.badRequest().body("Não foi possível realizar seu cadastro pois o email inserido já existe em nossa base!");
    }

    @RequestMapping(value="/{id}", method=RequestMethod.PUT)
    public ResponseEntity<Void> update(@Valid @RequestBody UserDto objDTO, @PathVariable Long id){
        User obj = userService.fromDTO(objDTO);
        obj.setId(id);
        obj = userService.update(obj);
        return ResponseEntity.noContent().build();
    }

}


