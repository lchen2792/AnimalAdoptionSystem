package com.animal.paymentservice.interceptor;

import com.animal.common.constant.Constants;
import org.axonframework.messaging.InterceptorChain;
import org.axonframework.messaging.MessageHandlerInterceptor;
import org.axonframework.messaging.unitofwork.UnitOfWork;
import org.axonframework.queryhandling.QueryMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

@Component
public class QueryHandlerInterceptor implements MessageHandlerInterceptor<QueryMessage<?, ?>> {
    @Override
    public Object handle(@Nonnull UnitOfWork<? extends QueryMessage<?, ?>> unitOfWork, @Nonnull InterceptorChain interceptorChain) throws Exception {
        QueryMessage<?, ?> queryMessage = unitOfWork.getMessage();
        Authentication authentication = (Authentication) queryMessage.getMetaData().get(Constants.AUTHENTICATION);
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
