package com.kl.grooveo.boundedContext.community.controller;

import com.kl.grooveo.boundedContext.community.entity.FreedomPost;
import com.kl.grooveo.boundedContext.form.FreedomPostForm;
import com.kl.grooveo.boundedContext.community.service.FreedomPostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequestMapping("/community/freedomPost")
@RequiredArgsConstructor
@Controller
public class FreedomPostController {
    private final FreedomPostService freedomPostService;

    @GetMapping("/list")
    public String showList(Model model) {
        List<FreedomPost> freedomPostList = this.freedomPostService.getList();
        model.addAttribute("freedomPostList", freedomPostList);
        return "usr/community/freedomPost/list";
    }

    @GetMapping(value = "/detail/{id}")
    public String showMoreDetail(Model model, @PathVariable("id") Long id) throws Exception {
        FreedomPost freedomPost = this.freedomPostService.getMoreInformation(id);
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
        String author = "test_user1";

        this.freedomPostService.create(freedomPostForm.getTitle(), freedomPostForm.getCategory(), freedomPostForm.getContent(), author);
        return "redirect:/community/freedomPost/list";
    }

    @DeleteMapping("/{id}")
    public String freedomPostDelete(Principal principal, @PathVariable("id") Long id) throws Exception {
        FreedomPost freedomPost = this.freedomPostService.getFreedomPost(id);
//        if (!freedomPost.getAuthor().getUsername().equals(principal.getName())) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
//        }
        this.freedomPostService.delete(freedomPost);
        return "redirect:/community/freedomPost/list";
    }

    @GetMapping("/modify/{id}")
    public String freedomPostModify(FreedomPostForm freedomPostForm, @PathVariable("id") Long id, Principal principal) throws Exception {
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
                                    Principal principal, @PathVariable("id") Long id) throws Exception {
        if (bindingResult.hasErrors()) {
            return "usr/community/freedomPost/form";
        }
        FreedomPost freedomPost = this.freedomPostService.getFreedomPost(id);
//        if (!freedomPost.getAuthor().getUsername().equals(principal.getName())) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
//        }
        String user = "test_user1";
        this.freedomPostService.modify(freedomPost, freedomPostForm.getTitle(), freedomPostForm.getCategory(), freedomPostForm.getContent(), user);
        return String.format("redirect:/community/freedomPost/detail/%s", id);
    }
}
