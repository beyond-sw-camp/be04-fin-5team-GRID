package org.highfives.grid.performance_review.query.service;
;
import org.highfives.grid.performance_review.query.dto.DetailGoalDTO;
import org.highfives.grid.performance_review.query.dto.GoalItemDTO;
import org.highfives.grid.performance_review.query.dto.PerformanceReviewGoalDTO;
import org.highfives.grid.performance_review.query.repository.PerformanceReviewGoalMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(value = "queryPerformanceReviewGoalServiceImpl")
public class PerformanceReviewGoalServiceImpl implements PerformanceReviewGoalService{

    private final PerformanceReviewGoalMapper performanceReviewGoalMapper;
    private final ModelMapper modelMapper;

    @Autowired
    public PerformanceReviewGoalServiceImpl(PerformanceReviewGoalMapper performanceReviewGoalMapper, ModelMapper modelMapper) {
        this.performanceReviewGoalMapper = performanceReviewGoalMapper;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<PerformanceReviewGoalDTO> findAllGoalByWriterId(int employeeId) {
        List<PerformanceReviewGoalDTO> findGoalList = performanceReviewGoalMapper.selectAllGoalByWriterId(employeeId);

        return findGoalList;
    }

    @Override
    public List<PerformanceReviewGoalDTO> findAllGoalByApproverId(int employeeId) {
        List<PerformanceReviewGoalDTO> findGoalList = performanceReviewGoalMapper.selectAllGoalByApproverId(employeeId);

        return findGoalList;
    }

    @Override
    public DetailGoalDTO findDetailGoalById(int id) {
        DetailGoalDTO findDetailGoal = performanceReviewGoalMapper.selectDetailGoalById(id);


        List<GoalItemDTO> findGoalItemList = performanceReviewGoalMapper.selectAllGoalItemByGoalId(id);
        findDetailGoal.setGoalItemList(findGoalItemList);

        return findDetailGoal;
    }

    @Override
    public PerformanceReviewGoalDTO findGoalByWriterIdAndYear(int writerId, int year) {
        Map<String, Integer> intMap = new HashMap();
        intMap.put("writerId", writerId);
        intMap.put("year", year);

        PerformanceReviewGoalDTO performanceReviewGoalDTO = performanceReviewGoalMapper.selectGoalByWriterIdAndYear(intMap);


        return performanceReviewGoalDTO;
    }
}
