package com.example.fail;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface ServerlessServicesRepository extends R2dbcRepository<ServerlessServices, Long> {

}
