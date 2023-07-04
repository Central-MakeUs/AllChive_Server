package allchive.server.domain.domains.user.domain;


import allchive.server.domain.common.model.BaseTimeEntity;
import javax.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "tbl_scrap")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Scrap extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private Long categoryId;

    @Builder
    private Scrap(User user, Long categoryId) {
        this.user = user;
        this.categoryId = categoryId;
    }

    public static Scrap of(User user, Long categoryId) {
        return Scrap.builder().user(user).categoryId(categoryId).build();
    }
}
