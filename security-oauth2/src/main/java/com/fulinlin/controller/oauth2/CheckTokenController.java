package com.fulinlin.controller.oauth2;

import com.fulinlin.exception.Auth2ResponseExceptionTranslator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 由于spring oauth2自带的 检查token的返回值
 * 并不符合我们自己的要求，
 * 所以我们给重写一些
 * @author Luke Taylor
 * @author Joel D'sa
 */
@Slf4j
@RestController
public class CheckTokenController {
    @Autowired
    private TokenStore jwtTokenStore;
    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;


    private Auth2ResponseExceptionTranslator exceptionTranslator = new Auth2ResponseExceptionTranslator();


    @RequestMapping(value = "/oauth/check")
    @ResponseBody
    public Map<String, ?> checkToken(@RequestParam("token") String value) {
        OAuth2AccessToken token = jwtTokenStore.readAccessToken(value);
        if (token == null) {
            throw new InvalidTokenException("Token was not recognised");
        }
        if (token.isExpired()) {
            throw new InvalidTokenException("Token has expired");
        }
        OAuth2Authentication authentication = jwtTokenStore.readAuthentication (token.getValue());
        Map<String, Object> response = (Map<String, Object>) jwtAccessTokenConverter.convertAccessToken(token, authentication);
        // gh-1070
        response.put("active", true);    // Always true if token exists and not expired
        return response;
    }


    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<OAuth2Exception> handleException(Exception e) throws Exception {
        log.info("Handling error: " + e.getClass().getSimpleName() + ", " + e.getMessage());
        // This isn't an oauth resource, so we don't want to send an
        // unauthorized code here. The client has already authenticated
        // successfully with basic auth and should just
        // get back the invalid token error.
        @SuppressWarnings("serial")
        InvalidTokenException e400 = new InvalidTokenException(e.getMessage()) {
            @Override
            public int getHttpErrorCode() {
                return 400;
            }
        };
        return exceptionTranslator.translate(e400);
    }

}
