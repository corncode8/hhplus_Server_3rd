package hhplus.serverjava.common.entity;

import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public class BaseEntity {

    @CreationTimestamp
    @Column(columnDefinition = "TIMESTAMP", name = "createdAt", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
