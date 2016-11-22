package encryptionservice.controller;

import com.bettercloud.vault.VaultException;
import encryptionservice.EncrytionService;
import encryptionservice.entities.Content;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class EncrytionController {

    private final static Logger LOGGER = LoggerFactory.getLogger(EncrytionController.class);

    private final EncrytionService encrytionService;

    @Autowired
    public EncrytionController(final EncrytionService encrytionService) {
        this.encrytionService = encrytionService;
    }

    @RequestMapping(value = "/encrypt", method = RequestMethod.POST)
    public Content encrypt(final @RequestBody Content content, final @CookieValue("access_token") String token) throws VaultException {
        LOGGER.info("Encrypting collection id:{}", content.getId());
        encrytionService.encryt(content, token);
        return content;
    }

    @RequestMapping(value = "/decrypt", method = RequestMethod.POST)
    public Content decrypt(final @RequestBody Content content, final @CookieValue("access_token") String token) throws VaultException {
        LOGGER.info("Decrypting collection id:{}", content.getId());
        encrytionService.decrypt(content, token);
        return content;
    }
}
