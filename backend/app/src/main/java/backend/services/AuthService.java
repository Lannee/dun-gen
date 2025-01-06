package backend.services;

import org.springframework.stereotype.Service;

import backend.DTO.TokenDTO;
import backend.DTO.UsersDTO;
import backend.exceptions.ApiException;
import backend.exceptions.DoesNotExistException;
import backend.exceptions.WrongPasswordException;
import backend.model.User;
import backend.repository.UserRepository;
import backend.security.JwtUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository usersRepository;
    private final JwtUtils jwtUtils;

    public TokenDTO login(UsersDTO req) throws DoesNotExistException, ApiException, WrongPasswordException {
        User userEntity = usersRepository.findByName(req.getName())
                .orElseThrow(() -> new DoesNotExistException(req.getName()));

        if (!UserService.encryptViaSHA384(req.getPassword()).equals(userEntity.getPassword()))
            throw new WrongPasswordException(req.getName());

        // CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);
        TokenDTO token = new TokenDTO(jwtUtils.generateAccessToken(userEntity));
        return token;
    }
}
