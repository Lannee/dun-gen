package backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "equipment")
@Data
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 256)
    private String name;
    
    @Column(nullable = false)
    private String description;
}
