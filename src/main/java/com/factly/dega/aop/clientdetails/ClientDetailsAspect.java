package com.factly.dega.aop.clientdetails;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.simple.parser.*;


/**
 * Aspect to retrieve client id from token/user_login on all Rest Controller Spring components.
 *
 * By default, it only runs with the "dev" profile.
 */
@Aspect
public class ClientDetailsAspect {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private Environment env;

    @Autowired
    private HttpServletRequest context;

    private final RestTemplate restTemplate;

    private String getUserByEmailUrl = "http://localhost:8082/api/dega-users/email/";//TODO: Get this from YML

    public ClientDetailsAspect(Environment env, RestTemplate restTemplate) {
        this.env = env;
        this.restTemplate = restTemplate;
    }

    /**
     * Pointcut that matches all repositories, services and Web REST endpoints.
     */
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void springBeanPointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    /**
     * Pointcut that matches all Spring beans in the application's main packages.
     */
    @Pointcut("within(com.factly.dega.web.rest..*)")
    public void applicationPackagePointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    /**
     * Advice that retrieves client id when a rest controller method is entered.
     *
     * @param joinPoint join point for advice
     * @throws Throwable throws IllegalArgumentException
     */
    @Before("applicationPackagePointcut() && springBeanPointcut()")
    public void retrieveClientID(JoinPoint joinPoint) throws Throwable {
        try {
            if (context != null && context.getAttribute("ClientID") == null) {
                OAuth2Authentication auth = (OAuth2Authentication) context.getUserPrincipal();
                String principal = (String) auth.getPrincipal();

                if (principal.startsWith("service-account-")) {
                    // Request with API token
                    String[] tokens = principal.split("service-account-");

                    if (tokens.length == 2) {
                        String clientID = tokens[1];
                        context.setAttribute("ClientID", clientID);
                    } else {
                        log.warn("No client found with the principal {}, exiting", principal);
                    }
                } else {

                    String token = "Bearer " + (OAuth2AuthenticationDetails.class.cast(auth.getDetails())).getTokenValue();
                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                    httpHeaders.add("Authorization", token);
                    HttpEntity<String> httpEntity = new HttpEntity(httpHeaders);
                    ResponseEntity<String> response = restTemplate.exchange(
                        getUserByEmailUrl+principal, HttpMethod.GET, httpEntity, String.class);
                    String responseBody = response.getBody();
                    JSONParser parser = new JSONParser();
                    Object obj = parser.parse(responseBody);
                    JSONObject jo = (JSONObject) obj;
                    String organizationCurrentId = (String) jo.get("organizationCurrentId");
                    JSONArray organizationsArray = (JSONArray) jo.get("organizations");
                    Iterator organizationsIterator = organizationsArray.iterator();
                    String clientId = null;
                    String tempClientId = null;
                    String currentOrganizationId = null;
                    while (organizationsIterator.hasNext())
                    {
                        Iterator<Map.Entry> itr1 = ((Map) organizationsIterator.next()).entrySet().iterator();
                        while (itr1.hasNext()) {
                            Map.Entry pair = itr1.next();
                            if(pair.getKey().equals("id")){
                                currentOrganizationId = (String)pair.getValue();
                            }else if(pair.getKey().equals("clientId")){
                                tempClientId = (String)pair.getValue();
                            }
                            if(tempClientId != null && currentOrganizationId != null && currentOrganizationId.equals(organizationCurrentId)){
                                clientId = tempClientId;
                                break;
                            }
                        }
                    }
                    if(clientId != null){
                        context.setAttribute("ClientID", clientId);
                    }else {
                        log.warn("No org found with the default org id {}, exiting", organizationCurrentId);
                    }
                }
            }

        } catch (IllegalArgumentException e) {
            log.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
                joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());

            throw e;
        }
    }
}
