package ru.flamexander.spring.security.jwt.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.flamexander.spring.security.jwt.dtos.JwtRequest;
import ru.flamexander.spring.security.jwt.dtos.JwtResponse;
import ru.flamexander.spring.security.jwt.dtos.RegistrationUserDto;
import ru.flamexander.spring.security.jwt.dtos.UserDto;
import ru.flamexander.spring.security.jwt.service.RegAuto.AuthService;
import org.springframework.validation.FieldError;
import ru.flamexander.spring.security.jwt.utils.JwtTokenUtils;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;



@RestController //аннотация указывает, что данный класс является контроллером REST, который отвечает за обработку HTTP-запросов и
// возвращение ответов в формате JSON.
@RequiredArgsConstructor // Эта аннотация от Lombok упрощает создание конструктора, который инициализирует все поля класса, помеченные как `final`.
// В данном случае, она сгенерирует конструктор с параметром `AuthService authService`, который будет автоматически внедрён Spring'ом (dependency injection).
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AuthController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtils jwtTokenUtils;


    @PostMapping("/reg")
    public ResponseEntity<?> createNewUser(@Valid @RequestBody RegistrationUserDto registrationUserDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }

        return authService.createNewUser(registrationUserDto);
    }


    @PostMapping("/check")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenUtils.generateToken((UserDetails) authentication.getPrincipal());

        Cookie cookie = new Cookie("JWT", jwt);
        cookie.setHttpOnly(true); // Защита от XSS атак
        cookie.setSecure(true); // Использовать только через HTTPS (если у вас настроен HTTPS)
        cookie.setPath("/"); // Доступно для всех путей
        cookie.setMaxAge(3600); // Время жизни 1 час

        response.addCookie(cookie); // Добавляем cookie в ответ

//        return ResponseEntity.ok(jwt);

        // Возвращаем токен в JSON-формате
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(new JwtResponse(jwt));
    }


    @PostMapping("/login")

    @PostMapping("/logout")



// токен в куки
//    @PostMapping("/auth")
//    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest, HttpServletResponse response) {
//        ResponseEntity<?> responseEntity = authService.createAuthToken(authRequest); // Получаем ResponseEntity от сервиса, обработка запроса
//
//        if (responseEntity.getStatusCode().is2xxSuccessful()) {
//            // Если токен успешно создан, добавляем его в cookie
//            String token = ((JwtResponse) responseEntity.getBody()).getToken(); // Извлекаем токен из ответа
//            Cookie cookie = new Cookie("JWT", token);
//            cookie.setHttpOnly(true); // Защита от XSS атак
//            cookie.setSecure(true); // Использовать только через HTTPS (если у вас настроен HTTPS)
//            cookie.setPath("/"); // Доступно для всех путей
//            cookie.setMaxAge(3600); // Время жизни 1 час
//
//            response.addCookie(cookie); // Добавляем cookie в ответ
//        }
//
//        return responseEntity; // Возвращаем ответ от сервиса
//    }



// Было изначально
//    @PostMapping("/auth") //Эта аннотация указывает, что метод `createAuthToken` будет обрабатывать POST-запросы, адресованные к `/auth`
//    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest, HttpServletResponse response) {
//        String token = authService.createAuthToken(authRequest); // Получаем токен от сервиса
//
//        // Создаем cookie для хранения JWT токена
//        Cookie cookie = new Cookie("JWT", token);
//        cookie.setHttpOnly(true); // Защита от XSS атак
//        cookie.setSecure(true); // Использовать только через HTTPS (если у вас настроен HTTPS)
//        cookie.setPath("/"); // Доступно для всех путей
//        cookie.setMaxAge(3600); // Время жизни 1 час
//
//        response.addCookie(cookie); // Добавляем cookie в ответ
//
//        return ResponseEntity.ok("Токен установлен в cookie");
//    }
//    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
//        return authService.createAuthToken(authRequest);
//    }


}
