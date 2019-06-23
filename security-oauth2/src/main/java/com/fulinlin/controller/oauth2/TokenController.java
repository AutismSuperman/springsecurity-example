//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.fulinlin.controller.oauth2;

import com.fulinlin.exception.Auth2ResponseExceptionTranslator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.*;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestValidator;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.security.Principal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 由于spring security自带的刷新token不符合我们的返回需求
 * 所以说这里重新构造一下
 */
@RestController
@Slf4j
public class TokenController {
    @Autowired
    private AuthorizationServerTokenServices jwkTokenServices;
    @Autowired
    private ClientDetailsService clientDetailsService;

    private OAuth2RequestFactory oAuth2RequestFactory;

    private TokenGranter tokenGranter;


    @PostConstruct
    private void init() {
        oAuth2RequestFactory = new DefaultOAuth2RequestFactory(clientDetailsService);
        tokenGranter = new RefreshTokenGranter(jwkTokenServices, clientDetailsService, oAuth2RequestFactory);
    }

    private Auth2ResponseExceptionTranslator exceptionTranslator = new Auth2ResponseExceptionTranslator();


    private OAuth2RequestValidator oAuth2RequestValidator = new DefaultOAuth2RequestValidator();


    private Set<HttpMethod> allowedRequestMethods;

    public TokenController() {
        this.allowedRequestMethods = new HashSet(Arrays.asList(HttpMethod.POST));
    }

    @GetMapping("/oauth/token")
    public ResponseEntity<OAuth2AccessToken> getAccessToken(Principal principal, @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        if (!this.allowedRequestMethods.contains(HttpMethod.GET)) {
            throw new HttpRequestMethodNotSupportedException("GET");
        } else {
            return this.postAccessToken(principal, parameters);
        }
    }

    @PostMapping("/oauth/token")
    public ResponseEntity<OAuth2AccessToken> postAccessToken(Principal principal, @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        if (!(principal instanceof Authentication)) {
            throw new InsufficientAuthenticationException("There is no client authentication. Try adding an appropriate authentication filter.");
        } else {
            String clientId = this.getClientId(principal);
            ClientDetails authenticatedClient = clientDetailsService.loadClientByClientId(clientId);

            TokenRequest tokenRequest = oAuth2RequestFactory.createTokenRequest(parameters, authenticatedClient);

            if (clientId != null && !clientId.equals("") && !clientId.equals(tokenRequest.getClientId())) {
                throw new InvalidClientException("Given client ID does not match authenticated client");
            } else {
                if (authenticatedClient != null) {
                    this.oAuth2RequestValidator.validateScope(tokenRequest, authenticatedClient);
                }
                if (!StringUtils.hasText(tokenRequest.getGrantType())) {
                    throw new InvalidRequestException("Missing grant type");
                } else if (tokenRequest.getGrantType().equals("implicit")) {
                    throw new InvalidGrantException("Implicit grant type not supported from token endpoint");
                } else {
                    if (this.isRefreshTokenRequest(parameters)) {
                        tokenRequest.setScope(OAuth2Utils.parseParameterList((String) parameters.get("scope")));
                    }
                    OAuth2AccessToken token = tokenGranter.grant(tokenRequest.getGrantType(), tokenRequest);
                    if (token == null) {
                        throw new UnsupportedGrantTypeException("Unsupported grant type: " + tokenRequest.getGrantType());
                    } else {
                        return this.getResponse(token);
                    }
                }
            }
        }
    }

    protected String getClientId(Principal principal) {
        Authentication client = (Authentication) principal;
        if (!client.isAuthenticated()) {
            throw new InsufficientAuthenticationException("The client is not authenticated.");
        } else {
            String clientId = client.getName();
            if (client instanceof OAuth2Authentication) {
                clientId = ((OAuth2Authentication) client).getOAuth2Request().getClientId();
            }

            return clientId;
        }
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<OAuth2Exception> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) throws Exception {
        if (log.isErrorEnabled()) {
            log.error("Handling error: " + e.getClass().getSimpleName() + ", " + e.getMessage(), e);
        }

        return exceptionTranslator.translate(e);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<OAuth2Exception> handleException(Exception e) throws Exception {
        if (log.isErrorEnabled()) {
            log.error("Handling error: " + e.getClass().getSimpleName() + ", " + e.getMessage(), e);
        }
        return exceptionTranslator.translate(e);
    }

    @ExceptionHandler({ClientRegistrationException.class})
    public ResponseEntity<OAuth2Exception> handleClientRegistrationException(Exception e) throws Exception {
        if (log.isErrorEnabled()) {
            log.error("Handling error: " + e.getClass().getSimpleName() + ", " + e.getMessage(), e);
        }
        return exceptionTranslator.translate(new BadClientCredentialsException());
    }

    @ExceptionHandler({OAuth2Exception.class})
    public ResponseEntity<OAuth2Exception> handleException(OAuth2Exception e) throws Exception {
        if (log.isErrorEnabled()) {
            log.error("Handling error: " + e.getClass().getSimpleName() + ", " + e.getMessage(), e);
        }
        return exceptionTranslator.translate(e);
    }

    private ResponseEntity<OAuth2AccessToken> getResponse(OAuth2AccessToken accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cache-Control", "no-store");
        headers.set("Pragma", "no-cache");
        return new ResponseEntity(accessToken, headers, HttpStatus.OK);
    }

    private boolean isRefreshTokenRequest(Map<String, String> parameters) {
        return "refresh_token".equals(parameters.get("grant_type")) && parameters.get("refresh_token") != null;
    }

    public void setAllowedRequestMethods(Set<HttpMethod> allowedRequestMethods) {
        this.allowedRequestMethods = allowedRequestMethods;
    }


}
