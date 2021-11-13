package br.com.pucgo.perguntaai.controllers.v1;

import br.com.pucgo.perguntaai.config.security.TokenService;
import br.com.pucgo.perguntaai.models.DTO.TokenDto;
import br.com.pucgo.perguntaai.models.User;
import br.com.pucgo.perguntaai.models.form.LoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class LoginController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private TokenService tokenService;

    /**
     * é submetido email e senha do usuário a fim de gerar autenticação
     * @param form, do tipo LoginForm
     * @return token e identificador do usuário
     */
    @PostMapping
    public ResponseEntity<?> authenticate(@RequestBody @Valid LoginForm form) {
        UsernamePasswordAuthenticationToken loginData = form.convert();
        try {
            final Authentication authentication = authManager.authenticate(loginData);
            final String token = tokenService.generateToken(authentication);
            User user = (User) authentication.getPrincipal();
            Long id = user.getId();
            return ResponseEntity.ok(new TokenDto(id, token, "Bearer"));
        } catch (AuthenticationException e) {
            if(e.getMessage() == "Bad credentials") {
                return ResponseEntity.status(400).body("E-mail ou senha incorretos.");
            }
            return ResponseEntity.badRequest().build();
        }
    }
}
