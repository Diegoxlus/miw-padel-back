package miw_padel_back.configuration;

import miw_padel_back.infraestructure.mongodb.entities.UserEntity;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator
        implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }
    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context){
        UserEntity user = (UserEntity) obj;
        return user.getPassword().equals(user.getMatchingPassword());
    }
}
