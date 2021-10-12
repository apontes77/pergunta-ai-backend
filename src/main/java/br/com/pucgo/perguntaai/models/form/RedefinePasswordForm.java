package br.com.pucgo.perguntaai.models.form;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RedefinePasswordForm {
    @NotNull
    @NotEmpty
    @Length(min = 3)
    private String password;
}
