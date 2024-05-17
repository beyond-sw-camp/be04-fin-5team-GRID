package org.highfives.grid.user.command.aggregate;

import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

@Getter
@AllArgsConstructor
@RedisHash(value = "refreshToken", timeToLive = 86400000)
public class RefreshToken {

    @Id
    private String  refreshToken;
    private int userId;


}
