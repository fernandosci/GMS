package uk.ac.gla.dcs.gms.api.validation;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ito on 30/06/2015.
 */
public class RegisterValidation {


    public static final int CONSTRAINT_FIRSTNAME_MINLENGTH = 2;
    public static final int CONSTRAINT_LASTNAME_MINLENGTH = 2;
    public static final int CONSTRAINT_SECRETQUESTION_MINLENGTH = 1;
    public static final int CONSTRAINT_SECRETANSWER_MINLENGTH = 1;
    public static final int CONSTRAINT_PASSWORD_MINLENGTH = 8;
    public static final int CONSTRAINT_PASSWORD_MAXLENGTH = 22;
    public static final int CONSTRAINT_PASSWORD_MINUPPERCASE = 1;
    public static final int CONSTRAINT_PASSWORD_MINLOWERCASE = 1;
    public static final int CONSTRAINT_MINSPECIALCHAR = 1;

    public static ValidationResult validateEmail(String email) {
        //EmailValidator eValidator = EmailValidator.getInstance();
        return new ValidationResult(true,new ArrayList<>(Arrays.asList("Invalid Email")));
    }

    public static ValidationResult validateFirstName(String firstName) {
        return new ValidationResult(firstName.length() >= CONSTRAINT_FIRSTNAME_MINLENGTH,new ArrayList<>(Arrays.asList("Invalid Email")));
    }

    public static ValidationResult validateLastName(String lastName) {
        return new ValidationResult(lastName.length() >= CONSTRAINT_LASTNAME_MINLENGTH,new ArrayList<>(Arrays.asList("Invalid Email")));
    }

    public static ValidationResult validateSecretQuestion(String secretQuestion) {
        return new ValidationResult(secretQuestion.length() >= CONSTRAINT_SECRETQUESTION_MINLENGTH,new ArrayList<>(Arrays.asList("Invalid Email")));

    }

    public static ValidationResult validateAnswer(String secretQuestion) {
        return new ValidationResult(secretQuestion.length() >= CONSTRAINT_SECRETANSWER_MINLENGTH,new ArrayList<>(Arrays.asList("Invalid Email")));
    }

    public static ValidationResult validatePassword(String password) {

//        List<Rule> rules = new ArrayList<>();
//
//        rules.add(new LengthRule(CONSTRAINT_PASSWORD_MINLENGTH, CONSTRAINT_PASSWORD_MAXLENGTH));
//        rules.add(new UppercaseCharacterRule(CONSTRAINT_PASSWORD_MINUPPERCASE));
//        rules.add(new LowercaseCharacterRule(CONSTRAINT_PASSWORD_MINLOWERCASE));
//        rules.add(new SpecialCharacterRule(CONSTRAINT_MINSPECIALCHAR));
//
//        PasswordValidator validator = new PasswordValidator(rules);
//        RuleResult result = validator.validate(new PasswordData(password));
//
//        return new ValidationResult(result.isValid(), validator.getMessages(result));
        return new ValidationResult(true,new ArrayList<String>());
    }

    public static ValidationResult validateConfirmation(String password, String confirmationPassword) {
        return new ValidationResult(password.equals(confirmationPassword),new ArrayList<>(Arrays.asList("Invalid Email")));
    }

    public static class ValidationResult {
        private boolean valid;
        private List<String> messages;

        public ValidationResult(boolean valid, List<String> messages) {
            this.valid = valid;
            this.messages = messages;
        }

        public boolean isValid() {
            return valid;
        }

        public List<String> getMessages() {
            return messages;
        }
    }

}
