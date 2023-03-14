package ewha.efub.zeje.config;

import ewha.efub.zeje.service.SpotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class BatchJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory; // 생성자 DI 받음
    private final StepBuilderFactory stepBuilderFactory;
    private final SpotService spotService;

    @Bean
    public Job job() { //Job
        return jobBuilderFactory.get("stepNextJob")
                .start(step1())
                .next(step2())
                .next(step3())
                .next(step4())
                .next(step5())
                .next(step6())
                .next(step7())
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>>>>this is step1");
                    log.info(spotService.addSpotApi("A01", "", ""));
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>>>>this is step2");
                    log.info(spotService.addSpotApi("A02", "A0202", "A02020700"));
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
    @Bean
    public Step step3() {
        return stepBuilderFactory.get("step3")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>>>>this is step3");
                    log.info(spotService.addSpotApi("A02", "A0202", "A02020600"));
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
    @Bean
    public Step step4() {
        return stepBuilderFactory.get("step4")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>>>>this is step4");
                    log.info(spotService.addSpotApi("A02", "A0202", "A02020200"));
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
    @Bean
    public Step step5() {
        return stepBuilderFactory.get("step5")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>>>>this is step5");
                    log.info(spotService.addSpotApi("A02", "A0203", "A02030400"));
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
    @Bean
    public Step step6() {
        return stepBuilderFactory.get("step6")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>>>>this is step6");
                    log.info(spotService.addSpotApi("A02", "A0203", "A02030100"));
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
    @Bean
    public Step step7() {
        return stepBuilderFactory.get("step7")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>>>>this is step7");
                    log.info(spotService.addSpotApi("A02", "A0203", "A02030600"));
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
