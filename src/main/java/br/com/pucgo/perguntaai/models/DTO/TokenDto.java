package br.com.pucgo.perguntaai.models.DTO;

import lombok.Getter;

@Getter
public class TokenDto {
    private Long id;
    private String token;
    private String type;
    public TokenDto(Long id, String token, String type) {
        this.id = id;
        this.token = token;
        this.type = type;
    }
}
