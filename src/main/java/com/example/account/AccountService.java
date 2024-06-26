package com.example.account;

import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService {

	private final JdbcTemplate jdbcTemplate;

	public AccountService(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public Account findByUsername(String username) {
		return this.jdbcTemplate.queryForObject(
				"SELECT username, password, two_factor_secret, two_factor_enabled FROM account WHERE username = ?",
				new DataClassRowMapper<>(Account.class), username);
	}

	@Transactional
	public int insert(Account account) {
		return this.jdbcTemplate.update(
				"INSERT INTO account(username, password, two_factor_secret, two_factor_enabled) VALUES (?, ?, ?, ?)",
				account.username(), account.password(), account.twoFactorSecret(), account.twoFactorEnabled());
	}

	@Transactional
	public Account enable2Fa(Account account) {
		this.jdbcTemplate.update("UPDATE account SET two_factor_enabled = true WHERE username = ?", account.username());
		return account.enable2Fa();
	}

}