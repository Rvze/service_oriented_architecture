package com.example.spring_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;

@Entity
@Table(name = "tickets")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @NotBlank(message = "Ticket name cannot be null or empty")
    private String name;

    @JdbcTypeCode(SqlTypes.JSON)
    @NotNull(message = "Ticket coordinates cannot be null")
    private Coordinates coordinates;

    @Column(name = "creation_date")
    @NotNull(message = "Ticket creation date cannot be null")
    private LocalDate creationDate;

    @Column(name = "price")
    @NotNull(message = "Ticket price cannot be null")
    @Min(value = 1, message = "Ticket price must be higher than 0")
    private Integer price;

    @Column(name = "type")
    @NotNull(message = "Ticket type cannot be null")
    private TicketType type;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "venue_id")
    private Venue venue;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "event_id")
    private Event event;

    @Column(name = "person_id")
    private Long personId;
}
