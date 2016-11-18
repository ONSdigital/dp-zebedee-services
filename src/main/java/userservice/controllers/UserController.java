package userservice.controllers;

import com.bettercloud.vault.VaultException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import userservice.UserAccountService;
import userservice.entities.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    private final static Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final UserAccountService vaultService;

    @Autowired
    public UserController(UserAccountService vaultService) {
        this.vaultService = vaultService;

    }

    @RequestMapping("/user")
    public List<UserDetails> getList(final @CookieValue("access_token") String token) throws VaultException {
        return vaultService.getUsers(token);
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public ResponseEntity<Response> createUser(final @RequestBody User user,
                                               final @CookieValue("access_token") String token) throws VaultException {
        LOGGER.info("Creating user account for {} ", user.getEmail());
        vaultService.createUser(user, token);
        return new ResponseEntity(new Response(true, ""), HttpStatus.OK);
    }

    @RequestMapping(value = "/user/{email}/", method = RequestMethod.DELETE)
    public ResponseEntity<Response> deleteUser(final @PathVariable String email,
                                               final @CookieValue("access_token") String token) throws VaultException {
        vaultService.deleteUser(email, token);
        return new ResponseEntity(new Response(true, ""), HttpStatus.OK);
    }

    @RequestMapping(value = "/user/password", method = RequestMethod.POST)
    public void resetPassword(final @RequestBody PasswordReset password) throws VaultException {
        vaultService.resetPassword(password);
    }

    @RequestMapping(value = "/user/authenticate", method = RequestMethod.POST)
    public ResponseEntity<Token> authenicate(final @RequestBody User user) {
        try {
            // If a user authenticated but no token returned they need to reset there password,
            // the error code 417 is returned.
            Optional<Token> token = vaultService.authenicate(user);
            if (token.isPresent()) {
                LOGGER.info("Authenticated {}", user.getEmail());
                return new ResponseEntity(vaultService.authenicate(user).get(), HttpStatus.OK);
            } else {
                LOGGER.info("Password reset needed for {}", user.getEmail());
                return new ResponseEntity(HttpStatus.EXPECTATION_FAILED);
            }
        } catch (final VaultException ex) {
            LOGGER.error("Authentication failed for {} ", user.getEmail(), ex);
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/user/{email}/permissions")
    public ResponseEntity<Permission> getPermissions(final @PathVariable String email) throws VaultException {
        return new ResponseEntity(vaultService.getUsersPolicies(email), HttpStatus.OK);
    }

    @RequestMapping(value = "/user/token/{token}/")
    public ResponseEntity<Permission> getPermissionsFromToken(final @PathVariable String token) throws VaultException {
        final Permission permission = new Permission();
        permission.setEditor(true);
        permission.setAdmin(true);
        return new ResponseEntity(permission, HttpStatus.OK);
    }
}
