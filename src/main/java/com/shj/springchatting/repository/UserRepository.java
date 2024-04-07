package com.shj.springchatting.repository;

import com.shj.springchatting.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
