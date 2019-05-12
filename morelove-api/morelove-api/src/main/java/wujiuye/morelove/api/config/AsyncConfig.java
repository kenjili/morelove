package wujiuye.morelove.api.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
// 启用spring线程池,配合AsyncConfigurer接口使用指定线程池
// 如果不配合AsyncConfigurer使用则spring会从bean容器中查找一个实现Executor接口的bean对象作为默认的线程池
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    private Integer corePoolSize = 200;
    private Integer maxPoolSize = 1000;
    private Integer queueCapacity = 200;
    private Integer keepAlive = 60;//单位秒

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("weiai-executor-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy(){
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
                super.rejectedExecution(r,e);
            }
        });
        executor.setKeepAliveSeconds(keepAlive);
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncUncaughtExceptionHandler() {
            @Override
            public void handleUncaughtException(Throwable throwable, Method method, Object... objects) {
                System.err.println(method.getName()+"异步方法执行异常===>"+throwable.getMessage());
                throwable.printStackTrace();
            }
        };
    }
}
