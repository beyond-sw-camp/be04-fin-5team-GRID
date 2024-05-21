package org.highfives.grid.performance_review.command.service;

import org.highfives.grid.performance_review.command.aggregate.PerformanceReviewStatus;
import org.highfives.grid.performance_review.command.aggregate.entity.PerformanceReview;
import org.highfives.grid.performance_review.command.dto.PerformanceReviewDTO;
import org.highfives.grid.performance_review.command.dto.PerformanceReviewItemDTO;
import org.highfives.grid.performance_review.command.repository.PerformanceReviewRepository;
import org.highfives.grid.performance_review.query.dto.PerformanceReviewGoalDTO;
import org.highfives.grid.user.query.dto.LeaderInfoDTO;
import org.highfives.grid.user.query.service.UserService;
import org.highfives.grid.performance_review.query.service.PerformanceReviewGoalService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Service(value = "commandPerformanceReviewServiceImpl")
public class PerformanceReviewServiceImpl implements PerformanceReviewService{

    private final PerformanceReviewRepository performanceReviewRepository;
    private final ModelMapper modelMapper;

    private final UserService userService;

    private final PerformanceReviewGoalService performanceReviewGoalService;

    private final PerformanceReviewItemService performanceReviewItemService;

    @Autowired
    public PerformanceReviewServiceImpl(PerformanceReviewRepository performanceReviewRepository,
                                        ModelMapper modelMapper,
                                        UserService userService,
                                        PerformanceReviewGoalService performanceReviewGoalService,
                                        PerformanceReviewItemService performanceReviewItemService) {

        this.performanceReviewRepository = performanceReviewRepository;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.performanceReviewGoalService = performanceReviewGoalService;
        this.performanceReviewItemService = performanceReviewItemService;
    }

    @Override
    @Transactional
    public PerformanceReviewDTO addNewPerformanceReview(PerformanceReviewDTO performanceReviewDTO) {

        // 업적 평가 생성
        // 팀리더 정보 조회
        LeaderInfoDTO leader = userService.findLeaderInfo(performanceReviewDTO.getWriterId());

        // 현재 작성 시간
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String now = dateFormat.format(currentTime);

        // 평가 목표 조회
        PerformanceReviewGoalDTO performanceReviewGoalDTO = performanceReviewGoalService.findGoalByWriterIdAndYear(
                performanceReviewDTO.getWriterId(),
                performanceReviewDTO.getYear()
        );

        PerformanceReview performanceReview = new PerformanceReview(
                performanceReviewDTO.getType(),
                performanceReviewDTO.getYear(),
                performanceReviewDTO.getReviewName(),
                String.valueOf(PerformanceReviewStatus.IP),
                performanceReviewDTO.getWriterId(),
                now,
                leader.getTeamLeaderId(),
                performanceReviewGoalDTO.getId()
        );

        PerformanceReview addReview = performanceReviewRepository.save(performanceReview);
        System.out.println(addReview);

        // 업적 평가 항목 생성
        List<PerformanceReviewItemDTO> reviewItemList = performanceReviewItemService.addNewItems(
                                                                performanceReview.getGoalId(), performanceReview.getId());
        System.out.println(reviewItemList);


        return modelMapper.map(addReview, PerformanceReviewDTO.class);
    }
}
