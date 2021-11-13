package br.com.pucgo.perguntaai.controllers.v1;

import br.com.pucgo.perguntaai.exceptions.NotFoundUserException;
import br.com.pucgo.perguntaai.exceptions.UserRegistrationException;
import br.com.pucgo.perguntaai.models.DTO.UserDto;
import br.com.pucgo.perguntaai.models.User;
import br.com.pucgo.perguntaai.models.form.RedefinePasswordForm;
import br.com.pucgo.perguntaai.models.form.UserForm;
import br.com.pucgo.perguntaai.models.form.UserRedefineForm;
import br.com.pucgo.perguntaai.services.MailService;
import br.com.pucgo.perguntaai.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
public class UserController {

    private final MailService notificationService;

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.status(200).body(userService.findAll());
    }

    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> find(@PathVariable Long id) {
        try{
            User obj = userService.findById(id);
            return ResponseEntity.ok().body(obj);
        }catch (NotFoundUserException e){
            return ResponseEntity.status(400).body("Objeto não encontrado! Id: " + id + ", Tipo: " + User.class.getName());
        }
    }

    @PostMapping("/registration")
    @Transactional
    public ResponseEntity<?> register(@RequestBody @Valid UserForm userForm) {

        if(!userService.findUserByEmail(userForm.getEmail())) {
            User user = userForm.convert();
            try {
                Optional<User> registerValidation = userService.userRegisterValidation(user);
                if (registerValidation.isPresent()) {
                    URI uri = ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/{id}")
                            .buildAndExpand(registerValidation.get().getId())
                            .toUri();
                    return ResponseEntity.created(uri).body(new UserDto(registerValidation.get()));
                }
            } catch(Exception e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
        return ResponseEntity.badRequest().body("Já existe usuário com este email registrado!");
    }

    @DeleteMapping
    @Transactional
    @CacheEvict(value = "user", allEntries = true)
    public ResponseEntity<?> delete(@RequestBody User user) {
        userService.deleteUser(user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{id}")
    @Transactional
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
        try {
            userService.deleteUserById(id);
            return new ResponseEntity<String>("User excluido", HttpStatus.OK);
        } catch (Exception e) {
            throw new NotFoundUserException("Não foi possível excluir este usuário, lançando esta exceção: "+e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Transactional
    @CacheEvict(value = "user", allEntries = true)
    public ResponseEntity<?> update(@RequestBody UserRedefineForm userRedefineForm, @PathVariable Long id){
        try{
            User obj = userService.fromUserRedefineForm(userRedefineForm);
            obj.setId(id);
            obj = userService.update(obj);

            URI uri = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(obj)
                    .toUri();
            return ResponseEntity.created(uri).body(new UserRedefineForm(obj));
        }catch (NotFoundUserException e){
            return ResponseEntity.status(400).body("Objeto não encontrado! Id: " + id + ", Tipo: " + User.class.getName());
        }
    }

    @PutMapping("/password/{id}")
    @Transactional
    @CacheEvict(value = "user", allEntries = true)
    public ResponseEntity<?> updatePassword(@RequestBody RedefinePasswordForm redefinePasswordForm, @PathVariable Long id){
        try{
            User obj = User.builder()
                    .id(id)
                    .password(redefinePasswordForm.getPassword())
                    .build();
            obj = userService.updatePassword(obj);

            URI uri = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/password/{id}")
                    .buildAndExpand(obj)
                    .toUri();
            return ResponseEntity.created(uri).body(new UserRedefineForm(obj));
        }catch (NotFoundUserException e){
            return ResponseEntity.status(400).body("Objeto não encontrado! Id: " + id + ", Tipo: " + User.class.getName());
        }
    }

    @GetMapping(value = "/send-mail")
    public ResponseEntity<?> sendMessageToRedefinePassword(@RequestParam final String toEmail) {
        try {
            User user = userService.getUserByEmail(toEmail);
            if(user!=null) {
                notificationService.sendMail(user);
                return ResponseEntity.status(200).body("E-mail enviado para: " + user.getEmail());
            }
            else {
                return ResponseEntity.status(404).body("Usuário não encontrado! E-mail: " + toEmail.toString() + ", Tipo: " + User.class.getName());
            }
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}

