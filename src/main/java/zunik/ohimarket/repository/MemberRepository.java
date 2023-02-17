package zunik.ohimarket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zunik.ohimarket.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByEmail(String email);

    Optional<Member> findByToken(String Token);
}
