package com.pragma.powerup.infrastructure.out.rabbitmq.adapter;

import com.pragma.powerup.domain.model.Bootcamp;
import com.pragma.powerup.domain.spi.IBootcampNotificationPort;
import com.pragma.powerup.infrastructure.out.r2dbc.repository.IPersonBootcampRepository;
import com.pragma.powerup.infrastructure.out.rabbitmq.dto.BootcampReportDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQProducerAdapter implements IBootcampNotificationPort {

    private final RabbitTemplate rabbitTemplate;
    private final IPersonBootcampRepository personBootcampRepository;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    @Override
    public void notifyBootcampCreation(Bootcamp bootcamp) {
        personBootcampRepository.countByBootcampId(bootcamp.getId())
                .defaultIfEmpty(0L)
                .map(count -> {
                    BootcampReportDTO dto = new BootcampReportDTO();
                    dto.setBootcampId(bootcamp.getId());
                    dto.setName(bootcamp.getName());
                    dto.setEnrolledCount(count);
                    dto.setCapacityCount(bootcamp.getCapabilityIds().size());
                    dto.setTechnologyCount(bootcamp.getTechnologyCount());

                    rabbitTemplate.convertAndSend(exchange, routingKey, dto);
                    log.info("Evento de reporte enviado a la cola para bootcamp: {}", bootcamp.getId());
                    return count;
                }).subscribe();
    }
}