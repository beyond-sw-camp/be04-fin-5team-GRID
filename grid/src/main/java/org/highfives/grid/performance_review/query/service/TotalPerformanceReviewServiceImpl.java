package org.highfives.grid.performance_review.query.service;

import org.highfives.grid.performance_review.query.dto.TotalPerformanceReviewDTO;
import org.highfives.grid.performance_review.query.repository.TotalPerformanceReviewMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "queryTotalPerformanceReviewServiceImpl")
public class TotalPerformanceReviewServiceImpl implements TotalPerformanceReviewService {

    private final TotalPerformanceReviewMapper totalPerformanceReviewMapper;

    @Autowired
    public TotalPerformanceReviewServiceImpl(TotalPerformanceReviewMapper totalPerformanceReviewMapper) {
        this.totalPerformanceReviewMapper = totalPerformanceReviewMapper;
    }

    @Override
    public List<TotalPerformanceReviewDTO> findTotalByRevieweeId(int employeeId) {
        List<TotalPerformanceReviewDTO> totalPerformanceReviewDTOList = totalPerformanceReviewMapper.selectTotalByRevieweeId(employeeId);

        return totalPerformanceReviewDTOList;
    }

    @Override
    public List<TotalPerformanceReviewDTO> findTotalByReviewerId(int employeeId) {
        List<TotalPerformanceReviewDTO> totalPerformanceReviewDTOList = totalPerformanceReviewMapper.selectTotalByReviewerId(employeeId);

        return totalPerformanceReviewDTOList;
    }

    @Override
    public List<TotalPerformanceReviewDTO> findAllTotal() {
        List<TotalPerformanceReviewDTO> totalPerformanceReviewDTOList = totalPerformanceReviewMapper.selectAllTotal();

        return totalPerformanceReviewDTOList;
    }

    @Override
    public TotalPerformanceReviewDTO findTotalById(int id) {
        TotalPerformanceReviewDTO totalPerformanceReviewDTO = totalPerformanceReviewMapper.selectTotalById(id);

        return totalPerformanceReviewDTO;
    }
}
