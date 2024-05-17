package org.highfives.grid.user.command.repository;

import org.highfives.grid.user.command.aggregate.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

    void deleteByRefreshToken(String token);
}
