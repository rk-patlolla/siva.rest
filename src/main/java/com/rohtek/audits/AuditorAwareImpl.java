package com.rohtek.audits;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

public class AuditorAwareImpl implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {

		return Optional
				.of(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
	}

}
