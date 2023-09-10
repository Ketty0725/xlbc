package com.ketty.api_entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Auther: Ketty Allen
 * @Date:2023/2/6 - 20:29
 * @Description:com.ketty.api_entity
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollectCountDTO {
    private Long beCollectId;
    private Integer type;
    private Integer count;
}
