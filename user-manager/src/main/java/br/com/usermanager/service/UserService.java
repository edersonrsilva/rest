package br.com.usermanager.service;

import br.com.usermanager.domain.entity.User;
import br.com.usermanager.domain.request.UserRequest;
import br.com.usermanager.exception.UserNotFoundException;
import br.com.usermanager.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(final Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found, id=" + id));
    }

    @Transactional
    public User save(final User user) {
        return userRepository.save(user);
    }

    @Transactional
    public User update(final Long id, final UserRequest request) {
        final User user = findById(id);
        user.setUsername(request.username());
        return userRepository.save(user);
    }

    @Transactional
    public void delete(final Long id) {
        final User user = findById(id);
        userRepository.delete(user);
    }

}
