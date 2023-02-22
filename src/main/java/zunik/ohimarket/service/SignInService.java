package zunik.ohimarket.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zunik.ohimarket.domain.Member;
import zunik.ohimarket.repository.MemberRepository;


@Slf4j
@Service
@RequiredArgsConstructor
public class SignInService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public boolean authCheck(String email, String password) {
        Member member = memberRepository.findByEmail(email);

        if (member == null) {
            return false;
        }

        if (!passwordEncoder.matches(password, member.getPassword())) {
            log.info("패스워드가 틀렸습니다.");
            return false;
        }

        return true;
    }
}
