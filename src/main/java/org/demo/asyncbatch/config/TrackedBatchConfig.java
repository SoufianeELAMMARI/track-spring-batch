package org.demo.asyncbatch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableBatchProcessing
@EnableAsync
public class TrackedBatchConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private SimpleJobExecutionListener simpleJobExecutionListener;

    @Bean
    public Job trackedProcessDataJob() {
        return jobBuilderFactory.get("processDataJob")
                .listener(simpleJobExecutionListener) // Add this line to your existing config
                .start(trackedProcessDataStep())
                .build();
    }

    @Bean
    public Step trackedProcessDataStep() {
        return stepBuilderFactory.get("processDataStep")
                .<String, String>chunk(2)
                .reader(itemReader())
                .processor(itemProcessor())
                .writer(itemWriter())
                .build();
    }

    @Bean
    public ItemReader<String> itemReader() {
        List<String> items = Arrays.asList("item1", "item2", "item3", "item4", "item5");
        return new ListItemReader<>(items);
    }

    @Bean
    public ItemProcessor<String, String> itemProcessor() {
        return item -> {
            Thread.sleep(2000); // 2 second delay
            System.out.println("Processing: " + item);
            return "processed-" + item;
        };
    }

    @Bean
    public ItemWriter<String> itemWriter() {
        return items -> {
            items.forEach(item -> System.out.println("Writing: " + item));
        };
    }
}