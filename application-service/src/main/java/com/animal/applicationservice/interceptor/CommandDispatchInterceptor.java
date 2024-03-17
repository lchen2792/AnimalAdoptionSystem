package com.animal.applicationservice.interceptor;

import com.animal.common.constant.Constants;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

@Component
public class CommandDispatchInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {
    @Nonnull
    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(@Nonnull List<? extends CommandMessage<?>> messages) {
        return (index, command) -> {
            Optional<? extends CommandMessage<?>> commandMessage = ReactiveSecurityContextHolder
                    .getContext()
                    .map(SecurityContext::getAuthentication)
                    .map(authentication -> Map.of(Constants.AUTHENTICATION, authentication))
                    .map(command::andMetaData)
                    .blockOptional();
            return commandMessage.isPresent()? commandMessage.get() : command;
        };
    }
}
