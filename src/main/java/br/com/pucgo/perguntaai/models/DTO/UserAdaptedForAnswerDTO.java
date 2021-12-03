package br.com.pucgo.perguntaai.models.DTO;

import br.com.pucgo.perguntaai.models.enums.AvatarOptions;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="user_adapted")
public class UserAdaptedForAnswerDTO {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String authorName;
    @JsonProperty("avatar")
    private AvatarOptions avatarOptions;
}
