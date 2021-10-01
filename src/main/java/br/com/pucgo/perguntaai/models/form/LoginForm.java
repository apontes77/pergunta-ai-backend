package br.com.pucgo.perguntaai.models.form;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Builder
public class LoginForm {
    @NotNull
    @NotEmpty
    @Length(min = 10)
    private String email;

    @NotNull
    @NotEmpty
    @Length(min = 3)
    private String password;

    public UsernamePasswordAuthenticationToken convert() {
        return new UsernamePasswordAuthenticationToken(email, password);
    }
}
