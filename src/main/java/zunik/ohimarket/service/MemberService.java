package zunik.ohimarket.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zunik.ohimarket.domain.Member;
import zunik.ohimarket.dto.MemberUpdateDto;
import zunik.ohimarket.repository.MemberRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository repository;

    @Transactional
    public void save(Member member){
        repository.save(member);
    }

    @Transactional
    public void update(Long memberId, MemberUpdateDto updateParam) {
        Member member = repository.findById(memberId).orElseThrow();
        member.setNickname(updateParam.getNickname());
        member.setIntroduction(updateParam.getIntroduction());
    }

    @Transactional(readOnly = true)
    public Member findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public Optional<Member> findById(Long id) {
        return repository.findById(id);
    }
}
