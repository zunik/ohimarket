package zunik.ohimarket.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zunik.ohimarket.domain.Member;
import zunik.ohimarket.repository.MemberRepository;


@Slf4j
@Service
@RequiredArgsConstructor
public class SignInService {
    private final MemberRepository memberRepository;

    @Transactional
    public boolean authCheck(String email, String password) {
        Member member = memberRepository.findByEmail(email);

        if (member == null || !password.equals(member.getPassword())) {
            return false;
        }

        return true;
    }
}
