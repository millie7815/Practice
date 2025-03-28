package ru.flamexander.spring.security.jwt.service;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.flamexander.spring.security.jwt.dtos.RegistrationUserDto;
import ru.flamexander.spring.security.jwt.entities.Role;
import ru.flamexander.spring.security.jwt.entities.User;
import ru.flamexander.spring.security.jwt.repositories.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    public boolean existsByUsername(String username) {
        return findByUsername(username).isPresent();
    }

    @Autowired
    public UserService(UserRepository userRepository, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
                String.format("Пользователь '%s' не найден", username)
        ));

        Role role = user.getRole(); // Получаем роль напрямую через связь
        if (role == null) {
            throw new RuntimeException("Роль не найдена");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(role.getName()))
        );
    }

    public User createNewUser(RegistrationUserDto registrationUserDto) {
        User user = new User();
        user.setUsername(registrationUserDto.getUsername());
        user.setEmail(registrationUserDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationUserDto.getPassword()));

        // Установка роли USER по умолчанию
        Role userRole = roleService.getUserRole();
        user.setRole(userRole); // Используем setRole вместо setRoleId

        return userRepository.save(user);
    }

    public boolean deleteById(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true; // Удаление прошло успешно
        }
        return false; // Пользователь не найден
    }

    public User updateUser(Long id, User userDetails) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User userToUpdate = optionalUser.get();
            userToUpdate.setUsername(userDetails.getUsername());
            userToUpdate.setEmail(userDetails.getEmail());
            // Обновите другие поля по мере необходимости
            return userRepository.save(userToUpdate);
        }
        return null; // Или выбросьте исключение, если пользователь не найден
    }
}

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        // Пример загрузки пользователя
//        if (username.equals("user")) {
//            return new org.springframework.security.core.userdetails.User("user", "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
//        } else if (username.equals("admin")) {
//            return new org.springframework.security.core.userdetails.User("admin", "password", List.of(new SimpleGrantedAuthority("ROLE_USER"), new SimpleGrantedAuthority("ROLE_ADMIN")));
//        } else {
//            throw new UsernameNotFoundException("Пользователь не найден");
//        }
//    }


















////package ru.flamexander.spring.security.jwt.service;
////
//import lombok.RequiredArgsConstructor;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import ru.flamexander.spring.security.jwt.dtos.RegistrationUserDto;
//import ru.flamexander.spring.security.jwt.entities.User;
//import ru.flamexander.spring.security.jwt.repositories.UserRepository;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//public class UserService implements UserDetailsService {
//    private UserRepository userRepository;
//    private RoleService roleService;
//    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    public void setUserRepository(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    @Autowired
//    public void setRoleService(RoleService roleService) {
//        this.roleService = roleService;
//    }
//
//    @Autowired
//    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    public Optional<User> findByUsername(String username) {
//        return userRepository.findByUsername(username);
//    }
//    public Optional<User> findById(Long id) {
//        return userRepository.findById(id);
//    }
//
//    @Override
//    @Transactional
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
//                String.format("Пользователь '%s' не найден", username)
//        ));
//        return new org.springframework.security.core.userdetails.User(
//                user.getUsername(),
//                user.getPassword(),
//                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList())
//        );
//    }
//
//    public User createNewUser(RegistrationUserDto registrationUserDto) {
//        User user = new User();
//        user.setUsername(registrationUserDto.getUsername());
//        user.setEmail(registrationUserDto.getEmail());
//        user.setPassword(passwordEncoder.encode(registrationUserDto.getPassword()));
//        user.setRoles(List.of(roleService.getUserRole()));
//        return userRepository.save(user);
//    }
//    public boolean deleteById(Long id) {
//        if (userRepository.existsById(id)) {
//            userRepository.deleteById(id);
//            return true; // Удаление прошло успешно
//        }
//        return false; // Пользователь не найден
//    }
//    public User updateUser(Long id, User userDetails) {
//        Optional<User> optionalUser = userRepository.findById(id);
//        if (optionalUser.isPresent()) {
//            User userToUpdate = optionalUser.get();
//            userToUpdate.setUsername(userDetails.getUsername());
//            userToUpdate.setEmail(userDetails.getEmail());
//            // Обновите другие поля по мере необходимости
//            return userRepository.save(userToUpdate);
//        }
//        return null; // Или выбросьте исключение, если пользователь не найден
//    }
//}
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import ru.flamexander.spring.security.jwt.entities.User;
//import ru.flamexander.spring.security.jwt.repositories.UserRepository;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class UserService {
//    private final UserRepository userRepository;
//
//    @Autowired
//    public UserService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    public List<User> getAllUsers() {
//        return userRepository.findAll();
//    }
//
//    public Optional<User> getUserById(Long id) {
//        return userRepository.findById(id);
//    }
//
//    public User createUser(User user) {
//        return userRepository.save(user);
//    }
//
//    public User updateUser(User user) {
//        return userRepository.save(user);
//    }
//
//    public void deleteUser(Long id) {
//        userRepository.deleteById(id);
//    }
//}