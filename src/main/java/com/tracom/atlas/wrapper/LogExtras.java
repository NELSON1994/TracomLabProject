package com.tracom.atlas.wrapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@Service
public class LogExtras {


    @Value("${baseUrlLoggerService}")
    private String url;
    @Value("${client-id}")
    private String clientId;

    //    Authentication auth;
    private final HttpServletRequest request;

    public LogExtras(HttpServletRequest request) {
        this.request = request;

    }

    /*
    source - source of the request eg, browser, postman
     */
    public String getSource() {
        String source = org.thymeleaf.util.StringUtils.abbreviate(request.getHeader("user-agent"), 100);
        return source;
    }

    /*
    - IP Address of the machine making the request
     */
    public String getIpAddress() {
        String ipAddress = request.getRemoteAddr();
        return ipAddress;
    }

    /*
    -client id is the oauth2 clint id assigned to the maker of the request
     */
    public String getClientId() {
        return clientId;
    }

    /*
    - id of the currently logged in user
    - id of maker
     */
    public Long getUserId() {
        try {

            System.out.println("BEARER TOKEN ================== : " + request.getHeader("Authorization").substring(7));
            //        get user email from Authentication object
            RestTemplate temp = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", request.getHeader("Authorization"));

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<UserResponseWrapper> response = temp.exchange(url + "ufs-common-modules/api/v1/user/me", HttpMethod.GET, entity, UserResponseWrapper.class);

//        UserResponseWrapper response = temp.exchange(url + "miliki-oauth-service/user/me", UserResponseWrapper.class, headers);
            System.out.println("FETCHED USER WITH USERNAME : " + response.getStatusCode());
            System.out.println(response.getBody().getData().getUserId());
            return response.getBody().getData().getUserId();
        } catch (NullPointerException e) {
            System.out.println("Bearer token not found : " + e.getMessage());
            return null;

        }
    }
}
