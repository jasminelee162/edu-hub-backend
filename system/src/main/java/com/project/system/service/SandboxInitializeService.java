package com.project.system.service;

import com.project.common.domain.Result;

public interface SandboxInitializeService {
    Result initializeSandbox(String questionId, String userStamp);
}