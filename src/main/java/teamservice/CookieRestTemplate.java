package teamservice;

import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;

public class CookieRestTemplate extends RestTemplate {

    private final String token;

    public CookieRestTemplate(final String token) {
        this.token = token;
    }

    @Override
    protected ClientHttpRequest createRequest(URI url, HttpMethod method) throws IOException {
        ClientHttpRequest request = super.createRequest(url, method);
        // Hardcoded token should be replaced.
        request.getHeaders().add("X-Vault-Token", token);
        return request;
    }
}