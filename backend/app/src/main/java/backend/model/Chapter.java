package backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import backend.DTO.ChapterExistedDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "chapters")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Chapter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Long id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "marines_count", nullable = false)
    private long marinesCount;

    @Column(name = "world", nullable = false, length = 255)
    private String world;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn
    @JsonIgnore
    private User user;

    public Chapter(String name, long marinesCount, String world, User user) {
        this.name = name;
        this.marinesCount = marinesCount;
        this.world = world;
        this.user = user;
    }

    public ChapterExistedDTO toDTO() {
        return ChapterExistedDTO.builder()
            .id(this.id)
            .name(this.name)
            .marinesCount(this.marinesCount)
            .world(this.world)
            .user_id(this.user.getId())
            .editable(false)
            .build();
    }
}

