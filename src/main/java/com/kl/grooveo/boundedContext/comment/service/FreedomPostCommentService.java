package com.kl.grooveo.boundedContext.comment.service;

import com.kl.grooveo.boundedContext.comment.entity.FreedomPostComment;
import com.kl.grooveo.boundedContext.comment.repository.FreedomPostCommentRepository;
import com.kl.grooveo.boundedContext.community.entity.FreedomPost;
import com.kl.grooveo.boundedContext.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class FreedomPostCommentService {
    private final FreedomPostCommentRepository freedomPostCommentRepository;

    public FreedomPostComment create(FreedomPost freedomPost, String content, Member author) {
        FreedomPostComment freedomPostComment = new FreedomPostComment();
        freedomPostComment.setContent(content);
        freedomPostComment.setCreateDate(LocalDateTime.now());
        freedomPostComment.setFreedomPost(freedomPost);
        freedomPostComment.setAuthor(author);
        this.freedomPostCommentRepository.save(freedomPostComment);

        return freedomPostComment;
    }
}
