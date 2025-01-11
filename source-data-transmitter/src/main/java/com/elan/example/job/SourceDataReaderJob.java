package com.elan.example.job;

import com.elan.example.log.LoggingSink;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class SourceDataReaderJob {
    public static void main(String[] args) {
        log.info("Source data Flink application start");

        try {
            StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

            List<String> names = Arrays.asList("udhay", "priya", "chezhiyan", "maaran");
            DataStream<String> nameDataStream = env.fromCollection(names, TypeInformation.of(String.class));

            // Log the data using a custom sink
            nameDataStream.print().setParallelism(1);

            // Execute the job
            env.execute("Source Data Reader Job");

        } catch (Exception e) {
            log.error("Error in Source Data Reader Job", e);
        }
    }
}
