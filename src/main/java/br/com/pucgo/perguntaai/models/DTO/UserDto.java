package br.com.pucgo.perguntaai.models.DTO;


import br.com.pucgo.perguntaai.models.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserDto {
    private final Long id;
    private final String name;
    private final String email;
    private final String password;
    private final String course;
    private final LocalDateTime creationDate;


    public UserDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.course = user.getCourse();
        this.creationDate = user.getCreationDate();
    }
}