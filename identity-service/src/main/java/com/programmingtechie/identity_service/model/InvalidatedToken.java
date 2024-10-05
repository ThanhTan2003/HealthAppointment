package com.programmingtechie.identity_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "invalidated-token")
public class InvalidatedToken {
    @Id
    String id;

    Date expiryTime;
}
