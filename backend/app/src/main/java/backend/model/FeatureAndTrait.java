package backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "features_and_traits")
public class FeatureAndTrait {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 256)
    private String name;
    
    @Column(nullable = false)
    private String description;
}
