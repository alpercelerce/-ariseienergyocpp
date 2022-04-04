package eu.chargetime.ocpp.jsonserverimplementation.entity;

import eu.chargetime.ocpp.jsonserverimplementation.type.TransactionType;
import eu.chargetime.ocpp.model.core.MeterValue;
import eu.chargetime.ocpp.model.core.Reason;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
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
public class EVTransaction {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @ManyToOne
    private EVConnection session;

    private LocalDateTime createTime;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private Integer connectorId;
    private String idTag;
    private Integer meterStart;
    private Integer meterStop;
    private Integer reservationId;
    private Integer transactionId;

    @Enumerated(EnumType.STRING)
    private Reason reason;

    @Column(columnDefinition="TEXT")
    private String data;

}
