package com.bajie.auth.client;

import com.bajie.api.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "bj-user")
public interface UserClient extends UserApi {

}
