package zunik.ohimarket.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zunik.ohimarket.controller.dto.SignInDto;
import zunik.ohimarket.domain.Member;
import zunik.ohimarket.exception.LoginFailureException;
import zunik.ohimarket.repository.MemberRepository;

import java.time.LocalDateTime;


@Slf4j
@Service
@RequiredArgsConstructor
public class SignInService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Member signIn(SignInDto param) throws LoginFailureException {
        Member member = memberRepository.findByEmail(param.getEmail()).orElseThrow(
                () -> new LoginFailureException()
        );

        if (!passwordEncoder.matches(param.getPassword(), member.getPassword())) {
            // 패스워드가 틀림
            throw new LoginFailureException();
        }

        //마지막 로그인 변경
        member.setLastLogin(LocalDateTime.now());

        return member;
    }
}
