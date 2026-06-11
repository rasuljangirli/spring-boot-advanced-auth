package com.core.identity.config;

import lombok.RequiredArgsConstructor;
import com.core.identity.model.ThreadErrorLog;
import com.core.identity.repository.ThreadErrorLogRepository;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
@RequiredArgsConstructor
public class AsyncConfig implements AsyncConfigurer {

    private final ThreadErrorLogRepository threadErrorLogRepository;

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(4);
        executor.setQueueCapacity(10);
        executor.setThreadNamePrefix("MyAsyncThread-");
        executor.initialize();

        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (throwable, method, objects) -> {
            ThreadErrorLog threadErrorLog = new ThreadErrorLog();

            threadErrorLog.setThreadName(Thread.currentThread().getName());

            String errorMessage = "Exception in method " + method.getName() + ": " + throwable.getMessage();
            threadErrorLog.setErrorMessage(errorMessage);

            threadErrorLogRepository.save(threadErrorLog);

        };
    }
}
