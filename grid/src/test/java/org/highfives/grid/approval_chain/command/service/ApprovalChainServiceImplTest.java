package org.highfives.grid.approval_chain.command.service;

import jakarta.transaction.Transactional;
import org.highfives.grid.approval_chain.command.aggregate.ChainStatus;
import org.highfives.grid.approval_chain.command.vo.ChainStatusVO;
import org.highfives.grid.approval_chain.command.vo.CommentVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

@SpringBootTest
class ApprovalChainServiceImplTest {

    @Autowired
    private ApprovalChainServiceImpl approvalChainService;

    static Stream<Arguments> comment() {
        return Stream.of(
                Arguments.of(
                        new CommentVO("이러한 사유로 승인합니다.", 1),
                        new CommentVO("이러한 사유로 반려합니다.", 1))
        );
    }

    static Stream<Arguments> chainStatus() {
        return Stream.of(
                Arguments.of(
                    new ChainStatusVO(1, 1, 3, ChainStatus.A),
                    new ChainStatusVO(1, 2, 3, ChainStatus.A)
                )
        );
    }

    @DisplayName("출장 결재 댓글 생성")
    @ParameterizedTest
    @MethodSource("comment")
    @Transactional
    void addBTApprovalComment(CommentVO comment) {

        Assertions.assertDoesNotThrow(
                () -> approvalChainService.addBTApprovalComment(comment)
        );
    }

    @DisplayName("시간 외 근무 결재 댓글 생성")
    @ParameterizedTest
    @MethodSource("comment")
    @Transactional
    void addOApprovalComment(CommentVO comment) {

        Assertions.assertDoesNotThrow(
                () -> approvalChainService.addOApprovalComment(comment)
        );
    }

    @DisplayName("단축 근무 결재 댓글 생성")
    @ParameterizedTest
    @MethodSource("comment")
    @Transactional
    void addRWApprovalComment(CommentVO comment) {

        Assertions.assertDoesNotThrow(
                () -> approvalChainService.addRWApprovalComment(comment)
        );
    }

    @DisplayName("휴가 결재 댓글 생성")
    @ParameterizedTest
    @MethodSource("comment")
    @Transactional
    void addVApprovalComment(CommentVO comment) {

        Assertions.assertDoesNotThrow(
                () -> approvalChainService.addVApprovalComment(comment)
        );
    }

    @Test
    void addBTApprovalChain() {
    }

    @Test
    void addOApprovalChain() {
    }

    @Test
    void addRWApprovalChain() {
    }

    @Test
    void addVApprovalChain() {
    }

    @Test
    void testAddBTApprovalComment() {
    }

    @Test
    void testAddOApprovalComment() {
    }

    @Test
    void testAddRWApprovalComment() {
    }

    @Test
    void testAddVApprovalComment() {
    }

    @Test
    void modifyChainStatus() {
    }

    @DisplayName("출장 결재 승인/반려")
    @ParameterizedTest
    @MethodSource("chainStatus")
    @Transactional
    void modifyBTChainStatus(ChainStatusVO chainStatus) {

        Assertions.assertDoesNotThrow(
                () -> approvalChainService.modifyBTChainStatus(chainStatus)
        );
    }
}