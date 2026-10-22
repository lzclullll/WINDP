package com.energy.platform.vo;

import java.util.List;

public record AssistantResponse(
    String answer,
    List<String> sources
) {}
