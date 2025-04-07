package com.edunext.exam_service.Interface;

import com.edunext.exam_service.Model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "EDUNEXT", url = "http://localhost:8050")
public interface UserClient {
    @GetMapping("/api/v1/users/profile/{id}")
    User getUserById(@PathVariable Integer id,
                     @RequestHeader("Authorization") String token);
}
