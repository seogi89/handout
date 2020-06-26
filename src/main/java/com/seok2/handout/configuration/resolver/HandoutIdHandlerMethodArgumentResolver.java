package com.seok2.handout.configuration.resolver;

import com.seok2.handout.service.TokenService;
import com.seok2.handout.utils.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
@Component
public class HandoutIdHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private final TokenService tokenService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return  parameter.hasParameterAnnotation(HandoutId.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String roomId = webRequest.getHeader("X-ROOM-ID");
        String tokenId = webRequest.getHeader("X-TOKEN-ID");
        return tokenService.findByToken(TokenProvider.concat(roomId, tokenId));
    }
}
