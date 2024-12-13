package core_client_reactive;

import com.anastasia.trade_project.dto.TelegramChatDto;
import com.anastasia.trade_project.dto.UserDto;
import com.anastasia.trade_project.enums.Language;
import com.anastasia.trade_project.enums.Role;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class TelegramChatResource extends HttpError404Handler {

    private static final String resourceUrl = "/telegram-chats";

    private final WebClient webClient;


    TelegramChatResource(String baseUrl) {
        baseUrl += resourceUrl;
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }


//    public Mono<TelegramChatDto> findById(long chatId) {
//        return process(() -> restClient.get()
//                .uri(CoreServiceClientReactiveV1.uriById(chatId))
//                .retrieve()
//                .bodyToMono(TelegramChatDto.class));
//    }

    //TODO TEMPORARY SOLUTION !!!!!!!!!!!!!!!!!!!!!!!

    public Mono<TelegramChatDto> findById(long chatId) {
        UserDto user = UserDto.builder()
                .id(1L)
                .name("Stanislav")
                .language(Language.RU)
                .login("example@email.com")
                .role(Role.USER)
                .build();
        return Mono.just(new TelegramChatDto(chatId, user));
    }
}
