package zunik.ohimarket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import zunik.ohimarket.repository.PostCategoryRepository;

@EnableJpaAuditing
@SpringBootApplication
public class OhimarketApplication {

	public static void main(String[] args) {
		SpringApplication.run(OhimarketApplication.class, args);
	}

	@Bean
	public DataInit dataInit(PostCategoryRepository postCategoryRepository) {
		return new DataInit(postCategoryRepository);
	}

}
