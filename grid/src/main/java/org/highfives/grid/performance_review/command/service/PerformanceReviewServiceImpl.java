package org.highfives.grid.performance_review.command.service;

import org.highfives.grid.performance_review.command.aggregate.PerformanceReviewStatus;
import org.highfives.grid.performance_review.command.aggregate.entity.PerformanceReview;
import org.highfives.grid.performance_review.command.dto.ModifyPerformanceReviewDTO;
import org.highfives.grid.performance_review.command.dto.PerformanceReviewDTO;
import org.highfives.grid.performance_review.command.dto.PerformanceReviewItemDTO;
import org.highfives.grid.performance_review.command.dto.TotalPerformanceReviewDTO;
import org.highfives.grid.performance_review.command.repository.PerformanceReviewRepository;
import org.highfives.grid.performance_review.command.vo.RequestPerformanceReviewVO;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service(value = "commandPerformanceReviewServiceImpl")
public class PerformanceReviewServiceImpl implements PerformanceReviewService{

    private final PerformanceReviewRepository performanceReviewRepository;
    private final ModelMapper modelMapper;

    private final UserService userService;

    private final PerformanceReviewGoalService performanceReviewGoalService;

    private final PerformanceReviewItemService performanceReviewItemService;
    private final TotalPerformanceReviewService totalPerformanceReviewService;

    @Autowired
    public PerformanceReviewServiceImpl(PerformanceReviewRepository performanceReviewRepository,
                                        ModelMapper modelMapper,
                                        UserService userService,
                                        PerformanceReviewGoalService performanceReviewGoalService,
                                        PerformanceReviewItemService performanceReviewItemService,
                                        TotalPerformanceReviewService totalPerformanceReviewService) {

        this.performanceReviewRepository = performanceReviewRepository;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.performanceReviewGoalService = performanceReviewGoalService;
        this.performanceReviewItemService = performanceReviewItemService;
        this.totalPerformanceReviewService =totalPerformanceReviewService;
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

    // 업적 평가 저장(작성자)
    @Override
    @Transactional
    public ModifyPerformanceReviewDTO modifyPerformanceReviewStatusInProgress(RequestPerformanceReviewVO requestPerformanceReviewVO) {
        // 변경된 평가 항목 수정
        List<PerformanceReviewItemDTO> performanceReviewItemDTOList =
                requestPerformanceReviewVO.getPerformanceReviewItemList();

        List<PerformanceReviewItemDTO> modifyItemList = new ArrayList<>();
        for (PerformanceReviewItemDTO performanceReviewItemDTO : performanceReviewItemDTOList) {
            PerformanceReviewItemDTO modifyItem =  performanceReviewItemService.modifyItem(performanceReviewItemDTO);
            modifyItemList.add(modifyItem);
        }

        // 평가 상태 수정
        PerformanceReview performanceReview = performanceReviewRepository.findById(requestPerformanceReviewVO.getReviewId())
                .orElseThrow(IllegalArgumentException::new);

        // 현재 작성 시간
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String now = dateFormat.format(currentTime);

        if(performanceReview != null){

            performanceReview.setApprovalStatus(String.valueOf(PerformanceReviewStatus.IP));
            performanceReview.setWriteTime(now);
        }

        ModifyPerformanceReviewDTO modifyPerformanceReviewDTO = new ModifyPerformanceReviewDTO(
                modelMapper.map(performanceReview, PerformanceReviewDTO.class),
                modifyItemList
        );

        return modifyPerformanceReviewDTO;
    }

    // 업적 평가 상신(작성자)
    @Override
    @Transactional
    public ModifyPerformanceReviewDTO modifyPerformanceReviewStatusSubmit(RequestPerformanceReviewVO requestPerformanceReviewVO) {
        // 변경된 평가 항목 수정
        List<PerformanceReviewItemDTO> performanceReviewItemDTOList =
                requestPerformanceReviewVO.getPerformanceReviewItemList();

        List<PerformanceReviewItemDTO> modifyItemList = new ArrayList<>();
        for (PerformanceReviewItemDTO performanceReviewItemDTO : performanceReviewItemDTOList) {
            PerformanceReviewItemDTO modifyItem =  performanceReviewItemService.modifyItem(performanceReviewItemDTO);
            modifyItemList.add(modifyItem);
        }

        // 평가 상태 수정
        PerformanceReview performanceReview = performanceReviewRepository.findById(requestPerformanceReviewVO.getReviewId())
                .orElseThrow(IllegalArgumentException::new);

        // 현재 작성 시간
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String now = dateFormat.format(currentTime);

        if(performanceReview != null){

            performanceReview.setApprovalStatus(String.valueOf(PerformanceReviewStatus.S));
            performanceReview.setWriteTime(now);
        }

        ModifyPerformanceReviewDTO modifyPerformanceReviewDTO = new ModifyPerformanceReviewDTO(
                modelMapper.map(performanceReview, PerformanceReviewDTO.class),
                modifyItemList
        );

        return modifyPerformanceReviewDTO;
    }

    // 업적 평가 확인중(결재자)
    @Override
    @Transactional
    public ModifyPerformanceReviewDTO modifyPerformanceReviewStatusRead(RequestPerformanceReviewVO requestPerformanceReviewVO) {
        // 변경된 평가 항목 수정
        List<PerformanceReviewItemDTO> performanceReviewItemDTOList =
                requestPerformanceReviewVO.getPerformanceReviewItemList();

        List<PerformanceReviewItemDTO> modifyItemList = new ArrayList<>();
        for (PerformanceReviewItemDTO performanceReviewItemDTO : performanceReviewItemDTOList) {
            PerformanceReviewItemDTO modifyItem =  performanceReviewItemService.modifyItem(performanceReviewItemDTO);
            modifyItemList.add(modifyItem);
        }

        // 평가 상태 수정
        PerformanceReview performanceReview = performanceReviewRepository.findById(requestPerformanceReviewVO.getReviewId())
                .orElseThrow(IllegalArgumentException::new);

        // 현재 작성 시간
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String now = dateFormat.format(currentTime);

        if(performanceReview != null){

            performanceReview.setApprovalStatus(String.valueOf(PerformanceReviewStatus.R));
            performanceReview.setApprovalTime(now);
        }

        ModifyPerformanceReviewDTO modifyPerformanceReviewDTO = new ModifyPerformanceReviewDTO(
                modelMapper.map(performanceReview, PerformanceReviewDTO.class),
                modifyItemList
        );

        return modifyPerformanceReviewDTO;
    }

    // 업적 평가 완료(결재자)
    @Override
    @Transactional
    public ModifyPerformanceReviewDTO modifyPerformanceReviewStatusComplete(RequestPerformanceReviewVO requestPerformanceReviewVO) {
        // 변경된 평가 항목 수정
        List<PerformanceReviewItemDTO> performanceReviewItemDTOList =
                requestPerformanceReviewVO.getPerformanceReviewItemList();

        List<PerformanceReviewItemDTO> modifyItemList = new ArrayList<>();
        for (PerformanceReviewItemDTO performanceReviewItemDTO : performanceReviewItemDTOList) {
            PerformanceReviewItemDTO modifyItem =  performanceReviewItemService.modifyItem(performanceReviewItemDTO);
            modifyItemList.add(modifyItem);
        }

        // 평가 상태 수정
        PerformanceReview performanceReview = performanceReviewRepository.findById(requestPerformanceReviewVO.getReviewId())
                .orElseThrow(IllegalArgumentException::new);

        // 현재 작성 시간
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String now = dateFormat.format(currentTime);

        if(performanceReview != null){

            performanceReview.setApprovalStatus(String.valueOf(PerformanceReviewStatus.C));
            performanceReview.setApprovalTime(now);
        }

        ModifyPerformanceReviewDTO modifyPerformanceReviewDTO = new ModifyPerformanceReviewDTO(
                modelMapper.map(performanceReview, PerformanceReviewDTO.class),
                modifyItemList
        );

        return modifyPerformanceReviewDTO;
    }

    // 업적 평가 확정(결재자)
    @Override
    @Transactional
    public ModifyPerformanceReviewDTO modifyPerformanceReviewStatusValid(RequestPerformanceReviewVO requestPerformanceReviewVO) {
        // 변경된 평가 항목 수정
        List<PerformanceReviewItemDTO> performanceReviewItemDTOList =
                requestPerformanceReviewVO.getPerformanceReviewItemList();

        List<PerformanceReviewItemDTO> modifyItemList = new ArrayList<>();
        for (PerformanceReviewItemDTO performanceReviewItemDTO : performanceReviewItemDTOList) {
            PerformanceReviewItemDTO modifyItem =  performanceReviewItemService.modifyItem(performanceReviewItemDTO);
            modifyItemList.add(modifyItem);
        }

        // 평가 상태 수정
        PerformanceReview performanceReview = performanceReviewRepository.findById(requestPerformanceReviewVO.getReviewId())
                .orElseThrow(IllegalArgumentException::new);

        // 현재 작성 시간
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String now = dateFormat.format(currentTime);

        if(performanceReview != null){

            performanceReview.setApprovalStatus(String.valueOf(PerformanceReviewStatus.V));
            performanceReview.setApprovalTime(now);
        }

        ModifyPerformanceReviewDTO modifyPerformanceReviewDTO = new ModifyPerformanceReviewDTO(
                modelMapper.map(performanceReview, PerformanceReviewDTO.class),
                modifyItemList
        );

        // 연말 평가를 확정하면 종합 평가 생성
        if(performanceReview.getType().equals("F")) {
            System.out.println("종합 생성");
            System.out.println(performanceReview.getGoalId());
            // GoalId로 중간, 연말 평가 조회
            List<PerformanceReview> performanceReviewList = performanceReviewRepository.findByGoalId(performanceReview.getGoalId());

            List<PerformanceReviewDTO> performanceReviewDTOList = performanceReviewList.stream()
                    .map(mPerformanceReview -> modelMapper.map(mPerformanceReview, PerformanceReviewDTO.class))
                    .collect(Collectors.toList());

            // 종합 평가 생성
            totalPerformanceReviewService.addTotalReview(performanceReviewDTOList);
        }

        return modifyPerformanceReviewDTO;
    }
}
