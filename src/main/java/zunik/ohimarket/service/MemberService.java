package zunik.ohimarket.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zunik.ohimarket.domain.Member;
import zunik.ohimarket.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository repository;

    @Transactional
    public void save(Member member){
        repository.save(member);
    }

    @Transactional
    public Member getByEmail(String email) {
        return repository.findByEmail(email);
    }
}
