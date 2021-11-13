package br.com.pucgo.perguntaai.config.validation;

import br.com.pucgo.perguntaai.models.User;
import br.com.pucgo.perguntaai.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Configuration
public class UserRecordFieldValidationHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@pucgo\\.edu\\.br$", Pattern.CASE_INSENSITIVE);

    public static final Pattern VALID_PASSWORD_REGEX =
            Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,15}$", Pattern.CASE_INSENSITIVE);

    public static final Pattern VALID_NAME_REGEX =
            Pattern.compile("^[a-zA-Z0-9_ ]{5,255}$", Pattern.CASE_INSENSITIVE);

    public Optional<User> validationOfFieldsInSavingUser(User user) {
        Matcher matcherMail = VALID_EMAIL_ADDRESS_REGEX.matcher(user.getEmail());
        Matcher matcherPassword = VALID_PASSWORD_REGEX.matcher(user.getPassword());
        Matcher matcherName = VALID_NAME_REGEX.matcher(user.getName());

        if(!matcherMail.find()) {
            LOGGER.info("Invalid Email: {}", user.getEmail());
            return Optional.empty();
        }

        if(!matcherPassword.find()) {
            LOGGER.info("Invalid Password: {}", user.getPassword());
            return Optional.empty();
        }

        if(!matcherName.find()) {
            LOGGER.info("Invalid Name: {}", user.getName());
            return Optional.empty();
        }

        LOGGER.info("Dados inseridos são válidos: email={}, senha={}, nome={}", user.getEmail(), user.getPassword(), user.getName());
        return Optional.of(user);
    }
}
