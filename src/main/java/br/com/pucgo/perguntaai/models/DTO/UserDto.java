package br.com.pucgo.perguntaai.models.DTO;


import br.com.pucgo.perguntaai.models.User;

public class UserDto {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String course;


    public UserDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.course = user.getCourse();
    }
}