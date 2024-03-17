package com.animal.applicationservice.interceptor;

import com.animal.common.constant.Constants;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.InterceptorChain;
import org.axonframework.messaging.MessageHandlerInterceptor;
import org.axonframework.messaging.unitofwork.UnitOfWork;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

@Component
public class CommandHandlerInterceptor implements MessageHandlerInterceptor<CommandMessage<?>> {
    @Override
    public Object handle(@Nonnull UnitOfWork<? extends CommandMessage<?>> unitOfWork, @Nonnull InterceptorChain interceptorChain) throws Exception {
        CommandMessage<?> commandMessage = unitOfWork.getMessage();
        Authentication authentication = (Authentication) commandMessage.getMetaData().get(Constants.AUTHENTICATION);
        if (authentication != null) {
            ReactiveSecurityContextHolder
                    .getContext()
                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication))
                    .subscribe();
            return interceptorChain.proceed();
        } else {
            return null;
        }
    }
}
