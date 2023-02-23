package zunik.ohimarket;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class PasswordEncoderTest {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("패스워드 테스트")
    void passwordEncode() {
        // given
        String rawPassword = "123";

        // when
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // then
        assertThat(passwordEncoder.matches(rawPassword, encodedPassword)).isEqualTo(true);
    }
}
