package org.highfives.grid.user.command.aggregate;

import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@RedisHash(value = "jwtToken", timeToLive = 86400000)
@AllArgsConstructor
public class RefreshToken {

    @Id
    private int id;

    private String  refreshToken;

}
