package com.bajie.search.client;


import com.bajie.item.api.SpecificationApi;
import org.springframework.cloud.openfeign.FeignClient;


@FeignClient(value = "bj-item")
public interface SpecificationClient extends SpecificationApi {
}
