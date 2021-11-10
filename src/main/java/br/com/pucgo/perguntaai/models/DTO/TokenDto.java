package br.com.pucgo.perguntaai.models.DTO;

import lombok.Getter;

@Getter
public class TokenDto {
    private final Long id;
    private final String token;
    private final String type;
    public TokenDto(Long id, String token, String type) {
        this.id = id;
        this.token = token;
        this.type = type;
    }
}
