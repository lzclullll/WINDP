package com.energy.platform.service;

import com.energy.platform.vo.AssistantResponse;

public interface AssistantService {
    AssistantResponse ask(String question);
}
