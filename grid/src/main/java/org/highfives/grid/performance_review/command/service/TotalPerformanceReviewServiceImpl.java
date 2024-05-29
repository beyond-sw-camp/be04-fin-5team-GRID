package org.highfives.grid.performance_review.command.service;

import org.highfives.grid.performance_review.command.aggregate.entity.ReviewGrade;
import org.highfives.grid.performance_review.command.aggregate.entity.TotalPerformanceReview;
import org.highfives.grid.performance_review.command.dto.PerformanceReviewDTO;
import org.highfives.grid.performance_review.command.dto.PerformanceReviewItemDTO;
import org.highfives.grid.performance_review.command.dto.TotalPerformanceReviewDTO;
import org.highfives.grid.performance_review.command.repository.ReviewGradeRepository;
import org.highfives.grid.performance_review.command.repository.TotalPerformanceReviewRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "commandTotalServiceImpl")
public class TotalPerformanceReviewServiceImpl implements TotalPerformanceReviewService{

    public final TotalPerformanceReviewRepository totalPerformanceReviewRepository;
    public final ModelMapper modelMapper;
    public final PerformanceReviewItemService performanceReviewItemService;
    public final ReviewGradeRepository reviewGradeRepository;


    @Autowired
    public TotalPerformanceReviewServiceImpl(TotalPerformanceReviewRepository totalPerformanceReviewRepository, ModelMapper modelMapper,
                                             PerformanceReviewItemService performanceReviewItemService,
                                             ReviewGradeRepository reviewGradeRepository) {
        this.totalPerformanceReviewRepository = totalPerformanceReviewRepository;
        this.modelMapper = modelMapper;
        this.performanceReviewItemService = performanceReviewItemService;
        this.reviewGradeRepository = reviewGradeRepository;
    }

    // 종합 평가 생성
    @Override
    public TotalPerformanceReviewDTO addTotalReview(List<PerformanceReviewDTO> performanceReviewDTOList) {

        // 업적 평가 중간, 연말 항목 조회
        PerformanceReviewDTO midReview = performanceReviewDTOList.get(0);
        PerformanceReviewDTO finReview = performanceReviewDTOList.get(1);

        List<PerformanceReviewItemDTO> midReviewItemList = performanceReviewItemService.findByReviewId(midReview.getId());
        List<PerformanceReviewItemDTO> finReviewItemList = performanceReviewItemService.findByReviewId(finReview.getId());

        System.out.println(midReviewItemList);
        System.out.println(finReviewItemList);

        // 업적 종합 평가 점수
        double totalScore = 0;
        for (int i = 0; i < midReviewItemList.size(); i++){
            double m = midReviewItemList.get(i).getSuperiorScore();
            double f = finReviewItemList.get(i).getSuperiorScore();

            totalScore += m * 0.3 + f * 0.7;
        }
        System.out.println(totalScore);

        // 등급 조회
        int totalId = calculateGrade(totalScore);
        System.out.println(totalId);


        // 종합 평가 생성
        TotalPerformanceReview totalPerformanceReview = new TotalPerformanceReview(
                finReview.getYear(),
                finReview.getReviewName(),
                totalId,
                totalScore,
                midReview.getId(),
                finReview.getId(),
                finReview.getWriterId(),
                finReview.getApproverId()
        );

        TotalPerformanceReview addTotalPerformanceReview = totalPerformanceReviewRepository.save(totalPerformanceReview);

        System.out.println(addTotalPerformanceReview);

        return modelMapper.map(addTotalPerformanceReview, TotalPerformanceReviewDTO.class);
    }

    // 점수에 대한 등급 계산
    private int calculateGrade(double totalScore) {
        // 디비에서 등급 정보 가져오기
        List<ReviewGrade> gradeList = reviewGradeRepository.findAll();

        // 등급 기준에 따라 등급 계산
        for (ReviewGrade grade : gradeList) {
            if (totalScore > grade.getScore()) {
                return grade.getId() - 1;
            }
        }

        // 디폴트 등급 설정
        return 5;
    }
}
