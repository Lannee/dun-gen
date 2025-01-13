package backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "history")
@Data
@NoArgsConstructor
public class History {

    public History(long object_id, String userName, String objectClass, LocalDateTime time) {
        this.objectId = object_id;
        this.userName = userName;
        this.objectClass = objectClass;
        this.time = time;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "objectId", nullable = false)
    private long objectId;
    
    @Column(name = "userName", nullable = false)
    private String userName;

    @Column(name = "objectClass", nullable = false)
    private String objectClass;

    @Column(name = "time", nullable = false)
    private LocalDateTime time;
}
