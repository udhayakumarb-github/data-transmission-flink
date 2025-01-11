package com.elan.example.log;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

@Slf4j
public class LoggingSink<T> implements SinkFunction<T> {
    @Override
    public void invoke(T value, Context context) {
        log.info("Data from stream: {}", value);
    }
}
