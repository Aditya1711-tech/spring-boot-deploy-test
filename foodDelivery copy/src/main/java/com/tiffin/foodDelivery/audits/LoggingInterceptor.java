package com.tiffin.foodDelivery.audits;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.method.HandlerMethod;

import static com.tiffin.foodDelivery.utils.constants.InterceptorColors.*;

@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Value("${colored-logs}")
    private boolean USE_COLOR_LOGGING;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        if (handler instanceof HandlerMethod handlerMethod) {
            String controllerName = handlerMethod.getBeanType().getSimpleName();
            String methodName = handlerMethod.getMethod().getName();
            String httpMethod = request.getMethod();
            String requestURI = request.getRequestURI();

            // Construct a well-formatted log message without colors
            String logMessage = String.format(
                    "HTTP Method: %s | URL: %s | Controller: %s | Method: %s",
                    httpMethod,
                    requestURI,
                    controllerName,
                    methodName
            );

            // Apply colors if toggle is enabled
            if (USE_COLOR_LOGGING) {
                logMessage = String.format(
                        "%sHTTP Method:%s %s %s| URL:%s %s %s| Controller:%s %s %s| Method:%s %s%s",
                        RESET,
                        BLUE, // Color for Controller
                        httpMethod,
                        RESET,
                        YELLOW, // Color for Method
                        requestURI,
                        RESET,
                        GREEN, // Color for HTTP Method
                        controllerName,
                        RESET,
                        MAGENTA, // Color for URL
                        methodName,
                        RESET // Reset color at the end
                );
            }

            logger.info(logMessage);
        }
        return true;
    }
}
