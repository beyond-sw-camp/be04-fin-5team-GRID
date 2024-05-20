package org.highfives.grid.performance_review.command.service;

import org.highfives.grid.performance_review.command.aggregate.PerformanceReviewStatus;
import org.highfives.grid.performance_review.command.aggregate.entity.PerformanceReview;
import org.highfives.grid.performance_review.command.dto.PerformanceReviewDTO;
import org.highfives.grid.performance_review.command.repository.PerformanceReviewRepository;
import org.highfives.grid.user.query.dto.LeaderInfoDTO;
import org.highfives.grid.user.query.service.UserService;
import org.highfives.grid.user.query.service.UserServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Service(value = "commandPerformanceReviewServiceImpl")
public class PerformanceReviewServiceImpl implements PerformanceReviewService{

    private final PerformanceReviewRepository performanceReviewRepository;
    private final ModelMapper modelMapper;

    private final UserService userService;

    @Autowired
    public PerformanceReviewServiceImpl(PerformanceReviewRepository performanceReviewRepository, ModelMapper modelMapper, UserService userService) {
        this.performanceReviewRepository = performanceReviewRepository;
        this.modelMapper = modelMapper;
        this.userService = userService;
    }



    @Override
    @Transactional
    public PerformanceReviewDTO addNewPerformanceReview(PerformanceReviewDTO performanceReviewDTO) {

        // 팀리더 정보 조회
        LeaderInfoDTO leader = userService.findLeaderInfo(performanceReviewDTO.getWriterId());

        // 현재 작성 시간
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String now = dateFormat.format(currentTime);

        PerformanceReview performanceReview = new PerformanceReview(
                performanceReviewDTO.getType(),
                performanceReviewDTO.getYear(),
                performanceReviewDTO.getReviewName(),
                String.valueOf(PerformanceReviewStatus.IP),
                performanceReviewDTO.getWriterId(),
                now,
                leader.getTeamLeaderId()
        );

        PerformanceReview addReview = performanceReviewRepository.save(performanceReview);

        System.out.println(addReview);
        return modelMapper.map(addReview, PerformanceReviewDTO.class);
    }
}
