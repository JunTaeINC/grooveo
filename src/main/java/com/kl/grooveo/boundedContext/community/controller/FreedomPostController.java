package com.kl.grooveo.boundedContext.community.controller;

import com.kl.grooveo.base.rq.Rq;
import com.kl.grooveo.boundedContext.community.entity.FreedomPost;
import com.kl.grooveo.boundedContext.community.service.FreedomPostService;
import com.kl.grooveo.boundedContext.form.CommentForm;
import com.kl.grooveo.boundedContext.form.FreedomPostForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/community/freedomPost")
@RequiredArgsConstructor
@Controller
public class FreedomPostController {
    private final FreedomPostService freedomPostService;
    private final Rq rq;
    static int boardTypeCode;

    @GetMapping("/{boardType}/list")
    public String showList(Model model, @PathVariable("boardType") Integer boardType, CategoryForm categoryForm) {
        List<FreedomPost> freedomPostList = this.freedomPostService.getList(boardType, categoryForm.options);
        model.addAttribute("freedomPostList", freedomPostList);
        model.addAttribute("boardType", boardType);
        boardTypeCode = boardType;

        return "usr/community/freedomPost/list";
    }

    @Setter
    public static class CategoryForm {
        private String options = "";
    }

    @GetMapping(value = "/detail/{id}")
    public String showMoreDetail(Model model, @PathVariable("id") Long id, CommentForm commentForm) {
        FreedomPost freedomPost = this.freedomPostService.getFreedomPost(id);
        model.addAttribute("freedomPost", freedomPost);
        return "usr/community/freedomPost/detail";
    }

    @GetMapping("/create")
    public String freedomPostCreate(Model model, FreedomPostForm freedomPostForm) {
        return "usr/community/freedomPost/form";
    }

    @PostMapping("/create")
    public String freedomPostCreate(Model model, @Valid FreedomPostForm freedomPostForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "usr/community/freedomPost/form";
        }

        this.freedomPostService.create(boardTypeCode, freedomPostForm.getTitle(), freedomPostForm.getCategory(), freedomPostForm.getContent(), rq.getMember());
        return String.format("redirect:/community/freedomPost/%d/list", boardTypeCode);
    }

    @DeleteMapping("/{id}")
    public String freedomPostDelete(@PathVariable("id") Long id) {
        FreedomPost freedomPost = this.freedomPostService.getFreedomPost(id);
//        if (!freedomPost.getAuthor().getUsername().equals(principal.getName())) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
//        }
        this.freedomPostService.delete(freedomPost);
        return String.format("redirect:/community/freedomPost/%d/list", boardTypeCode);
    }

    @GetMapping("/modify/{id}")
    public String freedomPostModify(FreedomPostForm freedomPostForm, @PathVariable("id") Long id) {
        FreedomPost freedomPost = this.freedomPostService.getFreedomPost(id);
//        if(!freedomPost.getAuthor().getUsername().equals(principal.getName())) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
//        }
        freedomPostForm.setTitle(freedomPost.getTitle());
        freedomPostForm.setCategory(freedomPost.getCategory());
        freedomPostForm.setContent(freedomPost.getContent());
        return "usr/community/freedomPost/form";
    }

    @PostMapping("/modify/{id}")
    public String freedomPostModify(@Valid FreedomPostForm freedomPostForm, BindingResult bindingResult,
                                    @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            return "usr/community/freedomPost/form";
        }
        FreedomPost freedomPost = this.freedomPostService.getFreedomPost(id);
//        if (!freedomPost.getAuthor().getUsername().equals(principal.getName())) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
//        }
        this.freedomPostService.modify(freedomPost, freedomPostForm.getTitle(), freedomPostForm.getCategory(), freedomPostForm.getContent());
        return String.format("redirect:/community/freedomPost/detail/%s", id);
    }
}