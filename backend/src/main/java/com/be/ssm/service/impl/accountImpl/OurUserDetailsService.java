package com.be.ssm.service.impl.accountImpl;

import com.be.ssm.entities.account.Accounts;
import com.be.ssm.exceptions.CustomException;
import com.be.ssm.exceptions.Error;
import com.be.ssm.repository.account.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OurUserDetailsService implements UserDetailsService {
    private final AccountRepository accountRepository;

    /**
     * Loads the user details based on the provided username for authentication purposes.
     * This method is used by Spring Security during the authentication process.
     *
     * @param username the username identifying the user whose data is required
     * @return a {@link UserDetails} object containing user's authentication and authorization information
     * @throws UsernameNotFoundException if no user is found with the given username
     */
    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) {
        log.info("Find user by username: {}", username);

        return accountRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("Account not found with username: " + username));
    }

    public Accounts getAccountAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new CustomException(Error.UNAUTHORIZED);
        }

        Accounts account = (Accounts) authentication.getPrincipal();

        return account;
    }


}
