package zunik.ohimarket.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zunik.ohimarket.domain.Member;
import zunik.ohimarket.service.dto.ProfileResponseDto;
import zunik.ohimarket.repository.MemberRepository;
import zunik.ohimarket.repository.PostRepository;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public ProfileResponseDto getProfile(String memberToken) {
        Member member = memberRepository.findByToken(memberToken).orElseThrow(
                () -> new IllegalArgumentException("해당 토큰의 계정을 찾지 못했습니다.")
        );
        Integer totalLikeCount = postRepository.sumLikeCountFindByMemberId(member.getId()).orElse(0);
        Integer totalViews = postRepository.sumViewsFindByMemberId(member.getId()).orElse(0);

        ProfileResponseDto profileResponseDto = new ProfileResponseDto();
        profileResponseDto.setMember(member);
        profileResponseDto.setTotalLikeCount(totalLikeCount);
        profileResponseDto.setTotalViews(totalViews);

        return profileResponseDto;
    }

}
