package zunik.ohimarket.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;
    private String password;
    private String nickname;
}
