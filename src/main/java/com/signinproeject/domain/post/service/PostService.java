package com.signinproeject.domain.post.service;

import com.signinproeject.domain.post.controller.dto.request.PostCreateRequest;
import com.signinproeject.domain.post.controller.dto.response.PostListResponse;
import com.signinproeject.domain.post.controller.dto.response.PostResponse;
import com.signinproeject.domain.post.repository.PostRepository;
import com.signinproeject.domain.post.controller.dto.request.PostUpdateRequest;
import com.signinproeject.domain.user.entity.entity.Member;
import com.signinproeject.domain.post.entity.Post;
import com.signinproeject.domain.auth.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Transactional
    public void createPost(PostCreateRequest postCreateRequest){

        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        Member member = memberRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("찾을 수 없는 엔티티 입니다."));

        Post post = new Post(postCreateRequest.getTitle(),postCreateRequest.getDescription(),member);
        postRepository.save(post);
    }

    @Transactional
    public void updatePost(Long postId, Long memberId, PostUpdateRequest request){
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new EntityNotFoundException("찾을 수 없는 엔티티 입니다."));
        if(!Objects.equals(post.getMember().getId(), memberId)){
            throw new IllegalStateException();
        }


        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalStateException(""));

        postRepository.save(
            new Post(
                postId,
                request.getTitle(),
                request.getDescription(),
                member
            )
        );
    }

    @Transactional
    public void deletePost(Long postId){
        postRepository.deleteById(postId);
    }

    public PostListResponse findAllPost() {

        List<PostResponse> postResponses = postRepository.findAll().stream()
                .map(it -> PostResponse.builder()
                        .description(it.getDescription())
                        .title(it.getTitle())
                        .memberId(it.getMember().getId())
                        .build()).toList();

        return PostListResponse.builder()
                .postCreateRequests(postResponses)
                .build();
    }



}