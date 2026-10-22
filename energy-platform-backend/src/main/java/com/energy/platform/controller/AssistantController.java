package com.energy.platform.controller;

import com.energy.platform.common.Result;
import com.energy.platform.dto.AssistantRequest;
import com.energy.platform.service.AssistantService;
import com.energy.platform.vo.AssistantResponse;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/assistant")
public class AssistantController {
    private final AssistantService assistantService;

    public AssistantController(AssistantService assistantService) {
        this.assistantService = assistantService;
    }

    @PostMapping("/ask")
    @PreAuthorize("hasAuthority('assistant:use')")
    public Result<AssistantResponse> ask(@Valid @RequestBody AssistantRequest request) {
        return Result.ok(assistantService.ask(request.getQuestion()));
    }
}
