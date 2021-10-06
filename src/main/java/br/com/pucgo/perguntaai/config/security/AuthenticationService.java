package br.com.pucgo.perguntaai.config.security;

import br.com.pucgo.perguntaai.models.User;
import br.com.pucgo.perguntaai.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String emailAuthentication) throws UsernameNotFoundException {
        final Optional<User> user = userRepository.findByEmail(emailAuthentication);
        if (user.isPresent()) {
            return User.builder()
                    .id(user.get().getId())
                    .name(user.get().getName())
                    .email(user.get().getEmail())
                    .password(user.get().getPassword())
                    .course(user.get().getCourse())
                    .build();
        }

        throw new UsernameNotFoundException("Dados inválidos!");
    }
}
