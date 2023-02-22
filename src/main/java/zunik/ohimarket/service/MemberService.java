package zunik.ohimarket.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zunik.ohimarket.controller.dto.SignUpDto;
import zunik.ohimarket.domain.Member;
import zunik.ohimarket.controller.dto.MemberUpdateDto;
import zunik.ohimarket.repository.MemberRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void save(SignUpDto createParam){
        Member member = new Member();
        member.setEmail(createParam.getEmail());
        member.setNickname(createParam.getNickname());
        member.setIntroduction(createParam.getIntroduction());
        member.setToken(UUID.randomUUID().toString());

        String encodedPassword = passwordEncoder.encode(createParam.getPassword());
        member.setPassword(encodedPassword);

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
