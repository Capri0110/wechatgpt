package com.nirvana.wechatgpt.utils;

import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.apache.http.client.HttpClient;
import java.util.Map;

/**
 * spring rest template
 * @author nirvana.xu
 * @Date 2022-05-07
 */
@Component
public class RestTemplateUtils {

    private static RestTemplate restTemplate;

    static {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
//        HttpClient httpClient = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();
        HttpClient httpClient = HttpClientBuilder.create().disableCookieManagement().disableRedirectHandling().build();
        factory.setConnectionRequestTimeout(10000);
        factory.setConnectTimeout(10000);
        factory.setReadTimeout(120000);
        factory.setHttpClient(httpClient);
        restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(factory);
    }

    // ----------------------------------GET-------------------------------------------------------
    /**
     * GET request call method
     *
     * @param url           request URL
     * @param responseType return obj. type
     * @return ResponseEntity response body obj.
     */
    public <T> ResponseEntity<T> get(String url, Class<T> responseType) {
        return restTemplate.getForEntity(url, responseType);
    }

    /**
     * GET request call method
     *
     * @param url           request URL
     * @param responseType return obj. type
     * @param uriVariables variables in URL in order
     * @return ResponseEntity response body obj.
     */
    public <T> ResponseEntity<T> get(String url, Class<T> responseType, Object... uriVariables) {
        return restTemplate.getForEntity(url, responseType, uriVariables);
    }

    /**
     * GET request call method
     *
     * @param url           request URL
     * @param responseType return obj. type
     * @param uriVariables variables in URL, correspond with Map key
     * @return ResponseEntity response body obj.
     */
    public <T> ResponseEntity<T> get(String url, Class<T> responseType, Map<String, ?> uriVariables) {
        return restTemplate.getForEntity(url, responseType, uriVariables);
    }

    /**
     * custom defined header GET request call method
     *
     * @param url           request URL
     * @param headers      header with params
     * @param responseType return obj. type
     * @param uriVariables variables in URL in order
     * @return ResponseEntity response body obj.
     */
    public <T> ResponseEntity<T> get(String url, Map<String, String> headers, Class<T> responseType, Object... uriVariables) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return get(url, httpHeaders, responseType, uriVariables);
    }

    /**
     * custom defined header GET request call method
     *
     * @param url           request URL
     * @param headers      header with params
     * @param responseType return obj. type
     * @param uriVariables variables in URL in order
     * @return ResponseEntity response body obj.
     */
    public <T> ResponseEntity<T> get(String url, HttpHeaders headers, Class<T> responseType, Object... uriVariables) {
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        return exchange(url, HttpMethod.GET, requestEntity, responseType, uriVariables);
    }

    /**
     * custom defined header GET request call method
     *
     * @param url           request URL
     * @param headers      header with params
     * @param responseType return obj. type
     * @param uriVariables variables in URL, correspond with Map key
     * @return ResponseEntity response body obj.
     */
    public <T> ResponseEntity<T> get(String url, Map<String, String> headers, Class<T> responseType, Map<String, ?> uriVariables) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return get(url, httpHeaders, responseType, uriVariables);
    }

    /**
     * custom defined header GET request call method
     *
     * @param url           request URL
     * @param headers      header with params
     * @param responseType return obj. type
     * @param uriVariables variables in URL, correspond with Map key
     * @return ResponseEntity response body obj.
     */
    public <T> ResponseEntity<T> get(String url, HttpHeaders headers, Class<T> responseType, Map<String, ?> uriVariables) {
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        return exchange(url, HttpMethod.GET, requestEntity, responseType, uriVariables);
    }

    // ----------------------------------POST-------------------------------------------------------

    /**
     * POST request call method
     *
     * @param url           request URL
     * @param responseType return obj. type
     * @return
     */
    public <T> ResponseEntity<T> post(String url, Class<T> responseType) {
        return restTemplate.postForEntity(url, HttpEntity.EMPTY, responseType);
    }

    /**
     * POST request call method
     *
     * @param url           request URL
     * @param requestBody  request body
     * @param responseType return obj. type
     * @return ResponseEntity response body obj.
     */
    public <T> ResponseEntity<T> post(String url, Object requestBody, Class<T> responseType) {
        return restTemplate.postForEntity(url, requestBody, responseType);
    }

    /**
     * POST request call method
     *
     * @param url           request URL
     * @param requestBody  request body
     * @param responseType return obj. type
     * @param uriVariables variables in URL in order
     * @return ResponseEntity response body obj.
     */
    public <T> ResponseEntity<T> post(String url, Object requestBody, Class<T> responseType, Object... uriVariables) {
        return restTemplate.postForEntity(url, requestBody, responseType, uriVariables);
    }

    /**
     * POST request call method
     *
     * @param url           request URL
     * @param requestBody  request body
     * @param responseType return obj. type
     * @param uriVariables variables in URL, correspond with Map key
     * @return ResponseEntity response body obj.
     */
    public <T> ResponseEntity<T> post(String url, Object requestBody, Class<T> responseType, Map<String, ?> uriVariables) {
        return restTemplate.postForEntity(url, requestBody, responseType, uriVariables);
    }

    /**
     * custom defined header POST request call method
     *
     * @param url           request URL
     * @param headers      header with params
     * @param requestBody  request body
     * @param responseType return obj. type
     * @param uriVariables variables in URL in order
     * @return ResponseEntity response body obj.
     */
    public <T> ResponseEntity<T> post(String url, Map<String, String> headers, Object requestBody, Class<T> responseType, Object... uriVariables) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return post(url, httpHeaders, requestBody, responseType, uriVariables);
    }

    /**
     * custom defined header POST request call method
     *
     * @param url           request URL
     * @param headers      header with params
     * @param requestBody  request body
     * @param responseType return obj. type
     * @param uriVariables variables in URL in order
     * @return ResponseEntity response body obj.
     */
    public <T> ResponseEntity<T> post(String url, HttpHeaders headers, Object requestBody, Class<T> responseType, Object... uriVariables) {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody, headers);
        return post(url, requestEntity, responseType, uriVariables);
    }

    /**
     * custom defined header POST request call method
     *
     * @param url           request URL
     * @param headers      header with params
     * @param requestBody  request body
     * @param responseType return obj. type
     * @param uriVariables variables in URL, correspond with Map key
     * @return ResponseEntity response body obj.
     */
    public <T> ResponseEntity<T> post(String url, Map<String, String> headers, Object requestBody, Class<T> responseType, Map<String, ?> uriVariables) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return post(url, httpHeaders, requestBody, responseType, uriVariables);
    }

    /**
     * custom defined header POST request call method
     *
     * @param url           request URL
     * @param headers      header with params
     * @param requestBody  request body
     * @param responseType return obj. type
     * @param uriVariables variables in URL, correspond with Map key
     * @return ResponseEntity response body obj.
     */
    public <T> ResponseEntity<T> post(String url, HttpHeaders headers, Object requestBody, Class<T> responseType, Map<String, ?> uriVariables) {
        HttpEntity<Object> requestEntity = new HttpEntity<>(requestBody, headers);
        return post(url, requestEntity, responseType, uriVariables);
    }

    /**
     * custom defined header&body POST request call method
     *
     * @param url            request URL
     * @param requestEntity request headers & body obj.
     * @param responseType  return obj. type
     * @param uriVariables  variables in URL in order
     * @return ResponseEntity response body obj.
     */
    public <T> ResponseEntity<T> post(String url, HttpEntity<?> requestEntity, Class<T> responseType, Object... uriVariables) {
        return restTemplate.exchange(url, HttpMethod.POST, requestEntity, responseType, uriVariables);
    }

    /**
     * custom defined header&body POST request call method
     *
     * @param url            request URL
     * @param requestEntity request headers & body obj.
     * @param responseType  return obj. type
     * @param uriVariables  variables in URL, correspond with Map key
     * @return ResponseEntity response body obj.
     */
    public <T> ResponseEntity<T> post(String url, HttpEntity<?> requestEntity, Class<T> responseType, Map<String, ?> uriVariables) {
        return restTemplate.exchange(url, HttpMethod.POST, requestEntity, responseType, uriVariables);
    }

    // ----------------------------------PUT-------------------------------------------------------

    /**
     * PUT request call method
     *
     * @param url           request URL
     * @param responseType return obj. type
     * @param uriVariables variables in URL in order
     * @return ResponseEntity response body obj.
     */
    public <T> ResponseEntity<T> put(String url, Class<T> responseType, Object... uriVariables) {
        return put(url, HttpEntity.EMPTY, responseType, uriVariables);
    }

    /**
     * PUT request call method
     *
     * @param url           request URL
     * @param requestBody  request body
     * @param responseType return obj. type
     * @param uriVariables variables in URL in order
     * @return ResponseEntity response body obj.
     */
    public <T> ResponseEntity<T> put(String url, Object requestBody, Class<T> responseType, Object... uriVariables) {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody);
        return put(url, requestEntity, responseType, uriVariables);
    }

    /**
     * PUT request call method
     *
     * @param url           request URL
     * @param requestBody  request body
     * @param responseType return obj. type
     * @param uriVariables variables in URL, correspond with Map key
     * @return ResponseEntity response body obj.
     */
    public <T> ResponseEntity<T> put(String url, Object requestBody, Class<T> responseType, Map<String, ?> uriVariables) {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody);
        return put(url, requestEntity, responseType, uriVariables);
    }

    /**
     * custom defined header PUT request call method
     *
     * @param url           request URL
     * @param headers      header with params
     * @param requestBody  request body
     * @param responseType return obj. type
     * @param uriVariables variables in URL in order
     * @return ResponseEntity response body obj.
     */
    public <T> ResponseEntity<T> put(String url, Map<String, String> headers, Object requestBody, Class<T> responseType, Object... uriVariables) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return put(url, httpHeaders, requestBody, responseType, uriVariables);
    }

    /**
     * custom defined header PUT request call method
     *
     * @param url           request URL
     * @param headers      header with params
     * @param requestBody  request body
     * @param responseType return obj. type
     * @param uriVariables variables in URL in order
     * @return ResponseEntity response body obj.
     */
    public <T> ResponseEntity<T> put(String url, HttpHeaders headers, Object requestBody, Class<T> responseType, Object... uriVariables) {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody, headers);
        return put(url, requestEntity, responseType, uriVariables);
    }

    /**
     * custom defined header PUT request call method
     *
     * @param url           request URL
     * @param headers      header with params
     * @param requestBody  request body
     * @param responseType return obj. type
     * @param uriVariables variables in URL, correspond with Map key
     * @return ResponseEntity response body obj.
     */
    public <T> ResponseEntity<T> put(String url, Map<String, String> headers, Object requestBody, Class<T> responseType, Map<String, ?> uriVariables) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return put(url, httpHeaders, requestBody, responseType, uriVariables);
    }

    /**
     * custom defined header PUT request call method
     *
     * @param url           request URL
     * @param headers      header with params
     * @param requestBody  request body
     * @param responseType return obj. type
     * @param uriVariables variables in URL, correspond with Map key
     * @return ResponseEntity response body obj.
     */
    public <T> ResponseEntity<T> put(String url, HttpHeaders headers, Object requestBody, Class<T> responseType, Map<String, ?> uriVariables) {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody, headers);
        return put(url, requestEntity, responseType, uriVariables);
    }

    /**
     * custom defined header&body PUT request call method
     *
     * @param url            request URL
     * @param requestEntity request headers & body obj.
     * @param responseType  return obj. type
     * @param uriVariables  variables in URL in order
     * @return ResponseEntity response body obj.
     */
    public <T> ResponseEntity<T> put(String url, HttpEntity<?> requestEntity, Class<T> responseType, Object... uriVariables) {
        return restTemplate.exchange(url, HttpMethod.PUT, requestEntity, responseType, uriVariables);
    }

    /**
     * custom defined header&body PUT request call method
     *
     * @param url            request URL
     * @param requestEntity request headers & body obj.
     * @param responseType  return obj. type
     * @param uriVariables  variables in URL, correspond with Map key
     * @return ResponseEntity response body obj.
     */
    public <T> ResponseEntity<T> put(String url, HttpEntity<?> requestEntity, Class<T> responseType, Map<String, ?> uriVariables) {
        return restTemplate.exchange(url, HttpMethod.PUT, requestEntity, responseType, uriVariables);
    }

    // ----------------------------------DELETE-------------------------------------------------------

    /**
     * DELETE request call method
     *
     * @param url           request URL
     * @param responseType return obj. type
     * @param uriVariables variables in URL in order
     * @return ResponseEntity response body obj.
     */
    public <T> ResponseEntity<T> delete(String url, Class<T> responseType, Object... uriVariables) {
        return delete(url, HttpEntity.EMPTY, responseType, uriVariables);
    }

    /**
     * DELETE request call method
     *
     * @param url           request URL
     * @param responseType return obj. type
     * @param uriVariables variables in URL, correspond with Map key
     * @return ResponseEntity response body obj.
     */
    public <T> ResponseEntity<T> delete(String url, Class<T> responseType, Map<String, ?> uriVariables) {
        return delete(url, HttpEntity.EMPTY, responseType, uriVariables);
    }

    /**
     * DELETE request call method
     *
     * @param url           request URL
     * @param requestBody  request body
     * @param responseType return obj. type
     * @param uriVariables variables in URL in order
     * @return ResponseEntity response body obj.
     */
    public <T> ResponseEntity<T> delete(String url, Object requestBody, Class<T> responseType, Object... uriVariables) {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody);
        return delete(url, requestEntity, responseType, uriVariables);
    }

    /**
     * DELETE request call method
     *
     * @param url           request URL
     * @param requestBody  request body
     * @param responseType return obj. type
     * @param uriVariables variables in URL, correspond with Map key
     * @return ResponseEntity response body obj.
     */
    public <T> ResponseEntity<T> delete(String url, Object requestBody, Class<T> responseType, Map<String, ?> uriVariables) {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody);
        return delete(url, requestEntity, responseType, uriVariables);
    }

    /**
     * custom defined header DELETE request call method
     *
     * @param url           request URL
     * @param headers      header with params
     * @param responseType return obj. type
     * @param uriVariables variables in URL in order
     * @return ResponseEntity response body obj.
     */
    public <T> ResponseEntity<T> delete(String url, Map<String, String> headers, Class<T> responseType, Object... uriVariables) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return delete(url, httpHeaders, responseType, uriVariables);
    }

    /**
     * custom defined header DELETE request call method
     *
     * @param url           request URL
     * @param headers      header with params
     * @param responseType return obj. type
     * @param uriVariables variables in URL in order
     * @return ResponseEntity response body obj.
     */
    public <T> ResponseEntity<T> delete(String url, HttpHeaders headers, Class<T> responseType, Object... uriVariables) {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
        return delete(url, requestEntity, responseType, uriVariables);
    }

    /**
     * custom defined header DELETE request call method
     *
     * @param url           request URL
     * @param headers      header with params
     * @param responseType return obj. type
     * @param uriVariables variables in URL, correspond with Map key
     * @return ResponseEntity response body obj.
     */
    public <T> ResponseEntity<T> delete(String url, Map<String, String> headers, Class<T> responseType, Map<String, ?> uriVariables) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return delete(url, httpHeaders, responseType, uriVariables);
    }

    /**
     * custom defined header DELETE request call method
     *
     * @param url           request URL
     * @param headers      header with params
     * @param responseType return obj. type
     * @param uriVariables variables in URL, correspond with Map key
     * @return ResponseEntity response body obj.
     */
    public <T> ResponseEntity<T> delete(String url, HttpHeaders headers, Class<T> responseType, Map<String, ?> uriVariables) {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
        return delete(url, requestEntity, responseType, uriVariables);
    }

    /**
     * custom defined header DELETE request call method
     *
     * @param url           request URL
     * @param headers      header with params
     * @param requestBody  request body
     * @param responseType return obj. type
     * @param uriVariables variables in URL in order
     * @return ResponseEntity response body obj.
     */
    public <T> ResponseEntity<T> delete(String url, Map<String, String> headers, Object requestBody, Class<T> responseType, Object... uriVariables) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return delete(url, httpHeaders, requestBody, responseType, uriVariables);
    }

    /**
     * custom defined header DELETE request call method
     *
     * @param url           request URL
     * @param headers      header with params
     * @param requestBody  request body
     * @param responseType return obj. type
     * @param uriVariables variables in URL in order
     * @return ResponseEntity response body obj.
     */
    public <T> ResponseEntity<T> delete(String url, HttpHeaders headers, Object requestBody, Class<T> responseType, Object... uriVariables) {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody, headers);
        return delete(url, requestEntity, responseType, uriVariables);
    }

    /**
     * custom defined header DELETE request call method
     *
     * @param url           request URL
     * @param headers      header with params
     * @param requestBody  request body
     * @param responseType return obj. type
     * @param uriVariables variables in URL, correspond with Map key
     * @return ResponseEntity response body obj.
     */
    public <T> ResponseEntity<T> delete(String url, Map<String, String> headers, Object requestBody, Class<T> responseType, Map<String, ?> uriVariables) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return delete(url, httpHeaders, requestBody, responseType, uriVariables);
    }

    /**
     * custom defined header DELETE request call method
     *
     * @param url           request URL
     * @param headers      header with params
     * @param requestBody  request body
     * @param responseType return obj. type
     * @param uriVariables variables in URL, correspond with Map key
     * @return ResponseEntity response body obj.
     */
    public <T> ResponseEntity<T> delete(String url, HttpHeaders headers, Object requestBody, Class<T> responseType, Map<String, ?> uriVariables) {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody, headers);
        return delete(url, requestEntity, responseType, uriVariables);
    }

    /**
     * custom defined header&body DELETE request call method
     *
     * @param url            request URL
     * @param requestEntity request headers & body obj.
     * @param responseType  return obj. type
     * @param uriVariables  variables in URL in order
     * @return ResponseEntity response body obj.
     */
    public <T> ResponseEntity<T> delete(String url, HttpEntity<?> requestEntity, Class<T> responseType, Object... uriVariables) {
        return restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, responseType, uriVariables);
    }

    /**
     * custom defined header&body DELETE request call method
     *
     * @param url            request URL
     * @param requestEntity request headers & body obj.
     * @param responseType  return obj. type
     * @param uriVariables  variables in URL, correspond with Map key
     * @return ResponseEntity response body obj.
     */
    public <T> ResponseEntity<T> delete(String url, HttpEntity<?> requestEntity, Class<T> responseType, Map<String, ?> uriVariables) {
        return restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, responseType, uriVariables);
    }

    // ----------------------------------通用方法-------------------------------------------------------

    /**
     * 通用调用方式
     *
     * @param url            request URL
     * @param method        request method type
     * @param requestEntity request headers & body obj.
     * @param responseType  return obj. type
     * @param uriVariables  variables in URL in order
     * @return ResponseEntity response body obj.
     */
    public <T> ResponseEntity<T> exchange(String url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType, Object... uriVariables) {
        return restTemplate.exchange(url, method, requestEntity, responseType, uriVariables);
    }

    /**
     * general call method
     *
     * @param url            request URL
     * @param method        request method type
     * @param requestEntity request headers & body obj.
     * @param responseType  return obj. type
     * @param uriVariables  variables in URL, correspond with Map key
     * @return ResponseEntity response body obj.
     */
    public <T> ResponseEntity<T> exchange(String url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType, Map<String, ?> uriVariables) {
        return restTemplate.exchange(url, method, requestEntity, responseType, uriVariables);
    }

    /**
     * get RestTemplate instance obj.，then use it freely
     *
     * @return RestTemplate instance obj.
     */
    public RestTemplate getRestTemplate() {
        return restTemplate;
    }
}


