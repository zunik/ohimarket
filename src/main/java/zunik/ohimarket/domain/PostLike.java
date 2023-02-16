package zunik.ohimarket.domain;


import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Data
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"post_id", "member_id"}
                )
        }
)
public class PostLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, name = "post_id")
    private Long postId;
    @Column(nullable = false, name = "member_id")
    private Long memberId;
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
