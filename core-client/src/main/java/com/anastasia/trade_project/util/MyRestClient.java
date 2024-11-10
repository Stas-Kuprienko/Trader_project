package com.anastasia.trade_project.util;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyRestClient<E> {

    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final Class<E> resourceEntity;


    public MyRestClient(RestTemplate restTemplate,
                        String baseUrl,
                        Class<E> resourceEntity) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
        this.resourceEntity = resourceEntity;
    }


    public static String uriById(Object id) {
        return "/" + id.toString();
    }

    public static String formParameters(Map<String, Object> parameters, String uri) {
        if (parameters != null && !parameters.isEmpty()) {
            StringBuilder uriWithParams = new StringBuilder(uri + '?');
            for (var e : parameters.entrySet()) {
                uriWithParams
                        .append(e.getKey())
                        .append('=')
                        .append(e.getValue().toString())
                        .append('&');
            }
            uriWithParams.deleteCharAt(uriWithParams.length() - 1);
            uri = uriWithParams.toString();
        }
        return uri;
    }

    public GetRequestBuilder<E> get() {
        return new GetRequestBuilder<>(restTemplate, baseUrl, resourceEntity);
    }

    public PostRequestBuilder<E> post() {
        return new PostRequestBuilder<>(restTemplate, baseUrl, resourceEntity);
    }

    public PutRequestBuilder<E> put() {
        return new PutRequestBuilder<>(restTemplate, baseUrl, resourceEntity);
    }

    public DeleteRequestBuilder<E> delete() {
        return new DeleteRequestBuilder<>(restTemplate, baseUrl, resourceEntity);
    }


    public static class GetRequestBuilder<E> extends AbstractRequestBuilder<E> {

        private Map<String, Object> parameters;

        private GetRequestBuilder(RestTemplate restTemplate,
                                  String baseUrl,
                                  Class<E> resourceEntity) {
            super(restTemplate, baseUrl, resourceEntity);
        }


        public GetRequestBuilder<E> uri(String uri) {
            this.uri = uri;
            return this;
        }

        public GetRequestBuilder<E> parameters(Map<String, Object> parameters) {
            this.parameters = parameters;
            return this;
        }

        public GetRequestBuilder<E> addParameter(String parameter, Object value) {
            if (this.parameters == null) {
                this.parameters = new HashMap<>();
            }
            this.parameters.put(parameter, value);
            return this;
        }

        public E complete() {
            uri = formParameters(parameters, uri);
            return restTemplate
                    .getForEntity(baseUrl + uri, resourceEntity)
                    .getBody();
        }

        public List<E> completeForList() {
            uri = formParameters(parameters, uri);
            ParameterizedTypeReference<List<E>> typeReference = new ParameterizedTypeReference<>() {
            };

            ResponseEntity<List<E>> response = restTemplate
                    .exchange(baseUrl + uri, HttpMethod.GET, HttpEntity.EMPTY, typeReference);
            return response.getBody();
        }
    }


    public static class PostRequestBuilder<E> extends AbstractRequestBuilder<E> {

        private Object bodyEntity;

        private PostRequestBuilder(RestTemplate restTemplate,
                                   String baseUrl,
                                   Class<E> resourceEntity) {
            super(restTemplate, baseUrl, resourceEntity);
        }


        public PostRequestBuilder<E> uri(String uri) {
            this.uri = uri;
            return this;
        }

        public PostRequestBuilder<E> body(Object bodyEntity) {
            this.bodyEntity = bodyEntity;
            return this;
        }

        public E complete() {
            return restTemplate.postForObject(baseUrl + uri, bodyEntity, resourceEntity);
        }
    }


    public static class PutRequestBuilder<E> extends AbstractRequestBuilder<E> {

        private Object bodyEntity;

        private PutRequestBuilder(RestTemplate restTemplate,
                                  String baseUrl,
                                  Class<E> resourceEntity) {
            super(restTemplate, baseUrl, resourceEntity);
        }


        public PutRequestBuilder<E> uri(String uri) {
            this.uri = uri;
            return this;
        }

        public PutRequestBuilder<E> body(Object bodyEntity) {
            this.bodyEntity = bodyEntity;
            return this;
        }

        public E complete() {
            RequestEntity<?> request = new RequestEntity<>(bodyEntity, HttpMethod.PUT, URI.create(baseUrl + uri));
            return restTemplate
                    .exchange(request , resourceEntity)
                    .getBody();
        }

        public void completeWithoutResponse() {
            restTemplate.put(baseUrl + uri, HttpEntity.EMPTY);
        }
    }


    public static class DeleteRequestBuilder<E> extends AbstractRequestBuilder<E> {

        private Map<String, Object> parameters;

        private DeleteRequestBuilder(RestTemplate restTemplate,
                                     String baseUrl,
                                     Class<E> resourceEntity) {
            super(restTemplate, baseUrl, resourceEntity);
        }


        public DeleteRequestBuilder<E> uri(String uri) {
            this.uri = uri;
            return this;
        }

        public DeleteRequestBuilder<E> parameters(Map<String, Object> parameters) {
            this.parameters = parameters;
            return this;
        }

        public DeleteRequestBuilder<E> addParameter(String parameter, Object value) {
            if (this.parameters == null) {
                this.parameters = new HashMap<>();
            }
            this.parameters.put(parameter, value);
            return this;
        }

        public void complete() {
            uri = formParameters(parameters, uri);
            restTemplate.delete(URI.create(baseUrl + uri));
        }
    }


    public abstract static class AbstractRequestBuilder<E> {

        protected final RestTemplate restTemplate;
        protected final String baseUrl;
        protected final Class<E> resourceEntity;
        protected String uri;


        private AbstractRequestBuilder(RestTemplate restTemplate,
                                       String baseUrl,
                                       Class<E> resourceEntity) {
            this.restTemplate = restTemplate;
            this.baseUrl = baseUrl;
            this.resourceEntity = resourceEntity;
            uri = "";
        }
    }
}
