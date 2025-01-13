package backend.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtResponse {
    private final String headerType = "Bearer";
    private String Name;
    private String accessToken;
    private String requestDate;
    private String expirationDate;
}
