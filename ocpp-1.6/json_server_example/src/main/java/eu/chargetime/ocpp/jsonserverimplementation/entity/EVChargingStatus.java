package eu.chargetime.ocpp.jsonserverimplementation.entity;

import eu.chargetime.ocpp.model.core.ChargePointErrorCode;
import eu.chargetime.ocpp.model.core.ChargePointStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
public class EVChargingStatus {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @ManyToOne
    private EVConnection session;

    private LocalDateTime createTime;

    private LocalDateTime lastUpdateTime;

    @Enumerated(EnumType.STRING)
    private ChargePointErrorCode errorCode;

    @Enumerated(EnumType.STRING)
    private ChargePointStatus status;
}
