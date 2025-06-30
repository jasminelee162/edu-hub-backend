package com.ape.apesystem.service;

import com.ape.apecommon.domain.Result;

public interface SandboxInitializeService {
    Result initializeSandbox(String questionId, String userStamp);
}