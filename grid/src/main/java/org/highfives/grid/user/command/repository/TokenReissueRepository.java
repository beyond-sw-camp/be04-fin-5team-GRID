package org.highfives.grid.user.command.repository;

import org.highfives.grid.user.command.aggregate.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface TokenReissueRepository extends CrudRepository<RefreshToken, String> {

    void deleteByRefreshToken(String token);

    Boolean existsByRefreshToken(String refreshToken);
}
