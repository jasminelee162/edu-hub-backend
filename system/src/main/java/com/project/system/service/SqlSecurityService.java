
package com.project.system.service;

import com.project.common.domain.Result;

public interface SqlSecurityService {
    Result validate(String sql);
}