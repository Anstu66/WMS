package com.wms.entity;

import lombok.Data;

/**
 * @author tk
 * @creats 2023-05-20 12:13
 */

@Data
public class RecordRes extends Record{

    private String username;
    private String adminame;
    private String goodsname;
    private String storagename;
    private String goodstypename;
}
