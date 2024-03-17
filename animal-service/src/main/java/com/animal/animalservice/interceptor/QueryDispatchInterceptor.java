package com.animal.animalservice.interceptor;

import com.animal.common.constant.Constants;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.axonframework.queryhandling.QueryMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

@Component
public class QueryDispatchInterceptor implements MessageDispatchInterceptor<QueryMessage<?, ?>> {
    @Nonnull
    @Override
    public BiFunction<Integer, QueryMessage<?, ?>, QueryMessage<?, ?>> handle(@Nonnull List<? extends QueryMessage<?, ?>> messages) {
        return (index, query) -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) {
                return query.andMetaData(Map.of(Constants.AUTHENTICATION, authentication));
            } else {
                return query;
            }
        };
    }
}
