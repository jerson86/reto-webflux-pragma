package com.pragma.powerup.infrastructure.out.rabbitmq.adapter;

import com.pragma.powerup.domain.api.IReportServicePort;
import com.pragma.powerup.domain.model.BootcampReport;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BootcampReportConsumer {

    private final IReportServicePort reportServicePort;

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void consumeBootcampEvent(BootcampReport report) {
        reportServicePort.saveBootcampReport(report).subscribe();
    }
}
