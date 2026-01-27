package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IEnrollmentServicePort;
import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.model.Bootcamp;
import com.pragma.powerup.domain.spi.IBootcampPersistencePort;
import com.pragma.powerup.domain.spi.IEnrollmentPersistencePort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
public class IEnrollmentUseCase implements IEnrollmentServicePort {

    private final IBootcampPersistencePort bootcampPersistencePort;
    private final IEnrollmentPersistencePort enrollmentPersistencePort;

    @Override
    public Mono<Void> enrollPerson(Long personId, List<Long> bootcampIds) {
        return Mono.zip(
                bootcampPersistencePort.findAllByPersonId(personId).collectList(),
                bootcampPersistencePort.findAllByIds(bootcampIds).collectList()
        ).flatMap(tuple -> {
            List<Bootcamp> currentBootcamps = tuple.getT1();
            List<Bootcamp> newBootcamps = tuple.getT2();

            validateEnrollmentRules(newBootcamps, currentBootcamps);

            return enrollmentPersistencePort.saveAll(personId, bootcampIds);
        });
    }

    private void validateEnrollmentRules(List<Bootcamp> newOnes, List<Bootcamp> currentOnes) {
        for (Bootcamp newB : newOnes) {
            LocalDate endNew = newB.getStartDate().plusDays(newB.getDurationDays() - 1L);

            for (Bootcamp currentB : currentOnes) {
                LocalDate endCurrent = currentB.getStartDate().plusDays(currentB.getDurationDays() - 1L);

                if (checkOverlap(newB.getStartDate(), endNew, currentB.getStartDate(), endCurrent)) {
                    throw new DomainException(String.format(
                            "Conflicto de fechas: El bootcamp '%s' se cruza con '%s'.",
                            newB.getName(), currentB.getName()));
                }
            }
        }
    }

    private boolean checkOverlap(LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2) {
        return !start1.isAfter(end2) && !start2.isAfter(end1);
    }
}
