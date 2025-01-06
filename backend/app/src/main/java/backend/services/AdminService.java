package backend.services;

import org.springframework.stereotype.Service;

import backend.DTO.IdDTO;
import backend.DTO.TokenDTO;
import backend.DTO.UsersCreatedDTO;
import backend.exceptions.DoesNotExistException;
import backend.exceptions.ForbiddenException;
import backend.model.User;
import backend.model.UsersAdminStatus;
import backend.repository.UserRepository;
import backend.security.JwtUtils;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.LinkedList;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository usersRepository;
    private final JwtUtils jwtUtils;

    private List<User> findAllAdmins() {
        List<User> usersList = usersRepository.findAll(); 
        List<User> adminList = new LinkedList<User>();
        for (User user : usersList) {
            if (user.isAdmin()) {
                adminList.add(user);
            }
        }

        return adminList;
    }

    public boolean isAdmin(long userId) throws DoesNotExistException {
        final User user = usersRepository.getReferenceById(userId);

        return user.isAdmin();
    }

    public boolean isAdmin(TokenDTO req) throws DoesNotExistException {
        final long userId = jwtUtils.getIdFromToken(req.getToken());
        
        return isAdmin(userId);
    }

    public UsersCreatedDTO addAdmin(TokenDTO req) throws DoesNotExistException {
        final long userId = jwtUtils.getIdFromToken(req.getToken());
        final User user = usersRepository.getReferenceById(userId);

        if (user.isAdmin()) {
            return new UsersCreatedDTO("User '" + user.getName() + "' is already admin", user.getId(), user.getName());
        }

        UsersCreatedDTO userChanged;
        final List<User> adminList = this.findAllAdmins();

        if (adminList.size() != 0) {
            user.setAdminStatus(UsersAdminStatus.PENDING);
            userChanged = new UsersCreatedDTO("Request was sent to admin to approve", user.getId(), user.getName());
        } else {
            user.setAdminStatus(UsersAdminStatus.ADMIN);
            userChanged = new UsersCreatedDTO("User '" + user.getName() + "' is admin now", user.getId(), user.getName());
        }
        usersRepository.save(user);

        return userChanged;
    }

    public List<User> getAllRequests(TokenDTO req) throws ForbiddenException {
        final long userId = jwtUtils.getIdFromToken(req.getToken());
        final User user = usersRepository.getReferenceById(userId);

        if (!user.isAdmin()) {
            throw new ForbiddenException("Only admins can get admin priviliges requests");
        }

        return usersRepository.findByAdminStatus(UsersAdminStatus.PENDING);
    }

    public UsersCreatedDTO approveAdmin(IdDTO req) throws ForbiddenException {
        final long adminId = jwtUtils.getIdFromToken(req.getToken().getToken());
        final User admin = usersRepository.getReferenceById(adminId);

        if (!admin.isAdmin()) {
            throw new ForbiddenException("Only admins can approve admin priviliges requests");
        }
        
        final long userId = req.getId();
        final User user = usersRepository.getReferenceById(userId);
        user.setAdminStatus(UsersAdminStatus.ADMIN);
        usersRepository.save(user);

        return new UsersCreatedDTO("Successfully approved", user.getId(), user.getName());
    }

    public UsersCreatedDTO rejectUser(IdDTO req) throws ForbiddenException {
        final long adminId = jwtUtils.getIdFromToken(req.getToken().getToken());
        final User admin = usersRepository.getReferenceById(adminId);

        if (!admin.isAdmin()) {
            throw new ForbiddenException("Only admins can approve admin priviliges requests");
        }
        
        final long userId = req.getId();
        final User user = usersRepository.getReferenceById(userId);
        user.setAdminStatus(UsersAdminStatus.USER);
        usersRepository.save(user);

        return new UsersCreatedDTO("Successfully rejected", user.getId(), user.getName());
    }
}
