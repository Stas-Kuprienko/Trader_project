package com.anastasia.trade_project.core_client;

import org.springframework.web.client.RestClientResponseException;
import java.util.Optional;
import java.util.function.Supplier;

abstract class HttpError404Handler {

    protected <R> Optional<R> catch404(Supplier<R> supplier) {
        try {
            return Optional.of(supplier.get());
        } catch (RestClientResponseException e) {
            if (e.getStatusCode().value() == 404) {
                return Optional.empty();
            } else {
                throw e;
            }
        }
    }
}
