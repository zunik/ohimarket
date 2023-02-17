package zunik.ohimarket.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zunik.ohimarket.domain.Member;
import zunik.ohimarket.dto.ProfileResponseDto;
import zunik.ohimarket.repository.MemberRepository;
import zunik.ohimarket.repository.PostRepository;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public ProfileResponseDto getProfile(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow();
        int totalLikeCount = postRepository.sumLikeCountFindByMemberId(memberId);
        int totalViews = postRepository.sumViewsFindByMemberId(memberId);

        ProfileResponseDto profileResponseDto = new ProfileResponseDto();
        profileResponseDto.setMember(member);
        profileResponseDto.setTotalLikeCount(totalLikeCount);
        profileResponseDto.setTotalViews(totalViews);

        return profileResponseDto;
    }

}
