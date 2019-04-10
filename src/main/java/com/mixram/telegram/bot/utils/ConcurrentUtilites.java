package com.mixram.telegram.bot.utils;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * @author mixram on 2019-04-10.
 * @since 0.1.1.0
 */
public class ConcurrentUtilites {

    /**
     * To run function in a new thread with {@link RequestContextHolder} data from LocalThread asynchronously.
     *
     * @param function function to be run in separate thread asynchronously.
     * @param <T>      type to prepare {@link CompletableFuture} with.
     *
     * @return instance of {@link CompletableFuture} with results of function applying.
     *
     * @since 0.1.1.0
     */
    public static <T> CompletableFuture<T> supplyAsyncWithLocalThreadContext(Function<Void, T> function) {
        final RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        //        final SecurityContext context = SecurityContextHolder.getContext();

        return CompletableFuture.supplyAsync(() -> {
            RequestContextHolder.setRequestAttributes(requestAttributes);
            //            SecurityContextHolder.setContext(context);
            try {
                return function.apply(null);
            } finally {
                RequestContextHolder.resetRequestAttributes();
                //                SecurityContextHolder.clearContext();
            }
        });
    }
}
