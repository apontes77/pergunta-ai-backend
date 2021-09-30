package br.com.pucgo.perguntaai.models.DTO;


import br.com.pucgo.perguntaai.models.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String course;
    private LocalDateTime creationDate;


    public UserDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.course = user.getCourse();
        this.creationDate = user.getCreationDate();
    }
}