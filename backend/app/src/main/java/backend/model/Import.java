package backend.model;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

import backend.DTO.ImportDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import lombok.extern.slf4j.Slf4j;

@Entity
@Table(name = "import")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Slf4j
public class Import {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ImportStatus status;
    
    @Column(name = "userName", nullable = false)
    private String userName;

    @Column(name = "count", nullable = false)
    private long count;

    @Column(name = "time", nullable = false)
    private LocalDateTime time;

    @Column(name = "objectName", nullable = false)
    private String objectName;

    public ImportDTO getCreatedImport() {
        return new ImportDTO(
            this.getStatus(),
            this.getUserName(),
            this.getCount(),
            this.getTime().toString(),
            objectName,
            null
        );
    }
}