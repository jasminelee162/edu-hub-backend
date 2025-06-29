
package com.ape.apesystem.service;

import com.ape.apecommon.domain.Result;

public interface SqlSecurityService {
    Result validate(String sql);
}