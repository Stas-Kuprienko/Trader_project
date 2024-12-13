package core_client_reactive;

import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import java.util.function.Supplier;

abstract class HttpError404Handler {

    protected <R> Mono<R> process(Supplier<Mono<R>> supplier) {
        try {
            return supplier.get();
        } catch (WebClientResponseException e) {
            if (e.getStatusCode().value() == 404) {
                return Mono.empty();
            } else {
                throw e;
            }
        }
    }
}
