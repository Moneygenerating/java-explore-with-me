package ewm.compilation.model;

import ewm.event.model.Event;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "compilations")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(fetch = FetchType.EAGER)
    @ToString.Exclude
    private Set<Event> events;
    private Boolean pinned;
    private String title;
}
