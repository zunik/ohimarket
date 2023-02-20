package zunik.ohimarket.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zunik.ohimarket.domain.Member;
import zunik.ohimarket.repository.PostQueryRepository;
import zunik.ohimarket.repository.dto.MemberSummeryDto;
import zunik.ohimarket.service.dto.ProfileResponseDto;
import zunik.ohimarket.repository.MemberRepository;
import zunik.ohimarket.repository.PostRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileService {
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final PostQueryRepository postQueryRepository;

    @Transactional(readOnly = true)
    public ProfileResponseDto getProfile(String memberToken) {
        Member member = memberRepository.findByToken(memberToken).orElseThrow(
                () -> new IllegalArgumentException("해당 토큰의 계정을 찾지 못했습니다.")
        );
        MemberSummeryDto summeryDto = postQueryRepository.summaryByMemberId(member.getId());

        ProfileResponseDto profileResponseDto = new ProfileResponseDto();
        profileResponseDto.setMember(member);
        profileResponseDto.setSummary(summeryDto);

        return profileResponseDto;
    }

}
