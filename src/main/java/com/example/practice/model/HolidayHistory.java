package com.example.practice.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="Holiday_History")
public class HolidayHistory{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull(message = "must have value")
    private String date;
    @NotNull(message = "must have value")
    private String destination;

    @OneToMany(targetEntity = Visitor.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "history_id")
    private List<Visitor> visitorList;
}
