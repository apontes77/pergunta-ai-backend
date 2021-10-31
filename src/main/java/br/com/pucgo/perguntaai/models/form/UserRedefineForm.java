package br.com.pucgo.perguntaai.models.form;

import br.com.pucgo.perguntaai.models.User;
import br.com.pucgo.perguntaai.models.enums.AvatarOptions;
import br.com.pucgo.perguntaai.models.enums.RoleUser;
import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRedefineForm {
    @NotNull
    @NotEmpty
    @Length(min = 6)
    private String name;
    @NotNull
    @NotEmpty
    @Length(min = 3)
    private String password;
    @NotNull
    @NotEmpty
    @Length(min = 3)
    private String course;
    @NotNull
    @NotEmpty
    private RoleUser roleUser;
    @NotNull
    @NotEmpty
    private AvatarOptions avatarOptions;
    @NotNull
    @NotEmpty
    private LocalDate birthDate;

    public UserRedefineForm(User user) {
        this.name = user.getName();
        this.password = user.getPassword();
        this.course = user.getCourse();
        this.roleUser = user.getRoleUser();
        this.avatarOptions = user.getAvatarOptions();
        this.birthDate = user.getBirthDate();
    }
}
