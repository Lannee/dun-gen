package backend.model.validators;

import backend.DTO.TokenizedDTO;
import backend.security.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TokenValidator extends Validator {
    private final JwtUtils jwtUtils;

    public TokenValidator validateToken(TokenizedDTO req) {
        if (req.getToken() == null) {
            this.addViolation("token", "Token was not passed.");
            return this;
        }
        return validateToken(req.getToken().getToken());
    }

    public TokenValidator validateToken(String token) {
        try {
            jwtUtils.validateAccessToken(token);
        } catch (ExpiredJwtException expEx) {
            this.addViolation("token", "Token has expired");
        } catch (UnsupportedJwtException unsEx) {
            this.addViolation("token", "Unsupported JWT");
        } catch (MalformedJwtException mjEx) {
            this.addViolation("token", "Malformed JWT");
        } catch (Exception e) {
            this.addViolation("token", "Invalid Token");
        }
        return this;
    }
}
