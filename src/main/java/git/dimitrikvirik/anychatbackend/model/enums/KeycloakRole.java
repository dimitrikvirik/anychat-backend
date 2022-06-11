package git.dimitrikvirik.anychatbackend.model.enums;

import lombok.Getter;

@Getter
public enum KeycloakRole {
    ADMIN(1),
    USER(2);

    private final int priority;
    @Override
    public String toString() {
        return this.name();
    }

    KeycloakRole(int priority) {
        this.priority = priority;
    }


    public static KeycloakRole fromText(String text){
        try{
          return KeycloakRole.valueOf(text.toUpperCase());
        }catch (IllegalArgumentException e){
           return  null;
        }
    }

}
