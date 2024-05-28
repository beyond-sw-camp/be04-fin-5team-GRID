package org.highfives.grid.performance_review.query.service;

import org.highfives.grid.performance_review.query.aggregate.PerformanceReviewGoal;
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
        System.out.println(employeeId);
        List<PerformanceReviewGoalDTO> findGoalList = performanceReviewGoalMapper.selectAllGoalByWriterId(employeeId);
        System.out.println(findGoalList);
        return findGoalList;
    }

    @Override
    public List<PerformanceReviewGoalDTO> findAllGoalByApproverId(int employeeId) {
        System.out.println(employeeId);
        List<PerformanceReviewGoalDTO> findGoalList = performanceReviewGoalMapper.selectAllGoalByApproverId(employeeId);
        System.out.println(findGoalList);
        return findGoalList;
    }

    @Override
    public DetailGoalDTO findDetailGoalById(int id) {
        System.out.println(id);
        DetailGoalDTO findDetailGoal = performanceReviewGoalMapper.selectDetailGoalById(id);


        List<GoalItemDTO> findGoalItemList = performanceReviewGoalMapper.selectAllGoalItemByGoalId(id);

        findDetailGoal.setGoalItemList(findGoalItemList);
        System.out.println(findDetailGoal);

        return findDetailGoal;
    }

    @Override
    public PerformanceReviewGoalDTO findGoalByWriterIdAndYear(int writerId, int year) {
        Map<String, Integer> intMap = new HashMap();
        intMap.put("writerId", writerId);
        intMap.put("year", year);

        System.out.println(intMap);
        PerformanceReviewGoalDTO performanceReviewGoalDTO = performanceReviewGoalMapper.selectGoalByWriterIdAndYear(intMap);

        return performanceReviewGoalDTO;
    }
}
