package todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/users")
public class UserController {

  @Autowired
  private IUserRepository userRepository;

  @PostMapping("/")
  public ResponseEntity create(@RequestBody UserModel userModel) {
    var user = this.userRepository.findByUsername(userModel.getUsername());

    if (user != null) {
      return ResponseEntity.status(400).body("Usuário Ja Cadastrado");
    }

    var passHash = BCrypt
    .withDefaults()
    .hashToString(12, userModel.getPassword()
    .toCharArray());

    userModel.setPassword(passHash);

    var useCreated = this.userRepository.save(userModel);
    return ResponseEntity.status(201).body(useCreated);
  }
}
