package br.com.pucgo.perguntaai.controllers.v1;

import br.com.pucgo.perguntaai.exceptions.NotFoundUserException;
import br.com.pucgo.perguntaai.models.DTO.UserDto;
import br.com.pucgo.perguntaai.models.User;
import br.com.pucgo.perguntaai.models.form.RedefinePasswordForm;
import br.com.pucgo.perguntaai.models.form.UserForm;
import br.com.pucgo.perguntaai.models.form.UserRedefineForm;
import br.com.pucgo.perguntaai.services.MailService;
import br.com.pucgo.perguntaai.services.PasswordResetService;
import br.com.pucgo.perguntaai.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MailService notificationService;

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

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
    @CacheEvict(value = "user", allEntries = true)
    public ResponseEntity<?> register(@RequestBody @Valid UserForm userForm) {

        if(!userForm.getEmail().contains("@pucgo.edu.br")){
            return ResponseEntity.status(400).body("E-mail inválido! O email deve ser do domínio: @pucgo.edu.br");
        }

        if(!userService.findUserByEmail(userForm.getEmail())) {
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


    @DeleteMapping
    @Transactional
    @CacheEvict(value = "user", allEntries = true)
    public ResponseEntity<?> delete(@RequestBody User user) {
        userService.deleteUser(user);

        return ResponseEntity.ok().build();
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

    @PutMapping("/send-reset-token")
    public ResponseEntity<?> resetPassword(HttpServletRequest request, @RequestParam("email") String userEmail) {
        try{
            User user = userService.getUserByEmail(userEmail);
            String token = UUID.randomUUID().toString();
            PasswordResetService passwordService = new PasswordResetService();
            mailSender.send(passwordService.constructResetTokenEmail(request.toString(),
                    request.getLocale(), token, user));
            return ResponseEntity.status(200).body("E-mail enviado para: " + userEmail);

        }catch(NotFoundUserException e){
            return ResponseEntity.status(400).body("Usuário não encontrado! E-mail: " + userEmail + ", Tipo: " + User.class.getName());
        }
        catch (MailException em)
        {
            return ResponseEntity.status(400).body(em);
        }
    }

    @GetMapping("/send-mail")
    public ResponseEntity<Void> sendMessageToRedefinePassword(@RequestParam String fromEmail,
                                                              @RequestParam String toEmail,
                                                              @RequestParam String subject,
                                                              @RequestParam String body) {

        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(fromEmail);
            simpleMailMessage.setTo(toEmail);
            simpleMailMessage.setSubject(subject);
            simpleMailMessage.setText(body);

            return ResponseEntity.ok().build();
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
}

