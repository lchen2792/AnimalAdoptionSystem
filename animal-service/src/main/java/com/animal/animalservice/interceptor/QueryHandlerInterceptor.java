package com.animal.animalservice.interceptor;

import com.animal.common.constant.Constants;
import org.axonframework.messaging.InterceptorChain;
import org.axonframework.messaging.MessageHandlerInterceptor;
import org.axonframework.messaging.unitofwork.UnitOfWork;
import org.axonframework.queryhandling.QueryMessage;
import org.springframework.security.core.Authentication;
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
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return interceptorChain.proceed();
        } else {
            return null;
        }
    }
}
