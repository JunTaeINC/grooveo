package com.kl.grooveo.boundedContext.comment.controller;

import com.kl.grooveo.base.rq.Rq;
import com.kl.grooveo.boundedContext.comment.entity.FreedomPostComment;
import com.kl.grooveo.boundedContext.comment.service.FreedomPostCommentService;
import com.kl.grooveo.boundedContext.community.entity.FreedomPost;
import com.kl.grooveo.boundedContext.community.service.FreedomPostService;
import com.kl.grooveo.boundedContext.form.CommentForm;
import com.kl.grooveo.boundedContext.member.entity.Member;
import com.kl.grooveo.boundedContext.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/comment")
@RequiredArgsConstructor
@Controller
public class FreedomPostCommentController {
    private final FreedomPostService freedomPostService;
    private final FreedomPostCommentService freedomPostCommentService;
    private final MemberService memberService;
    private final Rq rq;

    @PostMapping("/create/{id}")
    public String createAnswer(Model model, @PathVariable("id") Long id,
                               @Valid CommentForm commentForm, BindingResult bindingResult) {

        FreedomPost freedomPost = freedomPostService.getFreedomPost(id);
        Member member = memberService.findByUsername(rq.getMember().getUsername()).orElseThrow();

        if (bindingResult.hasErrors()) {
            model.addAttribute("freedomPost", freedomPost);
            return "usr/community/freedomPost/detail";
        }

        FreedomPostComment freedomPostComment = freedomPostCommentService.create(freedomPost, commentForm.getContent(), member);
        return String.format("redirect:/community/freedomPost/detail/%s#comment_%s",
                freedomPostComment.getFreedomPost().getId(), freedomPostComment.getId());
    }
}
