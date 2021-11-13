package br.com.pucgo.perguntaai.controllers.v1;

import br.com.pucgo.perguntaai.config.security.TokenService;
import br.com.pucgo.perguntaai.models.DTO.TokenDto;
import br.com.pucgo.perguntaai.models.User;
import br.com.pucgo.perguntaai.models.form.LoginForm;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class LoginController {

    private final AuthenticationManager authManager;
    private final TokenService tokenService;

    @PostMapping
    @Operation(description = "gera um token de autenticação para o usuário no ato do login.")
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
