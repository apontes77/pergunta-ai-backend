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

    private String name;

    private String course;

    private AvatarOptions avatarOptions;

    private LocalDate birthDate;

    public UserRedefineForm(User user) {
        this.name = user.getName();
        this.course = user.getCourse();
        this.avatarOptions = user.getAvatarOptions();
        this.birthDate = user.getBirthDate();
    }
}
