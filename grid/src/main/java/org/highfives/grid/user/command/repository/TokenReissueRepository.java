package org.highfives.grid.user.command.repository;

import org.highfives.grid.user.command.aggregate.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TokenReissueRepository extends CrudRepository<RefreshToken, Integer> {

    @Override
    Optional<RefreshToken> findById(Integer integer);

    void deleteById(int s);

}

