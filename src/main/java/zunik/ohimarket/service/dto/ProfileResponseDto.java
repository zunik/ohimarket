package zunik.ohimarket.service.dto;

import lombok.Data;
import zunik.ohimarket.domain.Member;
import zunik.ohimarket.repository.dto.MemberSummeryDto;

@Data
public class ProfileResponseDto {
    private Member member;
    private MemberSummeryDto summary;
}
