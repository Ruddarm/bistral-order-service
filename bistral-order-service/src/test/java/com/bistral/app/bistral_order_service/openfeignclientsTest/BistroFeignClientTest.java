package com.bistral.app.bistral_order_service.openfeignclientsTest;

import com.bistral.app.bistral_order_service.dtos.BranchResponse;
import com.bistral.app.bistral_order_service.openfeignclients.BistroFeignClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import java.util.UUID;

@Profile("test")
@SpringBootTest
public class BistroFeignClientTest {
    @Autowired
    BistroFeignClient bistroFeignClient;

    @Test
    void contextLoads() {
    }

    @Test
    public void testGetBranch() {
        BranchResponse branchResponse = bistroFeignClient.getBranch(UUID.fromString("3c19869c-6ec7-4aa5-aaf8-29a2aceeaf06"),
                UUID.fromString("9a124f9d-177d-4f35-a325-0af99d8f81af"));
        System.out.println(branchResponse);
    }

}













