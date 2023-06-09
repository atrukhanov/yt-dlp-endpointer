package ru.link.extractor.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Mono;
import ru.link.extractor.model.Response;

@Controller
@Slf4j
public class WebController {
    @Autowired
    ChannelExplorer service;

    @GetMapping({"/vidos", "/vidos/{c}"})
    @ResponseBody
    public Mono<Void> startVideo(ServerHttpResponse response, @PathVariable(required = false) String c) {
        Mono<Response> responseData = this.service.processTgChannel(c);
        return responseData.flatMap((r) -> {
            if (r.isSuccess()) {
                response.setStatusCode(HttpStatus.PERMANENT_REDIRECT);
                response.getHeaders().set("Location", r.getResult());
            } else {
                response.setStatusCode(HttpStatus.NOT_FOUND);
            }
            return response.setComplete();
        });
    }
}
