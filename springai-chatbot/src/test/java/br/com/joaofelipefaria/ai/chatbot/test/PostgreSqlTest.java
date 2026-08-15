package br.com.joaofelipefaria.ai.chatbot.test;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
public class PostgreSqlTest {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Test
	void shouldConnectToPostgre() {
		Integer result = jdbcTemplate.queryForObject("Select 1", Integer.class);
		assertThat(result).isEqualTo(1);
	}
}
