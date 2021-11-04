package br.com.pucgo.perguntaai.models.form;

import br.com.pucgo.perguntaai.models.User;
import br.com.pucgo.perguntaai.models.enums.AvatarOptions;
import br.com.pucgo.perguntaai.models.enums.RoleUser;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRedefineForm {
    @NotNull
    private String name;
    @NotNull
    private String password;
    @NotNull
    private String course;
    @NotNull
    private RoleUser roleUser;
    @NotNull
    private AvatarOptions avatarOptions;
    @NotNull
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
