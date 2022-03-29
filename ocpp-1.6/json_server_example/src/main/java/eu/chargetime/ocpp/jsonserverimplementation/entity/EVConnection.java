package eu.chargetime.ocpp.jsonserverimplementation.entity;

import eu.chargetime.ocpp.jsonserverimplementation.type.ConnectionStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
public class EVConnection {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    private String sessionId;

    private ConnectionStatus connectionStatus;

    private String identifier;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
