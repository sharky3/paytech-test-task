package com.magdieva.testtask.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class CustomerDto {
    private UUID referenceId;
}
