package edu.whu.demo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("supplier") // 表名映射
public class Supplier {
    @TableId("id") // 主键映射
    private Long id;
    private String name;
    private String contactName;
    private String contactEmail;
}
