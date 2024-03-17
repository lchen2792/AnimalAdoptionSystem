package com.animal.paymentservice.interceptor;

import com.animal.common.constant.Constants;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.axonframework.queryhandling.QueryMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

@Component
public class QueryDispatchInterceptor implements MessageDispatchInterceptor<QueryMessage<?, ?>> {
    @Nonnull
    @Override
    public BiFunction<Integer, QueryMessage<?, ?>, QueryMessage<?, ?>> handle(@Nonnull List<? extends QueryMessage<?, ?>> messages) {
        return (index, query) -> {
            Optional<? extends QueryMessage<?, ?>> queryMessage = ReactiveSecurityContextHolder
                    .getContext()
                    .map(SecurityContext::getAuthentication)
                    .map(authentication -> Map.of(Constants.AUTHENTICATION, authentication))
                    .map(query::andMetaData)
                    .blockOptional();
            return queryMessage.isPresent()? queryMessage.get() : query;
        };
    }
}
