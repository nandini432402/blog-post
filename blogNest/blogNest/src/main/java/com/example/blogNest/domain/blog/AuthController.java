package com.example.blogNest.domain.blog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
 private JwtUtil jwtUtil;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    static class RegisterRequest {
        public String username;
        public String email;
        public String password;
    }

    static class LoginRequest {
        public String email;
        public String password;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
        if (userRepository.existsByEmail(request.email)) {
            return ResponseEntity.badRequest().body("Email already in use");
        }
        User user = new User();
        user.setUsername(request.username);
        user.setEmail(request.email);
        user.setPassword(passwordEncoder.encode(request.password));
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.email);
        if (!optionalUser.isPresent() || !passwordEncoder.matches(request.password, optionalUser.get().getPassword())) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
          User user = optionalUser.get();
          String token = jwtUtil.generateToken(user.getEmail());
           return ResponseEntity.ok(new JwtResponse(token));
    }

    static class JwtResponse {
        public String token;
        public JwtResponse(String token) {
            this.token = token;
        }
    }
}