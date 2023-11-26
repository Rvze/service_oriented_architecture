package com.example.spring_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "venues")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Venue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @NotBlank(message = "Venue name cannot be null or empty")
    private String name;

    @Column(name = "capacity")
    @Min(value = 1, message = "Venue capacity must be higher than 0")
    private long capacity;

    @Column(name = "type")
    @Enumerated(value = EnumType.STRING)
    private VenueType type;

    @OneToMany(mappedBy = "venue", cascade = CascadeType.REMOVE)
    private List<Ticket> tickets;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Venue venue = (Venue) o;
        return capacity == venue.capacity && Objects.equals(name, venue.name) && type == venue.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, capacity, type);
    }
}
